package es.deusto.sd.authenticus_serv_central.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UserDTO {
    
    @Id
    @Schema(name="email", description="Email del usuario, es el atributo que identifica a un usuario.", example="ramon@gmail.com", requiredMode=Schema.RequiredMode.REQUIRED)
    private String email;
    @Schema(name="contrasena", description="Contraseña del usuario, debe contener al menos 8 caracteres.", example="12345678", requiredMode=Schema.RequiredMode.REQUIRED)
    private String contrasena;
    @Schema(name="nombre", description="Nombre del usuario", example="Ramon", requiredMode=Schema.RequiredMode.REQUIRED)
    private String nombre;
    @Schema(name="telefono", description="Numero de telefono del usuario", example="943271854", requiredMode=Schema.RequiredMode.REQUIRED)
    private String telefono;

    public UserDTO() {
    }
    
    public UserDTO(String email, String contrasena, String nombre, String telefono) {
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

    public String getTelefono() {
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

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "UserDTO [email=" + email + ", contrasena=********, nombre=" + nombre + ", telefono=" + telefono + "]";
    }
}