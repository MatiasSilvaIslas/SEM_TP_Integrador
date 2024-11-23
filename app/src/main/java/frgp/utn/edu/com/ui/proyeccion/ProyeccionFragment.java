package frgp.utn.edu.com.ui.proyeccion;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.concurrent.atomic.AtomicInteger;

import frgp.utn.edu.com.ui.home.PantallaPrincipalFragment;


public class ProyeccionFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsuarioElectrodomesticoProyeccionAdapter adapter;
    private ArrayList<UsuarioElectrodomestico> listaElectrodomesticos;
    private int usuarioId=-1 ;
    EditText etPotencia;
    private float totalKwh =0;
    TextView txtResultado;
    ElectrodomesticoDB electrodomesticoDB ;


    @Nullable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        electrodomesticoDB = new ElectrodomesticoDB(getActivity());



    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proyeccion, container, false);
        ((AppCompatActivity) getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));

        etPotencia = view.findViewById(R.id.etPotencia);
         txtResultado = view.findViewById(R.id.Resultadoc);
        recyclerView = view.findViewById(R.id.rvElectrodomesticoskwh);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listaElectrodomesticos = new ArrayList<>();
        adapter = new UsuarioElectrodomesticoProyeccionAdapter(listaElectrodomesticos);


        recyclerView.setAdapter(adapter);
        obtenerUsuarioId(idUsuario -> {
            this.usuarioId = idUsuario;

            cargarElectrodomesticos();
            calculosConsumo();
        });
        Button btnborrar = view.findViewById(R.id.btnLimpiar);
        btnborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("consumo", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("consumo", 0.0f);
                editor.apply();
                txtResultado.setText("El costo aproximado es: $0");
            }
        });


        return view;
    }

    private void calculosConsumo() {


        if (usuarioId != -1) {

        etPotencia = getView().findViewById(R.id.etPotencia);
        Button btnCalcular = getView().findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean paso = false;
                for (UsuarioElectrodomestico electrodomestico : listaElectrodomesticos) {

                    int totalPendientes = listaElectrodomesticos.size();
                    AtomicInteger pendientes = new AtomicInteger(totalPendientes); // Contador seguro para hilos

                    if (electrodomestico != null) {

                        ElectroDB electroDB = new ElectroDB();
                        electroDB.getAllElectrodomesticos(new ElectroDB.ElectrodomesticosCallback() {
                            @Override
                            public void onSuccess(ArrayList<Electrodomestico> electrodomesticos) {
                                for (Electrodomestico electrodomestico1 : electrodomesticos) {

                                    if (electrodomestico1.getId_electrodomestico() == electrodomestico.getElectrodomesticoId()) {

                                        setherra(((float) electrodomestico1.getConsumoHoraWh() /1000)*electrodomestico.getHoras()*electrodomestico.getDias()*electrodomestico.getCantidad());
                                        System.out.println("sad "+((float) electrodomestico1.getConsumoHoraWh() /1000)*electrodomestico.getHoras()*electrodomestico.getDias()*electrodomestico.getCantidad());
                                    }

                                }


                                if (pendientes.decrementAndGet() == 0) {
                                    txtResultado.setText("El consumo total es: " + totalKwh + " Kwh");
                                }
                            }

                            private void setherra(float totalKwh) {
                                SharedPreferences preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("consumo", 0);
                                float tot=totalKwh + preferences.getFloat("consumo", 0.0f);
                                System.out.println("Consumo total: " + tot);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putFloat("consumo", tot);
                                editor.apply();
                            }


                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(getActivity(), "Error al obtener electrodomésticos.", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                    SharedPreferences preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("consumo", 0);
                    float totalKwhx = preferences.getFloat("consumo", 0.00f);
                    float potencia = Float.parseFloat(etPotencia.getText().toString());
                    txtResultado.setText("El costo aproximado es: $" + String.format("%.2f",(totalKwhx*potencia)) + " ");
                }
            }
            });
        }

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


    private void obtenerUsuarioId(MisElectrodomesticosFragment.CallbackUsuarioId callback) {
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




/*extends Fragment {
    Toolbar toolbar;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_proyeccion,container,false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));
        initViews(view);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navegar al Fragment deseado o realizar una acción personalizada
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frgment_frame, new PantallaPrincipalFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_menu_drawer, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initViews(View view) {
        EditText TxtHorasUso = view.findViewById(R.id.etHorasUso);
        EditText txtPotencia = view.findViewById(R.id.etPotencia);
        EditText txtCosto = view.findViewById(R.id.etDiasUso);
        TextView txtResultado = view.findViewById(R.id.Resultadoc);

        Button btnCalcular = view.findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double horasUso = Double.parseDouble(TxtHorasUso.getText().toString());
                double potencia = Double.parseDouble(txtPotencia.getText().toString());
                double costo = Double.parseDouble(txtCosto.getText().toString());

                double resultado = horasUso * potencia * costo;
                txtResultado.setText("El costo de uso es: $" + resultado);
            }
        });

    }
}
*/