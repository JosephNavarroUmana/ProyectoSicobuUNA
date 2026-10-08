package cr.ac.una.sicobuws.controller;

import cr.ac.una.sicobuws.model.AbogadoDto;
import cr.ac.una.sicobuws.service.AbogadoService;
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
 * Servicios REST de abogados.
 * GET /Abogados/{id} · GET /Abogados/{cedula}/{nombre} · POST /Abogados/abogado · DELETE /Abogados/abogado/{id}
 */
@Path("/Abogados")
public class AbogadoController {

    private static final Logger LOG = Logger.getLogger(AbogadoController.class.getName());

    @EJB
    AbogadoService abogadoService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAbogado(@PathParam("id") Long id) {
        try {
            Respuesta r = abogadoService.getAbogado(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Abogado")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando el abogado.").build();
        }
    }

    @GET
    @Path("/{cedula}/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAbogados(@PathParam("cedula") String cedula, @PathParam("nombre") String nombre) {
        try {
            Respuesta r = abogadoService.getAbogados(cedula.toUpperCase(), nombre.toUpperCase());
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<AbogadoDto>>((List<AbogadoDto>) r.getResultado("Abogados")) {
            }).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando los abogados.").build();
        }
    }

    @POST
    @Path("/abogado")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarAbogado(@Valid AbogadoDto dto) {
        try {
            Respuesta r = abogadoService.guardarAbogado(dto);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Abogado")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error guardando el abogado.").build();
        }
    }

    @DELETE
    @Path("/abogado/{id}")
    public Response eliminarAbogado(@PathParam("id") Long id) {
        try {
            Respuesta r = abogadoService.eliminarAbogado(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error eliminando el abogado.").build();
        }
    }
}