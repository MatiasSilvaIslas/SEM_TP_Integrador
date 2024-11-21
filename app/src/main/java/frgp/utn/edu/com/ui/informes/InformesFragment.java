package frgp.utn.edu.com.ui.informes;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.List;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.GraficosHelper;

public class InformesFragment extends Fragment {

    private LineChart lineChart;
    private PieChart pieChart;
    private EditText editLimiteConsumo;
    //private TextView consumoPromedioTextView;
    private GraficosHelper graficosHelper;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graficos_tres, container, false);

        initializeViews(view);

        setupLineChart();

        graficosHelper = new GraficosHelper(requireContext());
        cargarDatosYActualizarGraficos();

        return view;
    }

    private void cargarDatosYActualizarGraficos() {
        double limiteConsumo = obtenerLimiteConsumo(); // Obtén este valor del EditText

        graficosHelper.actualizarGraficos(limiteConsumo, (datosLinea, datosTorta, consumoPromedio) -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    actualizarGraficoLinea(datosLinea);

                    //actualizarConsumoPromedio(consumoPromedio);
                });
            }
        });
    }

    private double obtenerLimiteConsumo() {
        if (editLimiteConsumo == null || editLimiteConsumo.getText().toString().trim().isEmpty()) {
            //Toast.makeText(getContext(), "Ingrese un límite válido", Toast.LENGTH_SHORT).show();
            return 0; // Valor por defecto
        }
        try {
            return Double.parseDouble(editLimiteConsumo.getText().toString().trim());
        } catch (NumberFormatException e) {
            //Toast.makeText(getContext(), "Ingrese un número válido", Toast.LENGTH_SHORT).show();
            return 0; // Valor por defecto
        }
    }

    private void initializeViews(View view) {
        editLimiteConsumo = view.findViewById(R.id.editLimiteConsumo);
        lineChart = view.findViewById(R.id.lineChart);

    }

    private void setupPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        // Configurar leyenda
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setWordWrapEnabled(true);
    }

    private void setupLineChart() {
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDrawGridBackground(false);

        // Configurar ejes
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);

        lineChart.getAxisRight().setEnabled(false);
    }

    private void actualizarGraficoTorta(List<PieEntry> datosTorta) {
        if (datosTorta.isEmpty()) {
            pieChart.setNoDataText("No hay datos disponibles");
            return;
        }

        PieDataSet dataSet = new PieDataSet(datosTorta, "Consumo por Electrodoméstico");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueFormatter(new PercentFormatter(pieChart));

        PieData data = new PieData(dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }

    private void actualizarGraficoLinea(List<Entry> datosLinea) {
        if (datosLinea.isEmpty()) {
            lineChart.setNoDataText("No hay datos disponibles");
            return;
        }

        LineDataSet dataSet = new LineDataSet(datosLinea, "Consumo Diario");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(9f);
        dataSet.setDrawFilled(true);
        dataSet.setFormLineWidth(1f);
        dataSet.setFormSize(15.f);
        dataSet.setFillColor(Color.BLUE);
        dataSet.setFillAlpha(30);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

}




    /*
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

        return view;
    }

    private void cargarDatosYActualizarGraficos() {
        double limiteConsumo = obtenerLimiteConsumo(); // Obtén este valor del EditText

        graficosHelper.actualizarGraficos(limiteConsumo, (datosLinea, datosTorta, consumoPromedio) -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    actualizarGraficoLinea(datosLinea);
                    actualizarGraficoTorta(datosTorta);
                    //actualizarConsumoPromedio(consumoPromedio);
                });
            }
        });
    }


    private double obtenerLimiteConsumo() {
        if (editLimiteConsumo == null || editLimiteConsumo.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Ingrese un límite válido", Toast.LENGTH_SHORT).show();
            return 0; // Valor por defecto
        }
        try {
            return Double.parseDouble(editLimiteConsumo.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Ingrese un número válido", Toast.LENGTH_SHORT).show();
            return 0; // Valor por defecto
        }
    }

    private void actualizarGraficoLinea(List<Entry> datosLinea) {
        if (datosLinea.isEmpty()) {
            lineChart.setNoDataText("No hay datos para mostrar");
        } else {
        LineDataSet lineDataSet = new LineDataSet(datosLinea, "Consumo Diario");
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setCircleColor(Color.RED);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
        }
    }

    private void actualizarGraficoTorta(List<PieEntry> datosTorta) {
        PieDataSet pieDataSet = new PieDataSet(datosTorta, "Consumo por Electrodoméstico");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS); // Colores del gráfico
        pieDataSet.setValueTextColor(Color.BLACK); // Color de texto
        pieDataSet.setValueTextSize(12f); // Tamaño del texto
        pieChart.setDrawEntryLabels(false); // Desactiva las etiquetas dentro del gráfico
        pieChart.setUsePercentValues(true); // Si es necesario, muestra porcentajes en vez de valores
        Legend legend = pieChart.getLegend();
        legend.setWordWrapEnabled(true); // Permite que las etiquetas en la leyenda ocupen varias líneas
        PieData pieData = new PieData(pieDataSet); // Crear PieData con el DataSet
        pieChart.setData(pieData); // Establecer los datos en el gráfico
        pieChart.invalidate(); // Refrescar el gráfico
    }

    //private void actualizarConsumoPromedio(float consumoPromedio) {
    //    consumoPromedioTextView.setText(String.format("Consumo Promedio: %.2f Wh", consumoPromedio));
    //}
}
*/