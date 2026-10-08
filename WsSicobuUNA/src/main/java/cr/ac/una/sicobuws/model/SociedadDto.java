package cr.ac.una.sicobuws.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DTO de la sociedad, con la lista de sus representantes.
 */
public class SociedadDto {

    private Long id;
    @NotNull(message = "La sociedad debe pertenecer a un bufete")
    private Long idBufete;
    @NotBlank(message = "La cédula jurídica no puede estar vacía")
    @Size(max = 30, message = "La cédula jurídica debe tener máximo 30 caracteres")
    private String cedulaJuridica;
    @NotBlank(message = "El nombre de la sociedad no puede estar vacío")
    @Size(max = 150, message = "El nombre de la sociedad debe tener máximo 150 caracteres")
    private String nombre;
    @Size(max = 20, message = "El teléfono debe tener máximo 20 caracteres")
    private String telefono;
    @Size(max = 300, message = "El domicilio debe tener máximo 300 caracteres")
    private String domicilio;
    @Size(max = 20, message = "El tomo debe tener máximo 20 caracteres")
    private String tomo;
    @Size(max = 20, message = "El folio debe tener máximo 20 caracteres")
    private String folio;
    @Size(max = 20, message = "El asiento debe tener máximo 20 caracteres")
    private String asiento;
    private LocalDate fechaInscripcion;
    @NotBlank(message = "El tipo de acciones no puede estar vacío")
    @Size(max = 20, message = "El tipo de acciones debe tener máximo 20 caracteres")
    private String tipoAcciones;
    @NotNull(message = "La cantidad de títulos no puede ser nula")
    @Min(value = 0, message = "La cantidad de títulos no puede ser negativa")
    private Long cantidadTitulos;
    @NotNull(message = "El valor del título no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El valor del título no puede ser negativo")
    private BigDecimal valorTitulo;
    private Long version;
    private List<SociedadRepresentanteDto> representantes;

    public SociedadDto() {
        this.representantes = new ArrayList<>();
    }

    /** No carga los representantes: los carga el service cuando hace falta. */
    public SociedadDto(Sociedad sociedad) {
        this();
        this.id = sociedad.getId();
        this.idBufete = sociedad.getBufete().getId();
        this.cedulaJuridica = sociedad.getCedulaJuridica();
        this.nombre = sociedad.getNombre();
        this.telefono = sociedad.getTelefono();
        this.domicilio = sociedad.getDomicilio();
        this.tomo = sociedad.getTomo();
        this.folio = sociedad.getFolio();
        this.asiento = sociedad.getAsiento();
        this.fechaInscripcion = sociedad.getFechaInscripcion();
        this.tipoAcciones = sociedad.getTipoAcciones();
        this.cantidadTitulos = sociedad.getCantidadTitulos();
        this.valorTitulo = sociedad.getValorTitulo();
        this.version = sociedad.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdBufete() {
        return idBufete;
    }

    public void setIdBufete(Long idBufete) {
        this.idBufete = idBufete;
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

    public List<SociedadRepresentanteDto> getRepresentantes() {
        return representantes;
    }

    public void setRepresentantes(List<SociedadRepresentanteDto> representantes) {
        this.representantes = representantes;
    }

    @Override
    public int hashCode() {
        return 53 * 7 + Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((SociedadDto) obj).id);
    }

    @Override
    public String toString() {
        return "SociedadDto{" + "id=" + id + ", cedulaJuridica=" + cedulaJuridica + ", nombre=" + nombre + '}';
    }
}