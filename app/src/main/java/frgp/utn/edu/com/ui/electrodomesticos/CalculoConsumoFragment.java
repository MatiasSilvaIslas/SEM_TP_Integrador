package frgp.utn.edu.com.ui.electrodomesticos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.ElectrodomesticoDB;
import frgp.utn.edu.com.entidad.Electrodomestico;

import java.util.ArrayList;

public class CalculoConsumoFragment extends Fragment {
    private Spinner spinnerElectrodomesticos;
    private EditText editHorasUso, editDiasUso;
    private TextView textConsumoEstimado;
    private ElectrodomesticoDB electrodomesticoDB;
    private ArrayList<Electrodomestico> listaElectrodomesticos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar la vista
        View view = inflater.inflate(R.layout.fragment_calculo_consumo, container, false);

        // Configurar Toolbar
        //Toolbar toolbar = view.findViewById(R.id.toolbar);
        //if (toolbar != null) {
       //    ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
       // }

        // Inicializar vistas
        spinnerElectrodomesticos = view.findViewById(R.id.spinnerElectrodomesticos);
        editHorasUso = view.findViewById(R.id.editHorasUso);
        editDiasUso = view.findViewById(R.id.editDiasUso);
        textConsumoEstimado = view.findViewById(R.id.textConsumoEstimado);
        Button btnCalcular = view.findViewById(R.id.btnCalcular);

        // Inicializar base de datos y cargar electrodomésticos
        electrodomesticoDB = new ElectrodomesticoDB(getContext());
        electrodomesticoDB.obtenerElectrodomesticosAsync(1, electrodomesticos -> {
            listaElectrodomesticos = electrodomesticos;

            // Configurar el adaptador del Spinner
            ArrayAdapter<Electrodomestico> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, electrodomesticos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerElectrodomesticos.setAdapter(adapter);
        });

        // Configurar botón de cálculo
        btnCalcular.setOnClickListener(v -> {
            // Validar que el Spinner tenga un elemento seleccionado
            if (spinnerElectrodomesticos.getSelectedItem() == null) {
                Toast.makeText(getContext(), "Selecciona un electrodoméstico", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener el electrodoméstico seleccionado
            Electrodomestico selected = (Electrodomestico) spinnerElectrodomesticos.getSelectedItem();

            // Verificar si los campos están vacíos
            String horasText = editHorasUso.getText().toString().trim();
            String diasText = editDiasUso.getText().toString().trim();

            if (horasText.isEmpty() || diasText.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Convertir valores a enteros
                int horasPorDia = Integer.parseInt(horasText);
                int dias = Integer.parseInt(diasText);

                // Calcular consumo
                double consumoTotal = (selected.getPotenciaPromedioWatts() / 1000.0) * horasPorDia * dias;
                textConsumoEstimado.setText("El consumo estimado es: " + consumoTotal + " kWh en " + dias + " días.");
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Entrada inválida. Ingresa solo números.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}


