package frgp.utn.edu.com.ui.soporte;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import frgp.utn.edu.com.R;

public class ContactoFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contacto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnSoporte = findViewById(R.id.btn_soporte);
        btnSoporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // La URL del formulario de Google
                String url = "https://forms.gle/BSRAzx3tQNMjrtWaA";

                // Crea un Intent explícito para abrir Chrome
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setPackage("com.android.chrome"); // Establece Chrome como la aplicación

                try {
                    // Inicia Chrome
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Si Chrome no está instalado, abre el enlace en el navegador predeterminado
                    Intent fallbackIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(fallbackIntent);
                }
            }
        });

    }
}