package es.deusto.sd.authenticus_serv_central.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class LoginRequestDTO {
   
    @Id
    @Schema(name="Email", description="Email del usuario", example="ramon@gmail.com", requiredMode=Schema.RequiredMode.REQUIRED)
    private String email;
    @Schema(name="Contraseña", description="Contraseña del usuario", example="123", requiredMode=Schema.RequiredMode.REQUIRED)
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