package es.deusto.sd.authenticus_serv_central.entity;

public class ArchImagen {
    private String nombre;
    private String extension;
    private String path;
    
    
    ArchImagen(String nombre, String extension, String path) {
        this.nombre = nombre;
        this.extension = extension;
        this.path = path;
    }


    public String getNombre() {
        return nombre;
    }

    public String getExtension() {
        return extension;
    }

    public String getPath() {
        return path;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getFilePath() {
        return this.path + this.nombre + "." + this.extension;
    }
}
