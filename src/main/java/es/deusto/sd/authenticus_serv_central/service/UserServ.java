package es.deusto.sd.authenticus_serv_central.service;

import org.springframework.stereotype.Service;
import es.deusto.sd.authenticus_serv_central.dto.UserDTO;
import es.deusto.sd.authenticus_serv_central.dto.UserDTO;
import es.deusto.sd.authenticus_serv_central.dto.LoginRequestDTO;
import es.deusto.sd.authenticus_serv_central.dto.LoginResponseDTO;
import es.deusto.sd.authenticus_serv_central.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServ {

    private Map<String, User> simulatedUserDatabase = new HashMap<>();
    private Map<String, User> activeTokens = new HashMap<>();
    
    /**
     * @param userDTO
     * @return 
     * @throws IllegalArgumentException 
     */
    public UserDTO crearUsuario(UserDTO userDTO) {

        if (simulatedUserDatabase.containsKey(userDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        if (userDTO.getContrasena() == null || userDTO.getContrasena().length() < 8) {
            throw new IllegalArgumentException("La contraseña no es válida (debe tener al menos 8 caracteres).");
        }

        User newUser = new User(
            userDTO.getEmail(), 
            userDTO.getContrasena(), // En una BBDD real, aquí se hashearía
            userDTO.getNombre(), 
            userDTO.getTelefono()
        );

        simulatedUserDatabase.put(newUser.getEmail(), newUser);        
        
        
        System.out.println("SIMULACIÓN: Creando y 'guardando' usuario...");
        System.out.println(" - Email: " + newUser.getEmail());
        System.out.println(" - Usuarios 'en BBDD' ahora: " + simulatedUserDatabase.size());
        
        // Por seguridad, nunca devolvemos la contraseña al cliente.
        userDTO.setContrasena(null); 
        
        return userDTO;
    }

    

    public LoginResponseDTO login(LoginRequestDTO loginDTO) { 
        User user = simulatedUserDatabase.get(loginDTO.getEmail());
        if (user == null || !user.getContrasena().equals(loginDTO.getContrasena())) {
            throw new IllegalArgumentException("Email o contraseña incorrectos."); 
        }
            String token = UUID.randomUUID().toString();
            activeTokens.put(token, user); 
            System.out.println("SIMULACIÓN: Login exitoso.");
            System.out.println(" - Usuario: " + user.getEmail()); 
            System.out.println(" - Token generado: " + token); 
            System.out.println(" - Tokens activos ahora: " + activeTokens.size());
            return new LoginResponseDTO(token); } 
    }  

