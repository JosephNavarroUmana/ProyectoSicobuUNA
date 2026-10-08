package cr.ac.una.sicobuws.model;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * DTO del requisito de un instrumento. No tiene id propio: se identifica por
 * (idInstrumento, idRequisito). "descripcion" y "orden" son solo informativos.
 */
public class InstrumentoRequisitoDto {

    private Long idInstrumento;
    @NotNull(message = "Debe indicar el requisito")
    private Long idRequisito;
    private String descripcion;
    private Integer orden;
    private Boolean cumplido;
    private Boolean noAplica;

    public InstrumentoRequisitoDto() {
        this.cumplido = false;
        this.noAplica = false;
    }

    public InstrumentoRequisitoDto(InstrumentoRequisito requisito) {
        this();
        this.idInstrumento = requisito.getId().getIdInstrumento();
        this.idRequisito = requisito.getId().getIdRequisito();
        this.descripcion = requisito.getRequisito().getDescripcion();
        this.orden = requisito.getRequisito().getOrden();
        this.cumplido = requisito.getCumplido() != null && requisito.getCumplido() == 1;
        this.noAplica = requisito.getNoAplica() != null && requisito.getNoAplica() == 1;
    }

    /** Un requisito no puede estar cumplido y "no aplica" al mismo tiempo. */
    @JsonbTransient
    @AssertTrue(message = "Un requisito no puede estar cumplido y no aplica a la vez")
    public boolean isEstadoValido() {
        return !(Boolean.TRUE.equals(cumplido) && Boolean.TRUE.equals(noAplica));
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

    public Boolean getCumplido() {
        return cumplido;
    }

    public void setCumplido(Boolean cumplido) {
        this.cumplido = cumplido;
    }

    public Boolean getNoAplica() {
        return noAplica;
    }

    public void setNoAplica(Boolean noAplica) {
        this.noAplica = noAplica;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInstrumento, idRequisito);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        InstrumentoRequisitoDto other = (InstrumentoRequisitoDto) obj;
        return Objects.equals(this.idInstrumento, other.idInstrumento)
                && Objects.equals(this.idRequisito, other.idRequisito);
    }

    @Override
    public String toString() {
        return "InstrumentoRequisitoDto{" + "idRequisito=" + idRequisito + ", cumplido=" + cumplido + ", noAplica=" + noAplica + '}';
    }
}