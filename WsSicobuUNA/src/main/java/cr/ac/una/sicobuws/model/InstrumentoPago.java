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
 * Pago realizado por una parte de un instrumento.
 */
@Entity
@Table(name = "INSTRUMENTOPAGO")
@NamedQueries({
    @NamedQuery(name = "InstrumentoPago.findByParte", query = "SELECT p FROM InstrumentoPago p WHERE p.parte.id = :idParte ORDER BY p.fecha, p.id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "InstrumentoPago.findByInstrumento", query = "SELECT p FROM InstrumentoPago p WHERE p.parte.instrumento.id = :idInstrumento ORDER BY p.fecha, p.id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "InstrumentoPago.findByBufeteRango", query = "SELECT p FROM InstrumentoPago p WHERE p.parte.instrumento.bufete.id = :idBufete AND p.fecha BETWEEN :desde AND :hasta ORDER BY p.fecha", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class InstrumentoPago implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "INSTRUMENTOPAGO_ID_GENERATOR", sequenceName = "SEQ_INSTRUMENTO_PAGO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSTRUMENTOPAGO_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_PAGO")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_PARTE", referencedColumnName = "ID_PARTE")
    private InstrumentoParte parte;
    @Column(name = "DETALLE")
    private String detalle;
    @Basic(optional = false)
    @Column(name = "FECHA")
    private LocalDate fecha;
    @Column(name = "FACTURA")
    private String factura;
    @Basic(optional = false)
    @Column(name = "MONTO")
    private BigDecimal monto;

    public InstrumentoPago() {
    }

    public InstrumentoPago(Long id) {
        this.id = id;
    }

    /** La parte la asigna el service. */
    public InstrumentoPago(InstrumentoPagoDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(InstrumentoPagoDto dto) {
        this.detalle = dto.getDetalle();
        this.fecha = dto.getFecha();
        this.factura = dto.getFactura();
        this.monto = dto.getMonto();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InstrumentoParte getParte() {
        return parte;
    }

    public void setParte(InstrumentoParte parte) {
        this.parte = parte;
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

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InstrumentoPago)) {
            return false;
        }
        InstrumentoPago other = (InstrumentoPago) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.InstrumentoPago[ id=" + id + " ]";
    }
}