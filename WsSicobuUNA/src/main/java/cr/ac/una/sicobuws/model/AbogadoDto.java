package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;

/**
 * DTO del abogado. El campo "modificado" lo usa el cliente para saber
 * si el abogado se agregó a la lista del bufete en esta edición.
 */
public class AbogadoDto {

    private Long id;
    @NotBlank(message = "El nombre del abogado no puede estar vacío")
    @Size(max = 150, message = "El nombre del abogado debe tener máximo 150 caracteres")
    private String nombre;
    @NotBlank(message = "La cédula del abogado no puede estar vacía")
    @Size(max = 30, message = "La cédula del abogado debe tener máximo 30 caracteres")
    private String cedula;
    @Size(max = 20, message = "El teléfono del abogado debe tener máximo 20 caracteres")
    private String telefono;
    @NotBlank(message = "El celular del abogado no puede estar vacío")
    @Size(max = 20, message = "El celular del abogado debe tener máximo 20 caracteres")
    private String celular;
    @NotBlank(message = "El correo del abogado no puede estar vacío")
    @Email(message = "El correo del abogado no cumple con el formato")
    @Size(max = 150, message = "El correo del abogado debe tener máximo 150 caracteres")
    private String correo;
    @Size(max = 300, message = "La dirección del abogado debe tener máximo 300 caracteres")
    private String direccion;
    private Boolean modificado;
    private Long version;

    public AbogadoDto() {
        this.modificado = false;
    }

    public AbogadoDto(Abogado abogado) {
        this();
        this.id = abogado.getId();
        this.nombre = abogado.getNombre();
        this.cedula = abogado.getCedula();
        this.telefono = abogado.getTelefono();
        this.celular = abogado.getCelular();
        this.correo = abogado.getCorreo();
        this.direccion = abogado.getDireccion();
        this.version = abogado.getVersion();
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

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getModificado() {
        return modificado;
    }

    public void setModificado(Boolean modificado) {
        this.modificado = modificado;
    }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    @Override
    public int hashCode() {
        return 59 * 7 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((AbogadoDto) obj).id);
    }

    @Override
    public String toString() {
        return "AbogadoDto{" + "id=" + id + ", nombre=" + nombre + ", cedula=" + cedula + '}';
    }
}