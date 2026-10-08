package cr.ac.una.sicobuws.service;

import cr.ac.una.sicobuws.model.Bufete;
import cr.ac.una.sicobuws.model.Cliente;
import cr.ac.una.sicobuws.model.Sociedad;
import cr.ac.una.sicobuws.model.SociedadDto;
import cr.ac.una.sicobuws.model.SociedadRepresentante;
import cr.ac.una.sicobuws.model.SociedadRepresentanteDto;
import cr.ac.una.sicobuws.util.CodigoRespuesta;
import cr.ac.una.sicobuws.util.ErroresJpa;
import cr.ac.una.sicobuws.util.Respuesta;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Mantenimiento de sociedades y sus representantes. Todo se filtra por bufete. */
@Stateless
@LocalBean
public class SociedadService {

    private static final Logger LOG = Logger.getLogger(SociedadService.class.getName());

    @PersistenceContext(unitName = "SicobuPU")
    private EntityManager em;

    /** Consulta una sociedad del bufete, con sus representantes. */
    public Respuesta getSociedad(Long id, Long idBufete) {
        try {
            TypedQuery<Sociedad> qry = em.createNamedQuery("Sociedad.findById", Sociedad.class);
            qry.setParameter("id", id);
            Sociedad s = qry.getSingleResult();
            if (!s.getBufete().getId().equals(idBufete)) {
                throw new NoResultException();
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Sociedad", dtoCompleto(s));
        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe una sociedad con el id ingresado.", "getSociedad NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar la sociedad.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar la sociedad.", "getSociedad " + ex.getMessage());
        }
    }

    /** Lista las sociedades del bufete filtrando por cédula jurídica y nombre (contiene). Sin representantes. */
    public Respuesta getSociedades(Long idBufete, String cedula, String nombre) {
        try {
            TypedQuery<Sociedad> qry = em.createNamedQuery("Sociedad.findByFiltros", Sociedad.class);
            qry.setParameter("idBufete", idBufete);
            qry.setParameter("cedula", "%" + cedula.trim() + "%");
            qry.setParameter("nombre", "%" + nombre.trim() + "%");
            List<SociedadDto> dtos = new ArrayList<>();
            for (Sociedad s : qry.getResultList()) {
                dtos.add(new SociedadDto(s));
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Sociedades", dtos);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar las sociedades.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar las sociedades.", "getSociedades " + ex.getMessage());
        }
    }

    /** Crea o modifica una sociedad y sincroniza sus representantes. */
    public Respuesta guardarSociedad(SociedadDto dto, Long idBufete) {
        try {
            Bufete bufete = em.find(Bufete.class, idBufete);
            if (bufete == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe el bufete indicado.", "guardarSociedad bufete inexistente");
            }
            String cedula = dto.getCedulaJuridica().trim().toUpperCase();
            Sociedad sociedad;
            if (dto.getId() != null && dto.getId() > 0) {
                sociedad = em.find(Sociedad.class, dto.getId());
                if (sociedad == null || !sociedad.getBufete().getId().equals(idBufete)) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró la sociedad a modificar.", "guardarSociedad NoResultException");
                }
                if (existeCedula(idBufete, cedula, sociedad.getId())) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Ya existe una sociedad con esa cédula jurídica en el bufete.", "guardarSociedad cédula duplicada");
                }
                sociedad.actualizar(dto);
                sociedad = em.merge(sociedad);
            } else {
                if (existeCedula(idBufete, cedula, null)) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Ya existe una sociedad con esa cédula jurídica en el bufete.", "guardarSociedad cédula duplicada");
                }
                sociedad = new Sociedad(dto);
                sociedad.setBufete(bufete);
                em.persist(sociedad);
            }

            Respuesta error = sincronizarRepresentantes(sociedad, dto.getRepresentantes(), idBufete);
            if (error != null) {
                return error;
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Sociedad", dtoCompleto(sociedad));
        } catch (Exception ex) {
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Ya existe una sociedad con esa cédula jurídica o los datos no son válidos.", "guardarSociedad " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al guardar la sociedad.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar la sociedad.", "guardarSociedad " + ex.getMessage());
        }
    }

    /** Elimina una sociedad del bufete junto con sus representantes. */
    public Respuesta eliminarSociedad(Long id, Long idBufete) {
        try {
            if (id == null || id <= 0) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar la sociedad a eliminar.", "eliminarSociedad id inválido");
            }
            Sociedad sociedad = em.find(Sociedad.class, id);
            if (sociedad == null || !sociedad.getBufete().getId().equals(idBufete)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró la sociedad a eliminar.", "eliminarSociedad NoResultException");
            }
            for (SociedadRepresentante r : getRepresentantes(id)) {
                em.remove(r);
            }
            em.remove(sociedad);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "No se puede eliminar la sociedad porque tiene relaciones con otros registros.", "eliminarSociedad " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al eliminar la sociedad.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar la sociedad.", "eliminarSociedad " + ex.getMessage());
        }
    }

    // ---------- Internos ----------

    private boolean existeCedula(Long idBufete, String cedula, Long idExcluir) {
        TypedQuery<Sociedad> qry = em.createNamedQuery("Sociedad.findByBufeteCedula", Sociedad.class);
        qry.setParameter("idBufete", idBufete);
        qry.setParameter("cedula", cedula);
        for (Sociedad s : qry.getResultList()) {
            if (idExcluir == null || !idExcluir.equals(s.getId())) {
                return true;
            }
        }
        return false;
    }

    private List<SociedadRepresentante> getRepresentantes(Long idSociedad) {
        TypedQuery<SociedadRepresentante> qry = em.createNamedQuery("SociedadRepresentante.findBySociedad", SociedadRepresentante.class);
        qry.setParameter("idSociedad", idSociedad);
        return qry.getResultList();
    }

    private SociedadDto dtoCompleto(Sociedad sociedad) {
        SociedadDto dto = new SociedadDto(sociedad);
        for (SociedadRepresentante r : getRepresentantes(sociedad.getId())) {
            dto.getRepresentantes().add(new SociedadRepresentanteDto(r));
        }
        return dto;
    }

    /** Deja los representantes de la sociedad igual a la lista del DTO. Devuelve una Respuesta de error o null. */
    private Respuesta sincronizarRepresentantes(Sociedad sociedad, List<SociedadRepresentanteDto> nuevos, Long idBufete) {
        List<SociedadRepresentanteDto> lista = nuevos != null ? nuevos : new ArrayList<>();
        Map<Long, SociedadRepresentante> actuales = new HashMap<>();
        for (SociedadRepresentante r : getRepresentantes(sociedad.getId())) {
            actuales.put(r.getId(), r);
        }
        Set<Long> conservar = new HashSet<>();
        for (SociedadRepresentanteDto rd : lista) {
            if (rd.getId() != null && rd.getId() > 0) {
                conservar.add(rd.getId());
            }
        }
        for (SociedadRepresentante r : actuales.values()) {
            if (!conservar.contains(r.getId())) {
                em.remove(r);
            }
        }
        for (SociedadRepresentanteDto rd : lista) {
            Cliente cliente = em.find(Cliente.class, rd.getIdCliente());
            if (cliente == null || !cliente.getBufete().getId().equals(idBufete)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "El cliente representante con id " + rd.getIdCliente() + " no existe en el bufete.", "guardarSociedad cliente inexistente");
            }
            SociedadRepresentante r;
            if (rd.getId() != null && rd.getId() > 0) {
                r = actuales.get(rd.getId());
                if (r == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el representante con id " + rd.getId() + ".", "guardarSociedad representante inexistente");
                }
            } else {
                r = new SociedadRepresentante();
                r.setSociedad(sociedad);
                em.persist(r);
            }
            r.setCliente(cliente);
            r.setCargo(rd.getCargo());
        }
        return null;
    }
}