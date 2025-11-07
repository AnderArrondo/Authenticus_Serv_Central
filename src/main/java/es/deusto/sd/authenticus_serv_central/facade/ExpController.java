package es.deusto.sd.authenticus_serv_central.facade;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.authenticus_serv_central.dto.ExpedDTO;
<<<<<<< HEAD
import es.deusto.sd.authenticus_serv_central.entity.Exped;
=======
import es.deusto.sd.authenticus_serv_central.dto.ResultadoDTO;
>>>>>>> actualizacionfuncionesalvaro
import es.deusto.sd.authenticus_serv_central.service.ExpServ;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.text.ParseException;
<<<<<<< HEAD
import java.util.List;
import java.util.Optional;
=======
import java.util.HashMap;
import java.util.Map;
>>>>>>> actualizacionfuncionesalvaro

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
=======
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
>>>>>>> actualizacionfuncionesalvaro



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
    @PostMapping("/crea")
    public ResponseEntity<?> crearExpediente(
        @Parameter(description = "Objeto Expediente a crear", required = true)
        @RequestBody ExpedDTO expedDTO) {
        try {
            ExpedDTO newExped = expedServ.crearExpediente(expedDTO);
            return new ResponseEntity<ExpedDTO>(newExped, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ParseException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

<<<<<<< HEAD
    @Operation(
        summary = "Consultar casos de investigacion",
        description = "Consulta los ultimos n casos de investigacion o visualiza todos los casos entre una fecha de inicio y una fecha de fin."
    )
    @ApiResponse(responseCode = "201", description = "Expediente encontrado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos para crear el expediente")
    @GetMapping("/consultar")
    public ResponseEntity<?> consultarExpediente(
        @RequestParam(required = false) Optional<Integer> numCasos,
        @RequestParam(required = false) Optional<String> fechaIni,
        @RequestParam(required = false) Optional<String> fechaFin) {

        try{
            List<ExpedDTO> listaExp = expedServ.consultaExped(numCasos, fechaIni, fechaFin);
            return new ResponseEntity<>(listaExp, HttpStatus.FOUND);
        }

        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }   
        
    }
    
}
=======
>>>>>>> actualizacionfuncionesalvaro
    

    @Operation(summary = "Eliminar un caso de investigación")
    @ApiResponse(responseCode = "200", description = "Caso eliminado o no existente")
    @DeleteMapping("/users/{userId}/casos/{casoId}")
    public Map<String, Object> eliminarCaso(
            @PathVariable long userId,
            @PathVariable long casoId) {

        boolean ok = expedServ.eliminarCaso(userId, casoId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("eliminado", ok);
        return resp;
    }

    @Operation(summary = "Mostrar resultados de un caso de investigación")
    @ApiResponse(responseCode = "200", description = "Resultados del caso")
    @GetMapping("/users/{userId}/casos/{casoId}/resultados")
    public ResultadoDTO resultados(
            @PathVariable long userId,
            @PathVariable long casoId) {

        return expedServ.resultadosDeCaso(userId, casoId);
    }
}
