package frgp.utn.edu.com.ui.electrodomesticos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.entidad.Electrodomestico;
import frgp.utn.edu.com.entidad.UsuarioElectrodomestico;

public class ElectrodomesticoAdapter extends RecyclerView.Adapter<ElectrodomesticoAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Electrodomestico> electrodomesticos;
    private ArrayList<Electrodomestico> seleccionados;
    private Map<Integer, SpinnerState> spinnerStates; // Guardar estado de spinners por ID
    private ArrayList<UsuarioElectrodomestico> electrodomesticosGuardados;

    // Clase para mantener el estado de los spinners
    private static class SpinnerState {
        int cantidad;
        int horas;
        int dias;

        SpinnerState(int cantidad, int horas, int dias) {
            this.cantidad = cantidad;
            this.horas = horas;
            this.dias = dias;
        }
    }

    public ElectrodomesticoAdapter(Context context, ArrayList<Electrodomestico> electrodomesticos,
                                   ArrayList<UsuarioElectrodomestico> electrodomesticosGuardados) {
        this.context = context;
        this.electrodomesticos = electrodomesticos;
        this.seleccionados = new ArrayList<>();
        this.spinnerStates = new HashMap<>();
        this.electrodomesticosGuardados = electrodomesticosGuardados;

        // Inicializar estados con datos guardados
        initializeSpinnerStates();
    }

    private void initializeSpinnerStates() {
        // Inicializar con datos guardados
        for (UsuarioElectrodomestico ue : electrodomesticosGuardados) {
            spinnerStates.put(ue.getElectrodomesticoId(),
                    new SpinnerState(ue.getCantidad(), ue.getHoras(), ue.getDias()));

            // Añadir a seleccionados si tiene datos guardados
            for (Electrodomestico e : electrodomesticos) {
                if (e.getId_electrodomestico() == ue.getElectrodomesticoId()) {
                    seleccionados.add(e);
                    break;
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Electrodomestico electrodomestico = electrodomesticos.get(position);
        int electroId = electrodomestico.getId_electrodomestico();

        // Configurar nombre
        holder.textNombre.setText(electrodomestico.getNombre());

        // Configurar CheckBox
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(seleccionados.contains(electrodomestico));
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!seleccionados.contains(electrodomestico)) {
                    seleccionados.add(electrodomestico);
                    // Guardar estado actual de los spinners
                    guardarEstadoSpinners(electroId, holder);
                }
            } else {
                seleccionados.remove(electrodomestico);
                spinnerStates.remove(electroId);
            }
        });

        // Configurar Spinners
        setupSpinner(holder.spinnerCantidad, getCantidadOptions());
        setupSpinner(holder.spinnerHoras, getHorasOptions());
        setupSpinner(holder.spinnerDias, getDiasOptions());

        // Restaurar estados guardados
        SpinnerState estado = spinnerStates.get(electroId);
        if (estado != null) {
            holder.spinnerCantidad.setSelection(estado.cantidad - 1);
            holder.spinnerHoras.setSelection(estado.horas - 1);
            holder.spinnerDias.setSelection(estado.dias - 1);
        } else {
            holder.spinnerCantidad.setSelection(0);
            holder.spinnerHoras.setSelection(0);
            holder.spinnerDias.setSelection(0);
        }

        // Configurar listeners de Spinners
        setupSpinnerListeners(holder, electroId);
    }

    private void setupSpinnerListeners(ViewHolder holder, int electroId) {
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (holder.checkBox.isChecked()) {
                    guardarEstadoSpinners(electroId, holder);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        holder.spinnerCantidad.setOnItemSelectedListener(listener);
        holder.spinnerHoras.setOnItemSelectedListener(listener);
        holder.spinnerDias.setOnItemSelectedListener(listener);
    }

    private void guardarEstadoSpinners(int electroId, ViewHolder holder) {
        int cantidad = holder.spinnerCantidad.getSelectedItemPosition() + 1;
        int horas = holder.spinnerHoras.getSelectedItemPosition() + 1;
        int dias = holder.spinnerDias.getSelectedItemPosition() + 1;

        spinnerStates.put(electroId, new SpinnerState(cantidad, horas, dias));
    }

    // Método para obtener los valores actuales para la base de datos
    public ArrayList<UsuarioElectrodomestico> getSeleccionadosParaGuardar(int userId) {
        ArrayList<UsuarioElectrodomestico> result = new ArrayList<>();

        for (Electrodomestico electrodomestico : seleccionados) {
            SpinnerState estado = spinnerStates.get(electrodomestico.getId_electrodomestico());
            if (estado != null) {
                result.add(new UsuarioElectrodomestico(
                        userId,
                        electrodomestico.getId_electrodomestico(),
                        estado.cantidad,
                        estado.horas,
                        estado.dias
                ));
            }
        }

        return result;
    }
    //////////////

    private Map<Integer, int[]> configuraciones;



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_electrodomestico, parent, false);
        return new ViewHolder(view);
    }


    private void guardarConfiguracion(Electrodomestico electrodomestico, ViewHolder holder) {
        int cantidad = holder.spinnerCantidad.getSelectedItemPosition() + 1;
        int horas = holder.spinnerHoras.getSelectedItemPosition() + 1;
        int dias = holder.spinnerDias.getSelectedItemPosition() + 1;
        configuraciones.put(electrodomestico.getId_electrodomestico(), new int[]{cantidad, horas, dias});
    }

    private UsuarioElectrodomestico obtenerDatosGuardados(int electrodomesticoId) {
        for (UsuarioElectrodomestico ue : electrodomesticosGuardados) {
            if (ue.getElectrodomesticoId() == electrodomesticoId) {
                return ue;
            }
        }
        return null;
    }

    private void setupSpinner(Spinner spinner, ArrayList<Integer> options) {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private ArrayList<Integer> getCantidadOptions() {
        ArrayList<Integer> options = new ArrayList<>();
        for (int i = 1; i <= 10; i++) options.add(i);
        return options;
    }

    private ArrayList<Integer> getHorasOptions() {
        ArrayList<Integer> options = new ArrayList<>();
        for (int i = 1; i <= 24; i++) options.add(i);
        return options;
    }

    private ArrayList<Integer> getDiasOptions() {
        ArrayList<Integer> options = new ArrayList<>();
        for (int i = 1; i <= 30; i++) options.add(i);
        return options;
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
        Spinner spinnerCantidad, spinnerHoras, spinnerDias;

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
