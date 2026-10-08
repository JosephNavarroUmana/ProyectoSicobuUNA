package cr.ac.una.sicobuws.controller;

import cr.ac.una.sicobuws.model.EstadoInstrumentoDto;
import cr.ac.una.sicobuws.service.EstadoInstrumentoService;
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
 * Servicios REST de estados de instrumento.
 * GET /Estados/activos · GET /Estados/{id} · GET /Estados/buscar/{descripcion}
 * POST /Estados/estado · DELETE /Estados/estado/{id}
 */
@Path("/Estados")
public class EstadoInstrumentoController {

    private static final Logger LOG = Logger.getLogger(EstadoInstrumentoController.class.getName());

    @EJB
    EstadoInstrumentoService estadoService;

    @GET
    @Path("/activos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivos() {
        return lista(estadoService.getEstadosActivos());
    }

    @GET
    @Path("/buscar/{descripcion}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEstados(@PathParam("descripcion") String descripcion) {
        return lista(estadoService.getEstados(descripcion.toUpperCase()));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEstado(@PathParam("id") Long id) {
        try {
            Respuesta r = estadoService.getEstado(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Estado")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando el estado.").build();
        }
    }

    @POST
    @Path("/estado")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarEstado(@Valid EstadoInstrumentoDto dto) {
        try {
            Respuesta r = estadoService.guardarEstado(dto);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Estado")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error guardando el estado.").build();
        }
    }

    @DELETE
    @Path("/estado/{id}")
    public Response eliminarEstado(@PathParam("id") Long id) {
        try {
            Respuesta r = estadoService.eliminarEstado(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error eliminando el estado.").build();
        }
    }

    /** Convierte una Respuesta con lista de estados en la respuesta HTTP. */
    private Response lista(Respuesta r) {
        try {
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<EstadoInstrumentoDto>>((List<EstadoInstrumentoDto>) r.getResultado("Estados")) {
            }).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando los estados.").build();
        }
    }
}