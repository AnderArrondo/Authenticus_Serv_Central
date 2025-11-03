package es.deusto.sd.authenticus_serv_central.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.deusto.sd.authenticus_serv_central.entity.Exped;
import es.deusto.sd.authenticus_serv_central.entity.User;

public class StateManagement {
    public static Set<User> usuarios = new HashSet<>();
    public static Map<String, User> tokenUsuario = new HashMap<>();
    public static Map<User, List<Exped>> usuarioExpediente = new HashMap<>();

    public static boolean isActiveToken(String token) {
        return tokenUsuario.containsKey(token);
    }
}
