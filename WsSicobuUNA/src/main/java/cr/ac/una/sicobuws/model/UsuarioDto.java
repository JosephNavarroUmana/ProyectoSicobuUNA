package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DTO del usuario.
 * - "clave" solo viaja del cliente al servidor (al crear o cambiar la clave).
 *   El servidor nunca la devuelve: el constructor desde la entidad no la copia.
 *   Si llega vacía al editar, significa "no cambiar la clave".
 * - El token de activación y la clave temporal no se exponen.
 */
public class UsuarioDto {

    private Long id;
    @NotNull(message = "El usuario debe pertenecer a un bufete")
    private Long idBufete;
    @NotBlank(message = "El tipo de identificación no puede estar vacío")
    @Size(max = 30, message = "El tipo de identificación debe tener máximo 30 caracteres")
    private String tipoIdentificacion;
    @NotBlank(message = "El número de identificación no puede estar vacío")
    @Size(max = 30, message = "El número de identificación debe tener máximo 30 caracteres")
    private String numeroIdentificacion;
    @NotBlank(message = "El nombre completo no puede estar vacío")
    @Size(max = 100, message = "El nombre completo debe tener máximo 100 caracteres")
    private String nombreCompleto;
    @NotBlank(message = "El usuario no puede estar vacío")
    @Size(max = 50, message = "El usuario debe tener máximo 50 caracteres")
    private String usuario;
    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El correo no cumple con el formato")
    @Size(max = 150, message = "El correo debe tener máximo 150 caracteres")
    private String correo;
    private String clave;
    @NotBlank(message = "El idioma no puede estar vacío")
    @Size(max = 10, message = "El idioma debe tener máximo 10 caracteres")
    private String idioma;
    @NotBlank(message = "El celular no puede estar vacío")
    @Size(max = 20, message = "El celular debe tener máximo 20 caracteres")
    private String celular;
    private byte[] foto;
    private Boolean activo;
    private Boolean administrador;
    private List<UsuarioPermisoDto> permisos;
    private Long version;
    
    public UsuarioDto() {
        this.activo = false;
        this.administrador = false;
        this.permisos = new ArrayList<>();
    }

    /** No copia clave, token ni clave temporal. Los permisos los carga el service. */
    public UsuarioDto(Usuario usuario) {
        this();
        this.id = usuario.getId();
        this.idBufete = usuario.getBufete().getId();
        this.tipoIdentificacion = usuario.getTipoIdentificacion();
        this.numeroIdentificacion = usuario.getNumeroIdentificacion();
        this.nombreCompleto = usuario.getNombreCompleto();
        this.usuario = usuario.getUsuario();
        this.correo = usuario.getCorreo();
        this.idioma = usuario.getIdioma();
        this.celular = usuario.getCelular();
        this.foto = usuario.getFoto();
        this.activo = usuario.getActivo() != null && usuario.getActivo() == 1;
        this.administrador = usuario.getEsAdministrador() != null && usuario.getEsAdministrador() == 1;
        this.version = usuario.getVersion(); 
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

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
    }

    public List<UsuarioPermisoDto> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<UsuarioPermisoDto> permisos) {
        this.permisos = permisos;
    }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    @Override
    public int hashCode() {
        return 97 * 7 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((UsuarioDto) obj).id);
    }

    @Override
    public String toString() {
        return "UsuarioDto{" + "id=" + id + ", usuario=" + usuario + ", nombreCompleto=" + nombreCompleto + '}';
    }
}