package frgp.utn.edu.com.ui.electrodomesticos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.entidad.Electrodomestico;

public class ElectrodomesticoAdapter extends RecyclerView.Adapter<ElectrodomesticoAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Electrodomestico> electrodomesticos;
    private ArrayList<Electrodomestico> seleccionados;

    public ElectrodomesticoAdapter(Context context, ArrayList<Electrodomestico> electrodomesticos) {
        this.context = context;
        this.electrodomesticos = electrodomesticos;
        this.seleccionados = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_electrodomestico, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Electrodomestico electrodomestico = electrodomesticos.get(position);

        holder.textNombre.setText(electrodomestico.getNombre());

        holder.checkBox.setChecked(seleccionados.contains(electrodomestico));

        // Configuración del Spinner de cantidad (1-10)
        ArrayAdapter<Integer> cantidadAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getCantidadOptions());
        cantidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerCantidad.setAdapter(cantidadAdapter);

        // Configuración del Spinner de horas (1-24)
        ArrayAdapter<Integer> horasAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getHorasOptions());
        horasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerHoras.setAdapter(horasAdapter);

        // Configuración del Spinner de días (1-30)
        ArrayAdapter<Integer> diasAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getDiasOptions());
        diasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerDias.setAdapter(diasAdapter);

        // Manejador de selección del CheckBox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                seleccionados.add(electrodomestico);
            } else {
                seleccionados.remove(electrodomestico);
            }
        });
    }

    private ArrayList<Integer> getCantidadOptions() {
        ArrayList<Integer> cantidadOptions = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            cantidadOptions.add(i);
        }
        return cantidadOptions;
    }

    private ArrayList<Integer> getHorasOptions() {
        ArrayList<Integer> horasOptions = new ArrayList<>();
        for (int i = 1; i <= 24; i++) {
            horasOptions.add(i);
        }
        return horasOptions;
    }

    private ArrayList<Integer> getDiasOptions() {
        ArrayList<Integer> diasOptions = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            diasOptions.add(i);
        }
        return diasOptions;
    }

    @Override
    public int getItemCount() {
        return electrodomesticos.size();
    }

    public ArrayList<Electrodomestico> getSeleccionados() {
        return seleccionados;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre;
        CheckBox checkBox;
        Spinner spinnerCantidad;
        Spinner spinnerHoras;
        Spinner spinnerDias;

        public ViewHolder(View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.textNombre);
            checkBox = itemView.findViewById(R.id.checkBox);
            spinnerCantidad = itemView.findViewById(R.id.spinnerCantidad);
            spinnerHoras = itemView.findViewById(R.id.spinnerHoras);
            spinnerDias = itemView.findViewById(R.id.spinnerDias); 
        }
    }
}
