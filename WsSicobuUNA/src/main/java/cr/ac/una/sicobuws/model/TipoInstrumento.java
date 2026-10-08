package cr.ac.una.sicobuws.model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Tipo de instrumento (escritura, poder, etc.). Guarda la ruta del machote
 * Word y la lista ordenada de requisitos que debe cumplir el instrumento.
 * El estado se guarda como NUMBER(1): 1 = activo, 0 = inactivo.
 */
@Entity
@Table(name = "TIPOINSTRUMENTO")
@NamedQueries({
    @NamedQuery(name = "TipoInstrumento.findAll", query = "SELECT t FROM TipoInstrumento t ORDER BY t.nombre", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "TipoInstrumento.findById", query = "SELECT t FROM TipoInstrumento t WHERE t.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "TipoInstrumento.findActivos", query = "SELECT t FROM TipoInstrumento t WHERE t.estado = 1 ORDER BY t.nombre", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "TipoInstrumento.findByNombre", query = "SELECT t FROM TipoInstrumento t WHERE UPPER(t.nombre) LIKE :nombre ORDER BY t.nombre", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class TipoInstrumento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TIPOINSTRUMENTO_ID_GENERATOR", sequenceName = "SEQ_TIPO_INSTRUMENTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPOINSTRUMENTO_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_TIPO_INSTRUMENTO")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private Integer estado;
    /** Ruta (en el sistema de archivos) del machote .docx. */
    @Column(name = "MACHOTE")
    private String machote;
    @Version
    @Basic(optional = false)
    @Column(name = "VERSION")
    private Long version;
    @OneToMany(mappedBy = "tipoInstrumento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("orden ASC")
    private List<TipoInstrumentoRequisito> requisitos = new ArrayList<>();

    public TipoInstrumento() {
    }

    public TipoInstrumento(Long id) {
        this.id = id;
    }

    public TipoInstrumento(TipoInstrumentoDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    /**
     * Copia los datos del DTO a la entidad. El machote solo se reemplaza si el
     * DTO trae una ruta nueva (null = conservar). Los requisitos los maneja el service.
     */
    public void actualizar(TipoInstrumentoDto dto) {
        this.nombre = dto.getNombre();
        this.estado = Boolean.TRUE.equals(dto.getActivo()) ? 1 : 0;
        if (dto.getMachote() != null) {
            this.machote = dto.getMachote();
        }
        this.version = dto.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getMachote() {
        return machote;
    }

    public void setMachote(String machote) {
        this.machote = machote;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<TipoInstrumentoRequisito> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(List<TipoInstrumentoRequisito> requisitos) {
        this.requisitos = requisitos;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoInstrumento)) {
            return false;
        }
        TipoInstrumento other = (TipoInstrumento) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.TipoInstrumento[ id=" + id + " ]";
    }
}