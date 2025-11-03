package es.deusto.sd.authenticus_serv_central.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.authenticus_serv_central.dto.UserDTO;
import es.deusto.sd.authenticus_serv_central.service.UserServ;

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
    
}
