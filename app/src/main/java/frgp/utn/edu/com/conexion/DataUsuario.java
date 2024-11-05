package frgp.utn.edu.com.conexion;

import android.content.Context;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataUsuario {
    private Context context;

    public DataUsuario(Context ct) {
        context = ct;
    }

    public void agregarUsuario() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            boolean success = false;

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

                // Datos de ejemplo hardcodeados
                int idUsuario = 1; // Ejemplo de ID de usuario
                int idProvincia = 10; // Ejemplo de ID de provincia
                int idLocalidad = 20; // Ejemplo de ID de localidad
                int idGenero = 1; // Ejemplo de ID de género
                String email = "ejemplo@dominio.com"; // Ejemplo de email
                String nombre = "Juan"; // Ejemplo de nombre
                String apellido = "Pérez"; // Ejemplo de apellido
                String fechaNac = "1990-01-01"; // Ejemplo de fecha de nacimiento

                // Consulta hardcodeada
                String query = "INSERT INTO Usuarios (ID_usuario, ID_provincia, ID_localidad, ID_genero, email_usuario, nombre_usuario, apellido_usuario, fecha_nac) " +
                        "VALUES (" + idUsuario + ", " + idProvincia + ", " + idLocalidad + ", " + idGenero + ", '" + email + "', '" + nombre + "', '" + apellido + "', '" + fechaNac + "')";

                PreparedStatement pst = con.prepareStatement(query);
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

            boolean finalSuccess = success;
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                if (finalSuccess) {
                    Toast.makeText(context, "Usuario agregado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al agregar usuario", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


}
