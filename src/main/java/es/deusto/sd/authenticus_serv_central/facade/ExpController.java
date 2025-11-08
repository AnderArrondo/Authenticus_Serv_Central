package es.deusto.sd.authenticus_serv_central.facade;

import es.deusto.sd.authenticus_serv_central.dto.ExpedDTO;
<<<<<<< HEAD
<<<<<<< HEAD
import es.deusto.sd.authenticus_serv_central.entity.Exped;
=======
import es.deusto.sd.authenticus_serv_central.dto.ResultadoDTO;
>>>>>>> actualizacionfuncionesalvaro
=======
>>>>>>> 5fe1131278b8963d750335008076e82fb37a8298
import es.deusto.sd.authenticus_serv_central.service.ExpServ;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

<<<<<<< HEAD
import java.text.ParseException;
<<<<<<< HEAD
=======
>>>>>>> 5fe1131278b8963d750335008076e82fb37a8298
import java.util.List;
import java.util.Optional;
=======
import java.util.HashMap;
import java.util.Map;
>>>>>>> actualizacionfuncionesalvaro

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
<<<<<<< HEAD
=======
import org.springframework.web.bind.annotation.DeleteMapping;
>>>>>>> actualizacionfuncionesalvaro
=======
import org.springframework.web.bind.annotation.PutMapping;
>>>>>>> 5fe1131278b8963d750335008076e82fb37a8298



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

<<<<<<< HEAD
    @Operation(
        summary = "Consultar casos de investigacion",
        description = "Consulta los ultimos n casos de investigacion o visualiza todos los casos entre una fecha de inicio y una fecha de fin.",
        parameters = {
            @Parameter(name = "numCasos", description = "numero de casos que se quieren consultar", required = false),
            @Parameter(name = "fechaIni", description = "fecha inicio por la que se quiere filtrar casos", required = false),
            @Parameter(name = "fechaFin", description = "fecha fin por la que se quiere filtrar casos", required = false)
        }
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
    
    @Operation(
        summary = "Añadir archivos a un caso",
        description = "Añade archivos adicionales al caso de uso que que quieras.",
        parameters = {
            @Parameter(name = "nombre", description = "nombre del caso", required = true),
            @Parameter(name = "token", description = "token del usuario", required = true)
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "lista de archivos a añadir", required = true
        )
    )
    @ApiResponse(responseCode = "201", description = "Expediente encontrado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos para crear el expediente")
    @PutMapping("/ainadir")
    public ResponseEntity<?> ainadirArchivosExpediente(
        @RequestParam(required = true) String nombre, 
        @RequestParam(required = true) String token,
        @RequestBody(required = true) List<String> archivos) {
        
        try{
            expedServ.ainadirArchivosAdicionales(nombre, token, archivos);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(Exception e){

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
    @GetMapping("resultados")
    public ResponseEntity<?> resultados(
            @RequestParam String token,
            @RequestParam String nombreCaso) {
    
        try {
            ResultadoDTO resultados = expedServ.resultadosDeCaso(token, nombreCaso);
            return new ResponseEntity<>(resultados, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}