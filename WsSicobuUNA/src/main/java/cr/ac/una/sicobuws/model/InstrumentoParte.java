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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parte de un instrumento: un cliente (persona física) O una sociedad (nunca
 * ambos ni ninguno, lo garantiza un CHECK en la BD), con su papel y el monto que debe pagar.
 */
@Entity
@Table(name = "INSTRUMENTOPARTE")
@NamedQueries({
    @NamedQuery(name = "InstrumentoParte.findById", query = "SELECT p FROM InstrumentoParte p WHERE p.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "InstrumentoParte.findByInstrumento", query = "SELECT p FROM InstrumentoParte p WHERE p.instrumento.id = :idInstrumento ORDER BY p.id", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class InstrumentoParte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "INSTRUMENTOPARTE_ID_GENERATOR", sequenceName = "SEQ_INSTRUMENTO_PARTE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSTRUMENTOPARTE_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_PARTE")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_INSTRUMENTO", referencedColumnName = "ID_INSTRUMENTO")
    private Instrumento instrumento;
    /** Cliente (persona física); null si la parte es una sociedad. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID_CLIENTE")
    private Cliente cliente;
    /** Sociedad (persona jurídica); null si la parte es un cliente. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SOCIEDAD", referencedColumnName = "ID_SOCIEDAD")
    private Sociedad sociedad;
    @Basic(optional = false)
    @Column(name = "TIPO_PERSONA")
    private String tipoPersona;
    @Basic(optional = false)
    @Column(name = "PAPEL")
    private String papel;
    @Column(name = "MONTO_A_PAGAR")
    private BigDecimal montoAPagar;
    @OneToMany(mappedBy = "parte", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("fecha ASC, id ASC")
    private List<InstrumentoPago> pagos = new ArrayList<>();

    public InstrumentoParte() {
    }

    public InstrumentoParte(Long id) {
        this.id = id;
    }

    /** El instrumento, el cliente/sociedad y los pagos los asigna el service. */
    public InstrumentoParte(InstrumentoParteDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(InstrumentoParteDto dto) {
        this.tipoPersona = dto.getTipoPersona();
        this.papel = dto.getPapel();
        this.montoAPagar = dto.getMontoAPagar();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instrumento getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(Instrumento instrumento) {
        this.instrumento = instrumento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Sociedad getSociedad() {
        return sociedad;
    }

    public void setSociedad(Sociedad sociedad) {
        this.sociedad = sociedad;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public BigDecimal getMontoAPagar() {
        return montoAPagar;
    }

    public void setMontoAPagar(BigDecimal montoAPagar) {
        this.montoAPagar = montoAPagar;
    }

    public List<InstrumentoPago> getPagos() {
        return pagos;
    }

    public void setPagos(List<InstrumentoPago> pagos) {
        this.pagos = pagos;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InstrumentoParte)) {
            return false;
        }
        InstrumentoParte other = (InstrumentoParte) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.InstrumentoParte[ id=" + id + " ]";
    }
}