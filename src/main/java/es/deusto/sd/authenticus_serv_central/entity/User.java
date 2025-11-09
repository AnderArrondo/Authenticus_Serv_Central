package es.deusto.sd.authenticus_serv_central.entity;

import java.util.Objects;

public class User {
    private String email;
    private String contrasena;
    private String nombre;
    private String telefono;

    public User(String email, String contrasena, String nombre, String telefono) {
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

    public String getTelefono() {
        return telefono;
    }

    //Setters

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override

    public String toString() {
        return "User [email=" + email + ", contrasena=" + contrasena + ", nombre=" + nombre + ", telefono=" + telefono
                + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (email == null && other.email != null) {
            return false;
        } else if (!email.toUpperCase().equals(other.email.toUpperCase())) // email como atributo unico
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.email);
    }
}


