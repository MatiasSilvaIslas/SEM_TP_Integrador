package frgp.utn.edu.com.ui.back;

import frgp.utn.edu.com.entidad.Usuario;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

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
import frgp.utn.edu.com.utils.InputFilterLetters;
import frgp.utn.edu.com.utils.SessionManager;

public class EditarPerfilActivity extends AppCompatActivity {
    private Usuario usuario;
    private EditText etNombre;
    private EditText etApellido;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        userEmail = SessionManager.getUserEmail(this);

        initViews();
        setupSpinners();

        cargarDatosUsuario();

        etFechaNacimiento = findViewById(R.id.et_fecha_nacimiento_edit);
        etFechaNacimiento.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        long todayInMillis = calendar.getTimeInMillis();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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


    private void initViews() {
        etNombre = findViewById(R.id.et_nombre_edit);
        etApellido = findViewById(R.id.et_apellido_edit);
        etFechaNacimiento = findViewById(R.id.et_fecha_nacimiento_edit);
        spinnerProvincia = findViewById(R.id.spinner_provincia_edit);
        spinnerLocalidad = findViewById(R.id.spinner_localidad_edit);
        spinnerGenero = findViewById(R.id.spinner_genero_edit);
        btnActualizar = findViewById(R.id.btn_guardar);

        InputFilterLetters inputFilterLetters = new InputFilterLetters();
        etNombre.setFilters(new InputFilter[]{inputFilterLetters});
        etApellido.setFilters(new InputFilter[]{inputFilterLetters});

        btnActualizar.setOnClickListener(v -> onActualizarClick());
    }

    private void setupSpinners() {
        // Configurar Spinner de Género
        ArrayAdapter<CharSequence> adapterGenero = ArrayAdapter.createFromResource(
                this,
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
            ProvinciaDB provinciaDB = new ProvinciaDB(this);
            listaProvincias = provinciaDB.obtenerProvincias();

            runOnUiThread(() -> {
                if (listaProvincias != null && !listaProvincias.isEmpty()) {
                    ArrayAdapter<Provincia> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaProvincias);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerProvincia.setAdapter(adapter);

                    // Después de cargar las provincias, mantenemos la provincia previamente seleccionada
                    if (provinciaBefore != null) {
                        setSpinnerProvincia(provinciaBefore); // Esto asegura que la provincia del usuario se mantiene
                        setSpinnerLocalidad(usuario.getLocalidad());
                    } else {
                        cargarLocalidades(listaProvincias.get(0).getId_provincia()); // Cargar localidades si no había provincia seleccionada previamente
                    }
                } else {
                    Toast.makeText(this, "No se encontraron provincias.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void cargarLocalidades(int idProvincia) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            LocalidadDB localidadDB = new LocalidadDB(this);
            listaLocalidades = localidadDB.obtenerLocalidades(idProvincia);

            runOnUiThread(() -> {
                if (listaLocalidades != null && !listaLocalidades.isEmpty()) {
                    ArrayAdapter<Localidad> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaLocalidades);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLocalidad.setAdapter(adapter);

                    // Actualizamos el spinner de localidad solo después de obtener las localidades
                    if (usuario != null) {
                        setSpinnerLocalidad(usuario.getLocalidad());  // Ya puedes acceder a usuario aquí
                    } else {
                        Toast.makeText(this, "Usuario no cargado correctamente", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "No se encontraron localidades para esta provincia.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void cargarDatosUsuario() {
        DataUsuario dataUsuario = new DataUsuario(this);
        dataUsuario.obtenerUsuarioPorEmail(userEmail, usuario -> {
            if (usuario != null) {
                this.usuario = usuario;  // Asigna el objeto usuario a la variable global

                etNombre.setText(usuario.getNombre_usuario());
                etApellido.setText(usuario.getApellido_usuario());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                etFechaNacimiento.setText(sdf.format(usuario.getFecha_nac()));


                ArrayAdapter<CharSequence> adapterGenero = ArrayAdapter.createFromResource(
                        this,
                        R.array.gender_array,
                        android.R.layout.simple_spinner_item
                );
                adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerGenero.setSelection(adapterGenero.getPosition(usuario.getGenero()));

                cargarProvincias();
                cargarLocalidades(usuario.getProvincia().getId_provincia());
                setSpinnerProvincia(usuario.getProvincia());
                setSpinnerLocalidad(usuario.getLocalidad()); // Ya puedes usar usuario aquí
            } else {
                Toast.makeText(this, "Error al cargar los datos del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpinnerProvincia(Provincia provincia) {
        provinciaBefore = usuario.getProvincia();
        for (int i = 0; i < listaProvincias.size(); i++) {
            if (listaProvincias.get(i).getId_provincia() == provincia.getId_provincia()) {
                spinnerProvincia.setSelection(i);
                break;
            }
        }
    }

    private void setSpinnerLocalidad(Localidad localidad) {
        localidadBefore = usuario.getLocalidad();
        if (listaLocalidades != null) {
            for (int i = 0; i < listaLocalidades.size(); i++) {
                if (listaLocalidades.get(i).getId_localidad() == localidad.getId_localidad()) {
                    spinnerLocalidad.setSelection(i);
                    break;
                }
            }
        } else {
            Toast.makeText(this, "La lista de localidades no está disponible.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onActualizarClick() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String generoSeleccionado = spinnerGenero.getSelectedItem().toString();
        Provincia provinciaSeleccionada = (Provincia) spinnerProvincia.getSelectedItem();
        Localidad localidadSeleccionada = (Localidad) spinnerLocalidad.getSelectedItem();
        Date fechaNacimiento = convertirStringADate(etFechaNacimiento.getText().toString().trim());

        usuario.setNombre_usuario(nombre);
        usuario.setApellido_usuario(apellido);
        usuario.setGenero(generoSeleccionado);
        usuario.setProvincia(provinciaSeleccionada);
        usuario.setLocalidad(localidadSeleccionada);
        usuario.setFecha_nac(fechaNacimiento);
        if (nombre.isEmpty() || apellido.isEmpty() || provinciaSeleccionada == null || localidadSeleccionada == null) {
            Toast.makeText(this, "Por favor complete todos los campos.", Toast.LENGTH_SHORT).show();
        } else {
            actualizarUsuario(usuario);
        }
    }

    private void actualizarUsuario(Usuario usuario) {
        DataUsuario dataUsuario = new DataUsuario(getApplicationContext());
        dataUsuario.updateUsuario(usuario, success -> {
            if (success) {
                Toast.makeText(getApplicationContext(), "Usuario modificado correctamente", Toast.LENGTH_SHORT).show();
                pasarASiguientePantalla();
            } else {
                Toast.makeText(getApplicationContext(), "Error al modificar usuario.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pasarASiguientePantalla() {
        Intent intent = new Intent(EditarPerfilActivity.this, PantallaPrincipalActivity.class);
        startActivity(intent);
    }

    private Date convertirStringADate(String fechaString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.parse(fechaString);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error al formatear la fecha", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
