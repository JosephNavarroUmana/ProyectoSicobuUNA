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
import java.time.LocalDateTime;

/**
 * Evento importante de un instrumento (se muestra en la agenda).
 * "estado" usa los valores de Catalogos.EVENTO_* y define el color en la agenda.
 */
@Entity
@Table(name = "INSTRUMENTOEVENTO")
@NamedQueries({
    @NamedQuery(name = "InstrumentoEvento.findById", query = "SELECT e FROM InstrumentoEvento e WHERE e.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "InstrumentoEvento.findByInstrumento", query = "SELECT e FROM InstrumentoEvento e WHERE e.instrumento.id = :idInstrumento ORDER BY e.fechaHora", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "InstrumentoEvento.findByBufeteRango", query = "SELECT e FROM InstrumentoEvento e WHERE e.instrumento.bufete.id = :idBufete AND e.fechaHora BETWEEN :desde AND :hasta ORDER BY e.fechaHora", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "InstrumentoEvento.findPendientesRango", query = "SELECT e FROM InstrumentoEvento e WHERE e.instrumento.bufete.id = :idBufete AND e.atendido = 0 AND e.fechaHora BETWEEN :desde AND :hasta ORDER BY e.fechaHora", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class InstrumentoEvento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "INSTRUMENTOEVENTO_ID_GENERATOR", sequenceName = "SEQ_INSTRUMENTO_EVENTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSTRUMENTOEVENTO_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_EVENTO")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_INSTRUMENTO", referencedColumnName = "ID_INSTRUMENTO")
    private Instrumento instrumento;
    @Basic(optional = false)
    @Column(name = "DETALLE")
    private String detalle;
    @Basic(optional = false)
    @Column(name = "FECHA_HORA")
    private LocalDateTime fechaHora;
    @Basic(optional = false)
    @Column(name = "ATENDIDO")
    private Integer atendido;
    @Column(name = "ESTADO")
    private String estado;

    public InstrumentoEvento() {
    }

    public InstrumentoEvento(Long id) {
        this.id = id;
    }

    /** El instrumento lo asigna el service. */
    public InstrumentoEvento(InstrumentoEventoDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(InstrumentoEventoDto dto) {
        this.detalle = dto.getDetalle();
        this.fechaHora = dto.getFechaHora();
        this.atendido = Boolean.TRUE.equals(dto.getAtendido()) ? 1 : 0;
        this.estado = dto.getEstado();
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

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Integer getAtendido() {
        return atendido;
    }

    public void setAtendido(Integer atendido) {
        this.atendido = atendido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InstrumentoEvento)) {
            return false;
        }
        InstrumentoEvento other = (InstrumentoEvento) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.InstrumentoEvento[ id=" + id + " ]";
    }
}