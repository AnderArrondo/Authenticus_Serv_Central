package es.deusto.sd.authenticus_serv_central.entity;

public class ArchImagen {
    private String nombre;
    private String path;

    public ArchImagen(String nombre, String path) {
        this.nombre = nombre;
        this.path = path;
    }

    public ArchImagen(String absPath) {
        String[] parts = absPath.split("/");
        this.nombre = parts[parts.length - 1];
        this.path = absPath.substring(0, absPath.lastIndexOf("/"));
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return this.path + "/" + this.nombre;
    }
}
