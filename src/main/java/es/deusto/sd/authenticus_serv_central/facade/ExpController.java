package es.deusto.sd.authenticus_serv_central.facade;

import es.deusto.sd.authenticus_serv_central.dto.ExpedDTO;
import es.deusto.sd.authenticus_serv_central.dto.ResultadoDTO;
import es.deusto.sd.authenticus_serv_central.service.ExpServ;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/exped")
@Tag(name="Expedientes de casos", description="Funcionalidades relativas a los expedientes de casos.")
public class ExpController {
    private final ExpServ expedServ;

    public ExpController(ExpServ expedServ) {
        this.expedServ = expedServ;
    }

    @Operation(
        summary="Crear un nuevo expediente.",
        description="Crea un nuevo expediente en el sistema con los datos proporcionados.",
        parameters = {
            @Parameter(name="Token", description = "Token de sesión del usuario.", required=true)
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos necesarios para crear un expediente",
            required = true
        )
    )
    @ApiResponse(responseCode = "201", description = "Expediente creado correctamente.",
     content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ExpedDTO.class,
            example = "{\n" +
                "  \"id\": 1,\n" +
                "  \"nombre\": \"Caso de investigación A\",\n" +
                "  \"descripcion\": \"Descripción del expediente\",\n" +
                "  \"fechaCreacion\": \"09/11/2025\",\n" +
                "  \"archivos\": [\"doc1.pdf\", \"imagen1.png\"]\n" +
                "}"
        )
            ))
    @ApiResponse(responseCode = "302",
    description = "Expediente(s) encontrado(s) correctamente.",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(
                implementation = ExpedDTO.class,
                example = "[{\n" +
                    "  \"id\": 1,\n" +
                    "  \"nombre\": \"Caso A\",\n" +
                    "  \"descripcion\": \"Descripción del caso A\",\n" +
                    "  \"fechaCreacion\": \"01/10/2025\"\n" +
                    "}, {\n" +
                    "  \"id\": 2,\n" +
                    "  \"nombre\": \"Caso B\",\n" +
                    "  \"descripcion\": \"Descripción del caso B\",\n" +
                    "  \"fechaCreacion\": \"03/11/2025\"\n" +
                    "}]"
            ))
        )
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
        summary = "Consultar casos de investigación.",
        description = "Si no se proporciona ninguno de los primeros parametros de personalización muestra los últimos 5 casos del usuario." +
            "Es posible modificar este número cambiando el parametro de <i>numCasos</i>." +
            "También es posible obtener los casos de investigación entre dos fechas dadas en orden cronológico.",
        parameters = {
            @Parameter(name="token", description = "Token de sesión del usuario."),
            @Parameter(name = "numCasos", description = "Número de casos que se quieren consultar.", required = false),
            @Parameter(name = "fechaIni", description = "Fecha inicio por la que se quiere filtrar casos, en formato <i>dd/MM/yyyy</i>.", required = false),
            @Parameter(name = "fechaFin", description = "Fecha fin por la que se quiere filtrar casos, en formato <i>dd/MM/yyyy</i>.", required = false)
        }
    )
    @ApiResponse(responseCode = "302", description = "Expediente(s) encontrado(s) correctamente.")
    @ApiResponse(responseCode = "400", description = "Datos inválidos para consultar expedientes.")
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
        summary = "Añadir archivos a un caso.",
        description = "Añade archivos adicionales al caso de investigación correspondiente.",
        parameters = {
            @Parameter(name = "nombre", description = "Nombre del caso", required = true),
            @Parameter(name = "token", description = "Token del usuario", required = true)
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Lista de archivos a añadir", required = true
        )
    )
    @ApiResponse(responseCode = "200",
    description = "Expediente actualizado correctamente.",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(
                example = "{ \"mensaje\": \"Archivos añadidos correctamente al expediente.\" }"
            ))
        )
    @ApiResponse(responseCode = "400", description = "Datos inválidos para actualizar expediente.")
    @PutMapping("/ainadir/{token}")
    public ResponseEntity<?> ainadirArchivosExpediente(
        @RequestParam(required = true) String nombre, 
        @PathVariable(required = true) String token,
        @RequestBody(required = true) List<String> archivos) {
        
        try{
            expedServ.ainadirArchivosAdicionales(nombre, token, archivos);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(Exception e){

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    

    @Operation(
        summary = "Eliminar un caso de investigación.",
        description = "Elimina un caso de investigación mediante el nombre del caso correspondiente.",
        parameters = {
            @Parameter(name = "token", description = "Token de sesión del usuario."),
            @Parameter(name = "nombreCaso", description = "Nombre identificativo del caso que se desea borrar.")
        }
    )
    @ApiResponse(responseCode = "200",
    description = "Caso eliminado correctamente.",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(
                example = "{ \"mensaje\": \"Caso eliminado correctamente.\" }"
            ))
        )
    @ApiResponse(responseCode = "404", description = "Caso no encontrado. No se han hecho modificaciones.")
    @ApiResponse(responseCode = "400", description = "Datos inválidos para eliminar el caso.")
    @DeleteMapping("eliminar/{token}")
    public ResponseEntity<?> eliminarCaso(
            @PathVariable String token,
            @RequestParam String nombreCaso) {

                try {
                    boolean result = expedServ.eliminarCaso(token, nombreCaso);
                    if(result) {
                        return new ResponseEntity<>("Caso eliminado correctamente.", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Caso no encontrado", HttpStatus.NOT_FOUND);
                    }
                } catch (Exception e) {
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
                }
    }

    @Operation(
        summary = "Mostrar resultados de un caso de investigación.",
        description = "Procesa los resultados de un caso dado (a partir del nombre de dicho caso)" +
        "teniendo en cuenta el tipo de procesamiento seleccionado." +
        "El resultado es un número entre [0, 1], si el tipo de procesamiento ha sido seleccionado." +
        "En caso contrario, el resultado es -1.",
        parameters = {
            @Parameter(name="token", description = "Token de sesión del usuario.", required = true),
            @Parameter(name="nombreCaso", description = "Nombre identificativo del caso del que se desea obtener resultados.", required = true)
        })
    @ApiResponse(responseCode = "200",
    description = "Resultados del caso procesados correctamente.",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(
                implementation = ResultadoDTO.class,
                example = "{\n" +
                    "  \"nombreCaso\": \"Caso A\",\n" +
                    "  \"resultado\": 0.87,\n" +
                    "  \"tipoProcesamiento\": \"Análisis de texto\"\n" +
                    "}"
            ))
        )
    @ApiResponse(responseCode = "400", description = "Datos inválidos para obtener resultados del caso de investigación.")
    @GetMapping("resultados/{token}")
    public ResponseEntity<?> resultados(
            @PathVariable String token,
            @RequestParam String nombreCaso) {
    
        try {
            ResultadoDTO resultados = expedServ.resultadosDeCaso(token, nombreCaso);
            return new ResponseEntity<>(resultados, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}