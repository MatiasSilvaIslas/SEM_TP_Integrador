package frgp.utn.edu.com.ui.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.databinding.FragmentMyaccountBinding;
import frgp.utn.edu.com.ui.Login.LoginFragment;
import frgp.utn.edu.com.viewmodel.LoginRegisterViewModel;


public class MyAccountFragment extends Fragment {

    private FragmentMyaccountBinding binding;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        binding = FragmentMyaccountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        ((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));
        Button logout = (Button)view.findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRegisterViewModel loginRegisterViewModel = new ViewModelProvider(getActivity()).get(LoginRegisterViewModel.class);
                loginRegisterViewModel.logout();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });



        /*TextView txtNombre = (TextView) view.findViewById(R.id.txtNombre);
        TextView txtCorreo = (TextView) view.findViewById(R.id.txtCorreo);
        User user = (User)getActivity().getIntent().getSerializableExtra(PutExtraConst.UserKey);
        txtNombre.setText(user.getName());
        txtCorreo.setText(user.getEmail());*/

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
