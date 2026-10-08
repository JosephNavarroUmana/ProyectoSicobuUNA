package cr.ac.una.sicobuws.util;

/**
 * Valores fijos (catálogos) que la base de datos no restringe.
 * Se usan como constantes String para que coincidan con las columnas VARCHAR2.
 * Los mismos valores deben usarse en el cliente (puede copiarse esta clase).
 */
public final class Catalogos {

    private Catalogos() {
    }

    // INSTRUMENTOPARTE.TIPO_PERSONA
    public static final String PERSONA_FISICA = "FISICA";
    public static final String PERSONA_JURIDICA = "JURIDICA";

    // INSTRUMENTOPARTE.PAPEL
    public static final String PAPEL_CEDENTE = "CEDENTE";
    public static final String PAPEL_CESIONARIO = "CESIONARIO";

    // SOCIEDAD.TIPO_ACCIONES
    public static final String ACCIONES_COMUN = "COMUN";
    public static final String ACCIONES_PREFERENTE = "PREFERENTE";

    // MOVIMIENTOVARIO.TIPO
    public static final String MOV_INGRESO = "INGRESO";
    public static final String MOV_GASTO = "GASTO";

    // USUARIOPERMISO.TIPO_PERMISO
    public static final String PERMISO_CONSULTAR = "CONSULTAR";
    public static final String PERMISO_INSERTAR = "INSERTAR";
    public static final String PERMISO_MODIFICAR = "MODIFICAR";
    public static final String PERMISO_ELIMINAR = "ELIMINAR";

    // USUARIO.IDIOMA
    public static final String IDIOMA_ES = "es";
    public static final String IDIOMA_EN = "en";

    // INSTRUMENTOEVENTO.ESTADO (la agenda usa un color por cada uno)
    public static final String EVENTO_PENDIENTE = "PENDIENTE";
    public static final String EVENTO_ATENDIDO = "ATENDIDO";
    public static final String EVENTO_VENCIDO = "VENCIDO";

    // NOTIFICACION.TIPO
    public static final String NOTIF_ACTIVACION = "ACTIVACION";
    public static final String NOTIF_RECUPERAR_CLAVE = "RECUPERAR_CLAVE";
    public static final String NOTIF_INSTRUMENTO_CREADO = "INSTRUMENTO_CREADO";
    public static final String NOTIF_CAMBIO_ESTADO = "CAMBIO_ESTADO";
    public static final String NOTIF_PAGO_REGISTRADO = "PAGO_REGISTRADO";
}