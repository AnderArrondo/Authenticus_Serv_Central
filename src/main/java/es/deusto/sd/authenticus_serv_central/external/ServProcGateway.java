package es.deusto.sd.authenticus_serv_central.external;

import java.net.http.HttpClient;

import org.springframework.stereotype.Component;

@Component
public class ServProcGateway implements IServProcGateway{
    private final String API_URL = "http://localhost:8081/";

    //private final HttpClient httpClient;

}
