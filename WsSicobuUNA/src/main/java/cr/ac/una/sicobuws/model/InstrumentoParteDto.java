package cr.ac.una.sicobuws.model;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DTO de la parte de un instrumento. Debe traer idCliente O idSociedad (uno solo).
 * "identificacion" y "nombre" son solo informativos. El saldo de la parte
 * (monto a pagar - pagos) se calcula con calcularSaldo(); no se guarda.
 */
public class InstrumentoParteDto {

    private Long id;
    private Long idInstrumento;
    private Long idCliente;
    private Long idSociedad;
    private String identificacion;
    private String nombre;
    @NotBlank(message = "El tipo de persona no puede estar vacío")
    @Pattern(regexp = "^(FISICA|JURIDICA)$", message = "El tipo de persona no cumple con el formato")
    private String tipoPersona;
    @NotBlank(message = "El papel de la parte no puede estar vacío")
    @Pattern(regexp = "^(CEDENTE|CESIONARIO)$", message = "El papel de la parte no cumple con el formato")
    private String papel;
    @DecimalMin(value = "0.0", message = "El monto a pagar no puede ser negativo")
    private BigDecimal montoAPagar;
    // Solo lectura: los calcula calcularSaldo()
    private BigDecimal totalPagado;
    private BigDecimal saldo;
    private List<InstrumentoPagoDto> pagos;

    public InstrumentoParteDto() {
        this.totalPagado = BigDecimal.ZERO;
        this.saldo = BigDecimal.ZERO;
        this.pagos = new ArrayList<>();
    }

    /** No carga los pagos: los carga el service cuando hace falta. */
    public InstrumentoParteDto(InstrumentoParte parte) {
        this();
        this.id = parte.getId();
        this.idInstrumento = parte.getInstrumento().getId();
        if (parte.getCliente() != null) {
            this.idCliente = parte.getCliente().getId();
            this.identificacion = parte.getCliente().getIdentificacion();
            this.nombre = parte.getCliente().getNombreCompleto();
        }
        if (parte.getSociedad() != null) {
            this.idSociedad = parte.getSociedad().getId();
            this.identificacion = parte.getSociedad().getCedulaJuridica();
            this.nombre = parte.getSociedad().getNombre();
        }
        this.tipoPersona = parte.getTipoPersona();
        this.papel = parte.getPapel();
        this.montoAPagar = parte.getMontoAPagar();
    }

    /** Valida que la parte sea un cliente o una sociedad, pero no ambos ni ninguno. */
    @JsonbTransient
    @AssertTrue(message = "La parte debe ser un cliente o una sociedad, no ambos")
    public boolean isClienteOSociedad() {
        return (idCliente != null) != (idSociedad != null);
    }

    /** Calcula el total pagado y el saldo de la parte a partir de sus pagos. */
    public void calcularSaldo() {
        BigDecimal pagado = BigDecimal.ZERO;
        for (InstrumentoPagoDto pago : pagos) {
            pagado = pagado.add(pago.getMonto() != null ? pago.getMonto() : BigDecimal.ZERO);
        }
        this.totalPagado = pagado;
        this.saldo = (montoAPagar != null ? montoAPagar : BigDecimal.ZERO).subtract(pagado);
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

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdSociedad() {
        return idSociedad;
    }

    public void setIdSociedad(Long idSociedad) {
        this.idSociedad = idSociedad;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public List<InstrumentoPagoDto> getPagos() {
        return pagos;
    }

    public void setPagos(List<InstrumentoPagoDto> pagos) {
        this.pagos = pagos;
    }

    @Override
    public int hashCode() {
        return 43 * 5 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((InstrumentoParteDto) obj).id);
    }

    @Override
    public String toString() {
        return "InstrumentoParteDto{" + "id=" + id + ", nombre=" + nombre + ", papel=" + papel + '}';
    }
}