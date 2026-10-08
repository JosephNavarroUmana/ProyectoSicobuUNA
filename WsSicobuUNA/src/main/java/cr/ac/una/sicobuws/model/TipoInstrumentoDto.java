package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DTO del tipo de instrumento, con la lista de sus requisitos (ordenados).
 * "machote" es la ruta del .docx; null significa "no cambiar el machote".
 */
public class TipoInstrumentoDto {

    private Long id;
    @NotBlank(message = "El nombre del tipo de instrumento no puede estar vacío")
    @Size(max = 150, message = "El nombre del tipo de instrumento debe tener máximo 150 caracteres")
    private String nombre;
    private Boolean activo;
    @Size(max = 300, message = "La ruta del machote debe tener máximo 300 caracteres")
    private String machote;
    private Long version;
    private List<TipoInstrumentoRequisitoDto> requisitos;

    public TipoInstrumentoDto() {
        this.activo = true;
        this.requisitos = new ArrayList<>();
    }

    /** No carga los requisitos: los carga el service cuando hace falta. */
    public TipoInstrumentoDto(TipoInstrumento tipo) {
        this();
        this.id = tipo.getId();
        this.nombre = tipo.getNombre();
        this.activo = tipo.getEstado() != null && tipo.getEstado() == 1;
        this.machote = tipo.getMachote();
        this.version = tipo.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getMachote() {
        return machote;
    }

    public void setMachote(String machote) {
        this.machote = machote;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<TipoInstrumentoRequisitoDto> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(List<TipoInstrumentoRequisitoDto> requisitos) {
        this.requisitos = requisitos;
    }

    @Override
    public int hashCode() {
        return 61 * 7 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((TipoInstrumentoDto) obj).id);
    }

    @Override
    public String toString() {
        return "TipoInstrumentoDto{" + "id=" + id + ", nombre=" + nombre + '}';
    }
}