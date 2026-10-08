package cr.ac.una.sicobuws.util;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Resultado estándar que devuelven los services al controller.
 * Lleva el estado, el código HTTP, los mensajes y los resultados nombrados.
 */
public class Respuesta implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean estado;
    private CodigoRespuesta codigoRespuesta;
    private String mensaje;
    private String mensajeInterno;
    private HashMap<String, Object> resultado;

    public Respuesta() {
        this.resultado = new HashMap<>();
    }

    public Respuesta(Boolean estado, CodigoRespuesta codigoRespuesta, String mensaje, String mensajeInterno) {
        this.estado = estado;
        this.codigoRespuesta = codigoRespuesta;
        this.mensaje = mensaje;
        this.mensajeInterno = mensajeInterno;
        this.resultado = new HashMap<>();
    }

    public Respuesta(Boolean estado, CodigoRespuesta codigoRespuesta, String mensaje, String mensajeInterno, String nombre, Object resultado) {
        this(estado, codigoRespuesta, mensaje, mensajeInterno);
        this.resultado.put(nombre, resultado);
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public CodigoRespuesta getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(CodigoRespuesta codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensajeInterno() {
        return mensajeInterno;
    }

    public void setMensajeInterno(String mensajeInterno) {
        this.mensajeInterno = mensajeInterno;
    }

    public Object getResultado(String nombre) {
        return resultado.get(nombre);
    }

    public void setResultado(String nombre, Object resultado) {
        this.resultado.put(nombre, resultado);
    }
}