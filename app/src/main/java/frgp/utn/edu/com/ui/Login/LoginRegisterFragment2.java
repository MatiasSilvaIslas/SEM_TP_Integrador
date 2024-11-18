package frgp.utn.edu.com.ui.Login;

import android.app.Application;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.textfield.TextInputEditText;
import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.DataUsuario;
import frgp.utn.edu.com.conexion.LocalidadDB;
import frgp.utn.edu.com.conexion.ProvinciaDB;
import frgp.utn.edu.com.entidad.Localidad;
import frgp.utn.edu.com.entidad.Provincia;
import frgp.utn.edu.com.entidad.Usuario;
import frgp.utn.edu.com.repository.AuthAppRepository;
import frgp.utn.edu.com.repository.UsuarioData;
import frgp.utn.edu.com.ui.electrodomesticos.ConsejosFragment;
import frgp.utn.edu.com.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginRegisterFragment2 extends Fragment {
    private AuthAppRepository authAppRepository;
    private Button registerButton;
    private TextInputEditText email, nombre,apellido;
    private TextInputEditText etFechaNacimiento;
    private Spinner spinnerProvincia;
    private Spinner spinnerLocalidad;
    private Spinner spinnerGenero;


    private List<Provincia> listaProvincias;
    private List<Localidad> listaLocalidades;
    private String userEmail;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_loginregister2, container, false);
        ((MainActivity) getActivity() ).setnavigateToMainMenu(false);
        initViews(view);
        setupSpinners();
        etFechaNacimiento = view.findViewById(R.id.et_fecha_nacimiento);
        etFechaNacimiento.setOnClickListener(v -> showDatePickerDialog());
        return view;
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Fecha máxima es hoy
        long todayInMillis = calendar.getTimeInMillis();

        // Establecer la fecha mínima como 12 años atrás
        calendar.add(Calendar.YEAR, -12);  // Resta 12 años
        long twelveYearsAgoInMillis = calendar.getTimeInMillis();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        etFechaNacimiento.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Establece la fecha mínima (12 años atrás)

        // Establece la fecha máxima (hoy)
        datePickerDialog.getDatePicker().setMaxDate(twelveYearsAgoInMillis);

        datePickerDialog.show();
    }


    public void initViews(View v) {
        spinnerProvincia = v.findViewById(R.id.spinner_provincia);
        spinnerLocalidad = v.findViewById(R.id.spinner_localidad);
        spinnerGenero = v.findViewById(R.id.spinner_genero);
        etFechaNacimiento = v.findViewById(R.id.et_fecha_nacimiento);
        nombre= v.findViewById(R.id.et_nombre);
        apellido= v.findViewById(R.id.et_apellido);

        nombre.setFilters(new InputFilter[]{letrasYEspaciosFilter});
        apellido.setFilters(new InputFilter[]{letrasYEspaciosFilter});


        registerButton = v.findViewById(R.id.btn_registrarfg2);
        registerButton.setOnClickListener(
                view -> registerUserIfValid(v)

        );
    }
    private void setupSpinners() {
        // Configurar Spinner de Género
        ArrayAdapter<CharSequence> adapterGenero = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.gender_array,
                android.R.layout.simple_spinner_item
        );
        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(adapterGenero);

        cargarProvincias();

        // Configurar Spinner de Localidad
        spinnerProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Asegúrate de que el objeto seleccionado sea de tipo Provincia
                Object selectedItem = spinnerProvincia.getSelectedItem();
                if (selectedItem instanceof Provincia) {
                    Provincia provinciaSeleccionada = (Provincia) selectedItem;
                    cargarLocalidades(provinciaSeleccionada.getId_provincia());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void cargarProvincias() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            ProvinciaDB provinciaDB = new ProvinciaDB(getActivity());
            listaProvincias = provinciaDB.obtenerProvincias();

            getActivity().runOnUiThread(() -> {
                if (listaProvincias != null && !listaProvincias.isEmpty()) {
                    ArrayAdapter<Provincia> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listaProvincias);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerProvincia.setAdapter(adapter);

                    // Después de cargar las provincias, mantenemos la provincia previamente seleccionada
                } else {
                    Toast.makeText(getActivity(), "No se encontraron provincias.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void cargarLocalidades(int idProvincia) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            LocalidadDB localidadDB = new LocalidadDB(getActivity());
            listaLocalidades = localidadDB.obtenerLocalidades(idProvincia);

            getActivity().runOnUiThread(() -> {
                if (listaLocalidades != null && !listaLocalidades.isEmpty()) {
                    ArrayAdapter<Localidad> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listaLocalidades);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLocalidad.setAdapter(adapter);

                    // Actualizamos el spinner de localidad solo después de obtener las localidades
                } else {
                    Toast.makeText(getActivity(), "No se encontraron localidades para esta provincia.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private java.util.Date convertirStringADate(String fechaString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.parse(fechaString);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error al formatear la fecha", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void registerUserIfValid(View v) {
        String nombrex = nombre.getText().toString().trim();
        String apellidox = apellido.getText().toString().trim();
        String fechaNacimientoString = etFechaNacimiento.getText().toString().trim();
        String generoSeleccionado = spinnerGenero.getSelectedItem().toString();
        Provincia provinciaSeleccionada = (Provincia) spinnerProvincia.getSelectedItem();
        Localidad localidadSeleccionada = (Localidad) spinnerLocalidad.getSelectedItem();

        if (nombrex.isEmpty() || apellidox.isEmpty() || fechaNacimientoString.isEmpty() ||
                generoSeleccionado.isEmpty() || provinciaSeleccionada == null || localidadSeleccionada == null) {
            Toast.makeText(getActivity(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(SessionManager.getUserEmail(getActivity()));
        usuario.setFecha_nac(convertirStringADate(fechaNacimientoString));
        usuario.setApellido_usuario(apellidox);
        usuario.setNombre_usuario(nombrex);
        usuario.setGenero(generoSeleccionado);
        usuario.setProvincia(provinciaSeleccionada);
        usuario.setLocalidad(localidadSeleccionada);

        // Llamamos a la función para agregar el usuario a la base de datos
        DataUsuario dataUsuario = new DataUsuario(getContext());
        dataUsuario.agregarUsuario(usuario, success -> {
            if (success) {
                Toast.makeText(getActivity(), "Datos del usuario registrados correctamente", Toast.LENGTH_SHORT).show();
                pasarASiguientePantalla();
            } else {
                Toast.makeText(getActivity(), "Error al registrar datos del usuario.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pasarASiguientePantalla() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frgment_frame, new LoginFragment());
        fragmentTransaction.commit();
    }

    InputFilter letrasYEspaciosFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && !source.toString().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                return ""; // Si el texto no coincide con la expresión regular, no se permite.
            }
            return null; // Si coincide, se permite.
        }
    };

}
