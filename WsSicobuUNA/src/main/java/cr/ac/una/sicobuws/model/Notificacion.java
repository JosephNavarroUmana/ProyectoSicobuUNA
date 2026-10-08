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
 * Bitácora de los correos enviados. El instrumento es opcional porque hay
 * correos que no pertenecen a uno (activación de usuario, recuperar clave).
 * "tipo" usa los valores de Catalogos.NOTIF_*.
 */
@Entity
@Table(name = "NOTIFICACION")
@NamedQueries({
    @NamedQuery(name = "Notificacion.findByInstrumento", query = "SELECT n FROM Notificacion n WHERE n.instrumento.id = :idInstrumento ORDER BY n.fechaEnvio DESC", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Notificacion.findByTipo", query = "SELECT n FROM Notificacion n WHERE n.tipo = :tipo ORDER BY n.fechaEnvio DESC", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class Notificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOTIFICACION_ID_GENERATOR", sequenceName = "SEQ_NOTIFICACION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICACION_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "ID_NOTIFICACION")
    private Long id;
    /** Instrumento relacionado; null si el correo no pertenece a un instrumento. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INSTRUMENTO", referencedColumnName = "ID_INSTRUMENTO")
    private Instrumento instrumento;
    @Basic(optional = false)
    @Column(name = "DESTINATARIO")
    private String destinatario;
    @Basic(optional = false)
    @Column(name = "ASUNTO")
    private String asunto;
    @Basic(optional = false)
    @Column(name = "FECHA_ENVIO")
    private LocalDateTime fechaEnvio;
    @Basic(optional = false)
    @Column(name = "TIPO")
    private String tipo;

    public Notificacion() {
    }

    public Notificacion(Long id) {
        this.id = id;
    }

    /** El instrumento (si aplica) lo asigna el service. */
    public Notificacion(NotificacionDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(NotificacionDto dto) {
        this.destinatario = dto.getDestinatario();
        this.asunto = dto.getAsunto();
        this.fechaEnvio = dto.getFechaEnvio();
        this.tipo = dto.getTipo();
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

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Notificacion)) {
            return false;
        }
        Notificacion other = (Notificacion) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cr.ac.una.sicobuws.model.Notificacion[ id=" + id + " ]";
    }
}