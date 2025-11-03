package es.deusto.sd.authenticus_serv_central.dto;

public class LoginRequestDTO {
   
    private String email;
    private String contrasena;

    // Constructor vacío (necesario para Spring)
    public LoginRequestDTO() {
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}