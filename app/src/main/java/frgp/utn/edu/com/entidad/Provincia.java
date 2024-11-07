package frgp.utn.edu.com.entidad;

public class Provincia {
    private int id_provincia;
    private String desripcion;
    private boolean estado;

    public Provincia(int id_provincia, String desripcion, boolean estado) {
        this.id_provincia = id_provincia;
        this.desripcion = desripcion;
        this.estado = estado;
    }

    public Provincia() {
    }

    public int getId_provincia() {
        return id_provincia;
    }

    public void setId_provincia(int id_provincia) {
        this.id_provincia = id_provincia;
    }

    public String getDesripcion() {
        return desripcion;
    }

    public void setDesripcion(String desripcion) {
        this.desripcion = desripcion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return desripcion;
    }
}
