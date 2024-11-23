package frgp.utn.edu.com.conexion;

import android.util.Log;
import frgp.utn.edu.com.entidad.Categoria;
import frgp.utn.edu.com.entidad.Electrodomestico;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ElectroDB {
    private static final String TAG = "ElectroDB";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public interface ElectrodomesticosCallback {
        void onSuccess(ArrayList<Electrodomestico> electrodomesticos);
        void onError(Exception e);
    }

    public void getAllElectrodomesticos(ElectrodomesticosCallback callback) {
        executorService.execute(() -> {
            ArrayList<Electrodomestico> electrodomesticos = new ArrayList<>();

            try (Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM Electrodomestico")) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id_electrodomestico");
                    String nombre = resultSet.getString("nombre");
                    int potenciaPromedioWatts = resultSet.getInt("potencia_promedio_watts");
                    int consumoHoraWh = resultSet.getInt("consumo_hora_wh");
                    int idCategoria = resultSet.getInt("id_categoria");

                    Categoria categoria = new Categoria();
                    categoria.setId_categoria(idCategoria);

                    Electrodomestico electrodomestico = new Electrodomestico(id, nombre, potenciaPromedioWatts, consumoHoraWh, categoria);
                    electrodomesticos.add(electrodomestico);
                }

                // Llamar al callback con éxito
                callback.onSuccess(electrodomesticos);

            } catch (Exception e) {
                Log.e(TAG, "Error al obtener electrodomésticos: ", e);

                // Llamar al callback con error
                callback.onError(e);
            }
        });
    }
}