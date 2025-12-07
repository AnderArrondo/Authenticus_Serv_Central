package es.deusto.sd.authenticus_serv_central.external;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.sd.authenticus_serv_central.dto.ExpedDTO;

@Component
public class ServBDDAO implements IServBDDAO{
    private final String API_URL = "http://localhost:8081/db/";

    private final HttpClient httpClient;

    public ServBDDAO() {
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public void deleteUserAndCases(String email) throws IOException, InterruptedException {
        String encodedEmail = URLEncoder.encode(email, "UTF-8");


        HttpRequest deleteUserRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "users/"+ encodedEmail))
                .DELETE()
                .build();

        HttpResponse<String> deleteUserResponse =
                httpClient.send(deleteUserRequest, HttpResponse.BodyHandlers.ofString());

        if (deleteUserResponse.statusCode() >= 400) {
            throw new RuntimeException("Error al borrar usuario en BBDD ("
                                       + deleteUserResponse.statusCode() + ")");
        }
    }

    @Override
    public Optional<ExpedDTO> saveExped(ExpedDTO expedDTO ){

        String url = API_URL + "exped/save/";

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(expedDTO);
            HttpRequest requestExped = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse responseExped = httpClient.send(requestExped, HttpResponse.BodyHandlers.ofString());
        
            if(responseExped.statusCode() != 200){
                return Optional.of(expedDTO);
            }
            else{
                return Optional.empty();
            }
        }
        catch (Exception ex) {
            return Optional.empty();
        }
    }
}
