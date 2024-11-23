package frgp.utn.edu.com.ui.proyeccion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.ui.home.PantallaPrincipalFragment;

public class ProyeccionFragment extends Fragment {
    Toolbar toolbar;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_proyeccion,container,false);
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

    private void initViews(View view) {
        EditText TxtHorasUso = view.findViewById(R.id.etHorasUso);
        EditText txtPotencia = view.findViewById(R.id.etPotencia);
        EditText txtCosto = view.findViewById(R.id.etDiasUso);
        TextView txtResultado = view.findViewById(R.id.Resultadoc);

        Button btnCalcular = view.findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double horasUso = Double.parseDouble(TxtHorasUso.getText().toString());
                double potencia = Double.parseDouble(txtPotencia.getText().toString());
                double costo = Double.parseDouble(txtCosto.getText().toString());

                double resultado = horasUso * potencia * costo;
                txtResultado.setText("El costo de uso es: $" + resultado);
            }
        });

    }
}
