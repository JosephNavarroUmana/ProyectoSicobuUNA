package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;

/**
 * DTO del parámetro general del sistema.
 */
public class ParametroDto {

    private Long id;
    @NotBlank(message = "El nombre del parámetro no puede estar vacío")
    @Size(max = 100, message = "El nombre del parámetro debe tener máximo 100 caracteres")
    private String nombre;
    @NotBlank(message = "El valor del parámetro no puede estar vacío")
    @Size(max = 500, message = "El valor del parámetro debe tener máximo 500 caracteres")
    private String valor;
    @NotBlank(message = "La descripción del parámetro no puede estar vacía")
    @Size(max = 300, message = "La descripción del parámetro debe tener máximo 300 caracteres")
    private String descripcion;

    public ParametroDto() {
    }

    public ParametroDto(Parametro parametro) {
        this.id = parametro.getId();
        this.nombre = parametro.getNombre();
        this.valor = parametro.getValor();
        this.descripcion = parametro.getDescripcion();
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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        return 19 * 7 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((ParametroDto) obj).id);
    }

    @Override
    public String toString() {
        return "ParametroDto{" + "id=" + id + ", nombre=" + nombre + '}';
    }
}