package frgp.utn.edu.com.ui.back;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.ui.usuario.EditarCredencialesActivity;

public class MiPerfilActivity extends AppCompatActivity {
    private Button btnModificarPerfil;
    private Button btnModificarCredenciales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        btnModificarPerfil = findViewById(R.id.btnModificarPerfil);
        btnModificarCredenciales = findViewById(R.id.btnModificarCredenciales);

        btnModificarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiPerfilActivity.this, EditarPerfilActivity.class);
                startActivity(intent);
            }
        });

        btnModificarCredenciales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiPerfilActivity.this, EditarCredencialesActivity.class);
                startActivity(intent);
            }
        });
    }
}