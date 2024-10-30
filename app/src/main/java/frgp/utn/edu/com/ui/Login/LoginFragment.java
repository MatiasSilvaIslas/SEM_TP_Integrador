package frgp.utn.edu.com.ui.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.firebase.auth.FirebaseUser;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.viewmodel.LoginRegisterViewModel;

public class LoginFragment extends Fragment {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton, registerButton;
    public static final String TAG = LoginFragment.class.getSimpleName();
    private LoginRegisterViewModel loginRegisterViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);

        loginRegisterViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Navigation.findNavController(getView()).navigate(R.id.action_loginRegisterFragment_to_loggedInFragment);
                }
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loginregister, container, false);
        initViews(view);
        emailEditText = view.findViewById(R.id.fragment_loginregister_email);
        passwordEditText = view.findViewById(R.id.fragment_loginregister_password);
        loginButton = view.findViewById(R.id.loginbtnvw);
        registerButton = view.findViewById(R.id.fragment_registervw);
        loginButton.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (email.length() > 0 && password.length() > 0) {
                    loginRegisterViewModel.login(email, password);
                } else {
                    Toast.makeText(getContext(), "Email Address and Password Must Be Entered", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return view;

    }
    private void initViews(View v) {


        registerButton = (Button) v.findViewById(R.id.fragment_registervw);



        registerButton.setOnClickListener(view -> goToRegister());

    }
    private void goToRegister(){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        RegisterFragment fragment = new RegisterFragment();
        ft.replace(R.id.activity_main_navHostFragment,fragment,RegisterFragment.TAG);
        ft.commit();
    }
/*    private void initViews(View v) {

        mEtEmail = (EditText) v.findViewById(R.id.et_email);
        mEtPassword = (EditText) v.findViewById(R.id.et_password);
        mBtLogin = (Button) v.findViewById(R.id.btn_login);
        mTiEmail = (TextInputLayout) v.findViewById(R.id.ti_email);
        mTiPassword = (TextInputLayout) v.findViewById(R.id.ti_password);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress);
        mTvRegister = (TextView) v.findViewById(R.id.tv_register);
        mTvForgotPassword = (TextView) v.findViewById(R.id.tv_forgot_password);

        mBtLogin.setOnClickListener(view -> login());
        mTvRegister.setOnClickListener(view -> goToRegister());
        mTvForgotPassword.setOnClickListener(view -> showDialog());
    }
    private void goToRegister(){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        RegisterFragment fragment = new RegisterFragment();
        ft.replace(R.id.fragmentFrame,fragment,RegisterFragment.TAG);
        ft.commit();
    }*/
    }
