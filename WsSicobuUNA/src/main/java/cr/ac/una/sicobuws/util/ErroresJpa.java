package cr.ac.una.sicobuws.util;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Utilidades para identificar el tipo de error de JPA/BD.
 * Las excepciones llegan envueltas (EJBException, PersistenceException...),
 * por eso se recorre toda la cadena de causas.
 */
public final class ErroresJpa {

    private ErroresJpa() {
    }

    /** Indica si algún eslabón de la cadena es un bloqueo optimista (otro usuario modificó el registro). */
    public static boolean esBloqueoOptimista(Throwable ex) {
        Throwable actual = ex;
        int limite = 0; // evita ciclos infinitos en cadenas raras
        while (actual != null && limite++ < 20) {
            if (actual instanceof jakarta.persistence.OptimisticLockException
                    || actual.getClass().getSimpleName().equals("OptimisticLockException")) {
                return true;
            }
            actual = actual.getCause();
        }
        return false;
    }

    /** Indica si es una violación de integridad (FK, único, etc.). */
    public static boolean esViolacionIntegridad(Throwable ex) {
        Throwable actual = ex;
        int limite = 0;
        while (actual != null && limite++ < 20) {
            if (actual instanceof SQLIntegrityConstraintViolationException) {
                return true;
            }
            actual = actual.getCause();
        }
        return false;
    }
}