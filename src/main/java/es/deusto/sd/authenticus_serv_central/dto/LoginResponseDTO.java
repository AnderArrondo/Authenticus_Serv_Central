package es.deusto.sd.authenticus_serv_central.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class LoginResponseDTO {
    @Schema(name="token", description="Token del usuario", example="123e4567-e89b-42d3-a456-556642440000", requiredMode=Schema.RequiredMode.REQUIRED)
    private String token;
    @Schema(name="username", description = "Nombre de usuario asociado al token", example="usuario123", requiredMode=Schema.RequiredMode.REQUIRED)
    private String username;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}