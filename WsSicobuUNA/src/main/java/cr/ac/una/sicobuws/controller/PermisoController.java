package cr.ac.una.sicobuws.controller;

import cr.ac.una.sicobuws.model.UsuarioPermisoDto;
import cr.ac.una.sicobuws.service.UsuarioService;
import cr.ac.una.sicobuws.util.CodigoRespuesta;
import cr.ac.una.sicobuws.util.Respuesta;
import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
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
 * Consulta de permisos por usuario (se guardan junto con el usuario).
 * GET /Permisos/usuario/{idUsuario}
 */
@Path("/Permisos")
public class PermisoController {

    private static final Logger LOG = Logger.getLogger(PermisoController.class.getName());

    @EJB
    UsuarioService usuarioService;

    @GET
    @Path("/usuario/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPermisos(@PathParam("idUsuario") Long idUsuario) {
        try {
            Respuesta r = usuarioService.getPermisos(idUsuario);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<UsuarioPermisoDto>>((List<UsuarioPermisoDto>) r.getResultado("Permisos")) {
            }).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando los permisos.").build();
        }
    }
}