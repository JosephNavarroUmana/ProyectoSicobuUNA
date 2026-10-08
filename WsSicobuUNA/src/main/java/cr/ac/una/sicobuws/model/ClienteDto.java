package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;

/**
 * DTO del cliente (persona física).
 */
public class ClienteDto {

    private Long id;
    @NotNull(message = "El cliente debe pertenecer a un bufete")
    private Long idBufete;
    @NotBlank(message = "La identificación del cliente no puede estar vacía")
    @Size(max = 30, message = "La identificación del cliente debe tener máximo 30 caracteres")
    private String identificacion;
    @NotBlank(message = "El tipo de identificación del cliente no puede estar vacío")
    @Size(max = 30, message = "El tipo de identificación debe tener máximo 30 caracteres")
    private String tipoIdentificacion;
    @NotBlank(message = "El nombre completo del cliente no puede estar vacío")
    @Size(max = 100, message = "El nombre completo debe tener máximo 100 caracteres")
    private String nombreCompleto;
    @Size(max = 30, message = "El estado civil debe tener máximo 30 caracteres")
    private String estadoCivil;
    @Size(max = 100, message = "La ocupación debe tener máximo 100 caracteres")
    private String ocupacion;
    @Size(max = 20, message = "El celular debe tener máximo 20 caracteres")
    private String celular;
    @Email(message = "El correo del cliente no cumple con el formato")
    @Size(max = 80, message = "El correo del cliente debe tener máximo 80 caracteres")
    private String correo;
    @Size(max = 300, message = "La dirección debe tener máximo 300 caracteres")
    private String direccion;
    @Size(max = 500, message = "Las observaciones deben tener máximo 500 caracteres")
    private String observaciones;
    private Long version;

    public ClienteDto() {
    }

    public ClienteDto(Cliente cliente) {
        this.id = cliente.getId();
        this.idBufete = cliente.getBufete().getId();
        this.identificacion = cliente.getIdentificacion();
        this.tipoIdentificacion = cliente.getTipoIdentificacion();
        this.nombreCompleto = cliente.getNombreCompleto();
        this.estadoCivil = cliente.getEstadoCivil();
        this.ocupacion = cliente.getOcupacion();
        this.celular = cliente.getCelular();
        this.correo = cliente.getCorreo();
        this.direccion = cliente.getDireccion();
        this.observaciones = cliente.getObservaciones();
        this.version = cliente.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdBufete() {
        return idBufete;
    }

    public void setIdBufete(Long idBufete) {
        this.idBufete = idBufete;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        return 61 * 3 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((ClienteDto) obj).id);
    }

    @Override
    public String toString() {
        return "ClienteDto{" + "id=" + id + ", identificacion=" + identificacion + ", nombreCompleto=" + nombreCompleto + '}';
    }
}