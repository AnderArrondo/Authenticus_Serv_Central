package es.deusto.sd.authenticus_serv_central.dto;

public class ArchivoResultadoDTO {
    private String archivo;
    private double integridad;
    private double veracidad;

    public ArchivoResultadoDTO(String archivo, double integridad, double veracidad) {
        this.archivo = archivo;
        this.integridad = integridad;
        this.veracidad = veracidad;
    }

    public String getArchivo() {
        return archivo;
    }

    public double getIntegridad() {
        return integridad;
    }

    public double getVeracidad() {
        return veracidad;
    }
}
