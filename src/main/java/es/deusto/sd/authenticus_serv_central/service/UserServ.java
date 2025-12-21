package es.deusto.sd.authenticus_serv_central.service;

import org.springframework.stereotype.Service;
import es.deusto.sd.authenticus_serv_central.dto.UserDTO;
import es.deusto.sd.authenticus_serv_central.dto.LoginRequestDTO;
import es.deusto.sd.authenticus_serv_central.dto.LoginResponseDTO;
import es.deusto.sd.authenticus_serv_central.entity.User;
import es.deusto.sd.authenticus_serv_central.external.BDGateway;


import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserServ {

    private final BDGateway bdGateway = new BDGateway();
    
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
        
        bdGateway.saveUser(userDTO);

        // Por seguridad, nunca devolvemos la contraseña al cliente.
        userDTO.setContrasena(null); 
        
        return userDTO;
    }

    

    public LoginResponseDTO login(LoginRequestDTO loginDTO) { 
        User user = StateManagement.usuarios.get(loginDTO.getEmail());
        if (user == null || !user.getContrasena().equals(loginDTO.getContrasena())) {
            throw new IllegalArgumentException("Email o contraseña incorrectos."); 
        }

        for (User u : StateManagement.tokenUsuario.values()) {
            if (u.getEmail().toUpperCase().equals(loginDTO.getEmail().toUpperCase())) {
                throw new IllegalArgumentException("Este usuario ya ha iniciado sesión.");
            }
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
            try {
                User user = StateManagement.tokenUsuario.remove(token);
            } catch (Exception e) {
                throw e;
            }

            // System.out.println("SIMULACIÓN: Logout exitoso.");
            // System.out.println(" - Usuario deslogeado: " + user.getEmail());
            // System.out.println(" - Token invalidado: " + token);
            // System.out.println(" - Tokens activos ahora: " + StateManagement.tokenUsuario.size());
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
        bdGateway.deleteUser(user.getEmail());

        StateManagement.usuarioExpediente.remove(user);
        StateManagement.tokenUsuario.remove(token);
    }

     

    // public void removeUserAndAllData(String token) throws Exception {
    //     if (!StateManagement.isActiveToken(token)) {
    //         throw new IllegalArgumentException("Token no válido o sesión ya cerrada.");
    //     }

    //     User user = StateManagement.tokenUsuario.get(token);
    //     if (user == null) {
    //         throw new IllegalArgumentException("Sesión inválida.");
    //     }

    //     String email = user.getEmail();

    //     servBDDAO.deleteUserAndCases(email);

    //     StateManagement.tokenUsuario.entrySet()
    //         .removeIf(e -> email.equalsIgnoreCase(e.getValue().getEmail()));

        
    //     StateManagement.usuarioExpediente.remove(user);

        
    //     StateManagement.usuarios.remove(email);
    // }
}