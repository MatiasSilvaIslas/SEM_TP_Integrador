package frgp.utn.edu.com.ui.myaccount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
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
import frgp.utn.edu.com.ui.home.PantallaPrincipalFragment;
import frgp.utn.edu.com.ui.usuario.EditarCredencialesActivity;
import frgp.utn.edu.com.ui.usuario.EditarPerfilFragment;
import frgp.utn.edu.com.viewmodel.LoginRegisterViewModel;


public class fragmentMiPerfil extends Fragment {

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_mi_perfil, container, false);
            ((AppCompatActivity) getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));
            initViews(view);
            return view;
        }

        public void initViews(View view) {

            // Inicializar los botones
            Button btnModificarPerfil = view.findViewById(R.id.btnModificarPerfil);
            Button btnCambiarContraseña = view.findViewById(R.id.btnCambiarContraseña);
            Button btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

            btnModificarPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navegar al fragmento de modificar perfil
                    if (getActivity() != null) {
                        ((MainActivity) getActivity()).setnavigateToMainMenu(true);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frgment_frame, new EditarPerfilFragment());
                        fragmentTransaction.commit();
                    }
                }
            });

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

            btnCambiarContraseña.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navegar al fragmento de cambiar contraseña
                    if (getActivity() != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frgment_frame, new EditarPasswordFragment());
                        fragmentTransaction.commit();
                    }
                }
            });

            btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Cerrar Sesión")
                                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Lógica para cerrar sesión
                                        LoginRegisterViewModel loginRegisterViewModel = new LoginRegisterViewModel(getActivity().getApplication());
                                        loginRegisterViewModel.logout(getContext().getApplicationContext());

                                        // Reemplazar con el fragmento de login
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                        // Configurar transiciones opcionales (esto es solo estético)
                                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

                                        // Reemplazar fragmento
                                        fragmentTransaction.replace(R.id.frgment_frame, new LoginFragment());
                                        fragmentTransaction.commit();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    } else {
                        Log.e("fragmentMiPerfil", "Error: getActivity() es null, no se puede cerrar sesión.");
                    }
                }
            });
        }
}

