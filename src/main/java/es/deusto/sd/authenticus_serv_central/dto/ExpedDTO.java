package es.deusto.sd.authenticus_serv_central.dto;

import java.util.List;

public class ExpedDTO {
    private String nombre;
    private String tipo;
    private String fecha;
    private List<String> imagenes;


    public ExpedDTO() {}

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
