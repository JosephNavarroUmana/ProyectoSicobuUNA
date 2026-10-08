package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;

/**
 * DTO del estado de instrumento.
 */
public class EstadoInstrumentoDto {

    private Long id;
    @NotBlank(message = "La descripción del estado no puede estar vacía")
    @Size(max = 100, message = "La descripción del estado debe tener máximo 100 caracteres")
    private String descripcion;
    private Boolean activo;
    private Boolean notifica;
    private Boolean esFinal;

    public EstadoInstrumentoDto() {
        this.activo = true;
        this.notifica = false;
        this.esFinal = false;
    }

    public EstadoInstrumentoDto(EstadoInstrumento estado) {
        this();
        this.id = estado.getId();
        this.descripcion = estado.getDescripcion();
        this.activo = estado.getActivo() != null && estado.getActivo() == 1;
        this.notifica = estado.getNotifica() != null && estado.getNotifica() == 1;
        this.esFinal = estado.getEsFinal() != null && estado.getEsFinal() == 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getNotifica() {
        return notifica;
    }

    public void setNotifica(Boolean notifica) {
        this.notifica = notifica;
    }

    public Boolean getEsFinal() {
        return esFinal;
    }

    public void setEsFinal(Boolean esFinal) {
        this.esFinal = esFinal;
    }

    @Override
    public int hashCode() {
        return 79 * 3 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((EstadoInstrumentoDto) obj).id);
    }

    @Override
    public String toString() {
        return "EstadoInstrumentoDto{" + "id=" + id + ", descripcion=" + descripcion + '}';
    }
}