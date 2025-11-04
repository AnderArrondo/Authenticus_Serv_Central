package es.deusto.sd.authenticus_serv_central.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import es.deusto.sd.authenticus_serv_central.dto.ExpedDTO;
import es.deusto.sd.authenticus_serv_central.entity.ArchImagen;
import es.deusto.sd.authenticus_serv_central.entity.Exped;
import es.deusto.sd.authenticus_serv_central.entity.TipoExp;
import es.deusto.sd.authenticus_serv_central.dto.ResultadoDTO;
import es.deusto.sd.authenticus_serv_central.dto.ArchivoResultadoDTO;

@Service
public class ExpServ {
    SimpleDateFormat dtFormatter = new SimpleDateFormat("dd/MM/yyyy");

    private final Map<Long, ArchImagen> imagenes = new HashMap<>();
    private final AtomicLong idGenImg = new AtomicLong(0);
    private final Map<Long, Exped> expedientes = new HashMap<>();
    private final AtomicLong idGenExp = new AtomicLong(0);

    public ExpServ() {
        ArchImagen img1, img2;
        img1 = new ArchImagen(idGenImg.incrementAndGet(),
            "escenario_crimen", ".jpg", "C:/imagenes");
        img2 = new ArchImagen(idGenImg.incrementAndGet(),
            "prueba1", ".png", "C:/imagenes");
        imagenes.put(idGenImg.incrementAndGet(), img1);
        imagenes.put(idGenImg.incrementAndGet(), img2);


        Date date;
        Calendar calendar;
        calendar = Calendar.getInstance();

        calendar.set(2024, Calendar.JANUARY, 15);
        date = calendar.getTime();
        Exped exped = new Exped(idGenExp.incrementAndGet(),
            "Caso Styles", TipoExp.AMBAS, date, List.of(img1, img2));
        expedientes.put(exped.getId(), exped);

        calendar.set(2020, Calendar.JUNE, 30);
        date = calendar.getTime();
        exped = new Exped(idGenExp.incrementAndGet(),
            "Caso Benedicto", TipoExp.VERACIDAD, date, List.of(img1)
        );
        expedientes.put(exped.getId(), exped);

        calendar.set(2017, Calendar.DECEMBER, 2);
        date = calendar.getTime();
        exped = new Exped(idGenExp.incrementAndGet(),
            "Caso Pamela", TipoExp.INTEGRIDAD, date, List.of(img2)
        );
        expedientes.put(exped.getId(), exped);
    }

    public ExpedDTO crearExpediente(ExpedDTO exped) throws IllegalArgumentException, ParseException {
        long newId = idGenExp.incrementAndGet();
        TipoExp tipoExp;
        Date fecha = dtFormatter.parse(exped.getFecha());
        switch (exped.getTipo().toString().toUpperCase()) {
            case "VERACIDAD":
                tipoExp = TipoExp.VERACIDAD;
                break;
            case "INTEGRIDAD":
                tipoExp = TipoExp.INTEGRIDAD;
                break;
            case "AMBAS":   
                tipoExp = TipoExp.AMBAS;
                break;
        
            default:
                throw new IllegalArgumentException("Tipo de expediente no válido");
        }

        List<ArchImagen> imagenes = new ArrayList<>();
        for (String img : exped.getImagenes().split(",")) {
            imagenes.add(new ArchImagen(idGenImg.incrementAndGet(), img.trim()));
        }

        Exped newExped = new Exped(newId,
            exped.getNombre(),
            tipoExp,
            fecha,
            imagenes
        );
        expedientes.put(newId, newExped);

        System.out.println(expedientes.toString());

        return toDTO(newExped);
    }

    private ExpedDTO toDTO(Exped exped) {
        return new ExpedDTO(
            exped.getNombre(),
            exped.getTipo().toString(),
            exped.getFecha().toString(),
            exped.getImagenes().toString()
        );
    }

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
    }
}
