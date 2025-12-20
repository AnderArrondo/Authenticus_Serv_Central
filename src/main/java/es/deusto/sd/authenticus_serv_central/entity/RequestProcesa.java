package es.deusto.sd.authenticus_serv_central.entity;

import es.deusto.sd.authenticus_serv_central.dto.ArchImagenDTO;

public class RequestProcesa {
    private ArchImagen archImagen;
    private TipoExp tipoExp;

    public RequestProcesa(ArchImagen archImagen, TipoExp tipoExp) {
        this.archImagen = archImagen;
        this.tipoExp = tipoExp;
    }

    public RequestProcesa(ArchImagenDTO archImagen, String tipoExp) {
        this.archImagen = new ArchImagen(archImagen.getNombre(), archImagen.getPath());
        this.tipoExp = TipoExp.valueOf(tipoExp);
    }

    public ArchImagen getArchImagen() {
        return archImagen;
    }

    public void setArchImagen(ArchImagen archImagen) {
        this.archImagen = archImagen;
    }

    public TipoExp getTipoExp() {
        return this.tipoExp;
    }

    public void setTipoExp(TipoExp tipoExp) {
        this.tipoExp = tipoExp;
    }
}