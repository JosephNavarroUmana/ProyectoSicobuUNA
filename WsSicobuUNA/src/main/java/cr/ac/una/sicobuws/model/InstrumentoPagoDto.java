package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO del pago de una parte.
 */
public class InstrumentoPagoDto {

    private Long id;
    private Long idParte;
    @Size(max = 300, message = "El detalle del pago debe tener máximo 300 caracteres")
    private String detalle;
    @NotNull(message = "La fecha del pago no puede ser nula")
    private LocalDate fecha;
    @Size(max = 30, message = "La factura debe tener máximo 30 caracteres")
    private String factura;
    @NotNull(message = "El monto del pago no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto del pago debe ser mayor que cero")
    private BigDecimal monto;

    public InstrumentoPagoDto() {
    }

    public InstrumentoPagoDto(InstrumentoPago pago) {
        this.id = pago.getId();
        this.idParte = pago.getParte().getId();
        this.detalle = pago.getDetalle();
        this.fecha = pago.getFecha();
        this.factura = pago.getFactura();
        this.monto = pago.getMonto();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdParte() {
        return idParte;
    }

    public void setIdParte(Long idParte) {
        this.idParte = idParte;
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
        return 37 * 5 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((InstrumentoPagoDto) obj).id);
    }

    @Override
    public String toString() {
        return "InstrumentoPagoDto{" + "id=" + id + ", fecha=" + fecha + ", monto=" + monto + '}';
    }
}