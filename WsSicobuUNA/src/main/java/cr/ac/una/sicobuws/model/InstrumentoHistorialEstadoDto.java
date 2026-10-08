package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO de un cambio de estado del instrumento. "descripcionEstado" es solo informativo.
 */
public class InstrumentoHistorialEstadoDto {

    private Long id;
    private Long idInstrumento;
    @NotNull(message = "El historial debe indicar el estado")
    private Long idEstado;
    private String descripcionEstado;
    @NotNull(message = "La fecha del historial no puede ser nula")
    private LocalDate fecha;
    @Size(max = 300, message = "La observación debe tener máximo 300 caracteres")
    private String observacion;

    public InstrumentoHistorialEstadoDto() {
    }

    public InstrumentoHistorialEstadoDto(InstrumentoHistorialEstado historial) {
        this.id = historial.getId();
        this.idInstrumento = historial.getInstrumento().getId();
        this.idEstado = historial.getEstado().getId();
        this.descripcionEstado = historial.getEstado().getDescripcion();
        this.fecha = historial.getFecha();
        this.observacion = historial.getObservacion();
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

    public Long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public int hashCode() {
        return 41 * 5 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((InstrumentoHistorialEstadoDto) obj).id);
    }

    @Override
    public String toString() {
        return "InstrumentoHistorialEstadoDto{" + "id=" + id + ", idEstado=" + idEstado + ", fecha=" + fecha + '}';
    }
}