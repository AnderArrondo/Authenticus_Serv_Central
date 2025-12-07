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
        description="Crea un nuevo expediente en el sistema con los datos proporcionados. La sesión debe estar activa.",
        parameters = {
            @Parameter(name="token", description = "Token de sesión del usuario. Debe estar activo.", required=true)
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos necesarios para crear un expediente en format de ExpedDTO.",
            required = true
        )
    )
    @ApiResponse(responseCode = "201", description = "Expediente creado correctamente. Devuelve el expediente en formato ExpedDTO.",
     content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ExpedDTO.class)))
    @ApiResponse(responseCode = "400",
    description = "Datos para crear un nuevo expediente no válidos. Devuelve un texto descriptivo del problema encontrado.",
    content = @Content(
        mediaType = "text/plain",
        schema = @Schema(
                type = "String",
                example = "Token inválido o sesión no iniciada."
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
        description = "Si no se proporciona ninguno de los primeros parametros de personalización muestra los últimos 5 casos del usuario. " +
            "Es posible modificar este número cambiando el parametro de <i>numCasos</i>." +
            "También es posible obtener los casos de investigación entre dos fechas dadas en orden cronológico proporcionando <i>fechaIni</i> y <i>fechaFin</i>. " +
            "En todo caso la sesión debe estar activa.",
        parameters = {
            @Parameter(name="token", description = "Token de sesión del usuario. Debe estar activo."),
            @Parameter(name = "numCasos", description = "Número de casos que se quieren consultar, por defecto 5.", required = false),
            @Parameter(name = "fechaIni", description = "Fecha inicio por la que se quiere filtrar casos, en formato <i>dd/MM/yyyy</i>. Si se proporciona, <i>fechaFin</i> es también requerido.", required = false),
            @Parameter(name = "fechaFin", description = "Fecha fin por la que se quiere filtrar casos, en formato <i>dd/MM/yyyy</i>. Si se proporciona, <i>fechaIni</i> es también requerido.", required = false)
        }
    )
    @ApiResponse(responseCode = "200", description = "Expediente(s) encontrado(s) correctamente. Devuelve una lista de ExpedDTO",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
            example = "[\r\n" + //
                                "  {\r\n" + //
                                "    \"nombre\": \"Caso Styles\",\r\n" + //
                                "    \"tipo\": \"AMBAS\",\r\n" + //
                                "    \"fecha\": \"Mon Jan 15 22:11:22 CET 2024\",\r\n" + //
                                "    \"imagenes\": [\r\n" + //
                                "      \"C:/imagenes/escenario_crimen.jpg\",\r\n" + //
                                "      \"C:/imagenes/prueba1.png\"\r\n" + //
                                "    ]\r\n" + //
                                "  },\r\n" + //
                                "  {\r\n" + //
                                "    \"nombre\": \"Caso Benedicto\",\r\n" + //
                                "    \"tipo\": \"VERACIDAD\",\r\n" + //
                                "    \"fecha\": \"Tue Jun 30 22:11:22 CEST 2020\",\r\n" + //
                                "    \"imagenes\": [\r\n" + //
                                "      \"C:/imagenes/escenario_crimen.jpg\"\r\n" + //
                                "    ]\r\n" + //
                                "  }\r\n" + //
                                "]")))
    @ApiResponse(responseCode = "400", description = "Datos inválidos para consultar expedientes. Devuelve un texto descriptivo del problema encontrado.",
        content = @Content(
        mediaType = "text/plain",
        schema = @Schema(
                type = "String",
                example = "Token inválido o sesión no iniciada."
            )))
    @GetMapping("/consultar/{token}")
    public ResponseEntity<?> consultarExpediente(
        @RequestParam(required = false) Optional<Integer> numCasos,
        @RequestParam(required = false) Optional<String> fechaIni,
        @RequestParam(required = false) Optional<String> fechaFin,
        @PathVariable String token) {

        try{
            List<ExpedDTO> listaExp = expedServ.consultaExped(numCasos, fechaIni, fechaFin, token);
            return new ResponseEntity<>(listaExp, HttpStatus.OK);
        }

        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }   
        
    }
    
    @Operation(
        summary = "Añadir archivos a un caso existente.",
        description = "Añade archivos adicionales al caso de investigación correspondiente. El caso debe existir previamente. La sesión ha de estar activa.",
        parameters = {
            @Parameter(name = "nombre", description = "Nombre identificativo del caso (mayúsculas y minúsculas indistintamente).", required = true),
            @Parameter(name = "token", description = "Token del usuario. Debe estar activo.", required = true)
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Lista de archivos a añadir. Los archivos esperados son rutas absolutas (<i>Unidad:/ruta</i>), por lo tanto deben contener al menos un caracter '/'.", required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "[\r\n" + //
                                                "  \"C:/caso_styles/pruebas/imagenes/prueba01.jpg\",\r\n" + //
                                                "  \"C:/caso_styles/pruebas/imagenes/prueba02.jpg\",\r\n" + //
                                                "  \"C:/caso_styles/pruebas/imagenes/prueba03.jpg\"\r\n" + //
                                                "]"
                )
            )
        )
    )
    @ApiResponse(responseCode = "200",
    description = "Expediente actualizado correctamente. Devuelve un mensaje de confirmación.",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(
                type = "String",
                example = "Archivos añadidos correctamente al expediente."
            ))
        )
    @ApiResponse(responseCode = "400",
    description = "Datos inválidos para actualizar expediente. Devuelve un texto descriptivo del problema encontrado.",
    content = @Content(
        mediaType = "text/plain",
        schema = @Schema(
            type = "String",
            example = "No ha iniciado sesión."
        )
    ))
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
        summary = "Eliminar un caso de investigación existente.",
        description = "Elimina un caso de investigación mediante el nombre del caso correspondiente. El caso debe de existir previamente y la sesión ha de estar activa.",
        parameters = {
            @Parameter(name = "token", description = "Token de sesión del usuario. Debe estar activa."),
            @Parameter(name = "nombreCaso", description = "Nombre identificativo del caso que se desea borrar (mayúsculas y minúsculas indistintamente).")
        }
    )
    @ApiResponse(responseCode = "200",
    description = "Caso eliminado correctamente. Devuelve un mensaje de confirmación.",
    content = @Content(
        mediaType = "text/plain",
        schema = @Schema(
                example = "Caso eliminado correctamente."
            ))
        )
    @ApiResponse(responseCode = "404", description = "Caso no encontrado. No se hacen modificaciones. Devuelve un mensaje de la situación.",
    content = @Content(
        mediaType = "text/plain",
        schema = @Schema(
            type = "String",
            example = "Caso no encontrado"
        )
    ))
    @ApiResponse(responseCode = "400",
    description = "Datos inválidos para eliminar el caso. Devuelve un mensaje descriptivo del error encontrado.",
    content = @Content(
        mediaType = "text/plain",
        schema = @Schema(
            type = "String",
            example = "Token inválido o sesión no iniciada."
        )
    ))
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
        summary = "Mostrar resultados de procesamiento de un caso de investigación. ",
        description = "Procesa los resultados de un caso dado (a partir del nombre)" +
        "teniendo en cuenta el tipo de procesamiento seleccionado. " +
        "A cada imagen del caso le corresponde un número entre [0, 1], si el tipo de procesamiento ha sido seleccionado. " +
        "En caso contrario, el resultado es -1. La sesión debe estar activa y el caso debe de existir previamente.",
        parameters = {
            @Parameter(name="token", description = "Token de sesión del usuario. Debe estar activa.", required = true),
            @Parameter(name="nombreCaso", description = "Nombre identificativo del caso del que se desea obtener resultados (mayúsculas y minúsculas indistintamente).", required = true)
        })
    @ApiResponse(responseCode = "200",
    description = "Resultados del caso procesados correctamente. Devuelve un ResultadoDTO del expediente seleccionado.",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(
                implementation = ResultadoDTO.class
            ))
        )
    @ApiResponse(responseCode = "400",
        description = "Datos inválidos para obtener resultados del caso de investigación. Devuelve un texto descriptivo del problema encontrado.",
        content = @Content(
            mediaType = "text/plain",
            schema = @Schema(
                type = "String",
                example = "Caso no encontrado."
            )
        ))
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

    @PostMapping("/save")
    public ResponseEntity<?> saveExped(@RequestBody ExpedDTO expedDTO) {
        
        try{
            ExpedDTO expediente = expedServ.saveExped(expedDTO);
            return new ResponseEntity<>(expediente, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
}