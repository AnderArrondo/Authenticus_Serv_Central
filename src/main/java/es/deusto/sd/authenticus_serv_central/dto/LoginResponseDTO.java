package es.deusto.sd.authenticus_serv_central.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class LoginResponseDTO {
   @Id
   @Schema(name="Token", description="Token del usuario", example="123e4567-e89b-42d3-a456-556642440000", requiredMode=Schema.RequiredMode.REQUIRED)
    private String token;

    // Constructor vacío
    public LoginResponseDTO() {
    }

    // Constructor para rellenar el token fácilmente
    public LoginResponseDTO(String token) {
        this.token = token;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}