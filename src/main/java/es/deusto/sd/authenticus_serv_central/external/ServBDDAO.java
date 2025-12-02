package es.deusto.sd.authenticus_serv_central.external;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Component;

@Component
public class ServBDDAO implements IServBDDAO{
    private final String API_URL = "http://localhost:8081/";

    private final HttpClient httpClient;

    public ServBDDAO() {
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public void deleteUserAndCases(String email) throws IOException, InterruptedException {
        String encodedEmail = URLEncoder.encode(email, "UTF-8");


        HttpRequest deleteUserRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/db/users/" + encodedEmail))
                .DELETE()
                .build();

        HttpResponse<String> deleteUserResponse =
                httpClient.send(deleteUserRequest, HttpResponse.BodyHandlers.ofString());

        if (deleteUserResponse.statusCode() >= 400) {
            throw new RuntimeException("Error al borrar usuario en BBDD ("
                                       + deleteUserResponse.statusCode() + ")");
        }
    }


}
