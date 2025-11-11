package es.deusto.sd.authenticus_serv_central.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public class ExpedDTO {
    @Schema(name="nombre", description="El nombre del expediente es único para cada expediente de un mismo usuario.", example="Expediente1", requiredMode=Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Schema(name="tipo", description="El tipo de expediente es convertido a mayúsculas para la conversión, " +
        "por lo que cualquier combinación de mayúsculas y minúsculas es válida dentro de los valores permitidos.",
        allowableValues = {"INTEGRIDAD", "VERACIDAD", "AMBAS"}, requiredMode=Schema.RequiredMode.REQUIRED)
    private String tipo;

    @Schema(name="fecha", description="Fecha del expediente en formato <i>dd/MM/yyyy</id>.", example="25/12/2023", requiredMode=Schema.RequiredMode.REQUIRED)
    private String fecha;

    @ArraySchema(
    arraySchema = @Schema(
        description = "Lista de imágenes asociadas al expediente.",
        requiredMode = Schema.RequiredMode.REQUIRED
    ),
    schema = @Schema(
        description = "Ruta absoluta de la imagen.",
        example = "C:/imagenes/imagen1.jpg",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
)
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
