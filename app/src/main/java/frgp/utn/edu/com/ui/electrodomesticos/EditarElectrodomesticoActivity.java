package frgp.utn.edu.com.ui.electrodomesticos;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import frgp.utn.edu.com.R;

public class EditarElectrodomesticoActivity extends AppCompatActivity {

    private EditText editNombre, editPotencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_electrodomestico); // Asegurarme de que el nombre es correcto

        editNombre = findViewById(R.id.edit_nombre);
        editPotencia = findViewById(R.id.edit_potencia);

        // Recibir datos del Intent
        String nombre = getIntent().getStringExtra("nombre");
        String potencia = getIntent().getStringExtra("potencia");

        // Verificar si los datos son nulos
        if (nombre != null) {
            editNombre.setText(nombre);
        } else {
            Log.e("EditarElectrodomestico", "El nombre recibido es nulo");
        }

        if (potencia != null) {
            editPotencia.setText(potencia);
        } else {
            Log.e("EditarElectrodomestico", "La potencia recibida es nula");
        }

        // Configurar el botón de guardar para capturar los datos editados
        Button buttonGuardar = findViewById(R.id.button_guardar);
        buttonGuardar.setOnClickListener(v -> {
            // Obtener los valores editados por el usuario
            String nombreEditado = editNombre.getText().toString();
            String potenciaEditada = editPotencia.getText().toString();

            // Aquí puedes agregar la lógica para actualizar estos datos en la base de datos o enviarlos de vuelta
            Toast.makeText(this, "Datos guardados: " + nombreEditado + " - " + potenciaEditada + "W", Toast.LENGTH_SHORT).show();
            Log.d("EditarElectrodomestico", "Datos guardados: " + nombreEditado + " - " + potenciaEditada);
        });
    }
}
