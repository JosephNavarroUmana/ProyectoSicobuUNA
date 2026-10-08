package cr.ac.una.sicobuws.controller;

import cr.ac.una.sicobuws.model.BufeteDto;
import cr.ac.una.sicobuws.service.BufeteService;
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
 * Servicios REST de bufetes.
 * GET /Bufetes · GET /Bufetes/{id} · POST /Bufetes/bufete · DELETE /Bufetes/bufete/{id}
 */
@Path("/Bufetes")
public class BufeteController {

    private static final Logger LOG = Logger.getLogger(BufeteController.class.getName());

    @EJB
    BufeteService bufeteService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBufetes() {
        try {
            Respuesta r = bufeteService.getBufetes();
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<BufeteDto>>((List<BufeteDto>) r.getResultado("Bufetes")) {
            }).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando los bufetes.").build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBufete(@PathParam("id") Long id) {
        try {
            Respuesta r = bufeteService.getBufete(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Bufete")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando el bufete.").build();
        }
    }

    @POST
    @Path("/bufete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarBufete(@Valid BufeteDto dto) {
        try {
            Respuesta r = bufeteService.guardarBufete(dto);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Bufete")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error guardando el bufete.").build();
        }
    }

    @DELETE
    @Path("/bufete/{id}")
    public Response eliminarBufete(@PathParam("id") Long id) {
        try {
            Respuesta r = bufeteService.eliminarBufete(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error eliminando el bufete.").build();
        }
    }
}