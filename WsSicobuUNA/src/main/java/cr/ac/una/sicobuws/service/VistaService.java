package cr.ac.una.sicobuws.service;

import cr.ac.una.sicobuws.model.Vista;
import cr.ac.una.sicobuws.model.VistaDto;
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

/** Mantenimiento de vistas (pantallas) sobre las que se asignan permisos. Tabla global. */
@Stateless
@LocalBean
public class VistaService {

    private static final Logger LOG = Logger.getLogger(VistaService.class.getName());

    @PersistenceContext(unitName = "SicobuPU")
    private EntityManager em;

    /** Consulta una vista por id. */
    public Respuesta getVista(Long id) {
        try {
            Vista v = em.createNamedQuery("Vista.findById", Vista.class)
                    .setParameter("id", id).getSingleResult();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Vista", new VistaDto(v));
        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe una vista con el id ingresado.", "getVista NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar la vista.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar la vista.", "getVista " + ex.getMessage());
        }
    }

    /** Lista todas las vistas. */
    public Respuesta getVistas() {
        try {
            List<VistaDto> dtos = new ArrayList<>();
            for (Vista v : em.createNamedQuery("Vista.findAll", Vista.class).getResultList()) {
                dtos.add(new VistaDto(v));
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Vistas", dtos);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar las vistas.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar las vistas.", "getVistas " + ex.getMessage());
        }
    }

    /** Crea o modifica una vista. */
    public Respuesta guardarVista(VistaDto dto) {
        try {
            Vista vista;
            if (dto.getId() != null && dto.getId() > 0) {
                vista = em.find(Vista.class, dto.getId());
                if (vista == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró la vista a modificar.", "guardarVista NoResultException");
                }
                vista.actualizar(dto);
                vista = em.merge(vista);
            } else {
                vista = new Vista(dto);
                em.persist(vista);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Vista", new VistaDto(vista));
        } catch (Exception ex) {
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Ya existe una vista con ese nombre.", "guardarVista " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al guardar la vista.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar la vista.", "guardarVista " + ex.getMessage());
        }
    }

    /** Elimina una vista (falla si hay permisos que la usan). */
    public Respuesta eliminarVista(Long id) {
        try {
            if (id == null || id <= 0) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar la vista a eliminar.", "eliminarVista id inválido");
            }
            Vista vista = em.find(Vista.class, id);
            if (vista == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró la vista a eliminar.", "eliminarVista NoResultException");
            }
            em.remove(vista);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "No se puede eliminar la vista porque tiene permisos asignados.", "eliminarVista " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al eliminar la vista.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar la vista.", "eliminarVista " + ex.getMessage());
        }
    }
}