package frgp.utn.edu.com.ui.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.ui.home.PantallaPrincipalFragment;
import frgp.utn.edu.com.ui.usuario.EditarCredencialesActivity;
import frgp.utn.edu.com.ui.usuario.EditarPerfilFragment;


public class fragmentMiPerfil extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mi_perfil, container, false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));
        initViews(view);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navegar al Fragment deseado o realizar una acción personalizada
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frgment_frame, new PantallaPrincipalFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_menu_drawer, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void initViews(View view) {

      Button  btnModificarPerfil = view.findViewById(R.id.btnModificarPerfil);
      Button  btnCambiarContraseña = view.findViewById(R.id.btnCambiarContraseña);

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
    }
}
