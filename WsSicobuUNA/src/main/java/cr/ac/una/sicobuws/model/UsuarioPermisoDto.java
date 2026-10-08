package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;

/**
 * DTO del permiso de usuario. "nombreVista" es solo informativo para mostrarlo
 * en las tablas del cliente.
 */
public class UsuarioPermisoDto {

    private Long id;
    private Long idUsuario;
    @NotNull(message = "El permiso debe indicar la vista")
    private Long idVista;
    private String nombreVista;
    @NotBlank(message = "El tipo de permiso no puede estar vacío")
    @Size(max = 20, message = "El tipo de permiso debe tener máximo 20 caracteres")
    private String tipoPermiso;

    public UsuarioPermisoDto() {
    }

    public UsuarioPermisoDto(UsuarioPermiso permiso) {
        this.id = permiso.getId();
        this.idUsuario = permiso.getUsuario().getId();
        this.idVista = permiso.getVista().getId();
        this.nombreVista = permiso.getVista().getNombre();
        this.tipoPermiso = permiso.getTipoPermiso();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdVista() {
        return idVista;
    }

    public void setIdVista(Long idVista) {
        this.idVista = idVista;
    }

    public String getNombreVista() {
        return nombreVista;
    }

    public void setNombreVista(String nombreVista) {
        this.nombreVista = nombreVista;
    }

    public String getTipoPermiso() {
        return tipoPermiso;
    }

    public void setTipoPermiso(String tipoPermiso) {
        this.tipoPermiso = tipoPermiso;
    }

    @Override
    public int hashCode() {
        return 71 * 5 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((UsuarioPermisoDto) obj).id);
    }

    @Override
    public String toString() {
        return "UsuarioPermisoDto{" + "id=" + id + ", idVista=" + idVista + ", tipoPermiso=" + tipoPermiso + '}';
    }
}