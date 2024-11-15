package frgp.utn.edu.com.ui.back;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.ui.electrodomesticos.ABMLElectrodomesticosActivity;
import frgp.utn.edu.com.ui.electrodomesticos.CalculoConsumoActivity;
import frgp.utn.edu.com.ui.electrodomesticos.ConsejosActivity;

public class PantallaPrincipalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        // Navegar a -> Mi Perfil
        Button btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PantallaPrincipalActivity.this, "Perfil", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PantallaPrincipalActivity.this, MiPerfilActivity.class);
                startActivity(intent);
            }
        });

        // Navegar a ABMLElectrodomesticosActivity
        Button btnManageAppliances = findViewById(R.id.btnManageAppliances);
        btnManageAppliances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaPrincipalActivity.this, ABMLElectrodomesticosActivity.class);
                startActivity(intent);
            }
        });

        // Navegar a CalculoConsumoActivity
        Button btnCalculateConsumption = findViewById(R.id.btnCalculateConsumption);
        btnCalculateConsumption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaPrincipalActivity.this, CalculoConsumoActivity.class);
                startActivity(intent);
            }
        });

        // Navegar a -> Consejos del uso eficiente
        Button btnCosejos = findViewById(R.id.btnCosejos);
        btnCosejos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaPrincipalActivity.this, ConsejosActivity.class);
                startActivity(intent);
            }
        });
    }
}