package frgp.utn.edu.com.ui.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.ui.electrodomesticos.ABMLElectrodomesticosActivity;

public class PantallaPrincipalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnManageAppliances = findViewById(R.id.btnManageAppliances);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaPrincipalActivity.this, MiPerfilActivity.class);
                startActivity(intent);
            }
        });

        // Navegar a ABMLElectrodomesticosActivity
        btnManageAppliances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaPrincipalActivity.this, ABMLElectrodomesticosActivity.class);
                startActivity(intent);
            }
        });
    }
}