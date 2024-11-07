package frgp.utn.edu.com.ui.Login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.repository.AuthAppRepository;
import frgp.utn.edu.com.viewmodel.LoginRegisterViewModel;

public class RegisterFragment extends Fragment {
    private AuthAppRepository authAppRepository;
    private LoginRegisterViewModel loginRegisterViewModel;
    private Button nextButton;
    public static final String TAG = RegisterFragment.class.getSimpleName();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);

        /*loginRegisterViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    // Navegar a fragment_loginregister2.xml al detectar que el usuario está registrado
                    Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_loginRegisterFragment2);
                }
            }
        });*/
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_loginregister,container,false);
        initViews(view);
        return view;
    }
    private void initViews(View v) {
        authAppRepository = new AuthAppRepository(requireActivity().getApplication()); // Inicializa aquí
        nextButton = v.findViewById(R.id.next_button);
        nextButton.setOnClickListener(view -> validateInputs());

    }

    private void validateInputs() {
        String email = ((EditText) getView().findViewById(R.id.fragment_loginregister_email)).getText().toString().trim();
        String password = ((EditText) getView().findViewById(R.id.fragment_loginregister_password)).getText().toString().trim();
        String repeatPassword = ((EditText) getView().findViewById(R.id.fragment_loginregister_password_repeat)).getText().toString().trim();

        if (!isValidEmail(email)) {
            Toast.makeText(getContext(), "El correo debe tener el formato mail@dominio.com", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!arePasswordsValid(password, repeatPassword)) {
            Toast.makeText(getContext(), "Las contraseñas no son iguales o no cumplen el formato requerido", Toast.LENGTH_SHORT).show();
            return;
        }

        authAppRepository.register(email, password, new RegistrationCallback() {
            @Override
            public void onSuccess() {
                goToRegister2();
                Log.d("Register", "Registration successful");
            }

            @Override
            public void onFailure(String errorMessage) {

                Log.e("Register", "Registration failed: " + errorMessage);
            }
        });

    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private boolean arePasswordsValid(String password, String repeatPassword) {
        // Las contraseñas no coinciden
        if (!password.equals(repeatPassword)) {
            return false;
        }

        // Validar la complejidad de la contraseña
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!password.matches(passwordPattern)) {
            // La contraseña no cumple con los requisitos
            return false;
        }

        // La contraseña debe contener al menos 8 caracteres
        if (password.length() < 8) {
            return false;
        }

        return true;
    }



    private void goToLogin(){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        LoginFragment fragment = new LoginFragment();
        //ft.replace(R.id.activity_main_navHostFragment,fragment,LoginFragment.TAG);
        ft.commit();
    }

    private void goToRegister2() {
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.action_registerFragment_to_loginRegisterFragment2);
    }


}
