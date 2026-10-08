package cr.ac.una.sicobuws.controller;

import cr.ac.una.sicobuws.model.ClienteDto;
import cr.ac.una.sicobuws.service.ClienteService;
import cr.ac.una.sicobuws.util.CodigoRespuesta;
import cr.ac.una.sicobuws.util.Respuesta;
import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicios REST de clientes.
 * TEMPORAL (sin JWT): el idBufete llega por ?idBufete= en consultas y eliminación,
 * y dentro del DTO al guardar. Con JWT se tomará del token en un solo lugar por método.
 */
@Path("/Clientes")
public class ClienteController {

    private static final Logger LOG = Logger.getLogger(ClienteController.class.getName());

    @EJB
    ClienteService clienteService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCliente(@PathParam("id") Long id, @QueryParam("idBufete") Long idBufete) {
        try {
            Respuesta r = clienteService.getCliente(id, idBufete);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Cliente")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando el cliente.").build();
        }
    }

    @GET
    @Path("/{identificacion}/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientes(@PathParam("identificacion") String identificacion,
            @PathParam("nombre") String nombre, @QueryParam("idBufete") Long idBufete) {
        try {
            Respuesta r = clienteService.getClientes(idBufete, identificacion.toUpperCase(), nombre.toUpperCase());
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<ClienteDto>>((List<ClienteDto>) r.getResultado("Clientes")) {
            }).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando los clientes.").build();
        }
    }

    @POST
    @Path("/cliente")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarCliente(@Valid ClienteDto dto) {
        try {
            // TODO JWT: reemplazar dto.getIdBufete() por el idBufete del token
            Respuesta r = clienteService.guardarCliente(dto, dto.getIdBufete());
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Cliente")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error guardando el cliente.").build();
        }
    }

    @DELETE
    @Path("/cliente/{id}")
    public Response eliminarCliente(@PathParam("id") Long id, @QueryParam("idBufete") Long idBufete) {
        try {
            Respuesta r = clienteService.eliminarCliente(id, idBufete);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error eliminando el cliente.").build();
        }
    }
}