package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DTO del instrumento, con todas sus listas hijas. Los saldos no se guardan en
 * la BD: se calculan con calcularSaldos() (saldo por parte = monto a pagar - pagos;
 * saldo del instrumento = suma de los saldos de las partes).
 */
public class InstrumentoDto {

    private Long id;
    @NotNull(message = "El instrumento debe pertenecer a un bufete")
    private Long idBufete;
    @NotNull(message = "Debe indicar el tipo de instrumento")
    private Long idTipoInstrumento;
    private String nombreTipoInstrumento;
    @NotNull(message = "Debe indicar el estado del instrumento")
    private Long idEstado;
    private String descripcionEstado;
    @NotNull(message = "Debe indicar el abogado titular")
    private Long idAbogado;
    private String nombreAbogado;
    @NotNull(message = "La fecha del estado no puede ser nula")
    private LocalDate fechaEstado;
    @Size(max = 300, message = "La observación del estado debe tener máximo 300 caracteres")
    private String observacionEstado;
    @Size(max = 2000, message = "El detalle debe tener máximo 2000 caracteres")
    private String detalle;
    private Boolean incluirIndiceNotarial;
    @NotNull(message = "La fecha y hora del instrumento no pueden ser nulas")
    private LocalDateTime fechaHora;
    @Size(max = 30, message = "El número de boleta debe tener máximo 30 caracteres")
    private String numeroBoleta;
    @NotBlank(message = "El tomo no puede estar vacío")
    @Size(max = 50, message = "El tomo debe tener máximo 50 caracteres")
    private String tomo;
    @NotBlank(message = "La escritura no puede estar vacía")
    @Size(max = 30, message = "La escritura debe tener máximo 30 caracteres")
    private String escritura;
    @Size(max = 30, message = "El folio debe tener máximo 30 caracteres")
    private String folio;
    @Size(max = 30, message = "El tomo asiento debe tener máximo 30 caracteres")
    private String tomoAsiento;
    @Size(max = 300, message = "Las observaciones deben tener máximo 300 caracteres")
    private String observaciones;
    @Size(max = 300, message = "La ruta del documento final debe tener máximo 300 caracteres")
    private String documentoFinal;
    private Boolean tributa;
    @Size(max = 30, message = "El número de recibo debe tener máximo 30 caracteres")
    private String numeroRecibo;
    @DecimalMin(value = "0.0", message = "El monto real no puede ser negativo")
    private BigDecimal montoReal;
    @DecimalMin(value = "0.0", message = "El monto facturado no puede ser negativo")
    private BigDecimal montoFacturado;
    private Long version;
    // Solo lectura: los calcula calcularSaldos()
    private BigDecimal totalAPagar;
    private BigDecimal totalPagado;
    private BigDecimal saldo;
    private List<InstrumentoHistorialEstadoDto> historial;
    private List<InstrumentoParteDto> partes;
    private List<InstrumentoRequisitoDto> requisitos;
    private List<InstrumentoEventoDto> eventos;
    private List<InstrumentoGastoDto> gastos;

    public InstrumentoDto() {
        this.incluirIndiceNotarial = false;
        this.tributa = false;
        this.totalAPagar = BigDecimal.ZERO;
        this.totalPagado = BigDecimal.ZERO;
        this.saldo = BigDecimal.ZERO;
        this.historial = new ArrayList<>();
        this.partes = new ArrayList<>();
        this.requisitos = new ArrayList<>();
        this.eventos = new ArrayList<>();
        this.gastos = new ArrayList<>();
    }

    /** No carga las listas hijas: las carga el service cuando hace falta. */
    public InstrumentoDto(Instrumento instrumento) {
        this();
        this.id = instrumento.getId();
        this.idBufete = instrumento.getBufete().getId();
        this.idTipoInstrumento = instrumento.getTipoInstrumento().getId();
        this.nombreTipoInstrumento = instrumento.getTipoInstrumento().getNombre();
        this.idEstado = instrumento.getEstado().getId();
        this.descripcionEstado = instrumento.getEstado().getDescripcion();
        this.idAbogado = instrumento.getAbogado().getId();
        this.nombreAbogado = instrumento.getAbogado().getNombre();
        this.fechaEstado = instrumento.getFechaEstado();
        this.observacionEstado = instrumento.getObservacionEstado();
        this.detalle = instrumento.getDetalle();
        this.incluirIndiceNotarial = instrumento.getIncluirIndiceNotarial() != null && instrumento.getIncluirIndiceNotarial() == 1;
        this.fechaHora = instrumento.getFechaHora();
        this.numeroBoleta = instrumento.getNumeroBoleta();
        this.tomo = instrumento.getTomo();
        this.escritura = instrumento.getEscritura();
        this.folio = instrumento.getFolio();
        this.tomoAsiento = instrumento.getTomoAsiento();
        this.observaciones = instrumento.getObservaciones();
        this.documentoFinal = instrumento.getDocumentoFinal();
        this.tributa = instrumento.getTributa() != null && instrumento.getTributa() == 1;
        this.numeroRecibo = instrumento.getNumeroRecibo();
        this.montoReal = instrumento.getMontoReal();
        this.montoFacturado = instrumento.getMontoFacturado();
        this.version = instrumento.getVersion();
    }

    /** Calcula el saldo de cada parte y los totales del instrumento (no se guardan en la BD). */
    public void calcularSaldos() {
        BigDecimal aPagar = BigDecimal.ZERO;
        BigDecimal pagado = BigDecimal.ZERO;
        for (InstrumentoParteDto parte : partes) {
            parte.calcularSaldo();
            aPagar = aPagar.add(parte.getMontoAPagar() != null ? parte.getMontoAPagar() : BigDecimal.ZERO);
            pagado = pagado.add(parte.getTotalPagado());
        }
        this.totalAPagar = aPagar;
        this.totalPagado = pagado;
        this.saldo = aPagar.subtract(pagado);
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

    public Long getIdTipoInstrumento() {
        return idTipoInstrumento;
    }

    public void setIdTipoInstrumento(Long idTipoInstrumento) {
        this.idTipoInstrumento = idTipoInstrumento;
    }

    public String getNombreTipoInstrumento() {
        return nombreTipoInstrumento;
    }

    public void setNombreTipoInstrumento(String nombreTipoInstrumento) {
        this.nombreTipoInstrumento = nombreTipoInstrumento;
    }

    public Long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }

    public Long getIdAbogado() {
        return idAbogado;
    }

    public void setIdAbogado(Long idAbogado) {
        this.idAbogado = idAbogado;
    }

    public String getNombreAbogado() {
        return nombreAbogado;
    }

    public void setNombreAbogado(String nombreAbogado) {
        this.nombreAbogado = nombreAbogado;
    }

    public LocalDate getFechaEstado() {
        return fechaEstado;
    }

    public void setFechaEstado(LocalDate fechaEstado) {
        this.fechaEstado = fechaEstado;
    }

    public String getObservacionEstado() {
        return observacionEstado;
    }

    public void setObservacionEstado(String observacionEstado) {
        this.observacionEstado = observacionEstado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Boolean getIncluirIndiceNotarial() {
        return incluirIndiceNotarial;
    }

    public void setIncluirIndiceNotarial(Boolean incluirIndiceNotarial) {
        this.incluirIndiceNotarial = incluirIndiceNotarial;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getNumeroBoleta() {
        return numeroBoleta;
    }

    public void setNumeroBoleta(String numeroBoleta) {
        this.numeroBoleta = numeroBoleta;
    }

    public String getTomo() {
        return tomo;
    }

    public void setTomo(String tomo) {
        this.tomo = tomo;
    }

    public String getEscritura() {
        return escritura;
    }

    public void setEscritura(String escritura) {
        this.escritura = escritura;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getTomoAsiento() {
        return tomoAsiento;
    }

    public void setTomoAsiento(String tomoAsiento) {
        this.tomoAsiento = tomoAsiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getDocumentoFinal() {
        return documentoFinal;
    }

    public void setDocumentoFinal(String documentoFinal) {
        this.documentoFinal = documentoFinal;
    }

    public Boolean getTributa() {
        return tributa;
    }

    public void setTributa(Boolean tributa) {
        this.tributa = tributa;
    }

    public String getNumeroRecibo() {
        return numeroRecibo;
    }

    public void setNumeroRecibo(String numeroRecibo) {
        this.numeroRecibo = numeroRecibo;
    }

    public BigDecimal getMontoReal() {
        return montoReal;
    }

    public void setMontoReal(BigDecimal montoReal) {
        this.montoReal = montoReal;
    }

    public BigDecimal getMontoFacturado() {
        return montoFacturado;
    }

    public void setMontoFacturado(BigDecimal montoFacturado) {
        this.montoFacturado = montoFacturado;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BigDecimal getTotalAPagar() {
        return totalAPagar;
    }

    public void setTotalAPagar(BigDecimal totalAPagar) {
        this.totalAPagar = totalAPagar;
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

    public List<InstrumentoHistorialEstadoDto> getHistorial() {
        return historial;
    }

    public void setHistorial(List<InstrumentoHistorialEstadoDto> historial) {
        this.historial = historial;
    }

    public List<InstrumentoParteDto> getPartes() {
        return partes;
    }

    public void setPartes(List<InstrumentoParteDto> partes) {
        this.partes = partes;
    }

    public List<InstrumentoRequisitoDto> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(List<InstrumentoRequisitoDto> requisitos) {
        this.requisitos = requisitos;
    }

    public List<InstrumentoEventoDto> getEventos() {
        return eventos;
    }

    public void setEventos(List<InstrumentoEventoDto> eventos) {
        this.eventos = eventos;
    }

    public List<InstrumentoGastoDto> getGastos() {
        return gastos;
    }

    public void setGastos(List<InstrumentoGastoDto> gastos) {
        this.gastos = gastos;
    }

    @Override
    public int hashCode() {
        return 47 * 7 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((InstrumentoDto) obj).id);
    }

    @Override
    public String toString() {
        return "InstrumentoDto{" + "id=" + id + ", tomo=" + tomo + ", escritura=" + escritura + '}';
    }
}