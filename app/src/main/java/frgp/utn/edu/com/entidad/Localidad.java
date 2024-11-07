package frgp.utn.edu.com.entidad;

public class Localidad {
    private int id_localidad;
    private int id_provincia;
    private String desripcion;
    private boolean estado;

    public Localidad(int id_localidad, int id_provincia, String desripcion, boolean estado) {
        this.id_localidad = id_localidad;
        this.id_provincia = id_provincia;
        this.desripcion = desripcion;
        this.estado = estado;
    }

    public Localidad() {
    }

    public int getId_localidad() {
        return id_localidad;
    }

    public void setId_localidad(int id_localidad) {
        this.id_localidad = id_localidad;
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
        return  desripcion;
    }
}
