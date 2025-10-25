package es.deusto.sd.authenticus_serv_central.service;

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

@Service
public class ExpServ {
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

    public ExpedDTO crearExpediente(ExpedDTO exped) {
        long newId = idGenExp.incrementAndGet();
        Exped newExped = new Exped(newId,
            exped.getNombre(),
            exped.getTipo(),
            exped.getFecha(),
            exped.getImagenes()
        );
        expedientes.put(newId, newExped);

        System.out.println(expedientes.size());

        return toDTO(newExped);
    }

    private ExpedDTO toDTO(Exped exped) {
        return new ExpedDTO(
            exped.getId(),
            exped.getNombre(),
            exped.getTipo(),
            exped.getFecha(),
            exped.getImagenes()
        );
    }
}
