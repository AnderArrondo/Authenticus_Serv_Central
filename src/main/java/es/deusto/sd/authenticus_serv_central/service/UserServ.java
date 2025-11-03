package es.deusto.sd.authenticus_serv_central.service;

import org.springframework.stereotype.Service;
import es.deusto.sd.authenticus_serv_central.dto.UserDTO;

@Service
public class UserServ {
    
    /**
     * @param userDTO
     * @return 
     * @throws IllegalArgumentException 
     */
    public UserDTO crearUsuario(UserDTO userDTO) {

        if (userDTO.getContrasena() == null || userDTO.getContrasena().length() < 8) {
            throw new IllegalArgumentException("La contraseña no es válida (debe tener al menos 8 caracteres).");
        }
        
        
        
        System.out.println("SIMULACIÓN: Creando usuario...");
        System.out.println(" - Email: " + userDTO.getEmail());
        System.out.println(" - Nombre: " + userDTO.getNombre());
        
        // Por seguridad, nunca devolvemos la contraseña al cliente.
        userDTO.setContrasena(null); 
        
        return userDTO;
    }
}