package es.deusto.sd.authenticus_serv_central.service;

import org.springframework.stereotype.Service;
import es.deusto.sd.authenticus_serv_central.dto.UserDTO;
import es.deusto.sd.authenticus_serv_central.dto.LoginRequestDTO;
import es.deusto.sd.authenticus_serv_central.dto.LoginResponseDTO;
import es.deusto.sd.authenticus_serv_central.entity.User;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserServ {

    
    /**
     * @param userDTO
     * @return 
     * @throws IllegalArgumentException 
     */
    public UserDTO crearUsuario(UserDTO userDTO) {

        if (StateManagement.usuarios.containsKey(userDTO.getEmail())) {
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

        StateManagement.usuarios.put(newUser.getEmail(), newUser); 
        StateManagement.usuarioExpediente.put(newUser, new ArrayList<>());       
        
        System.out.println("SIMULACIÓN: Creando y 'guardando' usuario...");
        System.out.println(" - Email: " + newUser.getEmail());
        System.out.println(" - Usuarios 'en BBDD' ahora: " + StateManagement.usuarios.size());
        
        // Por seguridad, nunca devolvemos la contraseña al cliente.
        userDTO.setContrasena(null); 
        
        return userDTO;
    }

    

    public LoginResponseDTO login(LoginRequestDTO loginDTO) { 
        User user = StateManagement.usuarios.get(loginDTO.getEmail());
        if (user == null || !user.getContrasena().equals(loginDTO.getContrasena())) {
            throw new IllegalArgumentException("Email o contraseña incorrectos."); 
        }
        String token = UUID.randomUUID().toString();
        StateManagement.tokenUsuario.put(token, user); 
        return new LoginResponseDTO(token); } 


    /**
     * @param token 
     * @throws IllegalArgumentException 
     */
    public void logout(String token) {
        if (StateManagement.isActiveToken(token)) {
            
            User user = StateManagement.tokenUsuario.remove(token);

            System.out.println("SIMULACIÓN: Logout exitoso.");
            System.out.println(" - Usuario deslogeado: " + user.getEmail());
            System.out.println(" - Token invalidado: " + token);
            System.out.println(" - Tokens activos ahora: " + StateManagement.tokenUsuario.size());
        } else {
            throw new IllegalArgumentException("Token no válido o sesión ya cerrada.");
        }
    }

    /** 
     * @param token El token de sesión del usuario a eliminar. * 
     * @throws IllegalArgumentException si el token no es válido. */ 
    public void removeUser(String token) { 
        if (!StateManagement.isActiveToken(token)) { 
            throw new IllegalArgumentException("Token no válido o sesión ya cerrada."); 
        }
        
        User user = StateManagement.tokenUsuario.get(token); 
        StateManagement.usuarios.remove(user.getEmail());

        StateManagement.usuarioExpediente.remove(user);
        StateManagement.tokenUsuario.remove(token);
    }
}
     


