package cr.ac.una.sicobuws;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configuración de Jakarta REST. Todos los servicios quedan bajo /ws.
 * Al no sobrescribir getClasses(), el servidor escanea el war y registra
 * automáticamente los @Path (controllers) y los @Provider (mappers, filtros).
 */
@ApplicationPath("ws")
public class JakartaRestConfiguration extends Application {
}