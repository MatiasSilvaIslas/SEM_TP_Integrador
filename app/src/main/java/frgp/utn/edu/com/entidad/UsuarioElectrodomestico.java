package frgp.utn.edu.com.entidad;

public class UsuarioElectrodomestico {

    private int id;
    private int usuarioId;
    private int electrodomesticoId;
    private int cantidad;
    private int horas;
    private int dias;

    public UsuarioElectrodomestico(int usuarioId, int electrodomesticoId, int cantidad, int horas, int dias) {
        this.usuarioId = usuarioId;
        this.electrodomesticoId = electrodomesticoId;
        this.cantidad = cantidad;
        this.horas = horas;
        this.dias = dias;
    }

    public UsuarioElectrodomestico(int id, int usuarioId, int electrodomesticoId, int cantidad, int horas, int dias) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.electrodomesticoId = electrodomesticoId;
        this.cantidad = cantidad;
        this.horas = horas;
        this.dias = dias;
    }

    public UsuarioElectrodomestico() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setElectrodomesticoId(int electrodomesticoId) {
        this.electrodomesticoId = electrodomesticoId;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public int getElectrodomesticoId() {
        return electrodomesticoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getHoras() {
        return horas;
    }

    public int getDias() {
        return dias;
    }
}
