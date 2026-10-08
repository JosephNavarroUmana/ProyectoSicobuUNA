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
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Sociedad (persona jurídica) de un bufete, con sus representantes.
 */
@Entity
@Table(name = "SOCIEDAD")
@NamedQueries({
    @NamedQuery(name = "Sociedad.findById", query = "SELECT s FROM Sociedad s WHERE s.id = :id", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Sociedad.findByBufete", query = "SELECT s FROM Sociedad s WHERE s.bufete.id = :idBufete ORDER BY s.nombre", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Sociedad.findByBufeteCedula", query = "SELECT s FROM Sociedad s WHERE s.bufete.id = :idBufete AND UPPER(s.cedulaJuridica) = :cedula", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Sociedad.findByFiltros", query = "SELECT s FROM Sociedad s WHERE s.bufete.id = :idBufete AND UPPER(s.cedulaJuridica) LIKE :cedula AND UPPER(s.nombre) LIKE :nombre ORDER BY s.nombre", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class Sociedad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SOCIEDAD_ID_GENERATOR", sequenceName = "SEQ_SOCIEDAD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SOCIEDAD_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_SOCIEDAD")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_BUFETE", referencedColumnName = "ID_BUFETE")
    private Bufete bufete;
    @Basic(optional = false)
    @Column(name = "CEDULA_JURIDICA")
    private String cedulaJuridica;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "TELEFONO")
    private String telefono;
    @Column(name = "DOMICILIO")
    private String domicilio;
    @Column(name = "TOMO")
    private String tomo;
    @Column(name = "FOLIO")
    private String folio;
    @Column(name = "ASIENTO")
    private String asiento;
    @Column(name = "FECHA_INSCRIPCION")
    private LocalDate fechaInscripcion;
    @Basic(optional = false)
    @Column(name = "TIPO_ACCIONES")
    private String tipoAcciones;
    @Basic(optional = false)
    @Column(name = "CANTIDAD_TITULOS")
    private Long cantidadTitulos;
    @Basic(optional = false)
    @Column(name = "VALOR_TITULO")
    private BigDecimal valorTitulo;
    @Version
    @Basic(optional = false)
    @Column(name = "VERSION")
    private Long version;
    @OneToMany(mappedBy = "sociedad", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SociedadRepresentante> representantes = new ArrayList<>();

    public Sociedad() {
    }

    public Sociedad(Long id) {
        this.id = id;
    }

    public Sociedad(SociedadDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    /** Copia los datos del DTO a la entidad. El bufete y los representantes los maneja el service. */
    public void actualizar(SociedadDto dto) {
        this.cedulaJuridica = dto.getCedulaJuridica();
        this.nombre = dto.getNombre();
        this.telefono = dto.getTelefono();
        this.domicilio = dto.getDomicilio();
        this.tomo = dto.getTomo();
        this.folio = dto.getFolio();
        this.asiento = dto.getAsiento();
        this.fechaInscripcion = dto.getFechaInscripcion();
        this.tipoAcciones = dto.getTipoAcciones();
        this.cantidadTitulos = dto.getCantidadTitulos();
        this.valorTitulo = dto.getValorTitulo();
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

    public String getCedulaJuridica() {
        return cedulaJuridica;
    }

    public void setCedulaJuridica(String cedulaJuridica) {
        this.cedulaJuridica = cedulaJuridica;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTomo() {
        return tomo;
    }

    public void setTomo(String tomo) {
        this.tomo = tomo;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getTipoAcciones() {
        return tipoAcciones;
    }

    public void setTipoAcciones(String tipoAcciones) {
        this.tipoAcciones = tipoAcciones;
    }

    public Long getCantidadTitulos() {
        return cantidadTitulos;
    }

    public void setCantidadTitulos(Long cantidadTitulos) {
        this.cantidadTitulos = cantidadTitulos;
    }

    public BigDecimal getValorTitulo() {
        return valorTitulo;
    }

    public void setValorTitulo(BigDecimal valorTitulo) {
        this.valorTitulo = valorTitulo;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<SociedadRepresentante> getRepresentantes() {
        return representantes;
    }

    public void setRepresentantes(List<SociedadRepresentante> representantes) {
        this.representantes = representantes;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Sociedad)) {
            return false;
        }
        Sociedad other = (Sociedad) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.Sociedad[ id=" + id + " ]";
    }
}