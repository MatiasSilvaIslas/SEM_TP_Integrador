package frgp.utn.edu.com.entidad;

public class Provincia {
    private int id_provincia;
    private String descripcion;
    private boolean estado;

    public Provincia(int id_provincia, String desripcion, boolean estado) {
        this.id_provincia = id_provincia;
        this.descripcion = desripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String desripcion) {
        this.descripcion = desripcion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
