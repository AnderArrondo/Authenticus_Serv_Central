package es.deusto.sd.authenticus_serv_central.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import es.deusto.sd.authenticus_serv_central.dto.UserDTO;
import es.deusto.sd.authenticus_serv_central.entity.ArchImagen;
import es.deusto.sd.authenticus_serv_central.entity.Exped;
import es.deusto.sd.authenticus_serv_central.entity.TipoExp;
import es.deusto.sd.authenticus_serv_central.entity.User;
import es.deusto.sd.authenticus_serv_central.external.BDGateway;

public class StateManagement {
    // email -> user
    public static Map<String, User> usuarios = new HashMap<>();
    // token -> user
    public static Map<String, User> tokenUsuario = new HashMap<>();
    // user -> listaExp
    public static Map<User, List<Exped>> usuarioExpediente = new HashMap<>();

    BDGateway bdGateway = new BDGateway();

    static {
        // TODO: leer datos de BD
        // Optional<List<UserDTO>> maybeUsers = new BDGateway().getAllUsers();
        // maybeUsers.ifPresent(userDTOs -> {
        //     for (UserDTO userDTO : userDTOs) {
        //         User user = new User(
        //             userDTO.getEmail(),
        //             userDTO.getContrasena(),
        //             userDTO.getNombre(),
        //             userDTO.getTelefono()
        //         );
        //         usuarios.put(user.getEmail(), user);
        //         usuarioExpediente.put(user, new ArrayList<>()); // TODO cargar expedientes reales o iniciar vacio
        //     }
        // });

        // Imágenes de prueba
        ArchImagen img1 = new ArchImagen("C:/imagenes/escenario_crimen.jpg");
        ArchImagen img2 = new ArchImagen("C:/imagenes/prueba1.png");
        ArchImagen img3 = new ArchImagen("C:/imagenes/pistas.jpg");

        // Crear varios expedientes y rellenar el mapa local 'expedientes'
        Calendar calendar = Calendar.getInstance();
        Date date;

        calendar.set(2024, Calendar.JANUARY, 15);
        date = calendar.getTime();
        Exped exped1 = new Exped("Caso Styles", TipoExp.AMBAS, date, List.of(img1, img2));

        calendar.set(2020, Calendar.JUNE, 30);
        date = calendar.getTime();
        Exped exped2 = new Exped("Caso Benedicto", TipoExp.VERACIDAD, date, List.of(img1));

        calendar.set(2017, Calendar.DECEMBER, 2);
        date = calendar.getTime();
        Exped exped3 = new Exped("Caso Pamela", TipoExp.INTEGRIDAD, date, List.of(img2, img3));

        User user1 = new User("user1@gmail.com", "password123", "User One", "123456789");
        User user2 = new User("user2@gmail.com", "password456", "User Two", "987654321");

        StateManagement.usuarioExpediente.put(user1, new ArrayList<>(List.of(exped1, exped2)));
        StateManagement.usuarioExpediente.put(user2, new ArrayList<>(List.of(exped3)));

        StateManagement.tokenUsuario.put("token_user1", user1);
        StateManagement.tokenUsuario.put("token_user2", user2);
    }

    public static boolean isActiveToken(String token) {
        return tokenUsuario.containsKey(token);
    }
}
