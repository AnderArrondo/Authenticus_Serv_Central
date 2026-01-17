package es.deusto.sd.authenticus_serv_central.entity;

import java.util.Objects;



public class ArchImagen {
    private String nombre;
    private String path;
    private double pVeracidad;
    private double pIntegridad;

    public ArchImagen(String nombre, String path) {
        this.nombre = nombre;
        this.path = path;
        this.pVeracidad = -1.0;
        this.pIntegridad = -1.0;
    }

    public ArchImagen(String absPath) {
        System.out.println("absPath = " +absPath);
        String normalizedPath = absPath.trim().replace("\\", "/");
        System.out.println("normalizedPath = " + normalizedPath);
        String[] parts = normalizedPath.split("/");
        this.nombre = parts[parts.length - 1];
        this.path = normalizedPath.substring(0, normalizedPath.lastIndexOf("/"));
        System.out.println("NOMBRE = " + this.nombre);
        System.out.println("PATH   = " + this.path);
        this.pVeracidad = -1.0;
        this.pIntegridad = -1.0;
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


        public double getpIntegridad() {
        return pIntegridad;
    }

    public void setpIntegridad(double pIntegridad) {
        this.pIntegridad = pIntegridad;
    }

    public double getpVeracidad() {
        return pVeracidad;
    }

    public void setpVeracidad(double pVeracidad) {
        this.pVeracidad = pVeracidad;
    }

    @Override
    public String toString() {
        return this.path + "/" + this.nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ArchImagen)) return false;
        ArchImagen other = (ArchImagen) obj;
        return this.nombre.toUpperCase().equals(other.nombre.toUpperCase()) && path.toUpperCase().equals(other.path.toUpperCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.nombre, this.path);
    }
}
