package frgp.utn.edu.com.ui.Login;


import static frgp.utn.edu.com.ui.Login.RegisterFragment.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import frgp.utn.edu.com.MainActivity;

import frgp.utn.edu.com.R;

import frgp.utn.edu.com.conexion.DataUsuario;
import frgp.utn.edu.com.ui.home.PantallaPrincipalFragment;
import frgp.utn.edu.com.utils.SessionManager;
import frgp.utn.edu.com.viewmodel.LoginRegisterViewModel;

public class LoginFragment extends Fragment {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton, registerButton;
    private LoginRegisterViewModel loginRegisterViewModel;

    public LoginFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);

        // Observamos el LiveData del usuario de Firebase
        loginRegisterViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (getActivity() != null) {
                    if (firebaseUser != null) {
                        String email = firebaseUser.getEmail();
                        String password = passwordEditText.getText().toString();

                        // Verificar si el correo está registrado en MySQL
                        DataUsuario dataUsuario = new DataUsuario(getContext());
                        dataUsuario.verificarEmail(email, resultado -> {
                            if (resultado == 1) {
                                // Si el email existe en MySQL, pasar a la pantalla principal
                                SessionManager.saveUserEmail(getActivity(), email);
                                SessionManager.saveUserPassword(getActivity(), password);
                                pasarPantallaInicio();
                            } else {
                                // Si el email no existe, redirigir al fragmento para completar los datos
                                SessionManager.saveUserEmail(getActivity(), email);
                                pasarPantallaDatosPersonales();
                            }
                        });
                    } else {
                        // Si el login con Firebase falla, mostrar un mensaje de error
                        Toast.makeText(getContext(), "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Actividad es null, no se puede navegar.");
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        emailEditText = view.findViewById(R.id.et_email);
        passwordEditText = view.findViewById(R.id.et_password);
        loginButton = view.findViewById(R.id.btn_login);
        registerButton = view.findViewById(R.id.btn_registrar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {
                    // Intentamos hacer login con Firebase
                    loginRegisterViewModel.login(email, password);
                } else {
                    Toast.makeText(getContext(), "Por favor ingresar dirección de correo electrónico y contraseña.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });

        // Agregar evento para "Olvidé mi contraseña"
        TextView forgotPasswordTextView = view.findViewById(R.id.tv_forgot_password);
        forgotPasswordTextView.setOnClickListener(v -> showResetPasswordDialog());
    }

    private void showResetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Restablecer Contraseña");

        final EditText emailInput = new EditText(getContext());
        emailInput.setHint("Ingresa tu correo electrónico");
        emailInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        emailInput.setLayoutParams(params);

        builder.setView(emailInput);

        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailInput.getText().toString().trim();
                if (isValidEmail(email)) {
                    sendPasswordResetEmail(email);
                } else {
                    Toast.makeText(getContext(), "Por favor ingresa un correo válido.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", null);

        builder.create().show();
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void sendPasswordResetEmail(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Correo de restablecimiento enviado.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "No se pudo enviar el correo. Intenta nuevamente.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToRegister() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frgment_frame, new RegisterFragment());
        fragmentTransaction.commit();
    }

    private void pasarPantallaInicio() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frgment_frame, new PantallaPrincipalFragment());
        fragmentTransaction.commit();
    }

    private void pasarPantallaDatosPersonales() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frgment_frame, new LoginRegisterFragment2());
        fragmentTransaction.commit();
    }
}
