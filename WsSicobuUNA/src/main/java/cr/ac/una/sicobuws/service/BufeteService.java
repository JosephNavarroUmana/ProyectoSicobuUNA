package cr.ac.una.sicobuws.service;

import cr.ac.una.sicobuws.model.Abogado;
import cr.ac.una.sicobuws.model.AbogadoDto;
import cr.ac.una.sicobuws.model.Bufete;
import cr.ac.una.sicobuws.model.BufeteDto;
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

/** Mantenimiento de bufetes y sus abogados propietarios (tabla BUFETEABOGADO). */
@Stateless
@LocalBean
public class BufeteService {

    private static final Logger LOG = Logger.getLogger(BufeteService.class.getName());

    @PersistenceContext(unitName = "SicobuPU")
    private EntityManager em;

    /** Consulta un bufete por id, con sus abogados. */
    public Respuesta getBufete(Long id) {
        try {
            TypedQuery<Bufete> qry = em.createNamedQuery("Bufete.findById", Bufete.class);
            qry.setParameter("id", id);
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Bufete", dtoCompleto(qry.getSingleResult()));
        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe un bufete con el id ingresado.", "getBufete NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar el bufete.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el bufete.", "getBufete " + ex.getMessage());
        }
    }

    /** Lista todos los bufetes (sin abogados). */
    public Respuesta getBufetes() {
        try {
            List<BufeteDto> dtos = new ArrayList<>();
            for (Bufete b : em.createNamedQuery("Bufete.findAll", Bufete.class).getResultList()) {
                dtos.add(new BufeteDto(b));
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Bufetes", dtos);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar los bufetes.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar los bufetes.", "getBufetes " + ex.getMessage());
        }
    }

    /** Crea o modifica un bufete y sincroniza sus abogados. */
    public Respuesta guardarBufete(BufeteDto dto) {
        try {
            Bufete bufete;
            if (dto.getId() != null && dto.getId() > 0) {
                bufete = em.find(Bufete.class, dto.getId());
                if (bufete == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el bufete a modificar.", "guardarBufete NoResultException");
                }
                bufete.actualizar(dto);
            } else {
                bufete = new Bufete(dto);
            }

            if (dto.getAbogadosEliminados() != null) {
                for (AbogadoDto ad : dto.getAbogadosEliminados()) {
                    if (ad.getId() != null) {
                        bufete.getAbogados().removeIf(a -> ad.getId().equals(a.getId()));
                    }
                }
            }
            if (dto.getAbogados() != null) {
                for (AbogadoDto ad : dto.getAbogados()) {
                    Abogado abogado;
                    if (ad.getId() != null && ad.getId() > 0) {
                        abogado = em.find(Abogado.class, ad.getId());
                        if (abogado == null) {
                            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el abogado con id " + ad.getId() + ".", "guardarBufete abogado inexistente");
                        }
                    } else {
                        abogado = new Abogado(ad);
                        em.persist(abogado);
                    }
                    if (!bufete.getAbogados().contains(abogado)) {
                        bufete.getAbogados().add(abogado);
                    }
                }
            }

            if (bufete.getId() == null) {
                em.persist(bufete);
            } else {
                bufete = em.merge(bufete);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Bufete", dtoCompleto(bufete));
        } catch (Exception ex) {
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Ya existe un bufete o un abogado con esos datos.", "guardarBufete " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al guardar el bufete.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar el bufete.", "guardarBufete " + ex.getMessage());
        }
    }

    /** Elimina un bufete (y sus asociaciones con abogados). */
    public Respuesta eliminarBufete(Long id) {
        try {
            if (id == null || id <= 0) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar el bufete a eliminar.", "eliminarBufete id inválido");
            }
            Bufete bufete = em.find(Bufete.class, id);
            if (bufete == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el bufete a eliminar.", "eliminarBufete NoResultException");
            }
            bufete.getAbogados().clear();
            em.remove(bufete);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "No se puede eliminar el bufete porque tiene relaciones con otros registros.", "eliminarBufete " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al eliminar el bufete.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar el bufete.", "eliminarBufete " + ex.getMessage());
        }
    }

    private BufeteDto dtoCompleto(Bufete bufete) {
        BufeteDto dto = new BufeteDto(bufete);
        for (Abogado a : bufete.getAbogados()) {
            dto.getAbogados().add(new AbogadoDto(a));
        }
        return dto;
    }
}