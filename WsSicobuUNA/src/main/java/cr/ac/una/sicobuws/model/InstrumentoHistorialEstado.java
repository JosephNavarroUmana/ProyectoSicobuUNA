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
import java.time.LocalDate;

/**
 * Registro de cada cambio de estado de un instrumento (con su observación).
 */
@Entity
@Table(name = "INSTRUMENTOHISTORIALESTADO")
@NamedQueries({
    @NamedQuery(name = "InstrumentoHistorialEstado.findByInstrumento", query = "SELECT h FROM InstrumentoHistorialEstado h WHERE h.instrumento.id = :idInstrumento ORDER BY h.id", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class InstrumentoHistorialEstado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "INSTRUMENTOHISTORIAL_ID_GENERATOR", sequenceName = "SEQ_INSTRUMENTO_HISTORIAL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSTRUMENTOHISTORIAL_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_HISTORIAL")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_INSTRUMENTO", referencedColumnName = "ID_INSTRUMENTO")
    private Instrumento instrumento;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_ESTADO", referencedColumnName = "ID_ESTADO")
    private EstadoInstrumento estado;
    @Basic(optional = false)
    @Column(name = "FECHA")
    private LocalDate fecha;
    @Column(name = "OBSERVACION")
    private String observacion;

    public InstrumentoHistorialEstado() {
    }

    public InstrumentoHistorialEstado(Long id) {
        this.id = id;
    }

    /** El instrumento y el estado los asigna el service. */
    public InstrumentoHistorialEstado(InstrumentoHistorialEstadoDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(InstrumentoHistorialEstadoDto dto) {
        this.fecha = dto.getFecha();
        this.observacion = dto.getObservacion();
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

    public EstadoInstrumento getEstado() {
        return estado;
    }

    public void setEstado(EstadoInstrumento estado) {
        this.estado = estado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InstrumentoHistorialEstado)) {
            return false;
        }
        InstrumentoHistorialEstado other = (InstrumentoHistorialEstado) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.InstrumentoHistorialEstado[ id=" + id + " ]";
    }
}