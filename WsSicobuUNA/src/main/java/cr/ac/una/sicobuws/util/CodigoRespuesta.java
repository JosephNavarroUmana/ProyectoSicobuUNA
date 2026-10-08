package cr.ac.una.sicobuws.util;

/**
 * Códigos HTTP que usa el servidor para responder al cliente.
 */
public enum CodigoRespuesta {
    CORRECTO(200),
    ERROR_CLIENTE(400),
    ERROR_NO_AUTENTICADO(401),   // token JWT ausente, inválido o vencido
    ERROR_ACCESO(403),           // autenticado pero sin permiso para la vista/acción
    ERROR_NOENCONTRADO(404),
    ERROR_INTERNO(500);

    private Integer value;

    private CodigoRespuesta(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}