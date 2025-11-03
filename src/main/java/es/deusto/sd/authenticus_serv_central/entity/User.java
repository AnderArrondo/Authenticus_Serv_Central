package es.deusto.sd.authenticus_serv_central.entity;

public class User {
    private String email;
    private String contrasena;
    private String nombre;
    private int telefono;

    public User(String email, String contrasena, String nombre, int telefono) {
        this.email = email;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    //Getters

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTelefono() {
        return telefono;
    }

    //Setters

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    @Override

    public String toString() {
        return "User [email=" + email + ", contrasena=" + contrasena + ", nombre=" + nombre + ", telefono=" + telefono
                + "]";
    }

}


