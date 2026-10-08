package cr.ac.una.sicobuws.service;

import cr.ac.una.sicobuws.model.Bufete;
import cr.ac.una.sicobuws.model.Cliente;
import cr.ac.una.sicobuws.model.ClienteDto;
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

/** Mantenimiento de clientes. Todo se filtra por el bufete (idBufete). */
@Stateless
@LocalBean
public class ClienteService {

    private static final Logger LOG = Logger.getLogger(ClienteService.class.getName());

    @PersistenceContext(unitName = "SicobuPU")
    private EntityManager em;

    /** Consulta un cliente por id, verificando que sea del bufete. */
    public Respuesta getCliente(Long id, Long idBufete) {
        try {
            Cliente c = em.createNamedQuery("Cliente.findById", Cliente.class)
                    .setParameter("id", id).getSingleResult();
            if (!c.getBufete().getId().equals(idBufete)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe un cliente con el id ingresado.", "getCliente otro bufete");
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Cliente", new ClienteDto(c));
        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe un cliente con el id ingresado.", "getCliente NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar el cliente.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el cliente.", "getCliente " + ex.getMessage());
        }
    }

    /** Busca clientes del bufete por identificación y nombre (%texto% en mayúsculas). */
    public Respuesta getClientes(Long idBufete, String identificacion, String nombre) {
        try {
            List<Cliente> lista = em.createNamedQuery("Cliente.findByFiltros", Cliente.class)
                    .setParameter("idBufete", idBufete)
                    .setParameter("identificacion", identificacion)
                    .setParameter("nombre", nombre).getResultList();
            List<ClienteDto> dtos = new ArrayList<>();
            for (Cliente c : lista) {
                dtos.add(new ClienteDto(c));
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Clientes", dtos);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar los clientes.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar los clientes.", "getClientes " + ex.getMessage());
        }
    }

    /** Crea o modifica un cliente del bufete (con control de versión). */
    public Respuesta guardarCliente(ClienteDto dto, Long idBufete) {
        try {
            Cliente cliente;
            if (dto.getId() != null && dto.getId() > 0) {
                cliente = em.find(Cliente.class, dto.getId());
                if (cliente == null || !cliente.getBufete().getId().equals(idBufete)) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el cliente a modificar.", "guardarCliente NoResultException");
                }
                // Bloqueo optimista
                if (dto.getVersion() == null || !dto.getVersion().equals(cliente.getVersion())) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Otro usuario modificó este registro, vuelva a cargarlo.", "guardarCliente version");
                }
                cliente.actualizar(dto);
                cliente = em.merge(cliente);
            } else {
                cliente = new Cliente(dto);
                // La relación la asigna el service, nunca el DTO
                cliente.setBufete(em.getReference(Bufete.class, idBufete));
                em.persist(cliente);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Cliente", new ClienteDto(cliente));
        } catch (Exception ex) {
            if (ErroresJpa.esBloqueoOptimista(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Otro usuario modificó este registro, vuelva a cargarlo.", "guardarCliente " + ex.getMessage());
            }
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "Ya existe un cliente con esa identificación en el bufete (o el bufete no existe).", "guardarCliente " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al guardar el cliente.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar el cliente.", "guardarCliente " + ex.getMessage());
        }
    }

    /** Elimina un cliente del bufete. */
    public Respuesta eliminarCliente(Long id, Long idBufete) {
        try {
            if (id == null || id <= 0) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar el cliente a eliminar.", "eliminarCliente id inválido");
            }
            Cliente cliente = em.find(Cliente.class, id);
            if (cliente == null || !cliente.getBufete().getId().equals(idBufete)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el cliente a eliminar.", "eliminarCliente NoResultException");
            }
            em.remove(cliente);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            if (ErroresJpa.esViolacionIntegridad(ex)) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE, "No se puede eliminar el cliente porque tiene relaciones con otros registros.", "eliminarCliente " + ex.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al eliminar el cliente.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar el cliente.", "eliminarCliente " + ex.getMessage());
        }
    }
}