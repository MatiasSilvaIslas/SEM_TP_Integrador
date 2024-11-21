package frgp.utn.edu.com.entidad;

public class ConsumoElectrodomestico extends Electrodomestico {
    private int dias;
    private int cantidad;
    private int horas;
    private float consumoDiario;

    public ConsumoElectrodomestico(int id_electrodomestico, String nombre, int potenciaPromedioWatts, int consumoHoraWh,
                                   Categoria categoria, int dias, int cantidad, int horas, float consumoDiario) {
        super(id_electrodomestico, nombre, potenciaPromedioWatts, consumoHoraWh, categoria);
        this.dias = dias;
        this.cantidad = cantidad;
        this.horas = horas;
        this.consumoDiario = consumoDiario;
    }


    public int getDias() { return dias; }
    public void setDias(int dias) { this.dias = dias; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public int getHoras() { return horas; }
    public void setHoras(int horas) { this.horas = horas; }

    public float getConsumoDiario() { return consumoDiario; }
    public void setConsumoDiario(float consumoDiario) { this.consumoDiario = consumoDiario; }
}
