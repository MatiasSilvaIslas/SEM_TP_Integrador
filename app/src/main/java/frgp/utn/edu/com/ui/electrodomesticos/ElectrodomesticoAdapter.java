package frgp.utn.edu.com.ui.electrodomesticos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.UsuarioElectrodomesticoDB;
import frgp.utn.edu.com.entidad.Electrodomestico;
import frgp.utn.edu.com.entidad.UsuarioElectrodomestico;

public class ElectrodomesticoAdapter extends RecyclerView.Adapter<ElectrodomesticoAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Electrodomestico> electrodomesticos;
    private ArrayList<Electrodomestico> seleccionados;
    private Map<Integer, int[]> configuraciones;
    private ArrayList<UsuarioElectrodomestico> electrodomesticosGuardados;
    private int usuarioId;

    public ElectrodomesticoAdapter(Context context, ArrayList<Electrodomestico> electrodomesticos,
                                   ArrayList<UsuarioElectrodomestico> electrodomesticosGuardados, int usuarioId) {
        this.context = context;
        this.electrodomesticos = electrodomesticos;
        this.seleccionados = new ArrayList<>();
        this.configuraciones = new HashMap<>();
        this.electrodomesticosGuardados = electrodomesticosGuardados;
        this.usuarioId = usuarioId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_electrodomestico, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Electrodomestico electrodomestico = electrodomesticos.get(position);
        holder.textNombre.setText(electrodomestico.getNombre());

        holder.checkBox.setChecked(seleccionados.contains(electrodomestico));
        setupSpinner(holder.spinnerCantidad, getCantidadOptions());
        setupSpinner(holder.spinnerHoras, getHorasOptions());
        setupSpinner(holder.spinnerDias, getDiasOptions());

        UsuarioElectrodomestico usuarioElectrodomestico = obtenerDatosGuardados(electrodomestico.getId_electrodomestico());
        if (usuarioElectrodomestico != null) {
            holder.checkBox.setChecked(true);
            holder.spinnerCantidad.setSelection(usuarioElectrodomestico.getCantidad() - 1);
            holder.spinnerHoras.setSelection(usuarioElectrodomestico.getHoras() - 1);
            holder.spinnerDias.setSelection(usuarioElectrodomestico.getDias() - 1);
            holder.btnEliminar.setVisibility(View.VISIBLE);
            if (!seleccionados.contains(electrodomestico)) {
                seleccionados.add(electrodomestico);
            }
        } else {
            holder.btnEliminar.setVisibility(View.GONE);
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!seleccionados.contains(electrodomestico)) {
                    seleccionados.add(electrodomestico);
                }
            } else {
                seleccionados.remove(electrodomestico);
            }
        });

        holder.btnEliminar.setOnClickListener(v -> {
            new UsuarioElectrodomesticoDB(context).eliminarElectrodomestico(usuarioId, electrodomestico.getId_electrodomestico(), success -> {
                if (success) {
                    configuraciones.remove(electrodomestico.getId_electrodomestico());
                    holder.checkBox.setChecked(false);
                    holder.spinnerCantidad.setSelection(0);
                    holder.spinnerHoras.setSelection(0);
                    holder.spinnerDias.setSelection(0);
                    holder.btnEliminar.setVisibility(View.GONE);
                    Toast.makeText(context, "Configuración eliminada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "No se pudo eliminar la configuración", Toast.LENGTH_SHORT).show();
                }
            });
        });

        holder.spinnerCantidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (holder.checkBox.isChecked()) {
                    guardarConfiguracion(electrodomestico, holder);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        holder.spinnerHoras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (holder.checkBox.isChecked()) {
                    guardarConfiguracion(electrodomestico, holder);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        holder.spinnerDias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (holder.checkBox.isChecked()) {
                    guardarConfiguracion(electrodomestico, holder);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void guardarConfiguracion(Electrodomestico electrodomestico, ViewHolder holder) {
        int cantidad = holder.spinnerCantidad.getSelectedItemPosition();
        int horas = holder.spinnerHoras.getSelectedItemPosition();
        int dias = holder.spinnerDias.getSelectedItemPosition();
        configuraciones.put(electrodomestico.getId_electrodomestico(), new int[]{cantidad, horas, dias});
    }

    private void setupSpinner(Spinner spinner, ArrayList<Integer> options) {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private ArrayList<Integer> getCantidadOptions() {
        ArrayList<Integer> cantidadOptions = new ArrayList<>();
        for (int i = 1; i <= 10; i++) cantidadOptions.add(i);
        return cantidadOptions;
    }

    private ArrayList<Integer> getHorasOptions() {
        ArrayList<Integer> horasOptions = new ArrayList<>();
        for (int i = 1; i <= 24; i++) horasOptions.add(i);
        return horasOptions;
    }

    private ArrayList<Integer> getDiasOptions() {
        ArrayList<Integer> diasOptions = new ArrayList<>();
        for (int i = 1; i <= 30; i++) diasOptions.add(i);
        return diasOptions;
    }

    @Override
    public int getItemCount() {
        return electrodomesticos.size();
    }

    public ArrayList<Electrodomestico> getSeleccionados() {
        return seleccionados;
    }

    public interface OnItemClickListener {
        void onEditarClick(Electrodomestico electrodomestico);
        void onEliminarClick(Electrodomestico electrodomestico);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre;
        CheckBox checkBox;
        Spinner spinnerCantidad, spinnerHoras, spinnerDias;
        ImageView btnEliminar;

        public ViewHolder(View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.textNombre);
            checkBox = itemView.findViewById(R.id.checkBox);
            spinnerCantidad = itemView.findViewById(R.id.spinnerCantidad);
            spinnerHoras = itemView.findViewById(R.id.spinnerHoras);
            spinnerDias = itemView.findViewById(R.id.spinnerDias);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }

    private UsuarioElectrodomestico obtenerDatosGuardados(int electrodomesticoId) {
        for (UsuarioElectrodomestico ue : electrodomesticosGuardados) {
            if (ue.getElectrodomesticoId() == electrodomesticoId) {
                return ue;
            }
        }
        return null;
    }
}