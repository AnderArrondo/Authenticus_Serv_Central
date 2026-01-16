package es.deusto.sd.authenticus_serv_central.external;

import es.deusto.sd.authenticus_serv_central.dto.ArchImagenDTO;
import es.deusto.sd.authenticus_serv_central.entity.ArchImagen;
import es.deusto.sd.authenticus_serv_central.entity.TipoExp;

public interface ISocketProcesaClient {

    ArchImagenDTO enviarRequestProcesa(ArchImagen img, TipoExp tipoExp);

    void closeConnection();
}
