package cr.ac.una.sicobuws.controller;

import cr.ac.una.sicobuws.model.ParametroDto;
import cr.ac.una.sicobuws.service.ParametroService;
import cr.ac.una.sicobuws.util.CodigoRespuesta;
import cr.ac.una.sicobuws.util.Respuesta;
import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicios REST del mantenimiento de parámetros.
 * Rutas: GET /Parametros, GET /Parametros/{id},
 *        POST /Parametros/parametro, DELETE /Parametros/parametro/{id}
 */
@Path("/Parametros")
public class ParametroController {

    private static final Logger LOG = Logger.getLogger(ParametroController.class.getName());

    @EJB
    ParametroService parametroService;

    /** Lista todos los parámetros. */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getParametros() {
        try {
            Respuesta r = parametroService.getParametros();
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<ParametroDto>>((List<ParametroDto>) r.getResultado("Parametros")) {
            }).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando los parámetros.").build();
        }
    }

    /** Consulta un parámetro por id. */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getParametro(@PathParam("id") Long id) {
        try {
            Respuesta r = parametroService.getParametro(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Parametro")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando el parámetro.").build();
        }
    }

    /** Guarda (crea o modifica) un parámetro. */
    @POST
    @Path("/parametro")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarParametro(@Valid ParametroDto dto) {
        try {
            Respuesta r = parametroService.guardarParametro(dto);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Parametro")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error guardando el parámetro.").build();
        }
    }

    /** Elimina un parámetro. */
    @DELETE
    @Path("/parametro/{id}")
    public Response eliminarParametro(@PathParam("id") Long id) {
        try {
            Respuesta r = parametroService.eliminarParametro(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error eliminando el parámetro.").build();
        }
    }
}