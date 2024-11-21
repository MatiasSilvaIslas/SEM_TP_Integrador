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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import frgp.utn.edu.com.utils.SessionManager;

public class GraficosHelper {
    private Context context;

    public GraficosHelper(Context ct) {
        context = ct;
    }

    public void actualizarGraficos(double limiteConsumo, GraficoListener listener) {
        new Thread(() -> {
            try (
                    Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);
                    PreparedStatement pst = con.prepareStatement(
                            "SELECT e.nombre, ue.dias, ue.cantidad, ue.horas, e.consumo_hora_wh, " +
                                    "(ue.horas * e.consumo_hora_wh * ue.cantidad) AS consumo_diario " +
                                    "FROM sql10735229.UsuarioElectrodomestico AS ue " +
                                    "LEFT JOIN sql10735229.Electrodomestico AS e ON ue.electrodomestico_id = e.id_electrodomestico " +
                                    "LEFT JOIN sql10735229.Usuarios AS u ON ue.usuario_id = u.ID_usuario " +
                                    "WHERE u.email_usuario = ? ORDER BY ue.dias;"
                    )
            ) {
                // Obtener el email del usuario desde las preferencias
                String userEmail = SessionManager.getUserEmail(context);
                if (userEmail == null || userEmail.isEmpty()) {
                    throw new IllegalStateException("El email del usuario no está disponible en la sesión.");
                }

                // Establecer el email en la consulta
                pst.setString(1, userEmail);
                ResultSet rs = pst.executeQuery();

                List<Entry> datosLinea = new ArrayList<>();
                List<PieEntry> datosTorta = new ArrayList<>();
                float totalConsumo = 0;

                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    int dias = rs.getInt("dias");
                    int cantidad = rs.getInt("cantidad");
                    int horas = rs.getInt("horas");
                    float consumoHoraWh = rs.getFloat("consumo_hora_wh");
                    float consumoDiario = rs.getFloat("consumo_diario");

                    totalConsumo += consumoDiario;

                    // Agregar entradas para el gráfico de línea y de torta
                    datosLinea.add(new Entry(dias, consumoDiario));
                    datosTorta.add(new PieEntry(consumoDiario, nombre));
                }

                float consumoPromedio = totalConsumo / (datosLinea.size() > 0 ? datosLinea.size() : 1);

                // Enviar datos al listener
                ((FragmentActivity) context).runOnUiThread(() -> listener.onDatosListos(datosLinea, datosTorta, consumoPromedio));
            } catch (Exception e) {
                e.printStackTrace();
                ((FragmentActivity) context).runOnUiThread(() ->
                        Toast.makeText(context, "Error al cargar datos: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    public interface GraficoListener {
        void onDatosListos(List<Entry> datosLinea, List<PieEntry> datosTorta, float consumoPromedio);
    }
}
