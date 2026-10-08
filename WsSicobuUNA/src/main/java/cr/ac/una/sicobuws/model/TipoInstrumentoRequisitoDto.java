package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;

/**
 * DTO del requisito de un tipo de instrumento.
 */
public class TipoInstrumentoRequisitoDto {

    private Long id;
    private Long idTipoInstrumento;
    @NotBlank(message = "La descripción del requisito no puede estar vacía")
    @Size(max = 300, message = "La descripción del requisito debe tener máximo 300 caracteres")
    private String descripcion;
    @NotNull(message = "El orden del requisito no puede ser nulo")
    @Min(value = 1, message = "El orden del requisito debe ser mayor o igual a 1")
    private Integer orden;

    public TipoInstrumentoRequisitoDto() {
    }

    public TipoInstrumentoRequisitoDto(TipoInstrumentoRequisito requisito) {
        this.id = requisito.getId();
        this.idTipoInstrumento = requisito.getTipoInstrumento().getId();
        this.descripcion = requisito.getDescripcion();
        this.orden = requisito.getOrden();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdTipoInstrumento() {
        return idTipoInstrumento;
    }

    public void setIdTipoInstrumento(Long idTipoInstrumento) {
        this.idTipoInstrumento = idTipoInstrumento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    @Override
    public int hashCode() {
        return 73 * 5 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((TipoInstrumentoRequisitoDto) obj).id);
    }

    @Override
    public String toString() {
        return "TipoInstrumentoRequisitoDto{" + "id=" + id + ", descripcion=" + descripcion + ", orden=" + orden + '}';
    }
}