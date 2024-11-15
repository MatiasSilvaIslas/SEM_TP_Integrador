package frgp.utn.edu.com.ui.soporte;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.DataDB;
import frgp.utn.edu.com.conexion.SoporteDB;

public class ContactoSoporteActivity extends AppCompatActivity {
    private EditText editCasoResumen, editCasoDetalle;
    private Button btnEnviarSoporte;
    private SoporteDB soporteDB;
    private int userId = -1; // Agregamos la variable userId como campo de la clase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto_soporte);

        // Inicializar componentes
        editCasoResumen = findViewById(R.id.editCasoResumen);
        editCasoDetalle = findViewById(R.id.editCasoDetalle);
        btnEnviarSoporte = findViewById(R.id.btnEnviarSoporte);

        // Inicializar base de datos
        soporteDB = new SoporteDB(this);

        obtenerUserId();

        // Botón de enviar
        btnEnviarSoporte.setOnClickListener(view -> {
            if (userId == -1) {
                Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
                return;
            }

            String resumen = editCasoResumen.getText().toString().trim();
            String detalle = editCasoDetalle.getText().toString().trim();

            if (!resumen.isEmpty() && !detalle.isEmpty()) {
                soporteDB.insertarCasoSoporte(userId, resumen, detalle); // Pasamos el userId
                Toast.makeText(this, "Se ha enviado con éxito. Pronto nos comunicaremos con usted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para obtener el ID del usuario a partir del email en SharedPreferences
    private void obtenerUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);

        if (email != null) {
            new ObtenerUserIdTask(email).execute();
        } else {
            Toast.makeText(this, "No se encontró un usuario registrado.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // AsyncTask para obtener el ID de usuario a partir del email
    @SuppressWarnings("deprecation")
    private class ObtenerUserIdTask extends AsyncTask<Void, Void, Integer> {
        private String email;
        private Exception error;

        public ObtenerUserIdTask(String email) {
            this.email = email;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            Connection con = null;
            PreparedStatement pst = null;
            ResultSet rs = null;
            try {
                // Conexión con la base de datos
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                // Consulta SQL para obtener el ID del usuario
                String query = "SELECT ID_usuario FROM Usuarios WHERE email = ?";
                pst = con.prepareStatement(query);
                pst.setString(1, email);
                rs = pst.executeQuery();

                if (rs.next()) {
                    return rs.getInt("ID_usuario");
                }
            } catch (Exception e) {
                error = e;
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (pst != null) pst.close();
                    if (con != null) con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (error != null) {
                Toast.makeText(ContactoSoporteActivity.this,
                        "Error al obtener datos: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            if (result != -1) {
                userId = result; // Asignamos el resultado a la variable de clase
            } else {
                Toast.makeText(ContactoSoporteActivity.this,
                        "Usuario no encontrado.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}