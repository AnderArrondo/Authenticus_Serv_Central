package es.deusto.sd.authenticus_serv_central.external;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.sd.authenticus_serv_central.dto.ExpedDTO;
import es.deusto.sd.authenticus_serv_central.dto.ResultadoDTO;
import es.deusto.sd.authenticus_serv_central.dto.UserDTO;

@Service
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

    public Optional<ExpedDTO> saveExped(ExpedDTO expedDTO, String userEmail) {
        try {
            String expedDTOJson = mapper.writeValueAsString(expedDTO);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(bdServiceURL).resolve("exped/create/" + userEmail))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(expedDTOJson))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ExpedDTO savedExped = mapper.readValue(response.body(), ExpedDTO.class);
                return Optional.of(savedExped);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean deleteExped(String nombreCaso, String userEmail) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(bdServiceURL).resolve("exped/delete/" + userEmail + 
                    "?nombreCaso=" + java.net.URLEncoder.encode(nombreCaso, "UTF-8")))
                .DELETE()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateArchImagenResultado(String nombreCaso, ResultadoDTO resultadoDTO, String userEmail) {
        try {
            String archImagenJson = mapper.writeValueAsString(resultadoDTO);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(bdServiceURL).resolve("exped/update-image-result/" + userEmail + 
                    "?nombreCaso=" + java.net.URLEncoder.encode(nombreCaso, "UTF-8")))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(archImagenJson))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
        
    
    public Optional<List<ExpedDTO>> getExpedientesByEmail(String email) {
        try {
            String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(bdServiceURL).resolve("exped/list/" + encodedEmail))
                .header("Accept", "application/json")
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<ExpedDTO> expedientes = mapper.readValue(response.body(), 
                    mapper.getTypeFactory().constructCollectionType(List.class, ExpedDTO.class));
                return Optional.of(expedientes);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean ainadirArchivos(String email, String nombreCaso, List<String> nuevasImgs) {
        try {
            String requestBody = mapper.writeValueAsString(nuevasImgs);

            String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
            String encodedNombreCaso = URLEncoder.encode(nombreCaso, StandardCharsets.UTF_8);

            String urlFinal = bdServiceURL + "exped/add-files/" + encodedEmail + "?nombreCaso=" + encodedNombreCaso;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlFinal))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Archivos añadidos correctamente en BD.");
                return true;
            } else {
                System.err.println("Error al añadir archivos. Status: " + response.statusCode() + " - " + response.body());
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}


