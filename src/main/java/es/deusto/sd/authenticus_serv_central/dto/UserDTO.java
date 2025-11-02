package es.deusto.sd.authenticus_serv_central.dto;

public class UserDTO {
    
    private String email;
    private String contrasena;
    private String nombre;
    private int telefono;

    public UserDTO() {
    }

    public UserDTO(String email, String contrasena, String nombre, int telefono) {
        this.email = email;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.telefono = telefono;
    }


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


    public void setEmail(String email) {
        this.email = email;
    }

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
        return "UserDTO [email=" + email + ", contrasena=********, nombre=" + nombre + ", telefono=" + telefono + "]";
    }
}