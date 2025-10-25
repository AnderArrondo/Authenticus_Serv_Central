package es.deusto.sd.authenticus_serv_central.dto;

import java.util.Date;
import java.util.List;

import es.deusto.sd.authenticus_serv_central.entity.ArchImagen;
import es.deusto.sd.authenticus_serv_central.entity.TipoExp;

public class ExpedDTO {
    private long id;
    private String nombre;
    private TipoExp tipo;
    private Date fecha;
    private List<ArchImagen> imagenes;

    public ExpedDTO() {}

    public ExpedDTO(long id, String nombre, TipoExp tipo, Date fecha, List<ArchImagen> imagenes) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fecha = fecha;
        this.imagenes = imagenes;
    }


    public long getId() {
        return id;
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

    // no setId()

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
