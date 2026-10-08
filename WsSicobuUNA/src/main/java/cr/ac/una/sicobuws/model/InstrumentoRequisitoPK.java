package cr.ac.una.sicobuws.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Llave primaria compuesta de INSTRUMENTOREQUISITO (instrumento + requisito).
 */
@Embeddable
public class InstrumentoRequisitoPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(name = "ID_INSTRUMENTO")
    private Long idInstrumento;
    @Basic(optional = false)
    @Column(name = "ID_REQUISITO")
    private Long idRequisito;

    public InstrumentoRequisitoPK() {
    }

    public InstrumentoRequisitoPK(Long idInstrumento, Long idRequisito) {
        this.idInstrumento = idInstrumento;
        this.idRequisito = idRequisito;
    }

    public Long getIdInstrumento() {
        return idInstrumento;
    }

    public void setIdInstrumento(Long idInstrumento) {
        this.idInstrumento = idInstrumento;
    }

    public Long getIdRequisito() {
        return idRequisito;
    }

    public void setIdRequisito(Long idRequisito) {
        this.idRequisito = idRequisito;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInstrumento, idRequisito);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof InstrumentoRequisitoPK)) {
            return false;
        }
        InstrumentoRequisitoPK other = (InstrumentoRequisitoPK) object;
        return Objects.equals(this.idInstrumento, other.idInstrumento)
                && Objects.equals(this.idRequisito, other.idRequisito);
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.InstrumentoRequisitoPK[ idInstrumento=" + idInstrumento + ", idRequisito=" + idRequisito + " ]";
    }
}