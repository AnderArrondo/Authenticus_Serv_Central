package es.deusto.sd.authenticus_serv_central.facade;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.authenticus_serv_central.dto.ExpedDTO;
import es.deusto.sd.authenticus_serv_central.service.ExpServ;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/exped")
@Tag(name="Expedientes de casos", description="Funcionalidades relativas a los expedientes de casos")
public class ExpController {

    private final ExpServ expedServ;

    public ExpController(ExpServ expedServ) {
        this.expedServ = expedServ;
    }

    @Operation(
        summary="Crear un nuevo expediente",
        description="Crea un nuevo expediente en el sistema con los datos proporcionados"
    )
    @PostMapping("/crea")
    public ResponseEntity<ExpedDTO> crearExpediente(
        @Parameter(description = "Objeto Expediente a crear", required = true)
        @RequestBody ExpedDTO expedDTO) {
        ExpedDTO newExped = expedServ.crearExpediente(expedDTO);

        return new ResponseEntity<ExpedDTO>(newExped, HttpStatus.CREATED);
    }
}
    
