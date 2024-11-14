package frgp.utn.edu.com.ui.Login;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import com.google.firebase.auth.FirebaseUser;
import frgp.utn.edu.com.MainActivity;

import frgp.utn.edu.com.R;

import frgp.utn.edu.com.interfaces.OnMainMenuNavigatorListener;
import frgp.utn.edu.com.ui.usuario.PantallaPrincipalActivity;
import frgp.utn.edu.com.ui.usuario.PantallaPrincipalFragment;
import frgp.utn.edu.com.utils.SessionManager;
import frgp.utn.edu.com.viewmodel.LoginRegisterViewModel;

public class LoginFragment extends Fragment {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton, registerButton;
    public static final String TAG = LoginFragment.class.getSimpleName();
    private LoginRegisterViewModel loginRegisterViewModel;

    private NavController navController;
    private MainActivity myContext;
    public LoginFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
        loginRegisterViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    SessionManager.saveUserEmail(getContext(), firebaseUser.getEmail());
                    pasarASiguientePantalla();
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
                    loginRegisterViewModel.login(email, password);

                } else {
                    Toast.makeText(getContext(), "Email Address and Password Must Be Entered", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });
    }

    private void goToRegister() {
        Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_registerFragment);
    }

    private void pasarASiguientePantalla() {
        //pantallaprincipalfragment
        /*NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.fragment_pantalla_principal);*/
        ((MainActivity) getActivity() ).setnavigateToMainMenu(true);


        FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frgment_frame, new PantallaPrincipalFragment());
        fragmentTransaction.commit();

    }
}
