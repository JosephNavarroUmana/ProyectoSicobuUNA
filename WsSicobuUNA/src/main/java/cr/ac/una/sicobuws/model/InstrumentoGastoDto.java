package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO del gasto de un instrumento.
 */
public class InstrumentoGastoDto {

    private Long id;
    private Long idInstrumento;
    @NotBlank(message = "El detalle del gasto no puede estar vacío")
    @Size(max = 300, message = "El detalle del gasto debe tener máximo 300 caracteres")
    private String detalle;
    @NotNull(message = "La fecha del gasto no puede ser nula")
    private LocalDate fecha;
    @NotNull(message = "El monto del gasto no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El monto del gasto no puede ser negativo")
    private BigDecimal monto;
    @Size(max = 30, message = "La factura debe tener máximo 30 caracteres")
    private String factura;
    private Boolean tributa;

    public InstrumentoGastoDto() {
        this.tributa = false;
    }

    public InstrumentoGastoDto(InstrumentoGasto gasto) {
        this();
        this.id = gasto.getId();
        this.idInstrumento = gasto.getInstrumento().getId();
        this.detalle = gasto.getDetalle();
        this.fecha = gasto.getFecha();
        this.monto = gasto.getMonto();
        this.factura = gasto.getFactura();
        this.tributa = gasto.getTributa() != null && gasto.getTributa() == 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdInstrumento() {
        return idInstrumento;
    }

    public void setIdInstrumento(Long idInstrumento) {
        this.idInstrumento = idInstrumento;
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

    public Boolean getTributa() {
        return tributa;
    }

    public void setTributa(Boolean tributa) {
        this.tributa = tributa;
    }

    @Override
    public int hashCode() {
        return 29 * 7 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((InstrumentoGastoDto) obj).id);
    }

    @Override
    public String toString() {
        return "InstrumentoGastoDto{" + "id=" + id + ", detalle=" + detalle + ", monto=" + monto + '}';
    }
}