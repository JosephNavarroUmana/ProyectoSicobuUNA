package cr.ac.una.sicobuws.controller;

import cr.ac.una.sicobuws.model.UsuarioDto;
import cr.ac.una.sicobuws.service.UsuarioService;
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
 * Servicios REST de usuarios.
 * TEMPORAL (sin JWT): el idBufete llega por ?idBufete= y dentro del DTO al guardar.
 * GET /Usuarios · GET /Usuarios/{id} · POST /Usuarios/usuario · DELETE /Usuarios/usuario/{id}
 * GET /Usuarios/activar/{token} (enlace del correo; debe quedar fuera del JWT)
 */
@Path("/Usuarios")
public class UsuarioController {

    private static final Logger LOG = Logger.getLogger(UsuarioController.class.getName());

    @EJB
    UsuarioService usuarioService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarios(@QueryParam("idBufete") Long idBufete) {
        try {
            Respuesta r = usuarioService.getUsuarios(idBufete);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<UsuarioDto>>((List<UsuarioDto>) r.getResultado("Usuarios")) {
            }).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando los usuarios.").build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuario(@PathParam("id") Long id, @QueryParam("idBufete") Long idBufete) {
        try {
            Respuesta r = usuarioService.getUsuario(id, idBufete);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Usuario")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error consultando el usuario.").build();
        }
    }

    @POST
    @Path("/usuario")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarUsuario(@Valid UsuarioDto dto) {
        try {
            // TODO JWT: reemplazar dto.getIdBufete() por el idBufete del token
            Respuesta r = usuarioService.guardarUsuario(dto, dto.getIdBufete());
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok(r.getResultado("Usuario")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error guardando el usuario.").build();
        }
    }

    @DELETE
    @Path("/usuario/{id}")
    public Response eliminarUsuario(@PathParam("id") Long id, @QueryParam("idBufete") Long idBufete) {
        try {
            Respuesta r = usuarioService.eliminarUsuario(id, idBufete);
            if (!r.getEstado()) {
                return Response.status(r.getCodigoRespuesta().getValue()).entity(r.getMensaje()).build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error eliminando el usuario.").build();
        }
    }

    /** Enlace de activación que llega por correo. Devuelve una página HTML sencilla. */
    @GET
    @Path("/activar/{token}")
    @Produces(MediaType.TEXT_HTML + "; charset=UTF-8")
    public Response activarUsuario(@PathParam("token") String token) {
        try {
            Respuesta r = usuarioService.activarUsuario(token);
            String mensaje = r.getEstado() ? "Su cuenta fue activada. Ya puede ingresar al sistema." : r.getMensaje();
            return Response.status(r.getEstado() ? 200 : r.getCodigoRespuesta().getValue())
                    .entity("<html><body style=\"font-family:sans-serif;text-align:center;margin-top:15%\"><h2>SicobuUNA</h2><p>"
                            + mensaje + "</p></body></html>").build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error activando la cuenta.").build();
        }
    }
}