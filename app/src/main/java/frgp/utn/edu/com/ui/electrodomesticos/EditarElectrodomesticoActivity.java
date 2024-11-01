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

        Button buttonGuardar = findViewById(R.id.button_guardar);
        buttonGuardar.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString();
            String potencia = editPotencia.getText().toString();
            // Lógica para guardar los datos o enviar al servidor
            Toast.makeText(this, "Datos guardados: " + nombre + " - " + potencia + "W", Toast.LENGTH_SHORT).show();
            Log.d("EditarElectrodomestico", "Botón Guardar presionado con datos: " + nombre + " - " + potencia);
        });
    }
}
