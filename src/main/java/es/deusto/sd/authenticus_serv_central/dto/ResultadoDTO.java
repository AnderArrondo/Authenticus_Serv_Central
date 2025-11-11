package es.deusto.sd.authenticus_serv_central.dto;

import java.util.Date;
import java.util.List;

import es.deusto.sd.authenticus_serv_central.entity.ArchImagen;
import es.deusto.sd.authenticus_serv_central.entity.TipoExp;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public class ResultadoDTO {
    @Schema(name="nombre", description="El nombre del expediente del que provienen los resultados.", example="Caso Styles", requiredMode=Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Schema(name="tipo", description="El tipo de expediente", example = "AMBAS", 
        allowableValues = {"INTEGRIDAD", "VERACIDAD", "AMBAS"}, requiredMode=Schema.RequiredMode.REQUIRED)
    private TipoExp tipo;

    @Schema(name="fecha", description="Fecha del expediente en formato <i>dd/MM/yyyy</id>.", example="15/01/2024", requiredMode=Schema.RequiredMode.REQUIRED)
    private Date fecha;

    @ArraySchema(
    arraySchema = @Schema(
        description = "Lista de imágenes asociadas al expediente con sus respectivos resultados de procesamiento..",
        requiredMode = Schema.RequiredMode.REQUIRED
    ),
    schema = @Schema(
        description = "Ruta absoluta de la imagen.",
        implementation = ArchImagenDTO.class,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
)
    private List<ArchImagenDTO> imagenes;

    public ResultadoDTO(String nombre, TipoExp tipo, Date fecha, List<ArchImagenDTO> imagenes) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.fecha = fecha;
        this.imagenes = imagenes;
    }


    public String getNombre() {
        return nombre;
    }

    public TipoExp getTipo() {
        return tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public List<ArchImagenDTO> getImagenes() {
        return imagenes;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(TipoExp tipo) {
        this.tipo = tipo;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setImagenes(List<ArchImagenDTO> imagenes) {
        this.imagenes = imagenes;
    }
}
