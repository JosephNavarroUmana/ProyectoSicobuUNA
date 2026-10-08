package cr.ac.una.sicobuws.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.QueryHint;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * Estado de un requisito del tipo de instrumento dentro de un instrumento
 * concreto (cumplido / no aplica). La llave es compuesta: instrumento + requisito.
 */
@Entity
@Table(name = "INSTRUMENTOREQUISITO")
@NamedQueries({
    @NamedQuery(name = "InstrumentoRequisito.findByInstrumento", query = "SELECT r FROM InstrumentoRequisito r WHERE r.instrumento.id = :idInstrumento ORDER BY r.requisito.orden", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class InstrumentoRequisito implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private InstrumentoRequisitoPK id;
    @MapsId("idInstrumento")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_INSTRUMENTO", referencedColumnName = "ID_INSTRUMENTO")
    private Instrumento instrumento;
    @MapsId("idRequisito")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_REQUISITO", referencedColumnName = "ID_REQUISITO")
    private TipoInstrumentoRequisito requisito;
    @Basic(optional = false)
    @Column(name = "CUMPLIDO")
    private Integer cumplido;
    @Basic(optional = false)
    @Column(name = "NO_APLICA")
    private Integer noAplica;

    public InstrumentoRequisito() {
    }

    public InstrumentoRequisito(InstrumentoRequisitoPK id) {
        this.id = id;
    }

    /** El instrumento, el requisito y la llave los asigna el service. */
    public InstrumentoRequisito(InstrumentoRequisitoDto dto) {
        actualizar(dto);
    }

    public void actualizar(InstrumentoRequisitoDto dto) {
        this.cumplido = Boolean.TRUE.equals(dto.getCumplido()) ? 1 : 0;
        this.noAplica = Boolean.TRUE.equals(dto.getNoAplica()) ? 1 : 0;
    }

    public InstrumentoRequisitoPK getId() {
        return id;
    }

    public void setId(InstrumentoRequisitoPK id) {
        this.id = id;
    }

    public Instrumento getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(Instrumento instrumento) {
        this.instrumento = instrumento;
    }

    public TipoInstrumentoRequisito getRequisito() {
        return requisito;
    }

    public void setRequisito(TipoInstrumentoRequisito requisito) {
        this.requisito = requisito;
    }

    public Integer getCumplido() {
        return cumplido;
    }

    public void setCumplido(Integer cumplido) {
        this.cumplido = cumplido;
    }

    public Integer getNoAplica() {
        return noAplica;
    }

    public void setNoAplica(Integer noAplica) {
        this.noAplica = noAplica;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InstrumentoRequisito)) {
            return false;
        }
        InstrumentoRequisito other = (InstrumentoRequisito) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.InstrumentoRequisito[ id=" + id + " ]";
    }
}