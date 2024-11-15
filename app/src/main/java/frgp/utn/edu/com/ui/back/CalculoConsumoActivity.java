package frgp.utn.edu.com.ui.back;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.ElectrodomesticoDB;
import frgp.utn.edu.com.entidad.Electrodomestico;

public class CalculoConsumoActivity extends AppCompatActivity {
    private Spinner spinnerElectrodomesticos;
    private EditText editHorasUso, editDiasUso;
    private TextView textConsumoEstimado;
    private ElectrodomesticoDB electrodomesticoDB;
    private ArrayList<Electrodomestico> listaElectrodomesticos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo_consumo);

        // Inicializar componentes
        spinnerElectrodomesticos = findViewById(R.id.spinnerElectrodomesticos);
        editHorasUso = findViewById(R.id.editHorasUso);
        editDiasUso = findViewById(R.id.editDiasUso);
        textConsumoEstimado = findViewById(R.id.textConsumoEstimado);
        Button btnCalcular = findViewById(R.id.btnCalcular);

        // Cargar electrodomésticos
        electrodomesticoDB = new ElectrodomesticoDB(this);
        electrodomesticoDB.obtenerElectrodomesticosAsync(1, electrodomesticos -> {
            listaElectrodomesticos = electrodomesticos;
            ArrayAdapter<Electrodomestico> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, electrodomesticos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerElectrodomesticos.setAdapter(adapter);
        });

        // Configurar botón de cálculo
        btnCalcular.setOnClickListener(view -> {
            // Obtener electrodoméstico seleccionado
            Electrodomestico selected = (Electrodomestico) spinnerElectrodomesticos.getSelectedItem();

            // Verificar si los campos están vacíos antes de convertirlos
            String horasText = editHorasUso.getText().toString().trim();
            String diasText = editDiasUso.getText().toString().trim();

            if (horasText.isEmpty() || diasText.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int horasPorDia = Integer.parseInt(horasText);
                int dias = Integer.parseInt(diasText);

                // Calcular consumo
                double consumoTotal = (selected.getPotenciaPromedioWatts() / 1000.0) * horasPorDia * dias;
                textConsumoEstimado.setText("El consumo estimado es: " + consumoTotal + " kWh en " + dias + " días.");
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Entrada inválida. Ingresa solo números.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

