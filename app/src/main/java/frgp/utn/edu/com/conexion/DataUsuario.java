package frgp.utn.edu.com.conexion;

import android.content.Context;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import frgp.utn.edu.com.entidad.Localidad;
import frgp.utn.edu.com.entidad.Provincia;
import frgp.utn.edu.com.entidad.Usuario;

public class DataUsuario {
    private Context context;
    private Usuario usuario;

    public DataUsuario(Context ct) {
        context = ct;
    }

    public void agregarUsuario(Usuario usuario, Callback callback) {
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
                pst.setInt(2, usuario.getLocalidad().getId_localidad());
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
            }

            boolean finalSuccess = success;
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> callback.onComplete(finalSuccess));
        });
    }
    public interface Callback {
        void onComplete(boolean success);
    }

    public void updateUsuario(Usuario usuario, Callback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            boolean success = false;

            try {
                // Establecer el controlador de la base de datos y conexión
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                // Formatear la fecha de nacimiento
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fechaFormateada = sdf.format(usuario.getFecha_nac());

                // Consulta SQL para actualizar los datos del usuario
                String query = "UPDATE Usuarios SET " +
                        "ID_provincia = ?, ID_localidad = ?, genero = ?, " +
                        "nombre_usuario = ?, apellido_usuario = ?, fecha_nac = ? " +
                        "WHERE email_usuario = ?"; // Usamos el email como criterio para identificar al usuario

                // Preparar la consulta con los valores del usuario
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, usuario.getProvincia().getId_provincia());
                pst.setInt(2, usuario.getLocalidad().getId_localidad());
                pst.setString(3, usuario.getGenero());
                pst.setString(4, usuario.getNombre_usuario());
                pst.setString(5, usuario.getApellido_usuario());
                pst.setString(6, fechaFormateada);
                pst.setString(7, usuario.getEmail()); // Usamos el email para identificar al usuario en la base de datos

                // Ejecutar la actualización
                int rowsAffected = pst.executeUpdate();
                success = rowsAffected > 0; // Si las filas afectadas son más de 0, la actualización fue exitosa

                pst.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
                // Si ocurre un error, mostrar un mensaje en el hilo principal
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
                });
            }

            // Notificar el resultado al callback en el hilo principal
            boolean finalSuccess = success;
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> callback.onComplete(finalSuccess));
        });
    }


    public Usuario obtenerUsuarioPorEmail(String email, CallbackUsuario callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Usuario usuario = null;

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                String query = "SELECT u.ID_usuario, u.genero, u.email_usuario, u.nombre_usuario, u.apellido_usuario, u.fecha_nac, " +
                        "p.codProvincia , p.provincia AS provincia_descripcion, p.estado AS provincia_estado, " +
                        "l.codLocalidad, l.localidad AS localidad_descripcion, l.estado AS localidad_estado " +
                        "FROM Usuarios u " +
                        "INNER JOIN Provincias p ON u.Id_provincia = p.codProvincia " +
                        "INNER JOIN Localidades l ON u.Id_localidad = l.codLocalidad " +
                        "WHERE u.email_usuario = ?";

                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, email);

                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("ID_usuario"));
                    usuario.setGenero(rs.getString("genero"));
                    usuario.setEmail(rs.getString("email_usuario"));
                    usuario.setNombre_usuario(rs.getString("nombre_usuario"));
                    usuario.setApellido_usuario(rs.getString("apellido_usuario"));
                    usuario.setFecha_nac(rs.getDate("fecha_nac"));

                    Provincia provincia = new Provincia();
                    provincia.setId_provincia(rs.getInt("CodProvincia"));
                    provincia.setDescripcion(rs.getString("provincia_descripcion"));
                    provincia.setEstado(rs.getBoolean("provincia_estado"));
                    usuario.setProvincia(provincia);

                    // Mapeo de la localidad
                    Localidad localidad = new Localidad();
                    localidad.setId_localidad(rs.getInt("codLocalidad"));
                    localidad.setDescripcion(rs.getString("localidad_descripcion"));
                    localidad.setEstado(rs.getBoolean("localidad_estado"));
                    usuario.setLocalidad(localidad);
                }

                rs.close();
                pst.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Usuario finalUsuario = usuario;
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> callback.onComplete(finalUsuario));
        });
        return usuario;
    }

    public interface CallbackUsuario {
        void onComplete(Usuario usuario);
    }


}
