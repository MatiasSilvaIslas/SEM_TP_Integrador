package frgp.utn.edu.com.ui.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.ui.Login.LoginFragment;
import frgp.utn.edu.com.ui.usuario.EditarCredencialesActivity;
import frgp.utn.edu.com.ui.usuario.EditarPerfilFragment;
import frgp.utn.edu.com.viewmodel.LoginRegisterViewModel;


public class fragmentMiPerfil extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mi_perfil, container, false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));
        initViews(view);
        return view;
    }

    public void initViews(View view) {

      Button  btnModificarPerfil = view.findViewById(R.id.btnModificarPerfil);
      Button  btnCambiarContraseña = view.findViewById(R.id.btnCambiarContraseña);
      Button  btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);


        btnModificarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity() ).setnavigateToMainMenu(true);
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frgment_frame, new EditarPerfilFragment());
                fragmentTransaction.commit();

            }
        });

        btnCambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity) getActivity() ).setnavigateToMainMenu(true);
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frgment_frame, new EditarPasswordFragment());
                fragmentTransaction.commit();
            }
        });


        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRegisterViewModel loginRegisterViewModel = new LoginRegisterViewModel(getActivity().getApplication());
                loginRegisterViewModel.logout(getContext().getApplicationContext());
                Fragment newFragment = new LoginFragment();
                //navigationView.setVisibility(View.GONE);
                //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frgment_frame, new LoginFragment());
            }
        });
    }
}
