package es.deusto.sd.authenticus_serv_central.external;

import java.util.Optional;

import es.deusto.sd.authenticus_serv_central.dto.ExpedDTO;

public interface IServBDDAO {
    void deleteUserAndCases(String email) throws Exception;
    public Optional<ExpedDTO> saveExped(ExpedDTO expedDTO );
    
}
