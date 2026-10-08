package cr.ac.una.sicobuws.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * Parámetro general del sistema (ruta de documentos, servidor SMTP, etc.).
 * Es global: no pertenece a un bufete. El nombre es único.
 */
@Entity
@Table(name = "PARAMETRO")
@NamedQueries({
    @NamedQuery(name = "Parametro.findAll", query = "SELECT p FROM Parametro p ORDER BY p.nombre", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Parametro.findById", query = "SELECT p FROM Parametro p WHERE p.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Parametro.findByNombre", query = "SELECT p FROM Parametro p WHERE p.nombre = :nombre", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class Parametro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PARAMETRO_ID_GENERATOR", sequenceName = "SEQ_PARAMETRO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARAMETRO_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_PARAMETRO")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "VALOR")
    private String valor;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;

    public Parametro() {
    }

    public Parametro(Long id) {
        this.id = id;
    }

    public Parametro(ParametroDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(ParametroDto dto) {
        this.nombre = dto.getNombre();
        this.valor = dto.getValor();
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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
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
        if (!(object instanceof Parametro)) {
            return false;
        }
        Parametro other = (Parametro) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.Parametro[ id=" + id + " ]";
    }
}