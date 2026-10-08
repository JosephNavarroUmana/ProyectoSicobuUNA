package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO del evento de un instrumento. "tomoInstrumento" y "escrituraInstrumento"
 * son solo informativos para mostrar el resumen en la agenda.
 */
public class InstrumentoEventoDto {

    private Long id;
    private Long idInstrumento;
    private String tomoInstrumento;
    private String escrituraInstrumento;
    @NotBlank(message = "El detalle del evento no puede estar vacío")
    @Size(max = 300, message = "El detalle del evento debe tener máximo 300 caracteres")
    private String detalle;
    @NotNull(message = "La fecha y hora del evento no pueden ser nulas")
    private LocalDateTime fechaHora;
    private Boolean atendido;
    @Pattern(regexp = "^(PENDIENTE|ATENDIDO|VENCIDO)$", message = "El estado del evento no cumple con el formato")
    private String estado;

    public InstrumentoEventoDto() {
        this.atendido = false;
        this.estado = "PENDIENTE";
    }

    public InstrumentoEventoDto(InstrumentoEvento evento) {
        this();
        this.id = evento.getId();
        this.idInstrumento = evento.getInstrumento().getId();
        this.tomoInstrumento = evento.getInstrumento().getTomo();
        this.escrituraInstrumento = evento.getInstrumento().getEscritura();
        this.detalle = evento.getDetalle();
        this.fechaHora = evento.getFechaHora();
        this.atendido = evento.getAtendido() != null && evento.getAtendido() == 1;
        this.estado = evento.getEstado();
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

    public String getTomoInstrumento() {
        return tomoInstrumento;
    }

    public void setTomoInstrumento(String tomoInstrumento) {
        this.tomoInstrumento = tomoInstrumento;
    }

    public String getEscrituraInstrumento() {
        return escrituraInstrumento;
    }

    public void setEscrituraInstrumento(String escrituraInstrumento) {
        this.escrituraInstrumento = escrituraInstrumento;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Boolean getAtendido() {
        return atendido;
    }

    public void setAtendido(Boolean atendido) {
        this.atendido = atendido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        return 31 * 7 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((InstrumentoEventoDto) obj).id);
    }

    @Override
    public String toString() {
        return "InstrumentoEventoDto{" + "id=" + id + ", detalle=" + detalle + ", fechaHora=" + fechaHora + '}';
    }
}