package es.deusto.sd.authenticus_serv_central.facade;

import es.deusto.sd.authenticus_serv_central.dto.ExpedDTO;
import es.deusto.sd.authenticus_serv_central.service.ExpServ;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


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
    @ApiResponse(responseCode = "201", description = "Expediente creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos para crear el expediente")
    @PostMapping("/crea/{token}")
    public ResponseEntity<?> crearExpediente(
        @Parameter(description = "Objeto Expediente a crear", required = true)
        @RequestBody ExpedDTO expedDTO,
        @PathVariable String token) {
        try {
            ExpedDTO newExped = expedServ.crearExpediente(expedDTO, token);
            return new ResponseEntity<ExpedDTO>(newExped, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
        summary = "Consultar casos de investigacion",
        description = "Consulta los ultimos n casos de investigacion o visualiza todos los casos entre una fecha de inicio y una fecha de fin."
    )
    @ApiResponse(responseCode = "201", description = "Expediente encontrado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos para crear el expediente")
    @GetMapping("/consultar/{token}")
    public ResponseEntity<?> consultarExpediente(
        @RequestParam(required = false) Optional<Integer> numCasos,
        @RequestParam(required = false) Optional<String> fechaIni,
        @RequestParam(required = false) Optional<String> fechaFin,
        @PathVariable String token) {

        try{
            List<ExpedDTO> listaExp = expedServ.consultaExped(numCasos, fechaIni, fechaFin, token);
            return new ResponseEntity<>(listaExp, HttpStatus.FOUND);
        }

        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }   
        
    }
    
}
    
