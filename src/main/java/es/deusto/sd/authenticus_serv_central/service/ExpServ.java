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
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import es.deusto.sd.authenticus_serv_central.dto.ExpedDTO;
import es.deusto.sd.authenticus_serv_central.entity.ArchImagen;
import es.deusto.sd.authenticus_serv_central.entity.Exped;
import es.deusto.sd.authenticus_serv_central.entity.TipoExp;

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

    public List<ExpedDTO> consultaExped(Optional<Integer> numCasos, Optional<String> dateIni, Optional<String> dateFin) throws IllegalArgumentException{
        List<ExpedDTO> expedientesCons;

        if(numCasos.isPresent()){
            
            Integer nCasos = numCasos.get();
            expedientesCons = expedientes.values().stream()
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

            for(Exped expediente: expedientes.values()){

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
            expedientesCons = expedientes.values().stream()
            .sorted(Comparator.comparing(Exped::getFecha).reversed())
            .limit(nCasos)
            .map(this::toDTO)  
            .toList();
        }
        return expedientesCons;
    }
}
