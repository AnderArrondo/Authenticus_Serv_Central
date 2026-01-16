package es.deusto.sd.authenticus_serv_central.external;

import java.util.List;
import java.util.Optional;

import es.deusto.sd.authenticus_serv_central.dto.ExpedDTO;
import es.deusto.sd.authenticus_serv_central.dto.ResultadoDTO;
import es.deusto.sd.authenticus_serv_central.dto.UserDTO;

// CORRECCION: interfaz añadida a gateway
public interface IBDGateway {

    Optional<UserDTO> saveUser(UserDTO userDTO);

    Optional<List<UserDTO>> getAllUsers();

    boolean deleteUser(String email);

    Optional<ExpedDTO> saveExped(ExpedDTO expedDTO, String userEmail);

    boolean deleteExped(String nombreCaso, String userEmail);

    boolean updateArchImagenResultado(String nombreCaso, ResultadoDTO resultadoDTO, String userEmail);

    Optional<List<ExpedDTO>> getExpedientesByEmail(String email);

    boolean ainadirArchivos(String email, String nombreCaso, List<String> nuevasImgs);
}
