package es.deusto.sd.authenticus_serv_central.dto;

public class LoginResponseDTO {
   
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