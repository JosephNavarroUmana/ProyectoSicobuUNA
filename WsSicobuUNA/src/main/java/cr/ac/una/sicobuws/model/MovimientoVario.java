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
 * Ingreso o gasto vario de un bufete, sin relación con un instrumento.
 * "tipo" usa los valores de Catalogos.MOV_*; "tributa" es 1 = sí, 0 = no.
 */
@Entity
@Table(name = "MOVIMIENTOVARIO")
@NamedQueries({
    @NamedQuery(name = "MovimientoVario.findById", query = "SELECT m FROM MovimientoVario m WHERE m.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "MovimientoVario.findByBufete", query = "SELECT m FROM MovimientoVario m WHERE m.bufete.id = :idBufete ORDER BY m.fecha DESC", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "MovimientoVario.findByBufeteRango", query = "SELECT m FROM MovimientoVario m WHERE m.bufete.id = :idBufete AND m.fecha BETWEEN :desde AND :hasta ORDER BY m.fecha", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "MovimientoVario.findByBufeteTipoRango", query = "SELECT m FROM MovimientoVario m WHERE m.bufete.id = :idBufete AND m.tipo = :tipo AND m.fecha BETWEEN :desde AND :hasta ORDER BY m.fecha", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class MovimientoVario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "MOVIMIENTOVARIO_ID_GENERATOR", sequenceName = "SEQ_MOVIMIENTO_VARIO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOVIMIENTOVARIO_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_MOVIMIENTO")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_BUFETE", referencedColumnName = "ID_BUFETE")
    private Bufete bufete;
    @Basic(optional = false)
    @Column(name = "TIPO")
    private String tipo;
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

    public MovimientoVario() {
    }

    public MovimientoVario(Long id) {
        this.id = id;
    }

    /** El bufete lo asigna el service. */
    public MovimientoVario(MovimientoVarioDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(MovimientoVarioDto dto) {
        this.tipo = dto.getTipo();
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

    public Bufete getBufete() {
        return bufete;
    }

    public void setBufete(Bufete bufete) {
        this.bufete = bufete;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
        if (!(object instanceof MovimientoVario)) {
            return false;
        }
        MovimientoVario other = (MovimientoVario) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.MovimientoVario[ id=" + id + " ]";
    }
}