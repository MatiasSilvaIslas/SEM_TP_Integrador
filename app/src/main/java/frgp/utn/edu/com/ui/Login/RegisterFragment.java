package frgp.utn.edu.com.ui.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.firebase.auth.FirebaseUser;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.viewmodel.LoginRegisterViewModel;

public class RegisterFragment extends Fragment {
    private LoginRegisterViewModel loginRegisterViewModel;
    private Button loginButton;
    public static final String TAG = RegisterFragment.class.getSimpleName();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_loginregister2,container,false);
        initViews(view);
        return view;
    }
    private void initViews(View v) {


        loginButton = (Button) v.findViewById(R.id.bt_login);



        loginButton.setOnClickListener(view -> goToLogin());

    }
    private void goToLogin(){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        LoginFragment fragment = new LoginFragment();
        ft.replace(R.id.activity_main_navHostFragment,fragment,LoginFragment.TAG);
        ft.commit();
    }


}
