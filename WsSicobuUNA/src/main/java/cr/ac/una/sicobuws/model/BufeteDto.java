package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DTO del bufete, con la lista de abogados propietarios y la de los que
 * se quitaron en la edición (mismo patrón que TipoPlanillaDto del profesor).
 */
public class BufeteDto {

    private Long id;
    @NotBlank(message = "El nombre del bufete no puede estar vacío")
    @Size(max = 80, message = "El nombre del bufete debe tener máximo 80 caracteres")
    private String nombre;
    @NotBlank(message = "El teléfono del bufete no puede estar vacío")
    @Size(max = 20, message = "El teléfono del bufete debe tener máximo 20 caracteres")
    private String telefono;
    @NotBlank(message = "El email del bufete no puede estar vacío")
    @Email(message = "El email del bufete no cumple con el formato")
    @Size(max = 150, message = "El email del bufete debe tener máximo 150 caracteres")
    private String email;
    @NotBlank(message = "La dirección del bufete no puede estar vacía")
    @Size(max = 300, message = "La dirección del bufete debe tener máximo 300 caracteres")
    private String direccion;
    private byte[] logo;
    private Long version;
    private List<AbogadoDto> abogados;
    private List<AbogadoDto> abogadosEliminados;

    public BufeteDto() {
        this.abogados = new ArrayList<>();
        this.abogadosEliminados = new ArrayList<>();
    }

    /** No carga los abogados: eso lo hace el service cuando hace falta. */
    public BufeteDto(Bufete bufete) {
        this();
        this.id = bufete.getId();
        this.nombre = bufete.getNombre();
        this.telefono = bufete.getTelefono();
        this.email = bufete.getEmail();
        this.direccion = bufete.getDireccion();
        this.logo = bufete.getLogo();
        this.version = bufete.getVersion();
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public List<AbogadoDto> getAbogados() {
        return abogados;
    }

    public void setAbogados(List<AbogadoDto> abogados) {
        this.abogados = abogados;
    }

    public List<AbogadoDto> getAbogadosEliminados() {
        return abogadosEliminados;
    }

    public void setAbogadosEliminados(List<AbogadoDto> abogadosEliminados) {
        this.abogadosEliminados = abogadosEliminados;
    }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    @Override
    public int hashCode() {
        return 59 * 5 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((BufeteDto) obj).id);
    }

    @Override
    public String toString() {
        return "BufeteDto{" + "id=" + id + ", nombre=" + nombre + '}';
    }
}