package cr.ac.una.sicobuws.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * Estado por el que pasa un instrumento. Los indicadores se guardan como
 * NUMBER(1): 1 = sí, 0 = no. "notifica" indica si se avisa a las partes al
 * entrar a este estado y "esFinal" si es un estado final (se adjunta el documento).
 */
@Entity
@Table(name = "ESTADOINSTRUMENTO")
@NamedQueries({
    @NamedQuery(name = "EstadoInstrumento.findAll", query = "SELECT e FROM EstadoInstrumento e ORDER BY e.descripcion", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "EstadoInstrumento.findById", query = "SELECT e FROM EstadoInstrumento e WHERE e.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "EstadoInstrumento.findActivos", query = "SELECT e FROM EstadoInstrumento e WHERE e.activo = 1 ORDER BY e.descripcion", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "EstadoInstrumento.findByDescripcion", query = "SELECT e FROM EstadoInstrumento e WHERE UPPER(e.descripcion) LIKE :descripcion ORDER BY e.descripcion", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class EstadoInstrumento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ESTADOINSTRUMENTO_ID_GENERATOR", sequenceName = "SEQ_ESTADO_INSTRUMENTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTADOINSTRUMENTO_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_ESTADO")
    private Long id;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private Integer activo;
    @Basic(optional = false)
    @Column(name = "NOTIFICA")
    private Integer notifica;
    @Basic(optional = false)
    @Column(name = "ES_FINAL")
    private Integer esFinal;

    public EstadoInstrumento() {
    }

    public EstadoInstrumento(Long id) {
        this.id = id;
    }

    public EstadoInstrumento(EstadoInstrumentoDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    /** Copia los datos del DTO a la entidad (convierte Boolean a 0/1). */
    public void actualizar(EstadoInstrumentoDto dto) {
        this.descripcion = dto.getDescripcion();
        this.activo = Boolean.TRUE.equals(dto.getActivo()) ? 1 : 0;
        this.notifica = Boolean.TRUE.equals(dto.getNotifica()) ? 1 : 0;
        this.esFinal = Boolean.TRUE.equals(dto.getEsFinal()) ? 1 : 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public Integer getNotifica() {
        return notifica;
    }

    public void setNotifica(Integer notifica) {
        this.notifica = notifica;
    }

    public Integer getEsFinal() {
        return esFinal;
    }

    public void setEsFinal(Integer esFinal) {
        this.esFinal = esFinal;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EstadoInstrumento)) {
            return false;
        }
        EstadoInstrumento other = (EstadoInstrumento) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.EstadoInstrumento[ id=" + id + " ]";
    }
}