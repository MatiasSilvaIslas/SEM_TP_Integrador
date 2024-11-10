package frgp.utn.edu.com.entidad;

public class Electrodomestico {
    private int id_electrodomestico;
    private String nombre;
    private int potenciaPromedioWatts;
    private int consumoHoraWh;
    private Categoria categoria;

    public Electrodomestico(int id_electrodomestico, String nombre, int potenciaPromedioWatts, int consumoHoraWh, Categoria categoria) {
        this.id_electrodomestico = id_electrodomestico;
        this.nombre = nombre;
        this.potenciaPromedioWatts = potenciaPromedioWatts;
        this.consumoHoraWh = consumoHoraWh;
        this.categoria = categoria;
    }

    public Electrodomestico() {
    }

    public int getId_electrodomestico() {
        return id_electrodomestico;
    }

    public void setId_electrodomestico(int id_electrodomestico) {
        this.id_electrodomestico = id_electrodomestico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPotenciaPromedioWatts() {
        return potenciaPromedioWatts;
    }

    public void setPotenciaPromedioWatts(int potenciaPromedioWatts) {
        this.potenciaPromedioWatts = potenciaPromedioWatts;
    }

    public int getConsumoHoraWh() {
        return consumoHoraWh;
    }

    public void setConsumoHoraWh(int consumoHoraWh) {
        this.consumoHoraWh = consumoHoraWh;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return nombre;
    }
}