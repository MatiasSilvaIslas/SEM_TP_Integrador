package frgp.utn.edu.com.ui.electrodomesticos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.entidad.Electrodomestico;

public class ElectrodomesticoAdapter extends RecyclerView.Adapter<ElectrodomesticoAdapter.ElectrodomesticoViewHolder> {

    private Context context;
    private ArrayList<Electrodomestico> electrodomesticos;
    private ArrayList<Electrodomestico> seleccionados = new ArrayList<>();

    public ElectrodomesticoAdapter(Context context, ArrayList<Electrodomestico> electrodomesticos) {
        this.context = context;
        this.electrodomesticos = electrodomesticos;
    }

    @Override
    public ElectrodomesticoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_electrodomestico, parent, false);
        return new ElectrodomesticoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ElectrodomesticoViewHolder holder, int position) {
        Electrodomestico electrodomestico = electrodomesticos.get(position);
        holder.bind(electrodomestico);
    }

    @Override
    public int getItemCount() {
        return electrodomesticos.size();
    }

    public ArrayList<Electrodomestico> getSeleccionados() {
        return seleccionados;
    }

    class ElectrodomesticoViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView textViewNombre;

        public ElectrodomesticoViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            textViewNombre = itemView.findViewById(R.id.textNombre);
        }

        public void bind(Electrodomestico electrodomestico) {
            textViewNombre.setText(electrodomestico.getNombre());

            // Si está seleccionado, agregar a la lista de seleccionados
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    seleccionados.add(electrodomestico);
                } else {
                    seleccionados.remove(electrodomestico);
                }
            });
        }
    }
}
