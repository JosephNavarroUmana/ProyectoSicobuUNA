package cr.ac.una.sicobuws.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Bufete. Sus abogados propietarios se guardan en la tabla BUFETEABOGADO
 * (relación N:N que solo se define de este lado).
 */
@Entity
@Table(name = "BUFETE")
@NamedQueries({
    @NamedQuery(name = "Bufete.findAll", query = "SELECT b FROM Bufete b ORDER BY b.nombre"),
    @NamedQuery(name = "Bufete.findById", query = "SELECT b FROM Bufete b WHERE b.id = :id")
})
public class Bufete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "BUFETE_ID_GENERATOR", sequenceName = "SEQ_BUFETE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUFETE_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_BUFETE")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "TELEFONO")
    private String telefono;
    @Basic(optional = false)
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @Column(name = "DIRECCION")
    private String direccion;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "LOGO")
    private byte[] logo;
    @Version
    @Basic(optional = false)
    @Column(name = "VERSION")
    private Long version;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "BUFETEABOGADO",
            joinColumns = {@JoinColumn(name = "ID_BUFETE", referencedColumnName = "ID_BUFETE")},
            inverseJoinColumns = {@JoinColumn(name = "ID_ABOGADO", referencedColumnName = "ID_ABOGADO")})
    private List<Abogado> abogados = new ArrayList<>();

    public Bufete() {
    }

    public Bufete(Long id) {
        this.id = id;
    }

    public Bufete(BufeteDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    /**
     * Copia los datos del DTO a la entidad. El logo solo se reemplaza si el
     * DTO trae uno nuevo, así una edición sin logo no lo borra.
     * Los abogados los maneja el service.
     */
    public void actualizar(BufeteDto dto) {
        this.nombre = dto.getNombre();
        this.telefono = dto.getTelefono();
        this.email = dto.getEmail();
        this.direccion = dto.getDireccion();
        if (dto.getLogo() != null) {
            this.logo = dto.getLogo();
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public List<Abogado> getAbogados() {
        return abogados;
    }

    public void setAbogados(List<Abogado> abogados) {
        this.abogados = abogados;
    }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }


    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Bufete)) {
            return false;
        }
        Bufete other = (Bufete) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.Bufete[ id=" + id + " ]";
    }
}