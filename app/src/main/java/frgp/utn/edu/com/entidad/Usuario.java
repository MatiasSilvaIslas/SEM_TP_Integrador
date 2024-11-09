package frgp.utn.edu.com.entidad;

import java.util.Date;

public class Usuario {
    private int idUsuario;
    private String genero;
    private String email;
    private String nombre_usuario;
    private String apellido_usuario;
    private Date fecha_nac;
    private Provincia provincia;
    private Localidad localidad;


    public Usuario() {
    }

    public Usuario(String genero, String email, String nombre_usuario, String apellido_usuario, Date fecha_nac, Provincia provincia, Localidad localidad) {
        this.genero = genero;
        this.email = email;
        this.nombre_usuario = nombre_usuario;
        this.apellido_usuario = apellido_usuario;
        this.fecha_nac = fecha_nac;
        this.provincia = provincia;
        this.localidad = localidad;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public Date getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(Date fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public String getApellido_usuario() {
        return apellido_usuario;
    }

    public void setApellido_usuario(String apellido_usuario) {
        this.apellido_usuario = apellido_usuario;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }


}
