package es.deusto.sd.authenticus_serv_central.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ExpedDTO {
    @Id
    @Schema(name="Nombre", description="Nombre del expediente", example="Expediente1", requiredMode=Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Schema(name="Tipo", description="Tipo del expediente:\n" + "El string es convertido a mayúsculas al comparar," +
    "por lo que cualquier combinación de mayúsculas y minúsculas es válida dentro de los valores permitidos",
    allowableValues = {"INTEGRIDAD", "VERACIDAD", "AMBAS"}, requiredMode=Schema.RequiredMode.REQUIRED)
    private String tipo;
    @Schema(name="Fecha", description="Fecha del expediente en formato dd/MM/yyyy", example="25/12/2023", requiredMode=Schema.RequiredMode.REQUIRED)
    private String fecha;

    @Schema(name="Imagenes", description="Lista de imágenes asociadas al expediente", requiredMode=Schema.RequiredMode.NOT_REQUIRED)
    private List<String> imagenes;

    public ExpedDTO(String nombre, String tipo, String fecha, List<String> imagenes) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.fecha = fecha;
        this.imagenes = imagenes;
    }


    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public List<String> getImagenes() {
        return imagenes;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }
}
