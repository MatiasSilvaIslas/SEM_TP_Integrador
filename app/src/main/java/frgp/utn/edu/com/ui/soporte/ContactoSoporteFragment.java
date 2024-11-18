package frgp.utn.edu.com.ui.soporte;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

    private void initializeViews(View view) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));
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

        // Primero obtenemos el ID del usuario
        soporteDB.obtenerIdUsuario(email, new SoporteDB.IdUsuarioCallback() {
            @Override
            public void onIdUsuarioObtenido(int idUsuario) {
                if (idUsuario != -1) {
                    // Una vez que tenemos el ID, insertamos el caso
                    soporteDB.insertarCasoSoporte(idUsuario, resumen, detalle, new SoporteDB.OperacionCallback() {
                        @Override
                        public void onExito() {
                            // Enviar notificación por email
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
                                            // El caso se guardó pero hubo error en el email
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
        getParentFragmentManager().popBackStack(); // En lugar de finish()

        Intent intent = new Intent(requireContext(), PantallaPrincipalActivity.class);
        startActivity(intent);

        // Finaliza el fragmento actual si es necesario
        requireActivity().finish();
    }
}