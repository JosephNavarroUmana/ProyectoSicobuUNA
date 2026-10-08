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
import jakarta.persistence.Version;
import java.io.Serializable;

/**
 * Cliente (persona física) de un bufete.
 */
@Entity
@Table(name = "CLIENTE")
@NamedQueries({
    @NamedQuery(name = "Cliente.findById", query = "SELECT c FROM Cliente c WHERE c.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Cliente.findByBufete", query = "SELECT c FROM Cliente c WHERE c.bufete.id = :idBufete ORDER BY c.nombreCompleto", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Cliente.findByBufeteIdentificacion", query = "SELECT c FROM Cliente c WHERE c.bufete.id = :idBufete AND UPPER(c.identificacion) = :identificacion", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Cliente.findByFiltros", query = "SELECT c FROM Cliente c WHERE c.bufete.id = :idBufete AND UPPER(c.identificacion) LIKE :identificacion AND UPPER(c.nombreCompleto) LIKE :nombre ORDER BY c.nombreCompleto", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CLIENTE_ID_GENERATOR", sequenceName = "SEQ_CLIENTE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLIENTE_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_CLIENTE")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_BUFETE", referencedColumnName = "ID_BUFETE")
    private Bufete bufete;
    @Basic(optional = false)
    @Column(name = "IDENTIFICACION")
    private String identificacion;
    @Basic(optional = false)
    @Column(name = "TIPO_IDENTIFICACION")
    private String tipoIdentificacion;
    @Basic(optional = false)
    @Column(name = "NOMBRE_COMPLETO")
    private String nombreCompleto;
    @Column(name = "ESTADO_CIVIL")
    private String estadoCivil;
    @Column(name = "OCUPACION")
    private String ocupacion;
    @Column(name = "CELULAR")
    private String celular;
    @Column(name = "CORREO")
    private String correo;
    @Column(name = "DIRECCION")
    private String direccion;
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Version
    @Basic(optional = false)
    @Column(name = "VERSION")
    private Long version;

    public Cliente() {
    }

    public Cliente(Long id) {
        this.id = id;
    }

    public Cliente(ClienteDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    /** Copia los datos del DTO a la entidad. El bufete lo asigna el service. */
    public void actualizar(ClienteDto dto) {
        this.identificacion = dto.getIdentificacion();
        this.tipoIdentificacion = dto.getTipoIdentificacion();
        this.nombreCompleto = dto.getNombreCompleto();
        this.estadoCivil = dto.getEstadoCivil();
        this.ocupacion = dto.getOcupacion();
        this.celular = dto.getCelular();
        this.correo = dto.getCorreo();
        this.direccion = dto.getDireccion();
        this.observaciones = dto.getObservaciones();
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

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.Cliente[ id=" + id + " ]";
    }
}