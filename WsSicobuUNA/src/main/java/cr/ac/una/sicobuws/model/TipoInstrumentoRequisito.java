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

/**
 * Requisito de un tipo de instrumento. "orden" define la posición en la lista
 * (se cambia con drag & drop en el cliente).
 */
@Entity
@Table(name = "TIPOINSTRUMENTOREQUISITO")
@NamedQueries({
    @NamedQuery(name = "TipoInstrumentoRequisito.findByTipoInstrumento", query = "SELECT r FROM TipoInstrumentoRequisito r WHERE r.tipoInstrumento.id = :idTipoInstrumento ORDER BY r.orden", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class TipoInstrumentoRequisito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TIPOINSTRUMENTOREQUISITO_ID_GENERATOR", sequenceName = "SEQ_TIPO_INSTRUMENTO_REQUISITO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPOINSTRUMENTOREQUISITO_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_REQUISITO")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_TIPO_INSTRUMENTO", referencedColumnName = "ID_TIPO_INSTRUMENTO")
    private TipoInstrumento tipoInstrumento;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "ORDEN")
    private Integer orden;

    public TipoInstrumentoRequisito() {
    }

    public TipoInstrumentoRequisito(Long id) {
        this.id = id;
    }

    /** El tipo de instrumento lo asigna el service. */
    public TipoInstrumentoRequisito(TipoInstrumentoRequisitoDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(TipoInstrumentoRequisitoDto dto) {
        this.descripcion = dto.getDescripcion();
        this.orden = dto.getOrden();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoInstrumento getTipoInstrumento() {
        return tipoInstrumento;
    }

    public void setTipoInstrumento(TipoInstrumento tipoInstrumento) {
        this.tipoInstrumento = tipoInstrumento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoInstrumentoRequisito)) {
            return false;
        }
        TipoInstrumentoRequisito other = (TipoInstrumentoRequisito) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.TipoInstrumentoRequisito[ id=" + id + " ]";
    }
}