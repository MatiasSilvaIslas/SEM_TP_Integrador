package frgp.utn.edu.com;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.ui.electrodomesticos.EditarElectrodomesticoActivity;

public class MainActivity extends AppCompatActivity {

   // private ActivityResultLauncher<Intent> editarElectrodomesticoLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_electrocarga);
        //setContentView(R.layout.activity_edit_profile);

        // Ejemplo: configuración de ícono dinámico para un electrodoméstico
        ImageView imageElectrodomestico = findViewById(R.id.image_electrodomestico);
        TextView nombreElectrodomestico = findViewById(R.id.text_nombre);

        // Cambiar icono según el nombre del electrodoméstico
        String nombre = nombreElectrodomestico.getText().toString().toLowerCase();
        switch (nombre) {
            case "refrigerador":
                imageElectrodomestico.setImageResource(R.drawable.ic_refrigerador);
                break;
            case "heladera":
                imageElectrodomestico.setImageResource(R.drawable.ic_heladera);
                break;
            case "lavarropa":
                imageElectrodomestico.setImageResource(R.drawable.ic_lavarropa);
                break;
            case "televisor":
                imageElectrodomestico.setImageResource(R.drawable.ic_televisor);
                break;
            default:
                imageElectrodomestico.setImageResource(R.drawable.ic_default);
                break;
        }

        // Configuración del botón Registrar
        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(v -> {
            // Acción para el botón Registrar
            Toast.makeText(MainActivity.this, "Registrar clicado", Toast.LENGTH_SHORT).show();
            // Puedes iniciar una nueva actividad o fragmento si lo deseas
        });
// Acción para el botón Editar
        Toast.makeText(MainActivity.this, "Editar clicado", Toast.LENGTH_SHORT).show();

        ImageButton buttonEdit = findViewById(R.id.button_edit);
        buttonEdit.setOnClickListener(v -> {
            // Datos de ejemplo para editar (estos deberían ser dinámicos en una implementación real)
           // String nombre = "LAvarropas";
            String potencia = "500W";

            // Acción para el botón Editar
            Toast.makeText(MainActivity.this, "Editar clicado", Toast.LENGTH_SHORT).show();

            // Iniciar la actividad para editar el electrodoméstico con datos
            Intent intent = new Intent(MainActivity.this, EditarElectrodomesticoActivity.class);

            // Datos de prueba
         //   intent.putExtra("nombre", "Electrodoméstico de prueba");
         //   intent.putExtra("potencia", "500W");

            intent.putExtra("nombre", nombre);
            intent.putExtra("potencia", potencia);

            startActivity(intent);
        });


        // Configuración del botón Eliminar
        ImageButton  buttonDelete = findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(v -> {
            // Acción para el botón Eliminar
            Toast.makeText(MainActivity.this, "Eliminar clicado", Toast.LENGTH_SHORT).show();
            // Aquí puedes agregar lógica adicional para confirmar la eliminación
        });

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_electrocarga, container, false);

        ImageView imageElectrodomestico = view.findViewById(R.id.image_electrodomestico);
        TextView nombreElectrodomestico = view.findViewById(R.id.text_nombre);

        return view;
    }


}



    EditText txtUsuario, txtpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        txtUsuario = findViewById(R.id.txtUsuario);
        txtpassword = findViewById(R.id.txtpassword);
    }

//    public void RegisterActivity(View view) {
//        setContentView(R.layout.register_user);
//    }
//
//    public void LoginActivity(View view) {
//        setContentView(R.layout.activity_main);
//    }
//
//    public void HomeApp() {
//        setContentView(R.layout.home_app);
//    }
//
//    public void Registrar(View view) {;
//        EditText et_name = (EditText) findViewById(R.id.editTextName);
//        EditText et_email = (EditText) findViewById(R.id.editTextEmail);
//        EditText et_pass = (EditText) findViewById(R.id.editTextPass);
//        EditText et_pass2 = (EditText) findViewById(R.id.editTextPass2);
//
//        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
//        SQLiteDatabase db = admin.getWritableDatabase();
//
//        String name = et_name.getText().toString();
//        String email = et_email.getText().toString();
//        String pass = et_pass.getText().toString();
//        String pass2 = et_pass2.getText().toString();
//
//        if (!name.isEmpty() && !email.isEmpty() && !pass.isEmpty() && !pass2.isEmpty()) {
//            if (pass.equals(pass2)) {
//                Cursor fila = db.rawQuery
//                        ("select * from users where name = '" + name + "'", null);
//
//                if (fila.moveToFirst()) {
//                    Toast.makeText(this, "El nombre de usuario no esta disponible", Toast.LENGTH_SHORT).show();
//                } else {
//                    ContentValues registro = new ContentValues();
//                    registro.put("name", name);
//                    registro.put("email", email);
//                    registro.put("pass", pass);
//
//                    db.insert("users", null, registro);
//
//                    Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show();
//                    et_name.setText("");
//                    et_email.setText("");
//                    et_pass.setText("");
//                    et_pass2.setText("");
//                    LoginActivity(view);
//                }
//            } else {
//                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
//            }
//
//        } else {
//            Toast.makeText(this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
//        }
//        db.close();
//    }
//
//    public void validarContrseña(String pass, User user) {
//        if (pass.equals(user.getPassword())) {
//            Toast.makeText(this, "Contraseña correcta", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, MainMenuActivity.class);
//            intent.putExtra(PutExtraConst.UserKey, user);
//            startActivity(intent);
//            return;
//        } else {
//            Toast.makeText(this, "Contraseña incorrecta: " , Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void BuscarUser(View view) {
//
//        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
//        SQLiteDatabase db = admin.getWritableDatabase();
//
//        String name = txtUsuario.getText().toString();
//        String pass = txtpassword.getText().toString();
//
//        if (!name.isEmpty() && !pass.isEmpty()) {
//            Cursor fila = db.rawQuery
//                    ("select name,email,pass from users where name = '" + name + "'", null);
//
//            if (fila.moveToFirst()) {
//                User user = new User (fila.getString(0),fila.getString(1),fila.getString(2));
//                validarContrseña(pass, user);
//            } else {
//                Toast.makeText(this, "No existe el usuario, debe registrarse", Toast.LENGTH_SHORT).show();
//            }
//
//        } else {
//            Toast.makeText(this, "Debes llenar todos lo campos", Toast.LENGTH_SHORT).show();
//        }
//        db.close();
 */
   }
