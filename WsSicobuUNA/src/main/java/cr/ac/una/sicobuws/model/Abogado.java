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
import jakarta.persistence.Version;
import java.io.Serializable;

/**
 * Abogado. Se relaciona con el bufete mediante la tabla BUFETEABOGADO
 * (la relación se define en la entidad Bufete).
 */
@Entity
@Table(name = "ABOGADO")
@NamedQueries({
    @NamedQuery(name = "Abogado.findAll", query = "SELECT a FROM Abogado a ORDER BY a.nombre"),
    @NamedQuery(name = "Abogado.findById", query = "SELECT a FROM Abogado a WHERE a.id = :id"),
    @NamedQuery(name = "Abogado.findByCedula", query = "SELECT a FROM Abogado a WHERE UPPER(a.cedula) = :cedula"),
    @NamedQuery(name = "Abogado.findByNombreCedula", query = "SELECT a FROM Abogado a WHERE UPPER(a.nombre) LIKE :nombre AND UPPER(a.cedula) LIKE :cedula ORDER BY a.nombre")
})
public class Abogado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ABOGADO_ID_GENERATOR", sequenceName = "SEQ_ABOGADO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ABOGADO_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_ABOGADO")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "CEDULA")
    private String cedula;
    @Column(name = "TELEFONO")
    private String telefono;
    @Basic(optional = false)
    @Column(name = "CELULAR")
    private String celular;
    @Basic(optional = false)
    @Column(name = "CORREO")
    private String correo;
    @Column(name = "DIRECCION")
    private String direccion;
    @Version
    @Basic(optional = false)
    @Column(name = "VERSION")
    private Long version;

    public Abogado() {
    }

    public Abogado(Long id) {
        this.id = id;
    }

    public Abogado(AbogadoDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    /** Copia los datos del DTO a la entidad (sin tocar el id). */
    public void actualizar(AbogadoDto dto) {
        this.nombre = dto.getNombre();
        this.cedula = dto.getCedula();
        this.telefono = dto.getTelefono();
        this.celular = dto.getCelular();
        this.correo = dto.getCorreo();
        this.direccion = dto.getDireccion();
        this.version = dto.getVersion();
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
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }


    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Abogado)) {
            return false;
        }
        Abogado other = (Abogado) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.Abogado[ id=" + id + " ]";
    }
}