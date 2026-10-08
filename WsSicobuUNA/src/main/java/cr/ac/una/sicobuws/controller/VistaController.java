package cr.ac.una.sicobuws.controller;

import cr.ac.una.sicobuws.model.VistaDto;
import cr.ac.una.sicobuws.service.VistaService;
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
 * Servicios REST de vistas.
 * GET /Vistas · GET /Vistas/{id} · POST /Vistas/vista · DELETE /Vistas/vista/{id}
 */
@Path("/Vistas")
public class VistaController {

    private static final Logger LOG = Logger.getLogger(VistaController.class.getName());

    @EJB
    VistaService vistaService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVistas() {
        try {
            Respuesta r = vistaService.getVistas();
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<VistaDto>>((List<VistaDto>) r.getResultado("Vistas")) {
            }).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando las vistas.").build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVista(@PathParam("id") Long id) {
        try {
            Respuesta r = vistaService.getVista(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Vista")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando la vista.").build();
        }
    }

    @POST
    @Path("/vista")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarVista(@Valid VistaDto dto) {
        try {
            Respuesta r = vistaService.guardarVista(dto);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Vista")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error guardando la vista.").build();
        }
    }

    @DELETE
    @Path("/vista/{id}")
    public Response eliminarVista(@PathParam("id") Long id) {
        try {
            Respuesta r = vistaService.eliminarVista(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error eliminando la vista.").build();
        }
    }
}