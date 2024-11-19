package frgp.utn.edu.com.ui.proyeccion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import frgp.utn.edu.com.R;

public class ProyeccionFragment extends Fragment {
    Toolbar toolbar;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_proyeccion,container,false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));
        initViews(view);
        return view;
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
