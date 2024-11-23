package frgp.utn.edu.com.ui.proyeccion;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.OnBackPressedCallback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import frgp.utn.edu.com.R;

import frgp.utn.edu.com.conexion.DataUsuario;
import frgp.utn.edu.com.conexion.ElectroDB;
import frgp.utn.edu.com.conexion.ElectrodomesticoDB;
import frgp.utn.edu.com.conexion.UsuarioElectrodomesticoDB;
import frgp.utn.edu.com.entidad.Electrodomestico;
import frgp.utn.edu.com.entidad.Usuario;
import frgp.utn.edu.com.entidad.UsuarioElectrodomestico;
import frgp.utn.edu.com.ui.electrodomesticos.MisElectrodomesticosFragment;

import frgp.utn.edu.com.utils.SessionManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import frgp.utn.edu.com.ui.home.PantallaPrincipalFragment;


public class ProyeccionFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsuarioElectrodomesticoProyeccionAdapter adapter;
    private ArrayList<UsuarioElectrodomestico> listaElectrodomesticos;
    private int usuarioId = -1;
    private EditText etPotencia,textInput;
    private TextView txtResultado;
    private ElectrodomesticoDB electrodomesticoDB;
    private float consu;
    @Nullable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        electrodomesticoDB = new ElectrodomesticoDB(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proyeccion, container, false);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));

        etPotencia = view.findViewById(R.id.etPotencia);
        txtResultado = view.findViewById(R.id.Resultadoc);


        recyclerView = view.findViewById(R.id.rvElectrodomesticoskwh);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));




        listaElectrodomesticos = new ArrayList<>();
        adapter = new UsuarioElectrodomesticoProyeccionAdapter(listaElectrodomesticos);
        recyclerView.setAdapter(adapter);

        obtenerUsuarioId(idUsuario -> {
            usuarioId = idUsuario;
            cargarElectrodomesticos();
            textInput = view.findViewById(R.id.searchInput);

            textInput.addTextChangedListener(new TextWatcher() {
                //numeros
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    cargarElectrodomesticosbyText(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });
        });

        configurarBotones(view);
        return view;
    }

    private void configurarBotones(View view) {
        Button btnCalcular = view.findViewById(R.id.btnCalcular);
        Button btnLimpiar = view.findViewById(R.id.btnLimpiar);

        btnCalcular.setOnClickListener(v -> calcularConsumoTotal(listaElectrodomesticos));
        btnLimpiar.setOnClickListener(v -> limpiarConsumo());
    }

    private void calcularConsumo() {
        if (usuarioId == -1) {
            Toast.makeText(getActivity(), "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (listaElectrodomesticos.isEmpty()) {
            Toast.makeText(getActivity(), "No hay electrodomésticos registrados.", Toast.LENGTH_SHORT).show();
            return;
        }

        String potenciaStr = etPotencia.getText().toString().trim();
        if (potenciaStr.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor, ingresa el costo por KWh.", Toast.LENGTH_SHORT).show();
            return;
        }

        float potencia = Float.parseFloat(potenciaStr);
        float consumoTotal = 0;

        for (UsuarioElectrodomestico electrodomesticox: listaElectrodomesticos) {
            ElectroDB electroDB = new ElectroDB(getActivity());

              electroDB.obtenerElectrodomesticoPorIdAsync(electrodomesticox.getElectrodomesticoId(), new ElectroDB.ElectrodomesticoCallback() {
                @Override
                public void onElectrodomesticoObtenido(Electrodomestico electrodomestico) {
                    // Mostrar los detalles del electrodoméstico
                    /*System.out.println("Nombre: " + electrodomestico.getNombre());
                    System.out.println("Potencia: " + electrodomestico.getPotenciaPromedioWatts());
                    System.out.println("Consumo por hora: " + electrodomestico.getConsumoHoraWh());*/
                    consu += (electrodomestico.getConsumoHoraWh()/1000 )* electrodomesticox.getCantidad() * electrodomesticox.getHoras() * electrodomesticox.getDias();
                    //calcularConsumoTotal(electod);
                    guardarConsumoEnPreferencias(consu);


                }


                @Override
                public void onError(Exception e) {
                    // Manejar errores
                    e.printStackTrace();
                    System.out.println("Error: " + e.getMessage());
                }
            });


        }



        guardarConsumoEnPreferencias(consumoTotal);

    }

    public void calcularConsumoTotal(List<UsuarioElectrodomestico> listaElectrodomesticos) {
        // Variable para rastrear el consumo total
        final AtomicInteger consumoTotal = new AtomicInteger(0);
        final CountDownLatch latch = new CountDownLatch(listaElectrodomesticos.size());

        for (UsuarioElectrodomestico electrodomesticox : listaElectrodomesticos) {
            ElectroDB electroDB = new ElectroDB(getActivity());

            electroDB.obtenerElectrodomesticoPorIdAsync(electrodomesticox.getElectrodomesticoId(), new ElectroDB.ElectrodomesticoCallback() {
                @Override
                public void onElectrodomesticoObtenido(Electrodomestico electrodomestico) {
                    // Actualizar el consumo total de forma segura
                    int consumoCalculado = electrodomestico.getConsumoHoraWh() * electrodomesticox.getCantidad() * electrodomesticox.getHoras() * electrodomesticox.getDias();
                    consumoTotal.addAndGet(consumoCalculado);

                    latch.countDown();

                }

                @Override
                public void onError(Exception e) {
                    // Manejar errores
                    e.printStackTrace();
                    System.out.println("Error: " + e.getMessage());

                    // También disminuir el contador en caso de error
                    latch.countDown();
                }
            });

        }

        // Esperar hasta que todas las tareas asíncronas hayan terminado
        new Thread(() -> {
            try {
                latch.await(); // Bloquear hasta que todas las tareas terminen
                getActivity().runOnUiThread(() -> {
                    // Aquí puedes usar el valor final de consumoTotal
                    System.out.println("Consumo total: " + consumoTotal.get());
                    txtResultado.setText("El costo aproximado es: $" + (consumoTotal.get()/1000)*Float.parseFloat(etPotencia.getText().toString().trim()));
                    // Actualizar la UI si es necesario
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        String potenciaStr = etPotencia.getText().toString().trim();
        if (potenciaStr.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor, ingresa el costo por KWh.", Toast.LENGTH_SHORT).show();
            return;
        }


    }

    private void limpiarConsumo() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("consumo", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("consumo", 0.0f);
        editor.apply();
        txtResultado.setText("El costo aproximado es: $0");
    }

    private void guardarConsumoEnPreferencias(float consumo) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("consumo", 0);
        SharedPreferences.Editor editor = preferences.edit();
        float consumoAnterior = preferences.getFloat("consumo", 0.0f) ;
        float consumoTotal = consumoAnterior + consumo;
        editor.putFloat("consumo", consumoTotal);
        editor.apply();
    }

    private void cargarElectrodomesticos() {
        if (usuarioId != -1) {
            UsuarioElectrodomesticoDB db = new UsuarioElectrodomesticoDB(getActivity());
            db.obtenerElectrodomesticosPorUsuario(usuarioId, new UsuarioElectrodomesticoDB.CallbackElectrodomesticos() {
                @Override
                public void onComplete(ArrayList<UsuarioElectrodomestico> electrodomesticos) {
                    if (electrodomesticos != null) {
                        listaElectrodomesticos.clear();
                        listaElectrodomesticos.addAll(electrodomesticos);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "No se encontraron electrodomésticos.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), "Error al obtener el usuario.", Toast.LENGTH_SHORT).show();
        }
    }
    private void cargarElectrodomesticosbyText(String text) {

        if (usuarioId != -1&& !Objects.equals(text, "")) {
            UsuarioElectrodomesticoDB db = new UsuarioElectrodomesticoDB(getActivity());
            db.obtenerElectrodomesticosPorUsuario(usuarioId, new UsuarioElectrodomesticoDB.CallbackElectrodomesticos() {
                @Override
                public void onComplete(ArrayList<UsuarioElectrodomestico> electrodomesticos) {
                    if (electrodomesticos != null) {

                        List<UsuarioElectrodomestico> filtrados = electrodomesticos.stream()
                                .filter(e -> e.getDias()==Integer.parseInt(text) || e.getHoras()==Integer.parseInt(text) || e.getCantidad()==Integer.parseInt(text))
                                .collect(Collectors.toList());

                        listaElectrodomesticos.clear();
                        listaElectrodomesticos.addAll(filtrados);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "No se encontraron electrodomésticos.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), "Error al obtener el usuario.", Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerUsuarioId(CallbackUsuarioId callback) {
        String userEmail = SessionManager.getUserEmail(getActivity());
        DataUsuario dataUsuario = new DataUsuario(getActivity());
        dataUsuario.obtenerUsuarioPorEmail(userEmail, new DataUsuario.CallbackUsuario() {
            @Override
            public void onComplete(Usuario usuario) {
                if (usuario != null) {
                    callback.onIdUsuarioObtenido(usuario.getIdUsuario());
                } else {
                    callback.onIdUsuarioObtenido(-1);
                }
            }
        });
    }

    public interface CallbackUsuarioId {
        void onIdUsuarioObtenido(int idUsuario);
    }
}
