package cr.ac.una.sicobuws.service;

import cr.ac.una.sicobuws.model.EstadoInstrumento;
import cr.ac.una.sicobuws.model.EstadoInstrumentoDto;
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

/** Mantenimiento de estados de instrumento (tabla global, sin versión). */
@Stateless
@LocalBean
public class EstadoInstrumentoService {

    private static final Logger LOG = Logger.getLogger(EstadoInstrumentoService.class.getName());

    @PersistenceContext(unitName = "SicobuPU")
    private EntityManager em;

    /** Consulta un estado por id. */
    public Respuesta getEstado(Long id) {
        try {
            EstadoInstrumento e = em.createNamedQuery("EstadoInstrumento.findById", EstadoInstrumento.class)
                    .setParameter("id", id).getSingleResult();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Estado", new EstadoInstrumentoDto(e));
        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe un estado con el id ingresado.", "getEstado NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar el estado.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el estado.", "getEstado " + ex.getMessage());
        }
    }

    /** Lista los estados filtrando por descripción (%texto% en mayúsculas). */
    public Respuesta getEstados(String descripcion) {
        try {
            List<EstadoInstrumento> lista = em.createNamedQuery("EstadoInstrumento.findByDescripcion", EstadoInstrumento.class)
                    .setParameter("descripcion", descripcion).getResultList();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Estados", aDtos(lista));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar los estados.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar los estados.", "getEstados " + ex.getMessage());
        }
    }

    /** Lista solo los estados activos (para combos). */
    public Respuesta getEstadosActivos() {
        try {
            List<EstadoInstrumento> lista = em.createNamedQuery("EstadoInstrumento.findActivos", EstadoInstrumento.class).getResultList();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Estados", aDtos(lista));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar los estados activos.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar los estados.", "getEstadosActivos " + ex.getMessage());
        }
    }

    /** Crea o modifica un estado. */
    public Respuesta guardarEstado(EstadoInstrumentoDto dto) {
        try {
            EstadoInstrumento estado;
            if (dto.getId() != null && dto.getId() > 0) {
                estado = em.find(EstadoInstrumento.class, dto.getId());
                if (estado == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el estado a modificar.", "guardarEstado NoResultException");
                }
                estado.actualizar(dto);
                estado = em.merge(estado);
            } else {
                estado = new EstadoInstrumento(dto);
                em.persist(estado);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Estado", new EstadoInstrumentoDto(estado));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al guardar el estado.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar el estado.", "guardarEstado " + ex.getMessage());
        }
    }

    /** Elimina un estado (falla si hay instrumentos que lo usan). */
    public Respuesta eliminarEstado(Long id) {
        try {
            if (id == null || id <= 0) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar el estado a eliminar.", "eliminarEstado id inválido");
            }
            EstadoInstrumento estado = em.find(EstadoInstrumento.class, id);
            if (estado == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el estado a eliminar.", "eliminarEstado NoResultException");
            }
            em.remove(estado);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "No se puede eliminar el estado porque tiene relaciones con otros registros.", "eliminarEstado " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al eliminar el estado.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar el estado.", "eliminarEstado " + ex.getMessage());
        }
    }

    /** Convierte una lista de entidades a DTOs. */
    private List<EstadoInstrumentoDto> aDtos(List<EstadoInstrumento> lista) {
        List<EstadoInstrumentoDto> dtos = new ArrayList<>();
        for (EstadoInstrumento e : lista) {
            dtos.add(new EstadoInstrumentoDto(e));
        }
        return dtos;
    }
}