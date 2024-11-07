package frgp.utn.edu.com.conexion;

import android.content.Context;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import frgp.utn.edu.com.entidad.Usuario;

public class DataUsuario {
    private Context context;
    private Usuario usuario;

    public DataUsuario(Context ct) {
        context = ct;
    }

    public void agregarUsuario(Usuario usuario) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            boolean success = false;

            try {
                Class.forName("com.mysql.jdbc.Driver");

                Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fechaFormateada = sdf.format(usuario.getFecha_nac());

                String query = "INSERT INTO Usuarios (ID_provincia, ID_localidad, genero, email_usuario, nombre_usuario, apellido_usuario, fecha_nac) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, usuario.getProvincia().getId_provincia());
                pst.setInt(2, usuario.getLocalidad().getId_provincia());
                pst.setString(3, usuario.getGenero());
                pst.setString(4, usuario.getEmail());
                pst.setString(5, usuario.getNombre_usuario());
                pst.setString(6, usuario.getApellido_usuario());
                pst.setString(7, fechaFormateada);

                int rowsAffected = pst.executeUpdate();
                success = rowsAffected > 0;

                pst.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "Error al agregar usuario", Toast.LENGTH_SHORT).show();
                });
                return;
            }
        });
    }
}
