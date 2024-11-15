package frgp.utn.edu.com.repository;

import android.content.Context;
import frgp.utn.edu.com.entidad.Usuario;

public class UsuarioData {
    private Context context;
    private Usuario usuario;

    public UsuarioData(Context ct) {
        context = ct;
    }
    public Usuario agregarUsuario(Usuario usuario) {

        return usuario;
    }


}
