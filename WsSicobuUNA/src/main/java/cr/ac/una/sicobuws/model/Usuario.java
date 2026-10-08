package cr.ac.una.sicobuws.model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Usuario del sistema. Pertenece a un bufete y tiene permisos por vista.
 * Los indicadores (activo, administrador) se guardan como NUMBER(1): 1 = sí, 0 = no.
 */
@Entity
@Table(name = "USUARIO")
@NamedQueries({
    @NamedQuery(name = "Usuario.findById", query = "SELECT u FROM Usuario u WHERE u.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Usuario.findByBufete", query = "SELECT u FROM Usuario u WHERE u.bufete.id = :idBufete ORDER BY u.nombreCompleto", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Usuario.findByBufeteUsuario", query = "SELECT u FROM Usuario u WHERE u.bufete.id = :idBufete AND UPPER(u.usuario) = :usuario", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Usuario.findByCorreo", query = "SELECT u FROM Usuario u WHERE UPPER(u.correo) = :correo", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Usuario.findByToken", query = "SELECT u FROM Usuario u WHERE u.tokenActivacion = :token", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "USUARIO_ID_GENERATOR", sequenceName = "SEQ_USUARIO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIO_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_USUARIO")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_BUFETE", referencedColumnName = "ID_BUFETE")
    private Bufete bufete;
    @Basic(optional = false)
    @Column(name = "TIPO_IDENTIFICACION")
    private String tipoIdentificacion;
    @Basic(optional = false)
    @Column(name = "NUMERO_IDENTIFICACION")
    private String numeroIdentificacion;
    @Basic(optional = false)
    @Column(name = "NOMBRE_COMPLETO")
    private String nombreCompleto;
    @Basic(optional = false)
    @Column(name = "USUARIO")
    private String usuario;
    @Basic(optional = false)
    @Column(name = "CORREO")
    private String correo;
    @Basic(optional = false)
    @Column(name = "CLAVE")
    private String clave;
    @Basic(optional = false)
    @Column(name = "IDIOMA")
    private String idioma;
    @Basic(optional = false)
    @Column(name = "CELULAR")
    private String celular;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "FOTO")
    private byte[] foto;
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private Integer activo;
    @Basic(optional = false)
    @Column(name = "ES_ADMINISTRADOR")
    private Integer esAdministrador;
    @Column(name = "TOKEN_ACTIVACION")
    private String tokenActivacion;
    @Column(name = "CLAVE_TEMPORAL")
    private String claveTemporal;
    @Version
    @Basic(optional = false)
    @Column(name = "VERSION")
    private Long version;
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UsuarioPermiso> permisos = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(Long id) {
        this.id = id;
    }

    public Usuario(UsuarioDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    /**
     * Copia los datos del DTO a la entidad. NO toca: el bufete, la clave, el
     * token de activación, la clave temporal ni los permisos; de eso se
     * encarga el service. La foto solo se reemplaza si el DTO trae una nueva.
     */
    public void actualizar(UsuarioDto dto) {
        this.tipoIdentificacion = dto.getTipoIdentificacion();
        this.numeroIdentificacion = dto.getNumeroIdentificacion();
        this.nombreCompleto = dto.getNombreCompleto();
        this.usuario = dto.getUsuario();
        this.correo = dto.getCorreo();
        this.idioma = dto.getIdioma();
        this.celular = dto.getCelular();
        this.activo = Boolean.TRUE.equals(dto.getActivo()) ? 1 : 0;
        this.esAdministrador = Boolean.TRUE.equals(dto.getAdministrador()) ? 1 : 0;
        if (dto.getFoto() != null) {
            this.foto = dto.getFoto();
        }
        this.version = dto.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bufete getBufete() {
        return bufete;
    }

    public void setBufete(Bufete bufete) {
        this.bufete = bufete;
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

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public Integer getEsAdministrador() {
        return esAdministrador;
    }

    public void setEsAdministrador(Integer esAdministrador) {
        this.esAdministrador = esAdministrador;
    }

    public String getTokenActivacion() {
        return tokenActivacion;
    }

    public void setTokenActivacion(String tokenActivacion) {
        this.tokenActivacion = tokenActivacion;
    }

    public String getClaveTemporal() {
        return claveTemporal;
    }

    public void setClaveTemporal(String claveTemporal) {
        this.claveTemporal = claveTemporal;
    }

    public List<UsuarioPermiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<UsuarioPermiso> permisos) {
        this.permisos = permisos;
    }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.Usuario[ id=" + id + " ]";
    }
}