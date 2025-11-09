package es.deusto.sd.authenticus_serv_central.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class ResultadoDTO {
    @Schema(name="Expediente", description="DTO del expediente original.", requiredMode = RequiredMode.REQUIRED)
    private ExpedDTO expediente;
    @Schema(name="Puntuación de integridad", description="Puntuación media de la integridad de las imagenes relativas al caso. Entre [0, 1] si se ha procesado, sino -1.", requiredMode = RequiredMode.REQUIRED)
    private double puntuacionIntegridad;
    @Schema(name="Puntuación de veracidad", description="Puntuación media de la veracidad de las imagenes relativas al caso. Entre [0, 1] si se ha procesado, sino -1.", requiredMode = RequiredMode.REQUIRED)
    private double puntuacionVeracidad;

    public ResultadoDTO(ExpedDTO expediente) {
        this.expediente = expediente;
        this.puntuacionIntegridad = -1;
        this.puntuacionVeracidad = -1;
    }

    public ResultadoDTO(ExpedDTO expediente, double puntuacionIntegridad, double puntuacionVeracidad) {
        this.expediente = expediente;
        this.puntuacionIntegridad = puntuacionIntegridad;
        this.puntuacionVeracidad = puntuacionVeracidad;
    }

    public ExpedDTO getExpediente() {
        return expediente;
    }

    public double getPuntuacionIntegridad() {
        return puntuacionIntegridad;
    }

    public double getPuntuacionVeracidad() {
        return puntuacionVeracidad;
    }

    public void setExpediente(ExpedDTO expediente) {
        this.expediente = expediente;
    }

    public void setPuntuacionIntegridad(double puntuacionIntegridad) {
        this.puntuacionIntegridad = puntuacionIntegridad;
    }

    public void setPuntuacionVeracidad(double puntuacionVeracidad) {
        this.puntuacionVeracidad = puntuacionVeracidad;
    }
}
