package es.deusto.sd.authenticus_serv_central.facade;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.authenticus_serv_central.dto.LoginRequestDTO;
import es.deusto.sd.authenticus_serv_central.dto.UserDTO;
import es.deusto.sd.authenticus_serv_central.service.UserServ;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import es.deusto.sd.authenticus_serv_central.dto.LoginResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/auth") 
@Tag(name="Gestión de usuario", description="Funcionalidades relativas a los usuarios.")
public class UserCon {
    private UserServ userServ;

    public UserCon(UserServ userServ) {
        this.userServ = userServ;
    }

    @PostMapping("/signup")
    @Operation(
        summary = "Registro de un nuevo usuario (Sign Up)",
        description = "Crea un nuevo usuario en el sistema a partir de los datos proporcionados (email, contraseña, nombre, teléfono).",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Los datos del usuario en el formato de UserDTO.", required=true
        ))
    @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente. Devuelve el UserDTO creado (sin contraseña).",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                implementation = UserDTO.class,
                example = "{\n" +
                    "\"email\": user1@gmail.com,\n" +
                    "\"contrasena\": 12345678" +
                    "\"nombre\": User One" +
                    "\"telefono\": 123456789" +
                    "}"
            )
        ))
    @ApiResponse(responseCode = "400", description = "Error de validación (ej. email ya registrado, contraseña no válida).",
        content = @Content(
            mediaType = "text/plain",
            schema = @Schema(
                type = "String",
                example = "La contraseña no es válida (debe tener al menos 8 caracteres)."
            )
        ))
    public ResponseEntity<?> signUp(@RequestBody UserDTO userDTO) {
        try {
            UserDTO createdUser = userServ.crearUsuario(userDTO);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/login") 
    @Operation(
        summary = "Login de usuario",
        description = "Inicia sesión y devuelve un token de autenticación.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de inicio de sesión en formato de LoginRequestDTO.", required = true
        ))
    @ApiResponse(responseCode = "200", 
                description = "Login exitoso, devuelve el token de autenticación.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = LoginResponseDTO.class,
                        example = "{\n" +
                            "\"token\": 123e4567-e89b-42d3-a456-556642440000" +
                            "}"
                    )
                ))
    @ApiResponse(responseCode = "400", 
                description = "Datos inválidos para iniciar sesión.",
                content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(
                        type = "String",
                        example = "Email o contraseña incorrectos."
                    )
                )
    )
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginDTO) { 
        try { 
            LoginResponseDTO response = userServ.login(loginDTO); 
            return ResponseEntity.ok(response); 
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); 
        }
    }   

    @Operation(summary = "Cierre de sesión (Logout)",
               description = "Invalida un token de sesión activo para cerrar la sesión del usuario.",
               parameters = {
                    @Parameter(name = "token", description = "Token de sesión del usuario.")
               })
    @ApiResponse(responseCode = "200", description = "Logout exitoso.",
        content = @Content(
            mediaType = "text/plain",
            schema = @Schema(
                type="String",
                example = "\"Logout exitoso.\""
            )
        ))
    @ApiResponse(responseCode = "400", description = "Token no válido o sesión ya cerrada.",
        content = @Content(
            mediaType = "text/plain",
            schema = @Schema(
                type="String",
                example = "Token no válido o sesión ya cerrada."
            )
        ))
    
    @PutMapping("/logout/{token}")
    public ResponseEntity<?> logout(@PathVariable String token) { 
        try {
            userServ.logout(token);
            return ResponseEntity.ok().body("Logout exitoso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/remove/{token}")

    @Operation(summary = "Eliminar usuario", 
                description = "Elimina el usuario asociado al token de sesión proporcionado.",
                parameters = {
                    @Parameter(name="token", description = "Token de sesión del usuario.")
                })

    @ApiResponse(responseCode = "200", 
                description = "Usuario eliminado correctamente.",
                content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(
                        type = "String",
                        example = "Usuario eliminado correctamente."
                    )
                ))
    @ApiResponse(responseCode = "400",
                description = "Datos inválidos para eliminar usuario.",
                content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(
                        type = "String",
                        example = "Token no válido o sesión ya cerrada."
                    )
                ))
    public ResponseEntity<?> remove(@PathVariable String token) { 
        String extractedToken = token; 
       
        try { 
            userServ.removeUser(extractedToken); 
            return ResponseEntity.ok().body("Usuario eliminado correctamente."); 
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); 
        }
    }   
}

