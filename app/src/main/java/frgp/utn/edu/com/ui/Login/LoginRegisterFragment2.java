package frgp.utn.edu.com.ui.Login;

import android.app.DatePickerDialog;
import android.os.Bundle;
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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginRegisterFragment2 extends Fragment {
    private AuthAppRepository authAppRepository;
    private Button registerButton;
    private String email;
    private TextInputEditText etFechaNacimiento;
    private Spinner spinnerProvincia;
    private Spinner spinnerLocalidad;
    private Spinner spinnerGenero;
    private Button btnActualizar;

    private Provincia provinciaBefore;
    private Localidad localidadBefore;

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

        long todayInMillis = calendar.getTimeInMillis();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        etFechaNacimiento.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.getDatePicker().setMaxDate(todayInMillis);

        datePickerDialog.show();
    }

    public void initViews(View v) {

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



    private void setSpinnerProvincia(Provincia provincia) {
       // provinciaBefore = usuario.getProvincia();
        for (int i = 0; i < listaProvincias.size(); i++) {
            if (listaProvincias.get(i).getId_provincia() == provincia.getId_provincia()) {
                spinnerProvincia.setSelection(i);
                break;
            }
        }
    }

    private void setSpinnerLocalidad(Localidad localidad) {
        //localidadBefore = usuario.getLocalidad();
        if (listaLocalidades != null) {
            for (int i = 0; i < listaLocalidades.size(); i++) {
                if (listaLocalidades.get(i).getId_localidad() == localidad.getId_localidad()) {
                    spinnerLocalidad.setSelection(i);
                    break;
                }
            }
        } else {
            Toast.makeText(getActivity(), "La lista de localidades no está disponible.", Toast.LENGTH_SHORT).show();
        }
    }

    public void registerUserIfValid(View v) {
        Usuario usuario = new Usuario();
        TextView emailTextView = v.findViewById(R.id.fragment_loginregister_email);
        usuario.setEmail(emailTextView.getText().toString());
        TextView nombreTextView = v.findViewById(R.id.et_apellido);
        usuario.setNombre_usuario(nombreTextView.getText().toString());
        TextView apellidoTextView = v.findViewById(R.id.et_nombre);
        usuario.setApellido_usuario(apellidoTextView.getText().toString());
        TextView generoTextView = v.findViewById(R.id.spinner_genero);
        usuario.setGenero( generoTextView.getText().toString());


        TextView provinciaTextView = v.findViewById(R.id.spinner_provincia);


        ProvinciaDB provinciaDB = new ProvinciaDB(getContext());
        usuario.setProvincia(provinciaDB.obtenerProvincia(provinciaTextView.getText().toString()));



        LocalidadDB localidadDB = new LocalidadDB(getContext());
        TextView localidadTextView = v.findViewById(R.id.spinner_localidad);
        usuario.setLocalidad(localidadDB.obtenerLocalidad(localidadTextView.getText().toString()));

        TextView fechaNacTextView = v.findViewById(R.id.et_fecha_nacimiento);
        usuario.setFecha_nac(Date.valueOf(fechaNacTextView.getText().toString()));
        DataUsuario dataUsuario = new DataUsuario(getContext());
        dataUsuario.agregarUsuario(usuario, null);


        FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frgment_frame, new LoginFragment());
        fragmentTransaction.commit();

    }

}
