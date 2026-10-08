package cr.ac.una.sicobuws.controller;

import cr.ac.una.sicobuws.model.SociedadDto;
import cr.ac.una.sicobuws.service.SociedadService;
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
 * Servicios REST de sociedades.
 * TEMPORAL (sin JWT): el idBufete llega por ?idBufete= y dentro del DTO al guardar.
 * GET /Sociedades/{id} · GET /Sociedades/{cedula}/{nombre} · POST /Sociedades/sociedad · DELETE /Sociedades/sociedad/{id}
 */
@Path("/Sociedades")
public class SociedadController {

    private static final Logger LOG = Logger.getLogger(SociedadController.class.getName());

    @EJB
    SociedadService sociedadService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSociedad(@PathParam("id") Long id, @QueryParam("idBufete") Long idBufete) {
        try {
            Respuesta r = sociedadService.getSociedad(id, idBufete);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Sociedad")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando la sociedad.").build();
        }
    }

    @GET
    @Path("/{cedula}/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSociedades(@PathParam("cedula") String cedula, @PathParam("nombre") String nombre,
            @QueryParam("idBufete") Long idBufete) {
        try {
            Respuesta r = sociedadService.getSociedades(idBufete, cedula.toUpperCase(), nombre.toUpperCase());
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<SociedadDto>>((List<SociedadDto>) r.getResultado("Sociedades")) {
            }).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando las sociedades.").build();
        }
    }

    @POST
    @Path("/sociedad")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarSociedad(@Valid SociedadDto dto) {
        try {
            // TODO JWT: reemplazar dto.getIdBufete() por el idBufete del token
            Respuesta r = sociedadService.guardarSociedad(dto, dto.getIdBufete());
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Sociedad")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error guardando la sociedad.").build();
        }
    }

    @DELETE
    @Path("/sociedad/{id}")
    public Response eliminarSociedad(@PathParam("id") Long id, @QueryParam("idBufete") Long idBufete) {
        try {
            Respuesta r = sociedadService.eliminarSociedad(id, idBufete);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error eliminando la sociedad.").build();
        }
    }
}