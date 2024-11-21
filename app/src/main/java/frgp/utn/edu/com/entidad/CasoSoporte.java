package frgp.utn.edu.com.entidad;

public class CasoSoporte {
    private int idCaso;
    private int idUsuario;
    private String resumen;
    private String detalle;
    private String estado;
    private long timestamp;

    public CasoSoporte(int idCaso, int idUsuario, String resumen, String detalle, String estado, long timestamp) {
        this.idCaso = idCaso;
        this.idUsuario = idUsuario;
        this.resumen = resumen;
        this.detalle = detalle;
        this.estado = estado;
        this.timestamp = timestamp;
    }

    public int getIdCaso() {

        return idCaso;
    }

    public int getIdUsuario() {

        return idUsuario;
    }

    public String getResumen() {

        return resumen;
    }

    public String getDetalle() {

        return detalle;
    }

    public String getEstado() {

        return estado;
    }

    public long getTimestamp() {

        return timestamp;
    }
}
