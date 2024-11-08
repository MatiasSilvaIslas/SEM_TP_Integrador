package frgp.utn.edu.com.ui.usuario;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.DataUsuario;
import frgp.utn.edu.com.conexion.LocalidadDB;
import frgp.utn.edu.com.conexion.ProvinciaDB;
import frgp.utn.edu.com.entidad.Localidad;
import frgp.utn.edu.com.entidad.Provincia;
import frgp.utn.edu.com.entidad.Usuario;
import frgp.utn.edu.com.utils.InputFilterLetters;

public class AltaDatosPersonalesFragment extends Fragment {
    private EditText etNombre;
    private EditText etApellido;
    private EditText etFechaNacimiento;
    private Button btnRegistrar;
    private String fechaParaBaseDeDatos;
    private String email;
    private List<Provincia> listaProvincias;
    private List<Localidad> listaLocalidades;
    private Spinner spinnerProvincia;
    private Spinner spinnerLocalidad;
    private Spinner spinnerGenero;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            email = getArguments().getString("email");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loginregister2, container, false);

        initViews(view);
        setupDatePicker();
        setupSpinnerGenero();
        cargarProvincias();

        return view;
    }

    private void initViews(View view) {
        etNombre = view.findViewById(R.id.et_nombre);
        etApellido = view.findViewById(R.id.et_apellido);
        etFechaNacimiento = view.findViewById(R.id.et_fecha_nacimiento);
        btnRegistrar = view.findViewById(R.id.btn_registrar);
        spinnerProvincia = view.findViewById(R.id.spinner_provincia);
        spinnerLocalidad = view.findViewById(R.id.spinner_localidad);
        spinnerGenero = view.findViewById(R.id.spinner_genero);

        // Filtro de letras para los campos de texto
        InputFilterLetters inputFilterLetters = new InputFilterLetters();
        etNombre.setFilters(new InputFilter[]{inputFilterLetters});
        etApellido.setFilters(new InputFilter[]{inputFilterLetters});

        btnRegistrar.setOnClickListener(v -> onRegistrarClick());

        spinnerProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Provincia provinciaSeleccionada = (Provincia) spinnerProvincia.getSelectedItem();
                cargarLocalidades(provinciaSeleccionada.getId_provincia());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });
    }

    private void setupDatePicker() {
        etFechaNacimiento.setOnClickListener(v -> showDatePickerDialog());
    }

    private void setupSpinnerGenero() {
        ArrayAdapter<CharSequence> adapterGenero = ArrayAdapter.createFromResource(
                getContext(),
                R.array.gender_array,
                android.R.layout.simple_spinner_item
        );
        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(adapterGenero);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, yearSelected, monthSelected, daySelected) -> {
                    etFechaNacimiento.setText(daySelected + "/" + (monthSelected + 1) + "/" + yearSelected);
                },
                year, month, day
        );

        // Limitar la selección de fecha a la actual
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void cargarProvincias() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            ProvinciaDB provinciaDB = new ProvinciaDB(requireContext());
            listaProvincias = provinciaDB.obtenerProvincias();

            requireActivity().runOnUiThread(() -> {
                ArrayAdapter<Provincia> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, listaProvincias);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProvincia.setAdapter(adapter);
            });
        });
    }

    private void cargarLocalidades(int id_provincia) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            LocalidadDB localidadDB = new LocalidadDB(requireContext());
            listaLocalidades = localidadDB.obtenerLocalidades(id_provincia);

            requireActivity().runOnUiThread(() -> {
                ArrayAdapter<Localidad> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, listaLocalidades);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerLocalidad.setAdapter(adapter);
            });
        });
    }


    private Date convertirStringADate(String fechaString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            return sdf.parse(fechaString);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error al formatear la fecha", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    private void onRegistrarClick() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String generoSeleccionado = spinnerGenero.getSelectedItem().toString();
        Provincia provinciaSeleccionada = (Provincia) spinnerProvincia.getSelectedItem();
        Localidad LocalidadSeleccionada = (Localidad) spinnerLocalidad.getSelectedItem();
        Date fechaNac = convertirStringADate(etFechaNacimiento.getText().toString());

        Usuario usuario = new Usuario(generoSeleccionado, email, nombre, apellido, fechaNac, provinciaSeleccionada, LocalidadSeleccionada );
        if (nombre.isEmpty() || apellido.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor complete todos los campos.", Toast.LENGTH_SHORT).show();
        } else {
            agregarUsuario(usuario);
        }
    }

    private void agregarUsuario(Usuario usuario) {
        DataUsuario dataUsuario = new DataUsuario(requireContext());
        dataUsuario.agregarUsuario(usuario, success -> {
            if (success) {
                Toast.makeText(requireContext(), "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                // Aquí agregamos la lógica para cambiar a la siguiente pantalla
                pasarASiguientePantalla();
            } else {
                Toast.makeText(requireContext(), "Error al registrar usuario.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pasarASiguientePantalla() {
        Intent intent = new Intent(getActivity(), PantallaPrincipalActivity.class);
        startActivity(intent);
    }

}
