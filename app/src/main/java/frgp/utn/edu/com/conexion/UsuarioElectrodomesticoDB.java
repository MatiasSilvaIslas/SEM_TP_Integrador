package frgp.utn.edu.com.conexion;

import android.content.Context;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import frgp.utn.edu.com.entidad.UsuarioElectrodomestico;

public class UsuarioElectrodomesticoDB {

    private Context context;

    public UsuarioElectrodomesticoDB(Context context) {
        this.context = context;
    }

    public void insertarElectrodomesticos(int usuarioId, ArrayList<UsuarioElectrodomestico> seleccionados, InsertarElectrodomesticosCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            boolean success = true;
            try {
                // Cargar el driver JDBC
                Class.forName("com.mysql.jdbc.Driver");

                // Establecer conexión con la base de datos
                Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                // Preparar la consulta para insertar los electrodomésticos
                String query = "INSERT INTO UsuarioElectrodomestico (usuario_id, electrodomestico_id, cantidad, horas, dias) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(query);

                // Insertar cada electrodoméstico seleccionado
                for (UsuarioElectrodomestico usuarioElectrodomestico : seleccionados) {
                    pst.setInt(1, usuarioId);
                    pst.setInt(2, usuarioElectrodomestico.getElectrodomesticoId());
                    pst.setInt(3, usuarioElectrodomestico.getCantidad());
                    pst.setInt(4, usuarioElectrodomestico.getHoras());
                    pst.setInt(5, usuarioElectrodomestico.getDias());
                    pst.addBatch();
                }

                pst.executeBatch();
                pst.close();
                con.close();
            } catch (Exception e) {
                success = false;
                e.printStackTrace();
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "Error al insertar electrodomésticos", Toast.LENGTH_SHORT).show();
                });
            }

            boolean finalSuccess = success;
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> callback.onElectrodomesticosInsertados(finalSuccess));
        });
    }

    // Interfaz para el callback de inserción
    public interface InsertarElectrodomesticosCallback {
        void onElectrodomesticosInsertados(boolean success);
    }
}
