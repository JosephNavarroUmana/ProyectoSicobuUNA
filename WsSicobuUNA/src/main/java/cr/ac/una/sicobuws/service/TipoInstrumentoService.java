package cr.ac.una.sicobuws.service;

import cr.ac.una.sicobuws.model.TipoInstrumento;
import cr.ac.una.sicobuws.model.TipoInstrumentoDto;
import cr.ac.una.sicobuws.model.TipoInstrumentoRequisito;
import cr.ac.una.sicobuws.model.TipoInstrumentoRequisitoDto;
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

/** Mantenimiento de tipos de instrumento y sus requisitos. Es una tabla global (sin bufete). */
@Stateless
@LocalBean
public class TipoInstrumentoService {

    private static final Logger LOG = Logger.getLogger(TipoInstrumentoService.class.getName());

    @PersistenceContext(unitName = "SicobuPU")
    private EntityManager em;

    /** Consulta un tipo por id, con sus requisitos. */
    public Respuesta getTipo(Long id) {
        try {
            TypedQuery<TipoInstrumento> qry = em.createNamedQuery("TipoInstrumento.findById", TipoInstrumento.class);
            qry.setParameter("id", id);
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Tipo", dtoCompleto(qry.getSingleResult()));
        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe un tipo de instrumento con el id ingresado.", "getTipo NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar el tipo de instrumento.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el tipo de instrumento.", "getTipo " + ex.getMessage());
        }
    }

    /** Lista todos los tipos (sin requisitos). */
    public Respuesta getTipos() {
        return lista(em.createNamedQuery("TipoInstrumento.findAll", TipoInstrumento.class), "getTipos");
    }

    /** Lista los tipos activos (sin requisitos). */
    public Respuesta getTiposActivos() {
        return lista(em.createNamedQuery("TipoInstrumento.findActivos", TipoInstrumento.class), "getTiposActivos");
    }

    /** Lista los tipos cuyo nombre contiene el texto (sin requisitos). */
    public Respuesta getTiposPorNombre(String nombre) {
        TypedQuery<TipoInstrumento> qry = em.createNamedQuery("TipoInstrumento.findByNombre", TipoInstrumento.class);
        qry.setParameter("nombre", "%" + nombre.trim() + "%");
        return lista(qry, "getTiposPorNombre");
    }

    /** Crea o modifica un tipo y sincroniza sus requisitos. */
    public Respuesta guardarTipo(TipoInstrumentoDto dto) {
        try {
            TipoInstrumento tipo;
            if (dto.getId() != null && dto.getId() > 0) {
                tipo = em.find(TipoInstrumento.class, dto.getId());
                if (tipo == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el tipo de instrumento a modificar.", "guardarTipo NoResultException");
                }
                tipo.actualizar(dto);
                tipo = em.merge(tipo);
            } else {
                tipo = new TipoInstrumento(dto);
                em.persist(tipo);
            }
            sincronizarRequisitos(tipo, dto.getRequisitos());
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Tipo", dtoCompleto(tipo));
        } catch (Exception ex) {
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "No se pueden quitar requisitos que ya están en uso por instrumentos, o los datos no son válidos.", "guardarTipo " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al guardar el tipo de instrumento.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar el tipo de instrumento.", "guardarTipo " + ex.getMessage());
        }
    }

    /** Elimina un tipo con sus requisitos. */
    public Respuesta eliminarTipo(Long id) {
        try {
            if (id == null || id <= 0) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar el tipo de instrumento a eliminar.", "eliminarTipo id inválido");
            }
            TipoInstrumento tipo = em.find(TipoInstrumento.class, id);
            if (tipo == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el tipo de instrumento a eliminar.", "eliminarTipo NoResultException");
            }
            for (TipoInstrumentoRequisito r : getRequisitos(id)) {
                em.remove(r);
            }
            em.remove(tipo);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "No se puede eliminar el tipo de instrumento porque tiene instrumentos asociados. Puede desactivarlo.", "eliminarTipo " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al eliminar el tipo de instrumento.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar el tipo de instrumento.", "eliminarTipo " + ex.getMessage());
        }
    }

    // ---------- Internos ----------

    private Respuesta lista(TypedQuery<TipoInstrumento> qry, String metodo) {
        try {
            List<TipoInstrumentoDto> dtos = new ArrayList<>();
            for (TipoInstrumento t : qry.getResultList()) {
                dtos.add(new TipoInstrumentoDto(t));
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Tipos", dtos);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar los tipos de instrumento.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar los tipos de instrumento.", metodo + " " + ex.getMessage());
        }
    }

    private List<TipoInstrumentoRequisito> getRequisitos(Long idTipo) {
        TypedQuery<TipoInstrumentoRequisito> qry = em.createNamedQuery("TipoInstrumentoRequisito.findByTipoInstrumento", TipoInstrumentoRequisito.class);
        qry.setParameter("idTipoInstrumento", idTipo);
        return qry.getResultList();
    }

    private TipoInstrumentoDto dtoCompleto(TipoInstrumento tipo) {
        TipoInstrumentoDto dto = new TipoInstrumentoDto(tipo);
        for (TipoInstrumentoRequisito r : getRequisitos(tipo.getId())) {
            dto.getRequisitos().add(new TipoInstrumentoRequisitoDto(r));
        }
        return dto;
    }

    /** Deja los requisitos del tipo igual a la lista del DTO: crea los sin id, actualiza los existentes y borra los que no vienen. */
    private void sincronizarRequisitos(TipoInstrumento tipo, List<TipoInstrumentoRequisitoDto> nuevos) {
        List<TipoInstrumentoRequisitoDto> lista = nuevos != null ? nuevos : new ArrayList<>();
        Map<Long, TipoInstrumentoRequisito> actuales = new HashMap<>();
        for (TipoInstrumentoRequisito r : getRequisitos(tipo.getId())) {
            actuales.put(r.getId(), r);
        }
        Set<Long> conservar = new HashSet<>();
        for (TipoInstrumentoRequisitoDto rd : lista) {
            if (rd.getId() != null && rd.getId() > 0) {
                conservar.add(rd.getId());
            }
        }
        for (TipoInstrumentoRequisito r : actuales.values()) {
            if (!conservar.contains(r.getId())) {
                em.remove(r);
            }
        }
        for (TipoInstrumentoRequisitoDto rd : lista) {
            TipoInstrumentoRequisito r = rd.getId() != null && rd.getId() > 0 ? actuales.get(rd.getId()) : null;
            if (r == null) {
                r = new TipoInstrumentoRequisito();
                r.setTipoInstrumento(tipo);
                em.persist(r);
            }
            r.setDescripcion(rd.getDescripcion());
            r.setOrden(rd.getOrden());
        }
    }
}