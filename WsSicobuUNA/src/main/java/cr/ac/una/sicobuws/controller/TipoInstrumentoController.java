package cr.ac.una.sicobuws.controller;

import cr.ac.una.sicobuws.model.TipoInstrumentoDto;
import cr.ac.una.sicobuws.service.TipoInstrumentoService;
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
 * Servicios REST de tipos de instrumento.
 * GET /TiposInstrumento · GET /TiposInstrumento/activos · GET /TiposInstrumento/buscar/{nombre}
 * GET /TiposInstrumento/{id} · POST /TiposInstrumento/tipo · DELETE /TiposInstrumento/tipo/{id}
 */
@Path("/TiposInstrumento")
public class TipoInstrumentoController {

    private static final Logger LOG = Logger.getLogger(TipoInstrumentoController.class.getName());

    @EJB
    TipoInstrumentoService tipoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTipos() {
        return lista(tipoService.getTipos());
    }

    @GET
    @Path("/activos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivos() {
        return lista(tipoService.getTiposActivos());
    }

    @GET
    @Path("/buscar/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTiposPorNombre(@PathParam("nombre") String nombre) {
        return lista(tipoService.getTiposPorNombre(nombre.toUpperCase()));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTipo(@PathParam("id") Long id) {
        try {
            Respuesta r = tipoService.getTipo(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Tipo")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando el tipo de instrumento.").build();
        }
    }

    @POST
    @Path("/tipo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarTipo(@Valid TipoInstrumentoDto dto) {
        try {
            Respuesta r = tipoService.guardarTipo(dto);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Tipo")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error guardando el tipo de instrumento.").build();
        }
    }

    @DELETE
    @Path("/tipo/{id}")
    public Response eliminarTipo(@PathParam("id") Long id) {
        try {
            Respuesta r = tipoService.eliminarTipo(id);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error eliminando el tipo de instrumento.").build();
        }
    }

    /** Convierte una Respuesta con lista de tipos en la respuesta HTTP. */
    private Response lista(Respuesta r) {
        try {
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<TipoInstrumentoDto>>((List<TipoInstrumentoDto>) r.getResultado("Tipos")) {
            }).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando los tipos de instrumento.").build();
        }
    }
}