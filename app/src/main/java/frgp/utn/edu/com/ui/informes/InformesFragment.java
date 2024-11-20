package frgp.utn.edu.com.ui.informes;

import android.app.Application;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.List;

import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.DataUsuario;
import frgp.utn.edu.com.conexion.GraficosHelper;
import frgp.utn.edu.com.entidad.Usuario;
import frgp.utn.edu.com.ui.myaccount.fragmentMiPerfil;
import frgp.utn.edu.com.utils.SessionManager;

public class InformesFragment extends Fragment {

    private LineChart lineChart;
    private PieChart pieChart;
    private EditText editLimiteConsumo;
    private GraficosHelper graficosHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graficos, container, false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));


        // Inicializar vistas
        lineChart = view.findViewById(R.id.lineChart);
        pieChart = view.findViewById(R.id.pieChart);
        //editLimiteConsumo = view.findViewById(R.id.editLimiteConsumo);

        // Inicializar helper de gráficos
        graficosHelper = new GraficosHelper(getContext());

        // Obtener datos y actualizar gráficos
        cargarDatosYActualizarGraficos();

        //Acá me voy para el perfil del usurio
        ImageView btnProfile = view.findViewById(R.id.icon_user);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity() ).setnavigateToMainMenu(true);
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frgment_frame, new fragmentMiPerfil());
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    private void cargarDatosYActualizarGraficos() {
        int userId = obtenerUserId(); // Implementa el método para obtener el ID del usuario
        double limiteConsumo = obtenerLimiteConsumo(); // Obtén este valor del EditText

        graficosHelper.actualizarGraficos(userId, limiteConsumo, new GraficosHelper.GraficoListener() {
            @Override
            public void onDatosListos(List<Entry> datosLinea, List<PieEntry> datosTorta) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        actualizarGraficoLinea(datosLinea);
                        actualizarGraficoTorta(datosTorta);
                    });
                }
            }
        });
    }

    private int obtenerUserId() {
//        String loco= SessionManager.getUserEmail(getActivity());
//        Usuario usuario = new Usuario();
//        DataUsuario dataUsuario = new DataUsuario( getActivity());
//        usuario = dataUsuario.obtenerUsuarioPorEmail(loco, null);
        return 1;
    }
    /// Aca diego
    private double obtenerLimiteConsumo() {
        //String limiteText = editLimiteConsumo.getText().toString().trim();
        try {
            return Double.parseDouble("100");
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Ingrese un límite válido", Toast.LENGTH_SHORT).show();
            return 0; // Valor por defecto
        }
    }

    private void actualizarGraficoLinea(List<Entry> datosLinea) {
        LineDataSet lineDataSet = new LineDataSet(datosLinea, "Consumo Diario");
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setCircleColor(Color.RED);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private void actualizarGraficoTorta(List<PieEntry> datosTorta) {
        PieDataSet pieDataSet = new PieDataSet(datosTorta, "Consumo por Electrodoméstico");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}