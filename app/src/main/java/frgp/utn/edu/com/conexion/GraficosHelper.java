package frgp.utn.edu.com.conexion;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraficosHelper {
    private Context context;
    public GraficosHelper( Context ct) {
        context = ct;
    }
    //DatabaseHelper db = new DatabaseHelper();


    public void actualizarGraficos(int userId, double limiteConsumo, GraficoListener listener) {


        new Thread(() -> {
            try (

                    Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);
                    PreparedStatement pst = con.prepareStatement(
                            "SELECT fecha, consumo_diario FROM Consumo WHERE ID_usuario = ? ORDER BY fecha"
                    )
            ) {
                pst.setInt(1, userId);
                ResultSet rs = pst.executeQuery();

                List<Entry> datosLinea = new ArrayList<>();
                List<PieEntry> datosTorta = new ArrayList<>();
                Map<String, Float> consumoPorMes = new HashMap<>();

                while (rs.next()) {
                    String fecha = rs.getString("fecha");
                    float consumoDiario = rs.getFloat("consumo_diario");

                    // Agregar datos a la línea
                    datosLinea.add(new Entry(datosLinea.size(), consumoDiario));

                    // Agregar datos a la torta
                    String mes = fecha.substring(0, 7);
                    if (consumoPorMes.containsKey(mes)) {
                        consumoPorMes.put(mes, consumoPorMes.get(mes) + consumoDiario);
                    } else {
                        consumoPorMes.put(mes, consumoDiario);
                    }
                }

                // Agregar datos a la torta
                for (Map.Entry<String, Float> entry : consumoPorMes.entrySet()) {
                    datosTorta.add(new PieEntry(entry.getValue(), entry.getKey()));
                }

                ((FragmentActivity) context).runOnUiThread(() -> listener.onDatosListos(datosLinea, datosTorta));

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
