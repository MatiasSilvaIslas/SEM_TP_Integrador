package frgp.utn.edu.com.conexion;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraficosHelper {
    private Context context;

    public GraficosHelper(Context context) {
        this.context = context;
    }

    public void actualizarGraficos(int userId, double limiteConsumo, GraficoListener listener) {
        new Thread(() -> {
            try (Connection connection = DatabaseHelper.getConnection()) {
                String query = "SELECT e.nombre, ue.horas, e.consumo_hora_wh " +
                        "FROM UsuarioElectrodomestico as ue " +
                        "LEFT JOIN Electrodomestico as e ON ue.electrodomestico_id = id_electrodomestico " +
                        "WHERE ue.usuario_id = ?";

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();

                List<Entry> datosLinea = new ArrayList<>();
                List<PieEntry> datosTorta = new ArrayList<>();
                Map<String, Double> consumoPorElectrodomestico = new HashMap<>();

                double consumoTotal = 0;
                int dia = 1;

                while (resultSet.next()) {
                    String nombre = resultSet.getString("nombre");
                    int horas = resultSet.getInt("horas");
                    double consumoHora = resultSet.getDouble("consumo_hora_wh");
                    double consumo = horas * consumoHora / 1000.0;

                    consumoTotal += consumo;

                    consumoPorElectrodomestico.put(nombre,
                            consumoPorElectrodomestico.getOrDefault(nombre, 0.0) + consumo);

                    datosLinea.add(new Entry(dia++, (float) consumoTotal));
                }

                for (Map.Entry<String, Double> entry : consumoPorElectrodomestico.entrySet()) {
                    datosTorta.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
                }

                listener.onDatosListos(datosLinea, datosTorta);

            } catch (Exception e) {
                e.printStackTrace();
                ((FragmentActivity) context).runOnUiThread(() ->
                        Toast.makeText(context, "Error al cargar datos: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    public interface GraficoListener {
        void onDatosListos(List<Entry> datosLinea, List<PieEntry> datosTorta);
    }
}
