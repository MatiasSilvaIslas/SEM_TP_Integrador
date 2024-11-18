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
import frgp.utn.edu.com.conexion.DataUsuario;
import frgp.utn.edu.com.databinding.FragmentMyaccountBinding;
import frgp.utn.edu.com.entidad.Usuario;
import frgp.utn.edu.com.ui.Login.LoginFragment;
import frgp.utn.edu.com.utils.SessionManager;
import frgp.utn.edu.com.viewmodel.LoginRegisterViewModel;


public class MyAccountFragment extends Fragment {

    private FragmentMyaccountBinding binding;

    private Usuario usuario;

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
                loginRegisterViewModel.logout(getContext());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });



        TextView txtNombre = (TextView) view.findViewById(R.id.txtNombre);
        TextView txtCorreo = (TextView) view.findViewById(R.id.txtCorreo);
        String email= SessionManager.getUserEmail(getActivity());
        DataUsuario dataUsuario = new DataUsuario(getActivity());
        dataUsuario.obtenerUsuarioPorEmail(email,usuario->{
            if(usuario!=null){
                this.usuario = usuario;
                txtCorreo.setText(usuario.getEmail());
                txtNombre.setText(usuario.getNombre_usuario());
            }else{
                Toast.makeText(getActivity(), "Error al obtener usuario", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
