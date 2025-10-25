package es.deusto.sd.authenticus_serv_central.entity;

public class ArchImagen {
    private long id;
    private String nombre;
    private String extension;
    private String path;

    public ArchImagen(long id, String nombre, String extension, String path) {
        this.id = id;
        this.nombre = nombre;
        this.extension = extension;
        this.path = path;
    }

    public ArchImagen(long id, String absPath) {
        this.id = id;
        String[] parts = absPath.replace("\\", "/").split("/");
        String fileName = parts[parts.length - 1];
        this.path = absPath.replace("/" + fileName, "/");
        String[] nameParts = fileName.split("\\.");
        this.nombre = nameParts[0];
        this.extension = nameParts.length > 1 ? nameParts[1] : "";
    }

    public long getId() {
        return id;
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


    // no setId()

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
