package cr.ac.una.sicobuws.service;

import cr.ac.una.sicobuws.model.Bufete;
import cr.ac.una.sicobuws.model.Usuario;
import cr.ac.una.sicobuws.model.UsuarioDto;
import cr.ac.una.sicobuws.model.UsuarioPermiso;
import cr.ac.una.sicobuws.model.UsuarioPermisoDto;
import cr.ac.una.sicobuws.model.Vista;
//import cr.ac.una.sicobuws.util.ClaveUtil;
import cr.ac.una.sicobuws.util.CodigoRespuesta;
import cr.ac.una.sicobuws.util.ErroresJpa;
import cr.ac.una.sicobuws.util.Respuesta;
import jakarta.ejb.EJB;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Mantenimiento de usuarios del bufete, sus permisos por vista y la activación de cuenta. */
@Stateless
@LocalBean
public class UsuarioService {

    private static final Logger LOG = Logger.getLogger(UsuarioService.class.getName());

    @PersistenceContext(unitName = "SicobuPU")
    private EntityManager em;
        
    @EJB
    private NotificacionService notificacionService;
    @EJB
    private ParametroService parametroService;

    /** Consulta un usuario del bufete, con sus permisos. */
    public Respuesta getUsuario(Long id, Long idBufete) {
        try {
            TypedQuery<Usuario> qry = em.createNamedQuery("Usuario.findById", Usuario.class);
            qry.setParameter("id", id);
            Usuario u = qry.getSingleResult();
            if (!u.getBufete().getId().equals(idBufete)) {
                throw new NoResultException();
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Usuario", dtoCompleto(u));
        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe un usuario con el id ingresado.", "getUsuario NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el usuario.", "getUsuario " + ex.getMessage());
        }
    }

    /** Lista los usuarios del bufete (sin permisos). */
    public Respuesta getUsuarios(Long idBufete) {
        try {
            TypedQuery<Usuario> qry = em.createNamedQuery("Usuario.findByBufete", Usuario.class);
            qry.setParameter("idBufete", idBufete);
            List<UsuarioDto> dtos = new ArrayList<>();
            for (Usuario u : qry.getResultList()) {
                dtos.add(new UsuarioDto(u));
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Usuarios", dtos);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar los usuarios.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar los usuarios.", "getUsuarios " + ex.getMessage());
        }
    }

    /** Lista los permisos de un usuario (para armar el menú en el cliente). */
    public Respuesta getPermisos(Long idUsuario) {
        try {
            List<UsuarioPermisoDto> dtos = new ArrayList<>();
            for (UsuarioPermiso p : permisosDe(idUsuario)) {
                dtos.add(new UsuarioPermisoDto(p));
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Permisos", dtos);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar los permisos.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar los permisos.", "getPermisos " + ex.getMessage());
        }
    }

    /** Crea o modifica un usuario. Si es nuevo queda inactivo con un token de activación. */
    public Respuesta guardarUsuario(UsuarioDto dto, Long idBufete) {
        try {
            Bufete bufete = em.find(Bufete.class, idBufete);
            if (bufete == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe el bufete indicado.", "guardarUsuario bufete inexistente");
            }
            boolean nuevo = dto.getId() == null || dto.getId() <= 0;
            boolean hayClave = dto.getClave() != null && !dto.getClave().isBlank();
            if (nuevo && !hayClave) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "La clave no puede estar vacía.", "guardarUsuario clave vacía");
            }
            Long idExcluir = nuevo ? null : dto.getId();
            if (existe("Usuario.findByBufeteUsuario", "usuario", dto.getUsuario(), idBufete, idExcluir)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Ya existe un usuario con ese nombre de usuario en el bufete.", "guardarUsuario usuario duplicado");
            }
            if (existe("Usuario.findByCorreo", "correo", dto.getCorreo(), null, idExcluir)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Ya existe un usuario con ese correo.", "guardarUsuario correo duplicado");
            }

            Usuario usuario;
            if (nuevo) {
                usuario = new Usuario(dto);
                usuario.setBufete(bufete);
                usuario.setClave(dto.getClave());
                usuario.setActivo(0);
                usuario.setTokenActivacion(UUID.randomUUID().toString());
                em.persist(usuario);
                                String enlace = parametroService.getValor("url.servidor", "http://localhost:8080/WsSicobuUNA/ws")
                        + "/Usuarios/activar/" + usuario.getTokenActivacion();
                notificacionService.enviarCorreo(null, usuario.getCorreo(), "SicobuUNA - Active su cuenta",
                        notificacionService.plantilla("Bienvenido a SicobuUNA",
                                "<p>Hola " + usuario.getNombreCompleto() + ", su cuenta fue creada. Para activarla haga clic en el botón:</p>"
                                + "<p><a href=\"" + enlace + "\" style=\"background:#8c2f39;color:#ffffff;padding:10px 20px;border-radius:4px;text-decoration:none;\">Activar cuenta</a></p>"),
                        "ACTIVACION", null);
            } else {
                usuario = em.find(Usuario.class, dto.getId());
                if (usuario == null || !usuario.getBufete().getId().equals(idBufete)) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el usuario a modificar.", "guardarUsuario NoResultException");
                }
                String claveActual = usuario.getClave();
                usuario.actualizar(dto);
                usuario.setClave(hayClave ? dto.getClave() : claveActual);
                usuario = em.merge(usuario);
            }

            Respuesta error = sincronizarPermisos(usuario, Boolean.TRUE.equals(dto.getAdministrador()), dto.getPermisos());
            if (error != null) {
                return error;
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Usuario", dtoCompleto(usuario));
        } catch (Exception ex) {
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Ya existe un usuario con ese usuario o correo, o los datos no son válidos.", "guardarUsuario " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al guardar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar el usuario.", "guardarUsuario " + ex.getMessage());
        }
    }

    /** Elimina un usuario del bufete junto con sus permisos. */
    public Respuesta eliminarUsuario(Long id, Long idBufete) {
        try {
            if (id == null || id <= 0) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar el usuario a eliminar.", "eliminarUsuario id inválido");
            }
            Usuario usuario = em.find(Usuario.class, id);
            if (usuario == null || !usuario.getBufete().getId().equals(idBufete)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el usuario a eliminar.", "eliminarUsuario NoResultException");
            }
            for (UsuarioPermiso p : permisosDe(id)) {
                em.remove(p);
            }
            em.remove(usuario);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "No se puede eliminar el usuario porque tiene relaciones con otros registros. Puede desactivarlo.", "eliminarUsuario " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al eliminar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar el usuario.", "eliminarUsuario " + ex.getMessage());
        }
    }

    /** Activa la cuenta a partir del token enviado por correo. */
    public Respuesta activarUsuario(String token) {
        try {
            TypedQuery<Usuario> qry = em.createNamedQuery("Usuario.findByToken", Usuario.class);
            qry.setParameter("token", token);
            Usuario usuario = qry.getSingleResult();
            usuario.setActivo(1);
            usuario.setTokenActivacion(null);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "El enlace de activación no es válido o ya fue utilizado.", "activarUsuario NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al activar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al activar el usuario.", "activarUsuario " + ex.getMessage());
        }
    }

    // ---------- Internos ----------

    /** Verifica si ya existe un usuario con ese valor (idBufete null = búsqueda global). */
    private boolean existe(String namedQuery, String parametro, String valor, Long idBufete, Long idExcluir) {
        TypedQuery<Usuario> qry = em.createNamedQuery(namedQuery, Usuario.class);
        if (idBufete != null) {
            qry.setParameter("idBufete", idBufete);
        }
        qry.setParameter(parametro, valor.trim().toUpperCase());
        for (Usuario u : qry.getResultList()) {
            if (idExcluir == null || !idExcluir.equals(u.getId())) {
                return true;
            }
        }
        return false;
    }

    private List<UsuarioPermiso> permisosDe(Long idUsuario) {
        TypedQuery<UsuarioPermiso> qry = em.createNamedQuery("UsuarioPermiso.findByUsuario", UsuarioPermiso.class);
        qry.setParameter("idUsuario", idUsuario);
        return qry.getResultList();
    }

    private UsuarioDto dtoCompleto(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto(usuario);
        for (UsuarioPermiso p : permisosDe(usuario.getId())) {
            dto.getPermisos().add(new UsuarioPermisoDto(p));
        }
        return dto;
    }

    /**
     * Deja los permisos del usuario igual a la lista del DTO. Un administrador no necesita permisos (acceso total).
     * Compara por (vista, tipo) para no chocar con el unique al reemplazar. Devuelve una Respuesta de error o null.
     */
    private Respuesta sincronizarPermisos(Usuario usuario, boolean administrador, List<UsuarioPermisoDto> nuevos) {
        Set<String> deseados = new HashSet<>();
        if (!administrador && nuevos != null) {
            for (UsuarioPermisoDto pd : nuevos) {
                deseados.add(pd.getIdVista() + "|" + pd.getTipoPermiso().trim().toUpperCase());
            }
        }
        Set<String> existentes = new HashSet<>();
        for (UsuarioPermiso p : permisosDe(usuario.getId())) {
            String clave = p.getVista().getId() + "|" + p.getTipoPermiso();
            if (deseados.contains(clave)) {
                existentes.add(clave);
            } else {
                em.remove(p);
            }
        }
        for (String clave : deseados) {
            if (existentes.contains(clave)) {
                continue;
            }
            String[] partes = clave.split("\\|");
            Vista vista = em.find(Vista.class, Long.valueOf(partes[0]));
            if (vista == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe la vista con id " + partes[0] + ".", "guardarUsuario vista inexistente");
            }
            UsuarioPermiso p = new UsuarioPermiso();
            p.setUsuario(usuario);
            p.setVista(vista);
            p.setTipoPermiso(partes[1]);
            em.persist(p);
        }
        return null;
    }
    
        // ---------- Manejo de claves (el login y la recuperación las usarán después) ----------

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String CARACTERES = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";

    /** Encripta la clave con PBKDF2 + salt (formato iteraciones:salt:hash). */
    public String hash(String clave) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return 120000 + ":" + Base64.getEncoder().encodeToString(salt) + ":"
                + Base64.getEncoder().encodeToString(derivar(clave, salt, 120000));
    }

    /** Compara una clave escrita contra la guardada. */
    public boolean verificarClave(String clave, String almacenado) {
        try {
            String[] partes = almacenado.split(":");
            byte[] salt = Base64.getDecoder().decode(partes[1]);
            byte[] esperado = Base64.getDecoder().decode(partes[2]);
            return MessageDigest.isEqual(esperado, derivar(clave, salt, Integer.parseInt(partes[0])));
        } catch (Exception ex) {
            return false;
        }
    }

    /** Genera una clave aleatoria para la recuperación de contraseña. */
    public String generarClaveAleatoria(int longitud) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            sb.append(CARACTERES.charAt(RANDOM.nextInt(CARACTERES.length())));
        }
        return sb.toString();
    }

    private byte[] derivar(String clave, byte[] salt, int iteraciones) {
        try {
            PBEKeySpec spec = new PBEKeySpec(clave.toCharArray(), salt, iteraciones, 256);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("No se pudo procesar la clave.", ex);
        }
    }
}