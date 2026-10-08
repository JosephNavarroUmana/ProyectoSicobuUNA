package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO del ingreso o gasto vario.
 */
public class MovimientoVarioDto {

    private Long id;
    @NotNull(message = "El movimiento debe pertenecer a un bufete")
    private Long idBufete;
    @NotBlank(message = "El tipo de movimiento no puede estar vacío")
    @Pattern(regexp = "^(INGRESO|GASTO)$", message = "El tipo de movimiento no cumple con el formato")
    private String tipo;
    @NotBlank(message = "El detalle del movimiento no puede estar vacío")
    @Size(max = 300, message = "El detalle del movimiento debe tener máximo 300 caracteres")
    private String detalle;
    @NotNull(message = "La fecha del movimiento no puede ser nula")
    private LocalDate fecha;
    @NotNull(message = "El monto del movimiento no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El monto del movimiento no puede ser negativo")
    private BigDecimal monto;
    @Size(max = 30, message = "La factura debe tener máximo 30 caracteres")
    private String factura;
    private Boolean tributa;

    public MovimientoVarioDto() {
        this.tributa = false;
    }

    public MovimientoVarioDto(MovimientoVario movimiento) {
        this();
        this.id = movimiento.getId();
        this.idBufete = movimiento.getBufete().getId();
        this.tipo = movimiento.getTipo();
        this.detalle = movimiento.getDetalle();
        this.fecha = movimiento.getFecha();
        this.monto = movimiento.getMonto();
        this.factura = movimiento.getFactura();
        this.tributa = movimiento.getTributa() != null && movimiento.getTributa() == 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdBufete() {
        return idBufete;
    }

    public void setIdBufete(Long idBufete) {
        this.idBufete = idBufete;
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

    public Boolean getTributa() {
        return tributa;
    }

    public void setTributa(Boolean tributa) {
        this.tributa = tributa;
    }

    @Override
    public int hashCode() {
        return 23 * 7 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((MovimientoVarioDto) obj).id);
    }

    @Override
    public String toString() {
        return "MovimientoVarioDto{" + "id=" + id + ", tipo=" + tipo + ", monto=" + monto + '}';
    }
}