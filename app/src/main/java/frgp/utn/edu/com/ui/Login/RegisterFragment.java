package frgp.utn.edu.com.ui.Login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.repository.AuthAppRepository;
import frgp.utn.edu.com.utils.SessionManager;
import frgp.utn.edu.com.viewmodel.LoginRegisterViewModel;

public class RegisterFragment extends Fragment {
    private AuthAppRepository authAppRepository;
    private LoginRegisterViewModel loginRegisterViewModel;
    private Button nextButton;
    private String email;
    public static final String TAG = RegisterFragment.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loginregister, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View v) {
        // Inicialización del repositorio y botones
        authAppRepository = new AuthAppRepository(requireActivity().getApplication());
        nextButton = v.findViewById(R.id.next_button);
        nextButton.setOnClickListener(view -> registerUserIfValid());

        // Inicializar el ícono de ayuda
        ImageView passwordHelpIcon = v.findViewById(R.id.password_help_icon);

        // Configurar el comportamiento para mostrar/ocultar el mensaje de ayuda
        passwordHelpIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear y mostrar un AlertDialog con los requisitos de la contraseña
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Requisitos de la contraseña")
                        .setMessage("La contraseña debe tener al menos:\n- 8 caracteres\n- 1 letra mayúscula\n- 1 letra minúscula\n- 1 número\n- 1 símbolo especial")
                        .setPositiveButton("Entendido", null)
                        .show();
            }
        });
    }


    private void registerUserIfValid() {
        email = getEmailInput();
        String password = getPasswordInput();
        String repeatPassword = getRepeatPasswordInput();

        // Validar los datos
        if (validateInputs(email, password, repeatPassword)) {
            // Si la validación es exitosa, proceder con el registro
            registerUser(email, password);
        }
    }

    private boolean validateInputs(String email, String password, String repeatPassword) {
        // Validar email
        if (!isValidEmail(email)) {
            showToast("El correo debe tener el formato mail@dominio.com");
            return false;
        }

        // Validar contraseñas
        if (!arePasswordsValid(password, repeatPassword)) {
            showToast("Las contraseñas no son iguales o no cumplen el formato requerido");
            return false;
        }

        return true;
    }

    private String getEmailInput() {
        return ((EditText) getView().findViewById(R.id.fragment_loginregister_email)).getText().toString().trim();
    }

    private String getPasswordInput() {
        return ((EditText) getView().findViewById(R.id.fragment_loginregister_password)).getText().toString().trim();
    }

    private String getRepeatPasswordInput() {
        return ((EditText) getView().findViewById(R.id.fragment_loginregister_password_repeat)).getText().toString().trim();
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void registerUser(String email, String password) {
        authAppRepository.register(email, password, new RegistrationCallback() {
            @Override
            public void onSuccess() {
                SessionManager.saveUserEmail(getContext(), email);
                goToRegister2(email);
                Log.d("Register", "Registration successful");
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("Register", "Registration failed: " + errorMessage);
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }

    private boolean arePasswordsValid(String password, String repeatPassword) {
        // Las contraseñas no coinciden
        if (!password.equals(repeatPassword)) {
            return false;
        }

        // Validar la complejidad de la contraseña
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordPattern);
    }


    private void goToLogin(){

        ((MainActivity) getActivity() ).setnavigateToMainMenu(true);
        FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frgment_frame, new LoginFragment());
        fragmentTransaction.commit();
    }

    private void goToRegister2(String email) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
//      NavController navController = Navigation.findNavController(getView());
//        navController.navigate(R.id.action_registerFragment_to_loginRegisterFragment2, bundle);
        ((MainActivity) getActivity() ).setnavigateToMainMenu(true);
        FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frgment_frame, new LoginRegisterFragment2());
        fragmentTransaction.commit();
    }



}
