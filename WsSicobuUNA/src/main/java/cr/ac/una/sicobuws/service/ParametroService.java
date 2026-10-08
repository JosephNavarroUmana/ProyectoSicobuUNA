package cr.ac.una.sicobuws.service;

import cr.ac.una.sicobuws.model.Parametro;
import cr.ac.una.sicobuws.model.ParametroDto;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Mantenimiento de los parámetros generales del sistema (tabla PARAMETRO).
 * Es una tabla global: no pertenece a ningún bufete.
 */
@Stateless
@LocalBean
public class ParametroService {

    private static final Logger LOG = Logger.getLogger(ParametroService.class.getName());

    @PersistenceContext(unitName = "SicobuPU")
    private EntityManager em;

    /** Consulta un parámetro por id. */
    public Respuesta getParametro(Long id) {
        try {
            TypedQuery<Parametro> qry = em.createNamedQuery("Parametro.findById", Parametro.class);
            qry.setParameter("id", id);
            Parametro p = qry.getSingleResult();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Parametro", new ParametroDto(p));
        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe un parámetro con el id ingresado.", "getParametro NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar el parámetro.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el parámetro.", "getParametro " + ex.getMessage());
        }
    }

    /** Lista todos los parámetros. */
    public Respuesta getParametros() {
        try {
            List<Parametro> lista = em.createNamedQuery("Parametro.findAll", Parametro.class).getResultList();
            List<ParametroDto> dtos = new ArrayList<>();
            for (Parametro p : lista) {
                dtos.add(new ParametroDto(p));
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Parametros", dtos);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar los parámetros.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar los parámetros.", "getParametros " + ex.getMessage());
        }
    }

    /** Crea o modifica un parámetro. */
    public Respuesta guardarParametro(ParametroDto dto) {
        try {
            Parametro parametro;
            if (dto.getId() != null && dto.getId() > 0) {
                parametro = em.find(Parametro.class, dto.getId());
                if (parametro == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el parámetro a modificar.", "guardarParametro NoResultException");
                }
                parametro.actualizar(dto);
                parametro = em.merge(parametro);
            } else {
                parametro = new Parametro(dto);
                em.persist(parametro);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Parametro", new ParametroDto(parametro));
        } catch (Exception ex) {
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Ya existe un parámetro con ese nombre.", "guardarParametro " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al guardar el parámetro.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar el parámetro.", "guardarParametro " + ex.getMessage());
        }
    }

    /** Elimina un parámetro por id. */
    public Respuesta eliminarParametro(Long id) {
        try {
            if (id == null || id <= 0) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar el parámetro a eliminar.", "eliminarParametro id inválido");
            }
            Parametro parametro = em.find(Parametro.class, id);
            if (parametro == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el parámetro a eliminar.", "eliminarParametro NoResultException");
            }
            em.remove(parametro);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "No se puede eliminar el parámetro porque tiene relaciones con otros registros.", "eliminarParametro " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al eliminar el parámetro.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar el parámetro.", "eliminarParametro " + ex.getMessage());
        }
    }

    // ---------- Lectura tipada para uso interno de otros services (no es REST) ----------

    /** Devuelve el valor del parámetro o el valor por defecto si no existe. */
    public String getValor(String nombre, String porDefecto) {
        try {
            TypedQuery<Parametro> qry = em.createNamedQuery("Parametro.findByNombre", Parametro.class);
            qry.setParameter("nombre", nombre);
            return qry.getSingleResult().getValor();
        } catch (Exception ex) {
            return porDefecto;
        }
    }

    /** Devuelve el valor como entero o el valor por defecto si no existe o no es numérico. */
    public int getValorInt(String nombre, int porDefecto) {
        try {
            return Integer.parseInt(getValor(nombre, String.valueOf(porDefecto)).trim());
        } catch (NumberFormatException ex) {
            return porDefecto;
        }
    }
}