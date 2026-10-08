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
import jakarta.persistence.Version;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Instrumento legal (escritura, etc.) de un bufete. Es la entidad central:
 * tiene historial de estados, partes (con sus pagos), requisitos, eventos y gastos.
 * Los indicadores se guardan como NUMBER(1): 1 = sí, 0 = no.
 */
@Entity
@Table(name = "INSTRUMENTO")
@NamedQueries({
    @NamedQuery(name = "Instrumento.findById", query = "SELECT i FROM Instrumento i WHERE i.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Instrumento.findByBufete", query = "SELECT i FROM Instrumento i WHERE i.bufete.id = :idBufete ORDER BY i.fechaHora DESC", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Instrumento.findByBufeteRango", query = "SELECT i FROM Instrumento i WHERE i.bufete.id = :idBufete AND i.fechaHora BETWEEN :desde AND :hasta ORDER BY i.fechaHora", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Instrumento.findByAbogadoRango", query = "SELECT i FROM Instrumento i WHERE i.bufete.id = :idBufete AND i.abogado.id = :idAbogado AND i.fechaHora BETWEEN :desde AND :hasta ORDER BY i.fechaHora", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Instrumento.findByClienteRango", query = "SELECT DISTINCT i FROM Instrumento i JOIN i.partes p WHERE i.bufete.id = :idBufete AND p.cliente.id = :idCliente AND i.fechaHora BETWEEN :desde AND :hasta ORDER BY i.fechaHora", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Instrumento.findByBufeteTomoEscritura", query = "SELECT i FROM Instrumento i WHERE i.bufete.id = :idBufete AND UPPER(i.tomo) = :tomo AND UPPER(i.escritura) = :escritura", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class Instrumento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "INSTRUMENTO_ID_GENERATOR", sequenceName = "SEQ_INSTRUMENTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSTRUMENTO_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_INSTRUMENTO")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_BUFETE", referencedColumnName = "ID_BUFETE")
    private Bufete bufete;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_TIPO_INSTRUMENTO", referencedColumnName = "ID_TIPO_INSTRUMENTO")
    private TipoInstrumento tipoInstrumento;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ESTADO", referencedColumnName = "ID_ESTADO")
    private EstadoInstrumento estado;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ABOGADO", referencedColumnName = "ID_ABOGADO")
    private Abogado abogado;
    @Basic(optional = false)
    @Column(name = "FECHA_ESTADO")
    private LocalDate fechaEstado;
    @Column(name = "OBSERVACION_ESTADO")
    private String observacionEstado;
    @Column(name = "DETALLE")
    private String detalle;
    @Basic(optional = false)
    @Column(name = "INCLUIR_INDICE_NOTARIAL")
    private Integer incluirIndiceNotarial;
    @Basic(optional = false)
    @Column(name = "FECHA_HORA")
    private LocalDateTime fechaHora;
    @Column(name = "NUMERO_BOLETA")
    private String numeroBoleta;
    @Basic(optional = false)
    @Column(name = "TOMO")
    private String tomo;
    @Basic(optional = false)
    @Column(name = "ESCRITURA")
    private String escritura;
    @Column(name = "FOLIO")
    private String folio;
    @Column(name = "TOMO_ASIENTO")
    private String tomoAsiento;
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    /** Ruta (en el sistema de archivos) del documento final firmado. */
    @Column(name = "DOCUMENTO_FINAL")
    private String documentoFinal;
    @Basic(optional = false)
    @Column(name = "TRIBUTA")
    private Integer tributa;
    @Column(name = "NUMERO_RECIBO")
    private String numeroRecibo;
    @Column(name = "MONTO_REAL")
    private BigDecimal montoReal;
    @Column(name = "MONTO_FACTURADO")
    private BigDecimal montoFacturado;
    @Version
    @Basic(optional = false)
    @Column(name = "VERSION")
    private Long version;
    @OneToMany(mappedBy = "instrumento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    private List<InstrumentoHistorialEstado> historial = new ArrayList<>();
    @OneToMany(mappedBy = "instrumento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<InstrumentoParte> partes = new ArrayList<>();
    @OneToMany(mappedBy = "instrumento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<InstrumentoRequisito> requisitos = new ArrayList<>();
    @OneToMany(mappedBy = "instrumento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("fechaHora ASC")
    private List<InstrumentoEvento> eventos = new ArrayList<>();
    @OneToMany(mappedBy = "instrumento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<InstrumentoGasto> gastos = new ArrayList<>();

    public Instrumento() {
    }

    public Instrumento(Long id) {
        this.id = id;
    }

    public Instrumento(InstrumentoDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    /**
     * Copia los datos del DTO a la entidad. El bufete, tipo, estado, abogado y
     * las listas hijas los maneja el service (necesita el EntityManager).
     */
    public void actualizar(InstrumentoDto dto) {
        this.fechaEstado = dto.getFechaEstado();
        this.observacionEstado = dto.getObservacionEstado();
        this.detalle = dto.getDetalle();
        this.incluirIndiceNotarial = Boolean.TRUE.equals(dto.getIncluirIndiceNotarial()) ? 1 : 0;
        this.fechaHora = dto.getFechaHora();
        this.numeroBoleta = dto.getNumeroBoleta();
        this.tomo = dto.getTomo();
        this.escritura = dto.getEscritura();
        this.folio = dto.getFolio();
        this.tomoAsiento = dto.getTomoAsiento();
        this.observaciones = dto.getObservaciones();
        if (dto.getDocumentoFinal() != null) {
            this.documentoFinal = dto.getDocumentoFinal();
        }
        this.tributa = Boolean.TRUE.equals(dto.getTributa()) ? 1 : 0;
        this.numeroRecibo = dto.getNumeroRecibo();
        this.montoReal = dto.getMontoReal();
        this.montoFacturado = dto.getMontoFacturado();
        this.version = dto.getVersion();
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

    public TipoInstrumento getTipoInstrumento() {
        return tipoInstrumento;
    }

    public void setTipoInstrumento(TipoInstrumento tipoInstrumento) {
        this.tipoInstrumento = tipoInstrumento;
    }

    public EstadoInstrumento getEstado() {
        return estado;
    }

    public void setEstado(EstadoInstrumento estado) {
        this.estado = estado;
    }

    public Abogado getAbogado() {
        return abogado;
    }

    public void setAbogado(Abogado abogado) {
        this.abogado = abogado;
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

    public Integer getIncluirIndiceNotarial() {
        return incluirIndiceNotarial;
    }

    public void setIncluirIndiceNotarial(Integer incluirIndiceNotarial) {
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

    public Integer getTributa() {
        return tributa;
    }

    public void setTributa(Integer tributa) {
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

    public List<InstrumentoHistorialEstado> getHistorial() {
        return historial;
    }

    public void setHistorial(List<InstrumentoHistorialEstado> historial) {
        this.historial = historial;
    }

    public List<InstrumentoParte> getPartes() {
        return partes;
    }

    public void setPartes(List<InstrumentoParte> partes) {
        this.partes = partes;
    }

    public List<InstrumentoRequisito> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(List<InstrumentoRequisito> requisitos) {
        this.requisitos = requisitos;
    }

    public List<InstrumentoEvento> getEventos() {
        return eventos;
    }

    public void setEventos(List<InstrumentoEvento> eventos) {
        this.eventos = eventos;
    }

    public List<InstrumentoGasto> getGastos() {
        return gastos;
    }

    public void setGastos(List<InstrumentoGasto> gastos) {
        this.gastos = gastos;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Instrumento)) {
            return false;
        }
        Instrumento other = (Instrumento) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.Instrumento[ id=" + id + " ]";
    }
}