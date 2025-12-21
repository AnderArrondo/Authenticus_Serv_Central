package es.deusto.sd.authenticus_serv_central.gateways;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.sd.authenticus_serv_central.dto.ExpedDTO;
import es.deusto.sd.authenticus_serv_central.dto.UserDTO;
import es.deusto.sd.authenticus_serv_central.entity.ArchImagen;
import es.deusto.sd.authenticus_serv_central.entity.Exped;
import es.deusto.sd.authenticus_serv_central.entity.TipoExp;

public class BDGateway {
    private final String bdServiceURL = "http://localhost:8082/";
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public BDGateway() {
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public Optional<UserDTO> saveUser(UserDTO userDTO) {
        try {
            String userDTOJson = mapper.writeValueAsString(userDTO);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(bdServiceURL).resolve("users/save"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(userDTOJson))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                UserDTO savedUser = mapper.readValue(response.body(), UserDTO.class);
                return Optional.of(savedUser);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<List<UserDTO>> getAllUsers() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(bdServiceURL).resolve("users/list"))
                .header("Accept", "application/json")
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<UserDTO> users = mapper.readValue(response.body(), 
                    mapper.getTypeFactory().constructCollectionType(List.class, UserDTO.class));
                return Optional.of(users);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean deleteUser(String email) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(bdServiceURL).resolve("users/delete/" + email))
                .DELETE()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ExpedDTO> listarExpedientes(String email) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(bdServiceURL).resolve("exped/list/" + email))
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.body(), new TypeReference<List<ExpedDTO>>() {}
                );
            }
            else{
                throw new RuntimeException("Error HTTP: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static Exped convertirExpedDTO(ExpedDTO dto) {

        TipoExp tipo = TipoExp.valueOf(dto.getTipo().toUpperCase());

        Date fecha;
        try {
            fecha = new SimpleDateFormat("dd/MM/yyyy").parse(dto.getFecha());
        } catch (ParseException e) {
            throw new RuntimeException("Formato de fecha incorrecto", e);
        }

        List<ArchImagen> imagenes = new ArrayList<>();
        for (String ruta : dto.getImagenes()) {
            imagenes.add(new ArchImagen(ruta));
        }

        return new Exped(
            dto.getNombre(),
            tipo,
            fecha,
            imagenes
        );
    }
}


