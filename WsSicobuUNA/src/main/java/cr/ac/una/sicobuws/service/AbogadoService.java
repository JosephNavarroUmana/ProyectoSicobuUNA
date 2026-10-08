package cr.ac.una.sicobuws.service;

import cr.ac.una.sicobuws.model.Abogado;
import cr.ac.una.sicobuws.model.AbogadoDto;
import cr.ac.una.sicobuws.util.CodigoRespuesta;
import cr.ac.una.sicobuws.util.ErroresJpa;
import cr.ac.una.sicobuws.util.Respuesta;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Mantenimiento de abogados (tabla global, se une a los bufetes por BUFETEABOGADO). */
@Stateless
@LocalBean
public class AbogadoService {

    private static final Logger LOG = Logger.getLogger(AbogadoService.class.getName());

    @PersistenceContext(unitName = "SicobuPU")
    private EntityManager em;

    /** Consulta un abogado por id. */
    public Respuesta getAbogado(Long id) {
        try {
            Abogado a = em.createNamedQuery("Abogado.findById", Abogado.class)
                    .setParameter("id", id).getSingleResult();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Abogado", new AbogadoDto(a));
        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe un abogado con el id ingresado.", "getAbogado NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar el abogado.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el abogado.", "getAbogado " + ex.getMessage());
        }
    }

    /** Busca abogados por cédula y nombre (los filtros llegan como %texto% en mayúsculas). */
    public Respuesta getAbogados(String cedula, String nombre) {
        try {
            List<Abogado> lista = em.createNamedQuery("Abogado.findByNombreCedula", Abogado.class)
                    .setParameter("nombre", nombre).setParameter("cedula", cedula).getResultList();
            List<AbogadoDto> dtos = new ArrayList<>();
            for (Abogado a : lista) {
                dtos.add(new AbogadoDto(a));
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Abogados", dtos);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar los abogados.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar los abogados.", "getAbogados " + ex.getMessage());
        }
    }

    /** Crea o modifica un abogado (con control de versión). */
    public Respuesta guardarAbogado(AbogadoDto dto) {
        try {
            Abogado abogado;
            if (dto.getId() != null && dto.getId() > 0) {
                abogado = em.find(Abogado.class, dto.getId());
                if (abogado == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el abogado a modificar.", "guardarAbogado NoResultException");
                }
                // Bloqueo optimista: si la versión no coincide, otro usuario ya lo modificó
                if (dto.getVersion() == null || !dto.getVersion().equals(abogado.getVersion())) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Otro usuario modificó este registro, vuelva a cargarlo.", "guardarAbogado version");
                }
                abogado.actualizar(dto);
                abogado = em.merge(abogado);
            } else {
                abogado = new Abogado(dto);
                em.persist(abogado);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Abogado", new AbogadoDto(abogado));
        } catch (Exception ex) {
            if (ErroresJpa.esBloqueoOptimista(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Otro usuario modificó este registro, vuelva a cargarlo.", "guardarAbogado " + ex.getMessage());
            }
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Ya existe un abogado con esa cédula.", "guardarAbogado " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al guardar el abogado.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar el abogado.", "guardarAbogado " + ex.getMessage());
        }
    }

    /** Elimina un abogado. */
    public Respuesta eliminarAbogado(Long id) {
        try {
            if (id == null || id <= 0) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar el abogado a eliminar.", "eliminarAbogado id inválido");
            }
            Abogado abogado = em.find(Abogado.class, id);
            if (abogado == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el abogado a eliminar.", "eliminarAbogado NoResultException");
            }
            em.remove(abogado);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "No se puede eliminar el abogado porque tiene relaciones con otros registros.", "eliminarAbogado " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al eliminar el abogado.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar el abogado.", "eliminarAbogado " + ex.getMessage());
        }
    }
}