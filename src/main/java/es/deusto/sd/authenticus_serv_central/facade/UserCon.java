package es.deusto.sd.authenticus_serv_central.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.authenticus_serv_central.dto.LoginRequestDTO;
import es.deusto.sd.authenticus_serv_central.dto.UserDTO;
import es.deusto.sd.authenticus_serv_central.service.UserServ;
import es.deusto.sd.authenticus_serv_central.dto.LoginResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/auth") // URL base para la autenticación
public class UserCon {

    @Autowired
    private UserServ userServ;

    /**
     * @param userDTO 
     * @return 
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDTO userDTO) {
        try {
            UserDTO createdUser = userServ.crearUsuario(userDTO);
            return ResponseEntity.ok(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al crear el usuario: " + e.getMessage());
        }
    }


    /** * Endpoint para el Login (Inicio de sesión). *
     * @param loginDTO Datos de login (email, contrasena) en formato JSON. * 
     * @return Un ResponseEntity con el LoginResponseDTO (token) o un error. 
     * */ @PostMapping("/login") 
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginDTO) { 
        try { 
            LoginResponseDTO response = userServ.login(loginDTO); // 200 OK con el token en el cuerpo 
            return ResponseEntity.ok(response); 
        } catch (IllegalArgumentException e) { // 401 Unauthorized para credenciales incorrectas 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage()); 
        } catch (Exception e) { 
            return ResponseEntity.internalServerError().body("Error durante el login: " + e.getMessage()); 
        } 
    }   

    /**
     * @param token 
     * @return 
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        
        String extractedToken = token;
        if (token.startsWith("Bearer ")) {
            extractedToken = token.substring(7);
        }
        
        try {
            userServ.logout(extractedToken);
            // 200 OK con un mensaje simple
            return ResponseEntity.ok().body("Logout exitoso.");
        } catch (IllegalArgumentException e) {
            // 401 Unauthorized si el token es inválido
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error durante el logout: " + e.getMessage());
        }
    }

    /** 
     * @param token El token de autorización (enviado como "Authorization: Bearer <token>"). * 
     * @return Un ResponseEntity con un mensaje de éxito o un error. */ 
    @DeleteMapping("/remove/{token}") // Usamos DELETE para eliminar un recurso 
    public ResponseEntity<?> remove(@PathVariable String token) { 
        String extractedToken = token; 
        if (token.startsWith("Bearer ")) { 
            extractedToken = token.substring(7); 
        } try { 
            userServ.removeUser(extractedToken); 
            return ResponseEntity.ok().body("Usuario eliminado correctamente."); 
        } catch (IllegalArgumentException e) { // 401 Unauthorized si el token es inválido 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage()); 
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al eliminar el usuario: " + e.getMessage()); 
        } 
    }   
}

