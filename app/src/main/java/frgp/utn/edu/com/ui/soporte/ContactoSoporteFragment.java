package frgp.utn.edu.com.ui.soporte;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.DataDB;
import frgp.utn.edu.com.conexion.SoporteDB;
import frgp.utn.edu.com.servicio.EmailService;
import frgp.utn.edu.com.ui.back.PantallaPrincipalActivity;
import frgp.utn.edu.com.ui.electrodomesticos.CalculoConsumoFragment;
import frgp.utn.edu.com.ui.home.PantallaPrincipalFragment;
import frgp.utn.edu.com.utils.SessionManager;

public class ContactoSoporteFragment extends Fragment {
    private EditText editCasoResumen, editCasoDetalle;
    private Button btnEnviarSoporte;
    private SoporteDB soporteDB;
    private ProgressDialog progressDialog;
    private static final String TAG = "ContactoSoporteFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacto_soporte, container, false);

        // Configura la toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> {
            // Navega a PantallaPrincipalFragment
          /*  FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.frgment_frame, new PantallaPrincipalFragment());
            transaction.commit(); */
        });

        // Habilita el botón de retroceso
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Configurar el MenuProvider
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.activity_main_menu_drawer, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == android.R.id.home) {
                    // Navega a PantallaPrincipalFragment
                    FragmentTransaction transaction = requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction();
                    transaction.replace(R.id.frgment_frame, new PantallaPrincipalFragment());
                    transaction.commit();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Debe iniciar sesión primero",
                    Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
            return view;
        }

        initializeViews(view);
        setupButton();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_menu_drawer, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Navega a PantallaPrincipalFragment
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.frgment_frame, new PantallaPrincipalFragment());
            transaction.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeViews(View view) {
        editCasoResumen = view.findViewById(R.id.editCasoResumen);
        editCasoDetalle = view.findViewById(R.id.editCasoDetalle);
        btnEnviarSoporte = view.findViewById(R.id.btnEnviarSoporte);
        soporteDB = new SoporteDB(requireContext());

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Procesando solicitud...");
        progressDialog.setCancelable(false);
    }

    private void setupButton() {
        btnEnviarSoporte.setOnClickListener(v -> {
            String resumen = editCasoResumen.getText().toString().trim();
            String detalle = editCasoDetalle.getText().toString().trim();

            if (resumen.isEmpty() || detalle.isEmpty()) {
                Toast.makeText(requireContext(), "Complete todos los campos",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String userEmail = obtenerEmailUsuario();
            if (userEmail != null) {
                procesarCasoSoporte(userEmail, resumen, detalle);
            } else {
                Toast.makeText(requireContext(), "No se pudo obtener el email del usuario",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String obtenerEmailUsuario() {
        String userEmail = SessionManager.getUserEmail(requireContext());
        if (userEmail == null) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                userEmail = currentUser.getEmail();
                SessionManager.saveUserEmail(requireContext(), userEmail);
            }
        }
        return userEmail;
    }

    private void procesarCasoSoporte(String email, String resumen, String detalle) {
        progressDialog.show();
        btnEnviarSoporte.setEnabled(false);

        soporteDB.obtenerIdUsuario(email, new SoporteDB.IdUsuarioCallback() {
            @Override
            public void onIdUsuarioObtenido(int idUsuario) {
                if (idUsuario != -1) {
                    soporteDB.insertarCasoSoporte(idUsuario, resumen, detalle, new SoporteDB.OperacionCallback() {
                        @Override
                        public void onExito() {
                            EmailService.enviarNotificacionNuevoCaso(
                                    resumen,
                                    detalle,
                                    email,
                                    new EmailService.EmailCallback() {
                                        @Override
                                        public void onSuccess() {
                                            finalizarProceso("Caso registrado exitosamente");
                                        }

                                        @Override
                                        public void onError(String error) {
                                            Log.w("ContactoSoporte", "Caso guardado pero " + error);
                                            finalizarProceso("Caso registrado, pero hubo un problema al enviar la notificación");
                                        }
                                    }
                            );
                        }

                        @Override
                        public void onError(String mensaje) {
                            manejarError(mensaje);
                        }
                    });
                } else {
                    manejarError("No se encontró el usuario en la base de datos");
                }
            }

            @Override
            public void onError(String mensaje) {
                manejarError(mensaje);
            }
        });
    }

    private void manejarError(String mensaje) {
        progressDialog.dismiss();
        btnEnviarSoporte.setEnabled(true);
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    private void finalizarProceso(String mensaje) {
        progressDialog.dismiss();
        btnEnviarSoporte.setEnabled(true);
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();

        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.frgment_frame, new PantallaPrincipalFragment());
        transaction.commit();
    }
}