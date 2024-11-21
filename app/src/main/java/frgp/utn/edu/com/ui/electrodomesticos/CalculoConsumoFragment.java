package frgp.utn.edu.com.ui.electrodomesticos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.ElectrodomesticoDB;
import frgp.utn.edu.com.entidad.Categoria;
import frgp.utn.edu.com.entidad.Electrodomestico;
import frgp.utn.edu.com.ui.myaccount.fragmentMiPerfil;

import java.util.ArrayList;
import java.util.Locale;

public class CalculoConsumoFragment extends Fragment {
    private Spinner spinnerCategoria;
    private Spinner spinnerElectrodomesticos;
    private EditText editHorasUso, editDiasUso;
    private TextView textConsumoEstimado;
    private ElectrodomesticoDB electrodomesticoDB;
    private ArrayList<Categoria> listaCategorias;
    private ArrayList<Electrodomestico> listaElectrodomesticos;
    Button btnCalcular;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar la vista
        View view = inflater.inflate(R.layout.fragment_calculo_consumo, container, false);
        ((AppCompatActivity) getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));

        // Inicializar vistas
        spinnerCategoria = view.findViewById(R.id.spinnerCategoria);
        spinnerElectrodomesticos = view.findViewById(R.id.spinnerElectrodomesticos);
        editHorasUso = view.findViewById(R.id.editHorasUso);
        editDiasUso = view.findViewById(R.id.editDiasUso);
        textConsumoEstimado = view.findViewById(R.id.textConsumoEstimado);
        btnCalcular = view.findViewById(R.id.btnCalcular);

        // Inicializar base de datos
        electrodomesticoDB = new ElectrodomesticoDB(getContext());

        //Acá me voy para el perfil del usurio
        ImageView btnProfile = view.findViewById(R.id.icon_user);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity() ).setnavigateToMainMenu(true);
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frgment_frame, new fragmentMiPerfil());
                fragmentTransaction.commit();
            }
        });
        // Retornar la vista de fragmento
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Paso 1: Cargar categorías en el spinner
        electrodomesticoDB.obtenerCategoriasAsync(categorias -> {
            listaCategorias = categorias;

            // Configurar el adaptador del spinner de categorías
            if (getContext() != null) {
                ArrayAdapter<Categoria> adapterCategoria = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categorias);
                adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategoria.setAdapter(adapterCategoria);
            }
        });

        // Paso 2: Manejar selección de categoría
        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Categoria categoriaSeleccionada = (Categoria) parent.getItemAtPosition(position);

                // Cargar electrodomésticos según la categoría seleccionada
                electrodomesticoDB.obtenerElectrodomesticosAsync(categoriaSeleccionada.getId_categoria(), electrodomesticos -> {
                    listaElectrodomesticos = electrodomesticos;

                    // Configurar el adaptador del spinner de electrodomésticos
                    if (getContext() != null) {
                        ArrayAdapter<Electrodomestico> adapterElectrodomestico = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, electrodomesticos);
                        adapterElectrodomestico.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerElectrodomesticos.setAdapter(adapterElectrodomestico);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Manejar caso donde no hay categoría seleccionada
                if (getContext() != null) {
                    ArrayAdapter<Electrodomestico> emptyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
                    emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerElectrodomesticos.setAdapter(emptyAdapter);
                }
            }
        });

        // Configurar botón para calcular el consumo estimado
        btnCalcular.setOnClickListener(v -> {
            // Validar que el Spinner tenga un elemento seleccionado
            if (spinnerElectrodomesticos.getSelectedItem() == null) {
                Toast.makeText(getContext(), "Selecciona un electrodoméstico", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener el electrodoméstico seleccionado
            Electrodomestico selected = (Electrodomestico) spinnerElectrodomesticos.getSelectedItem();

            // Verificar si los campos están vacíos
            String horasText = editHorasUso.getText().toString().trim();
            String diasText = editDiasUso.getText().toString().trim();

            if (horasText.isEmpty() || diasText.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Convertir valores a enteros
                int horasPorDia = Integer.parseInt(horasText);
                int dias = Integer.parseInt(diasText);

                // Calcular consumo
                double consumoTotal = (selected.getPotenciaPromedioWatts() / 1000.0) * horasPorDia * dias;

                // Formatear el resultado con un decimal
                String consumoFormateado = String.format(Locale.getDefault(), "%.1f", consumoTotal);

                textConsumoEstimado.setText("El consumo estimado es: " + consumoFormateado + " kWh en " + dias + " días.");
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Entrada inválida. Ingresa solo números.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
