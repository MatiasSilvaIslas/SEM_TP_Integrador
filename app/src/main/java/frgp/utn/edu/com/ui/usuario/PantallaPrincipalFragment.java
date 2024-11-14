package frgp.utn.edu.com.ui.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import frgp.utn.edu.com.R;


public class PantallaPrincipalFragment extends Fragment {
    Toolbar toolbar;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pantalla_principal,container,false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));
        initViews(view);
        return view;
    }
    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_menu_drawer, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Notify that this fragment changed the options menu
        setHasOptionsMenu(true);
    }*/

    public void initViews(View view)
    {
        Button btnProfile = view.findViewById(R.id.btnProfile);
        Button btnManageAppliances = view.findViewById(R.id.btnManageAppliances);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(PantallaPrincipalFragment.this, MiPerfilActivity.class);
                //startActivity(intent);
            }
        });

        // Navegar a ABMLElectrodomesticosActivity
        btnManageAppliances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(PantallaPrincipalFragment.this, ABMLElectrodomesticosActivity.class);
               // startActivity(intent);
            }
        });

        // Navegar a CalculoConsumoActivity
        Button btnCalculateConsumption = view.findViewById(R.id.btnCalculateConsumption);
        btnCalculateConsumption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(PantallaPrincipalFragment.this, CalculoConsumoActivity.class);
                startActivity(intent);*/
            }
        });

    }
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