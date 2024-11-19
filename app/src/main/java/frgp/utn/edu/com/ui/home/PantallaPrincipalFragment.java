package frgp.utn.edu.com.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import frgp.utn.edu.com.conexion.DataUsuario;
import frgp.utn.edu.com.entidad.Usuario;
import frgp.utn.edu.com.notifications.NotificacionesActivity;
import frgp.utn.edu.com.ui.informes.InformesFragment;
import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.ui.back.ABMLElectrodomesticosActivity;
import frgp.utn.edu.com.ui.electrodomesticos.CalculoConsumoFragment;
import frgp.utn.edu.com.ui.myaccount.fragmentMiPerfil;
import frgp.utn.edu.com.ui.proyeccion.ProyeccionFragment;
import frgp.utn.edu.com.ui.soporte.ContactoSoporteFragment;
import frgp.utn.edu.com.utils.SessionManager;


public class PantallaPrincipalFragment extends Fragment {
    Toolbar toolbar;
    Usuario usuario;
    TextView txtNombre;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pantalla_principal,container,false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));
        initViews(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_menu_drawer, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }




    public void initViews(View view)
    {

        txtNombre = view.findViewById(R.id.textWelcome);
        getUserName();
        Button btnProfile = view.findViewById(R.id.btnProfile);
        Button btnManageAppliances = view.findViewById(R.id.btnManageAppliances);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity() ).setnavigateToMainMenu(true);
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frgment_frame, new fragmentMiPerfil());
                fragmentTransaction.commit();
            }
        });

        // Navegar a ABMLElectrodomesticosActivity
        btnManageAppliances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*((MainActivity) getActivity() ).setnavigateToMainMenu(true);
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frgment_frame, new ABMLElectrodomesticosFragment());
                fragmentTransaction.commit();*/
                Intent intent = new Intent(getActivity(), ABMLElectrodomesticosActivity.class);
                startActivity(intent);
            }
        });

        // Navegar a CalculoConsumoActivity
        Button btnCalculateConsumption = view.findViewById(R.id.btnCalculateConsumption);
        btnCalculateConsumption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity() ).setnavigateToMainMenu(true);
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frgment_frame, new CalculoConsumoFragment()).commit();

            }
        });

        Button btnConsejos = view.findViewById(R.id.btnproyeccion);
        btnConsejos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity() ).setnavigateToMainMenu(true);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frgment_frame, new ProyeccionFragment()).commit();
            }
        });

        FloatingActionButton btnHelp = view.findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity() ).setnavigateToMainMenu(true);
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frgment_frame, new ContactoSoporteFragment());
                fragmentTransaction.commit();
            }
        });

        //Realizado a la antigua
        Button btncontactanos = view.findViewById(R.id.btnContactanos);
        btncontactanos.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(), NotificacionesActivity.class);
               startActivity(intent);
           }
        });



        Button btnReportes = view.findViewById(R.id.btnConsultReports);
        btnReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity() ).setnavigateToMainMenu(true);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frgment_frame, new InformesFragment()
                ).commit();
            }
        });
    }

    private void getUserName() {
        String email= SessionManager.getUserEmail(getActivity());
        DataUsuario dataUsuario = new DataUsuario(getActivity());
        dataUsuario.obtenerUsuarioPorEmail(email,usuario->{
            if(usuario!=null){
                this.usuario = usuario;
                String txt= "Bienvenido: "+usuario.getNombre_usuario();
                txtNombre.setText(txt);
                txtNombre.setText(txt);
            }else{
                Toast.makeText(getActivity(), "Error al obtener usuario", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //btn reportes



    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnManageAppliances = findViewById(R.id.btnManageAppliances);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaPrincipalFragment.this, MiPerfilActivity.class);
                startActivity(intent);
            }
        });

        // Navegar a ABMLElectrodomesticosActivity
        btnManageAppliances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaPrincipalFragment.this, ABMLElectrodomesticosActivity.class);
                startActivity(intent);
            }
        });

        // Navegar a CalculoConsumoActivity
        Button btnCalculateConsumption = findViewById(R.id.btnCalculateConsumption);
        btnCalculateConsumption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaPrincipalFragment.this, CalculoConsumoActivity.class);
                startActivity(intent);
            }
        });

    }*/
}