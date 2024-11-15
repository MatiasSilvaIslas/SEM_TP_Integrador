package frgp.utn.edu.com.ui.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.ui.usuario.EditarCredencialesActivity;
import frgp.utn.edu.com.ui.usuario.EditarPerfilActivity;
import frgp.utn.edu.com.ui.usuario.MiPerfilActivity;

public class fragmentMiPerfil extends Fragment {
    private Button btnModificarPerfil;
    private Button btnModificarCredenciales;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_mi_perfil);

        btnModificarPerfil = findViewById(R.id.btnModificarPerfil);
        btnModificarCredenciales = findViewById(R.id.btnModificarCredenciales);*/

        btnModificarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MiPerfilActivity.this, EditarPerfilActivity.class);
                startActivity(intent);*/
            }
        });

        btnModificarCredenciales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MiPerfilActivity.this, EditarCredencialesActivity.class);
                startActivity(intent);*/
            }
        });
    }
}
