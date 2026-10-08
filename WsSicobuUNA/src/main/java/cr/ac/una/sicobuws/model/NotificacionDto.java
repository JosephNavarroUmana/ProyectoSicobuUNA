package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO de la notificación (correo enviado). idInstrumento puede ser null.
 */
public class NotificacionDto {

    private Long id;
    private Long idInstrumento;
    @NotBlank(message = "El destinatario no puede estar vacío")
    @Email(message = "El destinatario no cumple con el formato de correo")
    @Size(max = 150, message = "El destinatario debe tener máximo 150 caracteres")
    private String destinatario;
    @NotBlank(message = "El asunto no puede estar vacío")
    @Size(max = 200, message = "El asunto debe tener máximo 200 caracteres")
    private String asunto;
    @NotNull(message = "La fecha de envío no puede ser nula")
    private LocalDateTime fechaEnvio;
    @NotBlank(message = "El tipo de notificación no puede estar vacío")
    @Size(max = 30, message = "El tipo de notificación debe tener máximo 30 caracteres")
    private String tipo;

    public NotificacionDto() {
        this.fechaEnvio = LocalDateTime.now();
    }

    public NotificacionDto(Notificacion notificacion) {
        this();
        this.id = notificacion.getId();
        this.idInstrumento = notificacion.getInstrumento() != null ? notificacion.getInstrumento().getId() : null;
        this.destinatario = notificacion.getDestinatario();
        this.asunto = notificacion.getAsunto();
        this.fechaEnvio = notificacion.getFechaEnvio();
        this.tipo = notificacion.getTipo();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdInstrumento() {
        return idInstrumento;
    }

    public void setIdInstrumento(Long idInstrumento) {
        this.idInstrumento = idInstrumento;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        return 17 * 7 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((NotificacionDto) obj).id);
    }

    @Override
    public String toString() {
        return "NotificacionDto{" + "id=" + id + ", destinatario=" + destinatario + ", tipo=" + tipo + '}';
    }
}