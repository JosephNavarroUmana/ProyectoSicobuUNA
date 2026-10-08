package cr.ac.una.sicobuws.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * Permiso de un usuario sobre una vista (el tipo de permiso indica qué puede hacer).
 */
@Entity
@Table(name = "USUARIOPERMISO")
@NamedQueries({
    @NamedQuery(name = "UsuarioPermiso.findByUsuario", query = "SELECT p FROM UsuarioPermiso p WHERE p.usuario.id = :idUsuario", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class UsuarioPermiso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "USUARIOPERMISO_ID_GENERATOR", sequenceName = "SEQ_USUARIO_PERMISO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIOPERMISO_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_USUARIO_PERMISO")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    private Usuario usuario;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_VISTA", referencedColumnName = "ID_VISTA")
    private Vista vista;
    @Basic(optional = false)
    @Column(name = "TIPO_PERMISO")
    private String tipoPermiso;

    public UsuarioPermiso() {
    }

    public UsuarioPermiso(Long id) {
        this.id = id;
    }

    /** El usuario y la vista los asigna el service (necesita el EntityManager). */
    public UsuarioPermiso(UsuarioPermisoDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(UsuarioPermisoDto dto) {
        this.tipoPermiso = dto.getTipoPermiso();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Vista getVista() {
        return vista;
    }

    public void setVista(Vista vista) {
        this.vista = vista;
    }

    public String getTipoPermiso() {
        return tipoPermiso;
    }

    public void setTipoPermiso(String tipoPermiso) {
        this.tipoPermiso = tipoPermiso;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UsuarioPermiso)) {
            return false;
        }
        UsuarioPermiso other = (UsuarioPermiso) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.UsuarioPermiso[ id=" + id + " ]";
    }
}