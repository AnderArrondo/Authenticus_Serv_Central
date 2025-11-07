package es.deusto.sd.authenticus_serv_central.dto;

import java.util.List;

public class ResultadoDTO {
    private long casoId;
    private String nombreCaso;
    private String tipo;
    private String fecha; 
    private List<ArchivoResultadoDTO> archivos;

    public ResultadoDTO(long casoId, String nombreCaso, String tipo, String fecha, List<ArchivoResultadoDTO> archivos) {
        this.casoId = casoId;
        this.nombreCaso = nombreCaso;
        this.tipo = tipo;
        this.fecha = fecha;
        this.archivos = archivos;
    }

    public long getCasoId() {
        return casoId;
    }

    public String getNombreCaso() {
        return nombreCaso;
    }

    public String getTipo() {
        return tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public List<ArchivoResultadoDTO> getArchivos() {
        return archivos;
    }
}
