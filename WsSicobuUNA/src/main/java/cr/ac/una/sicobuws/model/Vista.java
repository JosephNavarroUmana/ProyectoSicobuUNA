package cr.ac.una.sicobuws.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * Vista (pantalla) del sistema sobre la que se asignan permisos a los usuarios.
 */
@Entity
@Table(name = "VISTA")
@NamedQueries({
    @NamedQuery(name = "Vista.findAll", query = "SELECT v FROM Vista v ORDER BY v.nombre"),
    @NamedQuery(name = "Vista.findById", query = "SELECT v FROM Vista v WHERE v.id = :id")
})
public class Vista implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "VISTA_ID_GENERATOR", sequenceName = "SEQ_VISTA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VISTA_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_VISTA")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "DESCRIPCION")
    private String descripcion;

    public Vista() {
    }

    public Vista(Long id) {
        this.id = id;
    }

    public Vista(VistaDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(VistaDto dto) {
        this.nombre = dto.getNombre();
        this.descripcion = dto.getDescripcion();
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
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Vista)) {
            return false;
        }
        Vista other = (Vista) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.Vista[ id=" + id + " ]";
    }
}