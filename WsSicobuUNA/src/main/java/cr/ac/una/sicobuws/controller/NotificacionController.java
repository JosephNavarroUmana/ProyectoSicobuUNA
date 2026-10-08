package cr.ac.una.sicobuws.controller;

import cr.ac.una.sicobuws.model.NotificacionDto;
import cr.ac.una.sicobuws.service.NotificacionService;
import cr.ac.una.sicobuws.util.CodigoRespuesta;
import cr.ac.una.sicobuws.util.Respuesta;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicios REST de notificaciones.
 * GET /Notificaciones/instrumento/{idInstrumento} · GET /Notificaciones/tipo/{tipo} · POST /Notificaciones/prueba?correo=
 */
@Path("/Notificaciones")
public class NotificacionController {

    private static final Logger LOG = Logger.getLogger(NotificacionController.class.getName());

    @EJB
    NotificacionService notificacionService;

    @GET
    @Path("/instrumento/{idInstrumento}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPorInstrumento(@PathParam("idInstrumento") Long idInstrumento) {
        return lista(notificacionService.getNotificaciones(idInstrumento));
    }

    @GET
    @Path("/tipo/{tipo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPorTipo(@PathParam("tipo") String tipo) {
        return lista(notificacionService.getNotificacionesPorTipo(tipo.toUpperCase()));
    }

    @POST
    @Path("/prueba")
    public Response enviarPrueba(@QueryParam("correo") String correo) {
        try {
            if (correo == null || correo.isBlank()) {
                return Response.status(CodigoRespuesta.ERROR_CLIENTE.getValue()).entity("Debe indicar el correo de destino.").build();
            }
            Respuesta r = notificacionService.enviarPrueba(correo);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error enviando el correo de prueba.").build();
        }
    }

    /** Convierte una Respuesta con lista de notificaciones en la respuesta HTTP. */
    private Response lista(Respuesta r) {
        try {
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<NotificacionDto>>((List<NotificacionDto>) r.getResultado("Notificaciones")) {
            }).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando las notificaciones.").build();
        }
    }
}