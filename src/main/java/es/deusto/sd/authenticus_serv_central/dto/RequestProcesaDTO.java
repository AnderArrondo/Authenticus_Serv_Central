package es.deusto.sd.authenticus_serv_central.dto;

import java.io.Serializable;

public class RequestProcesaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArchImagenDTO archImagenDTO;
    private String TipoExp;

    public RequestProcesaDTO(ArchImagenDTO archImagenDTO, String tipoExp) {
        this.archImagenDTO = archImagenDTO;
        this.TipoExp = tipoExp;
    }

    public ArchImagenDTO getArchImagenDTO() {
        return archImagenDTO;
    }

    public void setArchImagenDTO(ArchImagenDTO archImagenDTO) {
        this.archImagenDTO = archImagenDTO;
    }

    public String getTipoExp() {
        return TipoExp;
    }

    public void setTipoExp(String tipoExp) {
        TipoExp = tipoExp;
    }
}