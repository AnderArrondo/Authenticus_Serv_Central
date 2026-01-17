package es.deusto.sd.authenticus_serv_central.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.deusto.sd.authenticus_serv_central.dto.ArchImagenDTO;
import es.deusto.sd.authenticus_serv_central.dto.ExpedDTO;
import es.deusto.sd.authenticus_serv_central.entity.ArchImagen;
import es.deusto.sd.authenticus_serv_central.entity.Exped;
import es.deusto.sd.authenticus_serv_central.entity.TipoExp;
import es.deusto.sd.authenticus_serv_central.dto.ResultadoDTO;
import es.deusto.sd.authenticus_serv_central.entity.User;
import es.deusto.sd.authenticus_serv_central.external.BDGateway;
import es.deusto.sd.authenticus_serv_central.external.IBDGateway;
import es.deusto.sd.authenticus_serv_central.external.SocketProcesaClient;

@Service
public class ExpServ {
    private SimpleDateFormat dtFormatter = new SimpleDateFormat("dd/MM/yyyy");
    private final IBDGateway bdGateway;
    /*/
    public ExpServ() {
    }
    /*/
        public ExpServ(BDGateway bdGateway) {
        this.bdGateway = bdGateway;
    }

    public ExpedDTO crearExpediente(ExpedDTO expedDTO, String token) throws IllegalArgumentException, ParseException {
        if(!StateManagement.isActiveToken(token)){
            throw new IllegalArgumentException("Token inválido o sesión no iniciada.");
        }
        User usuario = StateManagement.tokenUsuario.get(token);
        
        Date fecha = dtFormatter.parse(expedDTO.getFecha());
        TipoExp tipoExp = parseTipoExp(expedDTO.getTipo());

        List<ArchImagen> imagenes = toArchImagenList(expedDTO.getImagenes());

        Exped exped = new Exped(
            expedDTO.getNombre(),
            tipoExp,
            fecha,
            imagenes
        );

        if(existeExpediente(exped, usuario)) {
            throw new IllegalArgumentException("El expediente ya existe.");
        }

        StateManagement.usuarioExpediente.get(usuario).add(exped);
        Optional<ExpedDTO> result = bdGateway.saveExped(expedDTO, usuario.getEmail());

        if(result.isPresent()) {
            return result.get();
        }
        return null;
    }

    private boolean existeExpediente(Exped newExped, User usuario){
        for(Exped exped : StateManagement.usuarioExpediente.get(usuario)){
            if(exped.equals(newExped)){
                return true;
            }
        }
        return false;
    }

    private List<ArchImagen> toArchImagenList(List<String> imagenesStr){
        List<ArchImagen> imagenes = new ArrayList<>();
        for(String img : imagenesStr){
            ArchImagen newImg = new ArchImagen(img);
            imagenes.add(newImg);
        }
        return imagenes;
    }

    private TipoExp parseTipoExp(String tipo) throws IllegalArgumentException {
        switch (tipo.toUpperCase()) {
            case "VERACIDAD":
                return TipoExp.VERACIDAD;
            case "INTEGRIDAD":
                return TipoExp.INTEGRIDAD;
            case "AMBAS":
                return TipoExp.AMBAS;
            default:
                throw new IllegalArgumentException("Tipo de expediente no válido.");
        }
    }

    private ExpedDTO toDTO(Exped exped) {
        return new ExpedDTO(
            exped.getNombre(),
            exped.getTipo().toString(),
            exped.getFecha().toString(),
            exped.getImagenes().stream()
                .map(ArchImagen::toString)
                .toList()
        );
    }

    public List<ExpedDTO> consultaExped(Optional<Integer> numCasos, Optional<String> dateIni, Optional<String> dateFin, String token) throws IllegalArgumentException, Exception{
        List<ExpedDTO> expedientesCons;

        if(StateManagement.isActiveToken(token)){
            User user = StateManagement.tokenUsuario.get(token);

            if(numCasos.isPresent()){
            
                Integer nCasos = numCasos.get();
                expedientesCons = StateManagement.usuarioExpediente.get(user).stream()
                .sorted(Comparator.comparing(Exped::getFecha).reversed())
                .limit(nCasos)
                .map(this::toDTO)  
                .toList();
                
            }
            else if(dateIni.isPresent() && dateFin.isPresent()){
                expedientesCons = new ArrayList<>();
                Optional<Date> fechaIni = dateIni.map(fechaStr -> {
                    try {
                        return dtFormatter.parse(fechaStr);
                    } catch (ParseException e) {
                        throw new IllegalArgumentException("Formato de fecha inicial inválido: " + fechaStr, e);
                    }
                }); 
                Optional<Date> fechaFin = dateFin.map(fechaStr -> {
                    try {
                        return dtFormatter.parse(fechaStr);
                    } catch (ParseException e) {
                        throw new IllegalArgumentException("Formato de fecha final inválido: " + fechaStr, e);
                    }
                });
                Date fechaInicio = fechaIni.get();
                Date fechaFinal = fechaFin.get();
    
                for(Exped expediente: StateManagement.usuarioExpediente.get(user)){
    
                    if((expediente.getFecha().equals(fechaInicio) || expediente.getFecha().after(fechaInicio))
                        && (expediente.getFecha().equals(fechaFinal) || expediente.getFecha().before(fechaFinal))){
    
                            expedientesCons.add(toDTO(expediente));
                    }
                }
            }
    
            else if(dateIni.isPresent() || dateFin.isPresent()){
    
                throw new IllegalArgumentException("Fecha de inicio o fecha de fin no introducidos.");
            }
            else{
    
                Integer nCasos = 5;
                expedientesCons = StateManagement.usuarioExpediente.get(user).stream()
                .sorted(Comparator.comparing(Exped::getFecha).reversed())
                .limit(nCasos)
                .map(this::toDTO)  
                .toList();
            }
        }
        else{

            throw new IllegalArgumentException("Token inválido o sesión no iniciada.");
        }
        
        return expedientesCons;
    }

    public boolean eliminarCaso(String token, String nombreCaso) throws Exception {
        if(!StateManagement.isActiveToken(token)){
            throw new Exception("Token inválido o sesión no iniciada.");
        }
        User usuario = StateManagement.tokenUsuario.get(token);
        List<Exped> listaExpedientes = StateManagement.usuarioExpediente.get(usuario);
        bdGateway.deleteExped(nombreCaso, usuario.getEmail());
        return listaExpedientes.removeIf(exped -> exped.getNombre().toUpperCase().equals(nombreCaso.toUpperCase()));
    }
    

    public ResultadoDTO resultadosDeCaso(String token, String nombreCaso) {
        if(!StateManagement.isActiveToken(token)) {
            throw new IllegalArgumentException("Token inválido o sesión no iniciada.");
        }
        User usuario = StateManagement.tokenUsuario.get(token);
        List<Exped> listaExpedientes = StateManagement.usuarioExpediente.get(usuario);
        Exped caso = listaExpedientes.stream()
            .filter(e -> e.getNombre().toUpperCase().equals(nombreCaso.toUpperCase()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Caso no encontrado."));

        TipoExp tipoCaso = caso.getTipo();
        List<ArchImagen> listaImagenes = caso.getImagenes();
        List<ArchImagenDTO> imagenesResultado = new ArrayList<>();

        SocketProcesaClient clientSocket = new SocketProcesaClient(); // CORRECION: info sobre ip y puerto en el constructor por defecto

        for(ArchImagen img : listaImagenes) {
            ArchImagenDTO resultDTO = clientSocket.enviarRequestProcesa(img, tipoCaso);
            imagenesResultado.add(resultDTO);

            ArchImagen imgResult = new ArchImagen(resultDTO.getPath(), resultDTO.getNombre());
            imgResult.setpVeracidad(resultDTO.getpVeracidad());
            imgResult.setpIntegridad(resultDTO.getpIntegridad());
        }

        clientSocket.closeConnection();

        ResultadoDTO resultDTO = new ResultadoDTO(caso.getNombre(), caso.getTipo(), caso.getFecha(), imagenesResultado);
        bdGateway.updateArchImagenResultado(nombreCaso, resultDTO, usuario.getEmail());
        return resultDTO;
    }

    public void ainadirArchivosAdicionales(String nombreCaso, String token, List<String> archivos)throws Exception{

        if(StateManagement.isActiveToken(token)){
            User user = StateManagement.tokenUsuario.get(token);

            for(Exped e: StateManagement.usuarioExpediente.get(user)){
                if(e.getNombre().toUpperCase().equals(nombreCaso.toUpperCase())){

                    e.add(toArchImagenList(archivos));
                    bdGateway.ainadirArchivos(user.getEmail(), nombreCaso, archivos);
                }
            }
        }
        else{
            throw new Exception("No ha iniciado sesión");
        }
    }
}
