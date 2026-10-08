package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;

/**
 * DTO de la vista (pantalla) del sistema.
 */
public class VistaDto {

    private Long id;
    @NotBlank(message = "El nombre de la vista no puede estar vacío")
    @Size(max = 100, message = "El nombre de la vista debe tener máximo 100 caracteres")
    private String nombre;
    @Size(max = 300, message = "La descripción de la vista debe tener máximo 300 caracteres")
    private String descripcion;

    public VistaDto() {
    }

    public VistaDto(Vista vista) {
        this.id = vista.getId();
        this.nombre = vista.getNombre();
        this.descripcion = vista.getDescripcion();
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        return 83 * 3 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((VistaDto) obj).id);
    }

    @Override
    public String toString() {
        return "VistaDto{" + "id=" + id + ", nombre=" + nombre + '}';
    }
}