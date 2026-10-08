package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;

/**
 * DTO del representante de una sociedad. "identificacionCliente" y
 * "nombreCliente" son solo informativos para mostrar en las tablas del cliente.
 */
public class SociedadRepresentanteDto {

    private Long id;
    private Long idSociedad;
    @NotNull(message = "El representante debe ser un cliente")
    private Long idCliente;
    private String identificacionCliente;
    private String nombreCliente;
    @NotBlank(message = "El cargo del representante no puede estar vacío")
    @Size(max = 30, message = "El cargo debe tener máximo 30 caracteres")
    private String cargo;

    public SociedadRepresentanteDto() {
    }

    public SociedadRepresentanteDto(SociedadRepresentante representante) {
        this.id = representante.getId();
        this.idSociedad = representante.getSociedad().getId();
        this.idCliente = representante.getCliente().getId();
        this.identificacionCliente = representante.getCliente().getIdentificacion();
        this.nombreCliente = representante.getCliente().getNombreCompleto();
        this.cargo = representante.getCargo();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdSociedad() {
        return idSociedad;
    }

    public void setIdSociedad(Long idSociedad) {
        this.idSociedad = idSociedad;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdentificacionCliente() {
        return identificacionCliente;
    }

    public void setIdentificacionCliente(String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public int hashCode() {
        return 67 * 5 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((SociedadRepresentanteDto) obj).id);
    }

    @Override
    public String toString() {
        return "SociedadRepresentanteDto{" + "id=" + id + ", idCliente=" + idCliente + ", cargo=" + cargo + '}';
    }
}