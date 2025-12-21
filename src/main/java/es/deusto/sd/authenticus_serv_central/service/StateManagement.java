package es.deusto.sd.authenticus_serv_central.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.deusto.sd.authenticus_serv_central.dto.ExpedDTO;
import es.deusto.sd.authenticus_serv_central.dto.UserDTO;
import es.deusto.sd.authenticus_serv_central.entity.ArchImagen;
import es.deusto.sd.authenticus_serv_central.entity.Exped;
import es.deusto.sd.authenticus_serv_central.entity.TipoExp;
import es.deusto.sd.authenticus_serv_central.entity.User;
import es.deusto.sd.authenticus_serv_central.external.BDGateway;

public class StateManagement { 
    
    public static Map<String, User> usuarios = new HashMap<>();
    public static Map<String, User> tokenUsuario = new HashMap<>();
    public static Map<User, List<Exped>> usuarioExpediente = new HashMap<>();

    private static final SimpleDateFormat dtFormatter = new SimpleDateFormat("dd/MM/yyyy");

    public static boolean isActiveToken(String token) {
        return tokenUsuario.containsKey(token);
    }

    public static void cargarDatosDeBD(BDGateway bdGateway) {
        System.out.println("--- INICIANDO CARGA DE DATOS DESDE EL SERVIDOR BD (Puerto 8082) ---");
        
        try {
            // 1. Obtener todos los usuarios
            List<UserDTO> usersDTO = bdGateway.getAllUsers().orElse(Collections.emptyList());
            
            for (UserDTO uDTO : usersDTO) {
                // Crear Usuario local
                User user = new User(
                    uDTO.getEmail(),
                    uDTO.getContrasena(), 
                    uDTO.getNombre(),
                    uDTO.getTelefono()
                );

                usuarios.put(user.getEmail(), user);
                
                List<Exped> listaExpeds = new ArrayList<>();

                // 2. Obtener expedientes
                List<ExpedDTO> expedsDTO = bdGateway.getExpedientesByEmail(user.getEmail())
                                                    .orElse(Collections.emptyList());

                for (ExpedDTO eDTO : expedsDTO) {
                    try {
                        List<ArchImagen> imgs = new ArrayList<>();
                        if (eDTO.getImagenes() != null) {
                            for (String path : eDTO.getImagenes()) {
                                imgs.add(new ArchImagen(path));
                            }
                        }

                        TipoExp tipoEnum;
                        try {
                            tipoEnum = TipoExp.valueOf(eDTO.getTipo().toUpperCase());
                        } catch (Exception ex) {
                            tipoEnum = TipoExp.AMBAS; 
                        }

                        Exped exped = new Exped(
                            eDTO.getNombre(),
                            tipoEnum,
                            dtFormatter.parse(eDTO.getFecha()), 
                            imgs
                        );
                        listaExpeds.add(exped);
                    } catch (Exception ex) {
                        System.err.println("Error al parsear expediente: " + ex.getMessage());
                    }
                }

                usuarioExpediente.put(user, listaExpeds);
                System.out.println("Cargado usuario: " + user.getEmail() + " con " + listaExpeds.size() + " expedientes.");
            }
        } catch (Exception e) {
            System.err.println("ERROR FATAL cargando datos de BD: " + e.getMessage());
        }
        
        System.out.println("--- CARGA FINALIZADA ---");
    }

} 