package es.deusto.sd.authenticus_serv_central.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import es.deusto.sd.authenticus_serv_central.external.BDGateway;
import es.deusto.sd.authenticus_serv_central.service.StateManagement;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BDGateway bdGateway;

    public DataInitializer(BDGateway bdGateway) {
        this.bdGateway = bdGateway;
    }

    @Override
    public void run(String... args) throws Exception {
        // Al arrancar, llenamos la memoria con lo que haya en la base de datos
        StateManagement.cargarDatosDeBD(bdGateway);
    }
}