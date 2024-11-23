package frgp.utn.edu.com.ui.myaccount;

import android.app.Application;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.entidad.Usuario;
import frgp.utn.edu.com.ui.home.PantallaPrincipalFragment;
import frgp.utn.edu.com.ui.usuario.EditarPerfilFragment;
import frgp.utn.edu.com.utils.InputFilterLetters;
import frgp.utn.edu.com.repository.AuthAppRepository; // Asegúrate de que este import sea correcto
import frgp.utn.edu.com.utils.SessionManager;

public class EditarPasswordFragment extends Fragment {

    private Usuario usuario;
    private EditText etPassword;
    private EditText etNuevaContraseña;
    private EditText etConfirmarContraseña;
    private Button btnActualizar;
    private AuthAppRepository authAppRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cambiar_contrasenia, container, false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));
        initViews(view);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navegar al Fragment deseado o realizar una acción personalizada
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frgment_frame, new fragmentMiPerfil())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    private void initViews(View view) {
        etPassword = view.findViewById(R.id.et_password);
        etNuevaContraseña = view.findViewById(R.id.et_nueva_contraseña);
        etConfirmarContraseña = view.findViewById(R.id.et_nueva_contraseña_2);

        btnActualizar = view.findViewById(R.id.btn_guardar);


        authAppRepository = new AuthAppRepository(getActivity().getApplication() );

        btnActualizar.setOnClickListener(v -> onActualizarClick());
    }

    private void onActualizarClick() {
        String oldPassword = etPassword.getText().toString();
        String newPassword = etNuevaContraseña.getText().toString();
        String confirmPassword = etConfirmarContraseña.getText().toString();

        // Validación de campos vacíos
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getContext(), "Por favor complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación de que la nueva contraseña no sea igual a la actual
        if (oldPassword.equals(newPassword)) {
            Toast.makeText(getContext(), "La nueva contraseña no puede ser igual a la actual.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación de formato de la nueva contraseña
        if (!esContraseñaValida(newPassword)) {
            Toast.makeText(getContext(), "La contraseña debe tener al menos 8 caracteres, una letra mayúscula, una letra minúscula, un número y un símbolo especial.", Toast.LENGTH_LONG).show();
            return;
        }

        // Validación de coincidencia entre nueva contraseña y confirmación
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Suponiendo que el email del usuario ya está disponible
        String email = SessionManager.getUserEmail(getContext()); // Asegúrate de obtener el email del usuario correctamente

        // Llamar al método changePassword
        authAppRepository.changePassword(
                email,
                oldPassword,
                newPassword,
                new AuthAppRepository.ChangePasswordCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(), "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show();
                        SessionManager.limpiarSesion(getContext());
                        SessionManager.saveUserPassword(getContext(), newPassword);
                        SessionManager.saveUserEmail(getContext(), email);

                        ((MainActivity) getActivity() ).setnavigateToMainMenu(true);
                        FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frgment_frame, new fragmentMiPerfil());
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private boolean esContraseñaValida(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(regex);
    }

}
