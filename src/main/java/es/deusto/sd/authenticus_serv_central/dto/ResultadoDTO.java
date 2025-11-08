package es.deusto.sd.authenticus_serv_central.dto;

public class ResultadoDTO {
    private ExpedDTO expediente;
    private float puntuacionIntegridad;
    private float puntuacionVeracidad;

    public ResultadoDTO(ExpedDTO expediente) {
        this.expediente = expediente;
        this.puntuacionIntegridad = -1;
        this.puntuacionVeracidad = -1;
    }

    public ResultadoDTO(ExpedDTO expediente, float puntuacionIntegridad, float puntuacionVeracidad) {
        this.expediente = expediente;
        this.puntuacionIntegridad = puntuacionIntegridad;
        this.puntuacionVeracidad = puntuacionVeracidad;
    }

    public ExpedDTO getExpediente() {
        return expediente;
    }

    public float getPuntuacionIntegridad() {
        return puntuacionIntegridad;
    }

    public float getPuntuacionVeracidad() {
        return puntuacionVeracidad;
    }

    public void setExpediente(ExpedDTO expediente) {
        this.expediente = expediente;
    }

    public void setPuntuacionIntegridad(float puntuacionIntegridad) {
        this.puntuacionIntegridad = puntuacionIntegridad;
    }

    public void setPuntuacionVeracidad(float puntuacionVeracidad) {
        this.puntuacionVeracidad = puntuacionVeracidad;
    }
}
