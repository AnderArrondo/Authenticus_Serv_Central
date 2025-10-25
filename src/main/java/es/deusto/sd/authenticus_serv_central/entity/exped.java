package es.deusto.sd.authenticus_serv_central.entity;

import java.util.Date;
import java.util.List;

public class Exped {
    String nombre;
    TipoExp tipo;
    Date fecha;
    List<ArchImagen> imagenes;

    
    public Exped(String nombre, TipoExp tipo, Date fecha, List<ArchImagen> imagenes) {
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

    public List<ArchImagen> getImagenes() {
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

    public void setImagenes(List<ArchImagen> imagenes) {
        this.imagenes = imagenes;
    }
}
