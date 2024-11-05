package frgp.utn.edu.com.ui.usuario;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.DataUsuario;
import frgp.utn.edu.com.repository.AuthAppRepository;

public class AltaDatosPersonalesFragment extends Fragment {
    private EditText etNombre;
    private EditText etApellido;
    private Button btnRegistrar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(requireContext(), "Por aca si paso jaja saludos", Toast.LENGTH_SHORT).show();

       // View view = inflater.inflate(R.layout.fragment_loginregister2, container, false);

        //etNombre = view.findViewById(R.id.et_nombre);
        //etApellido = view.findViewById(R.id.et_apellido);

        //btnRegistrar = view.findViewById(R.id.btn_registrar);


        /*btnRegistrar.setOnClickListener(v -> {
            // Muestra el Toast
            Toast.makeText(requireContext(), "Mierda compacta: ", Toast.LENGTH_SHORT).show();
            // Llama al método para agregar el usuario
            agregarUsuario();
        });*/

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_loginregister2,container,false);
        Toast.makeText(requireContext(), "Por aca si paso jaja saludos", Toast.LENGTH_SHORT).show();

        initViews(view);
        return view;
    }

    private void initViews(View v) {
        btnRegistrar = v.findViewById(R.id.btn_registrar);
        btnRegistrar.setOnClickListener(vi -> {
            // Muestra el Toast
            Toast.makeText(requireContext(), "Mierda compacta: ", Toast.LENGTH_SHORT).show();
            // Llama al método para agregar el usuario
            agregarUsuario();
        });

    }

    private void agregarUsuario() {
        DataUsuario dataUsuario = new DataUsuario(requireContext());
        dataUsuario.agregarUsuario();
    }
}
