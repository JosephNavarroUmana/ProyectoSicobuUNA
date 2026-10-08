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
 * Representante de una sociedad: un cliente con el cargo que ejerce en ella.
 */
@Entity
@Table(name = "SOCIEDADREPRESENTANTE")
@NamedQueries({
    @NamedQuery(name = "SociedadRepresentante.findBySociedad", query = "SELECT r FROM SociedadRepresentante r WHERE r.sociedad.id = :idSociedad", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class SociedadRepresentante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SOCIEDADREPRESENTANTE_ID_GENERATOR", sequenceName = "SEQ_SOCIEDAD_REPRESENTANTE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SOCIEDADREPRESENTANTE_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_SOCIEDAD_REPRESENTANTE")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_SOCIEDAD", referencedColumnName = "ID_SOCIEDAD")
    private Sociedad sociedad;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID_CLIENTE")
    private Cliente cliente;
    @Basic(optional = false)
    @Column(name = "CARGO")
    private String cargo;

    public SociedadRepresentante() {
    }

    public SociedadRepresentante(Long id) {
        this.id = id;
    }

    /** La sociedad y el cliente los asigna el service (necesita el EntityManager). */
    public SociedadRepresentante(SociedadRepresentanteDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(SociedadRepresentanteDto dto) {
        this.cargo = dto.getCargo();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sociedad getSociedad() {
        return sociedad;
    }

    public void setSociedad(Sociedad sociedad) {
        this.sociedad = sociedad;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SociedadRepresentante)) {
            return false;
        }
        SociedadRepresentante other = (SociedadRepresentante) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.SociedadRepresentante[ id=" + id + " ]";
    }
}