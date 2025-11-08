package es.deusto.sd.authenticus_serv_central.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.deusto.sd.authenticus_serv_central.dto.ExpedDTO;
import es.deusto.sd.authenticus_serv_central.entity.ArchImagen;
import es.deusto.sd.authenticus_serv_central.entity.Exped;
import es.deusto.sd.authenticus_serv_central.entity.TipoExp;
import es.deusto.sd.authenticus_serv_central.dto.ResultadoDTO;
import es.deusto.sd.authenticus_serv_central.dto.ArchivoResultadoDTO;
import es.deusto.sd.authenticus_serv_central.entity.User;

@Service
public class ExpServ {
    SimpleDateFormat dtFormatter = new SimpleDateFormat("dd/MM/yyyy");

    private final Map<Long, Exped> expedientes = new HashMap<>();

    public ExpServ() {
        // Imágenes de prueba
        ArchImagen img1 = new ArchImagen("C:/imagenes/escenario_crimen.jpg");
        ArchImagen img2 = new ArchImagen("C:/imagenes/prueba1.png");
        ArchImagen img3 = new ArchImagen("C:/imagenes/pistas.jpg");

        // Crear varios expedientes y rellenar el mapa local 'expedientes'
        Calendar calendar = Calendar.getInstance();
        Date date;

        calendar.set(2024, Calendar.JANUARY, 15);
        date = calendar.getTime();
        Exped exped1 = new Exped("Caso Styles", TipoExp.AMBAS, date, List.of(img1, img2));

        calendar.set(2020, Calendar.JUNE, 30);
        date = calendar.getTime();
        Exped exped2 = new Exped("Caso Benedicto", TipoExp.VERACIDAD, date, List.of(img1));

        calendar.set(2017, Calendar.DECEMBER, 2);
        date = calendar.getTime();
        Exped exped3 = new Exped("Caso Pamela", TipoExp.INTEGRIDAD, date, List.of(img2, img3));

        User user1 = new User("user1@gmail.com", "password123", "User One", "123456789");
        User user2 = new User("user2@gmail.com", "password456", "User Two", "987654321");

        StateManagement.usuarioExpediente.put(user1, new ArrayList<>(List.of(exped1, exped2)));
        StateManagement.usuarioExpediente.put(user2, new ArrayList<>(List.of(exped3)));

        StateManagement.tokenUsuario.put("token_user1", user1);
        StateManagement.tokenUsuario.put("token_user2", user2);
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

        return toDTO(exped);
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

<<<<<<< HEAD
<<<<<<< HEAD
    public List<ExpedDTO> consultaExped(Optional<Integer> numCasos, Optional<String> dateIni, Optional<String> dateFin) throws IllegalArgumentException{
=======
    public List<ExpedDTO> consultaExped(Optional<Integer> numCasos, Optional<String> dateIni, Optional<String> dateFin, String token) throws IllegalArgumentException, Exception{
>>>>>>> 5fe1131278b8963d750335008076e82fb37a8298
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
                        throw new RuntimeException("Formato inválido: " + fechaStr, e);
                    }
                }); 
                Optional<Date> fechaFin = dateFin.map(fechaStr -> {
                    try {
                        return dtFormatter.parse(fechaStr);
                    } catch (ParseException e) {
                        throw new RuntimeException("Formato inválido: " + fechaStr, e);
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
    
                throw new IllegalArgumentException("Fecha de inicio o Fecha de fin no introducidos");
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

            throw new Exception("No ha iniciado sesión");
        }
        
        return expedientesCons;
    }
=======
    private final Map<Long, List<Exped>> casosPorUsuario = new HashMap<>();

    public void setCasosDeUsuario(long userId, List<Exped> casos) {

        casosPorUsuario.put(userId, new ArrayList<>(casos));
    }

    private List<Exped> obtenerLista(long userId) {

        return casosPorUsuario.computeIfAbsent(userId, k -> new ArrayList<>());
    }

    public void addCaso(long userId, Exped e) {

        obtenerLista(userId).add(e);
    }

    public boolean eliminarCaso(long userId, long casoId) {

        List<Exped> lista = obtenerLista(userId);
        return lista.removeIf(e -> e.getId() == casoId);
    }

    public ResultadoDTO resultadosDeCaso(long userId, long casoId) {

        List<Exped> lista = obtenerLista(userId);
        Exped caso = lista.stream().filter(e -> e.getId() == casoId).findFirst()

            .orElseThrow(() -> new IllegalArgumentException("Caso no encontrado"));

        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.List<ArchivoResultadoDTO> archivos = new java.util.ArrayList<>();

        for (es.deusto.sd.authenticus_serv_central.entity.ArchImagen img : caso.getImagenes()) {

            double integridad = -1.0;

            double veracidad = -1.0;

            switch (caso.getTipo()) {

                case INTEGRIDAD:
                    integridad = generarPuntuacion(img);
                    break;

                case VERACIDAD:
                    veracidad = generarPuntuacion(img);
                    break;

                case AMBAS:
                    integridad = generarPuntuacion(img);
                    veracidad = generarPuntuacion(img);
                    break;
            }

            archivos.add(new ArchivoResultadoDTO(img.getFilePath(), integridad, veracidad));
        }

        return new ResultadoDTO(
            caso.getId(),
            caso.getNombre(),
            caso.getTipo().toString(),
            df.format(caso.getFecha()),
            archivos
        );
    }

    private double generarPuntuacion(es.deusto.sd.authenticus_serv_central.entity.ArchImagen img) {

        int h = (img.getNombre() + "." + img.getExtension()).hashCode();
        double v = (h & 0x7fffffff) / (double) Integer.MAX_VALUE; 
        return Math.round(v * 1000.0) / 1000.0; 
        // 3 decimales
>>>>>>> actualizacionfuncionesalvaro
    }

    public void ainadirArchivosAdicionales(String nombreCaso, String token, List<String> archivos)throws Exception{

        if(StateManagement.isActiveToken(token)){
            User user = StateManagement.tokenUsuario.get(token);

            for(Exped e: StateManagement.usuarioExpediente.get(user)){

                if(e.getNombre().equals(nombreCaso)){

                    e.add(toArchImagenList(archivos));
                }
            }
        }
        else{

            throw new Exception("No ha iniciado sesión");
        }
    }
}
