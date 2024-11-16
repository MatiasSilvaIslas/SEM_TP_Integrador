package frgp.utn.edu.com.ui.electrodomesticos;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.ui.electrodomesticos.ElectrodomesticoAdapter;
import frgp.utn.edu.com.conexion.ElectrodomesticoDB;
import frgp.utn.edu.com.entidad.Electrodomestico;

public class ElectrodomesticoCrudFragment extends Fragment {

    private RecyclerView recyclerView;
    private ElectrodomesticoAdapter adapter;
    private ArrayList<Electrodomestico> listaElectrodomesticos;
    private ElectrodomesticoDB db;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño
        View view = inflater.inflate(R.layout.fragment_lista_electrodomesticos, container, false);

        // Inicializar componentes
        recyclerView = view.findViewById(R.id.recyclerElectrodomesticos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Botón para agregar electrodoméstico
        view.findViewById(R.id.btnAgregarElectrodomestico).setOnClickListener(v -> agregarElectrodomestico());

        // Inicializar la base de datos y cargar datos
        db = new ElectrodomesticoDB(getContext());
        cargarDatos();

        return view;
    }

    private void cargarDatos() {
        db.obtenerElectrodomesticosAsync(1, electrodomesticos -> {
            listaElectrodomesticos = electrodomesticos;
            configurarAdapter();
        });
    }


    private void configurarAdapter() {
/*        adapter = new ElectrodomesticoAdapter(getContext(), listaElectrodomesticos, new ElectrodomesticoAdapter.OnItemClickListener() {
            @Override
            public void onEditarClick(Electrodomestico electrodomestico) {
                editarElectrodomestico(electrodomestico);
            }

            @Override
            public void onEliminarClick(Electrodomestico electrodomestico) {
                eliminarElectrodomestico(electrodomestico);
            }
        });

        recyclerView.setAdapter(adapter);*/
    }

    private void agregarElectrodomestico() {
        // Abrir un cuadro de diálogo o una nueva actividad para agregar un electrodoméstico
        Toast.makeText(getContext(), "Función agregar no implementada aún", Toast.LENGTH_SHORT).show();
    }

    private void editarElectrodomestico(Electrodomestico electrodomestico) {
        // Abrir un cuadro de diálogo o una nueva actividad para editar
        Toast.makeText(getContext(), "Editando: " + electrodomestico.getNombre(), Toast.LENGTH_SHORT).show();
    }

    private void eliminarElectrodomestico(Electrodomestico electrodomestico) {
        listaElectrodomesticos.remove(electrodomestico);
        adapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Eliminado: " + electrodomestico.getNombre(), Toast.LENGTH_SHORT).show();
    }



    /*private void mostrarDialogoElectrodomestico(@Nullable Electrodomestico electrodomesticoExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_electrodomestico, null);
        builder.setView(dialogView);

        EditText editNombre = dialogView.findViewById(R.id.editNombre);
        EditText editPotencia = dialogView.findViewById(R.id.editPotencia);
        EditText editConsumo = dialogView.findViewById(R.id.editConsumo);
        EditText editCategoria = dialogView.findViewById(R.id.editCategoria);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardar);

        // Si es edición, rellenar los campos
        if (electrodomesticoExistente != null) {
            editNombre.setText(electrodomesticoExistente.getNombre());
            editPotencia.setText(String.valueOf(electrodomesticoExistente.getPotencia()));
            editConsumo.setText(String.valueOf(electrodomesticoExistente.getConsumoHora()));
            editCategoria.setText(electrodomesticoExistente.getCategoria());
        }

        AlertDialog dialog = builder.create();

        btnGuardar.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString();
            int potencia = Integer.parseInt(editPotencia.getText().toString());
            int consumo = Integer.parseInt(editConsumo.getText().toString());
            String categoria = editCategoria.getText().toString();

            if (electrodomesticoExistente == null) {
                // Agregar un nuevo electrodoméstico
                Electrodomestico nuevo = new Electrodomestico(listaElectrodomesticos.size() + 1, nombre, potencia, consumo, categoria);
                listaElectrodomesticos.add(nuevo);
            } else {
                // Editar el existente
                electrodomesticoExistente.setNombre(nombre);
                electrodomesticoExistente.setPotencia(potencia);
                electrodomesticoExistente.setConsumoHora(consumo);
                electrodomesticoExistente.setCategoria(categoria);
            }

            adapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        dialog.show();
    }*/

}
