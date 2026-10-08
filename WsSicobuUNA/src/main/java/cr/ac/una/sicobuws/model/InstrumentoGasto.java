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
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Gasto de un instrumento (módulo contable). "tributa" es 1 = sí, 0 = no.
 */
@Entity
@Table(name = "INSTRUMENTOGASTO")
@NamedQueries({
    @NamedQuery(name = "InstrumentoGasto.findByInstrumento", query = "SELECT g FROM InstrumentoGasto g WHERE g.instrumento.id = :idInstrumento ORDER BY g.fecha, g.id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "InstrumentoGasto.findByBufeteRango", query = "SELECT g FROM InstrumentoGasto g WHERE g.instrumento.bufete.id = :idBufete AND g.fecha BETWEEN :desde AND :hasta ORDER BY g.fecha", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class InstrumentoGasto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "INSTRUMENTOGASTO_ID_GENERATOR", sequenceName = "SEQ_INSTRUMENTO_GASTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSTRUMENTOGASTO_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_GASTO")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_INSTRUMENTO", referencedColumnName = "ID_INSTRUMENTO")
    private Instrumento instrumento;
    @Basic(optional = false)
    @Column(name = "DETALLE")
    private String detalle;
    @Basic(optional = false)
    @Column(name = "FECHA")
    private LocalDate fecha;
    @Basic(optional = false)
    @Column(name = "MONTO")
    private BigDecimal monto;
    @Column(name = "FACTURA")
    private String factura;
    @Basic(optional = false)
    @Column(name = "TRIBUTA")
    private Integer tributa;

    public InstrumentoGasto() {
    }

    public InstrumentoGasto(Long id) {
        this.id = id;
    }

    /** El instrumento lo asigna el service. */
    public InstrumentoGasto(InstrumentoGastoDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(InstrumentoGastoDto dto) {
        this.detalle = dto.getDetalle();
        this.fecha = dto.getFecha();
        this.monto = dto.getMonto();
        this.factura = dto.getFactura();
        this.tributa = Boolean.TRUE.equals(dto.getTributa()) ? 1 : 0;
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

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public Integer getTributa() {
        return tributa;
    }

    public void setTributa(Integer tributa) {
        this.tributa = tributa;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InstrumentoGasto)) {
            return false;
        }
        InstrumentoGasto other = (InstrumentoGasto) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.InstrumentoGasto[ id=" + id + " ]";
    }
}