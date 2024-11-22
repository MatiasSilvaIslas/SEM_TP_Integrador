package frgp.utn.edu.com.ui.electrodomesticos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.UsuarioElectrodomesticoDB;
import frgp.utn.edu.com.entidad.UsuarioElectrodomestico;

public class UsuarioElectrodomesticoAdapter extends RecyclerView.Adapter<UsuarioElectrodomesticoAdapter.ViewHolder> {

    private final List<UsuarioElectrodomestico> listaElectrodomesticos;
    private final OnEliminarClickListener eliminarClickListener;

    public UsuarioElectrodomesticoAdapter(List<UsuarioElectrodomestico> listaElectrodomesticos, OnEliminarClickListener eliminarClickListener) {
        this.listaElectrodomesticos = listaElectrodomesticos;
        this.eliminarClickListener = eliminarClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario_electrodomestico, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsuarioElectrodomestico electrodomestico = listaElectrodomesticos.get(position);

        // Establecer otros campos
        holder.txtCantidad.setText("Cantidad: " + electrodomestico.getCantidad());
        holder.txtHoras.setText("Horas: " + electrodomestico.getHoras());
        holder.txtDias.setText("Días: " + electrodomestico.getDias());

        new UsuarioElectrodomesticoDB(holder.itemView.getContext()).obtenerDetallesElectrodomestico(electrodomestico.getElectrodomesticoId(), new UsuarioElectrodomesticoDB.CallbackDetalles() {
            @Override
            public void onDetallesObtenidos(String nombre, int potencia, int consumo) {
                holder.txtNombre.setText(nombre);
                holder.txtPotnecia.setText("Potencia promedio: " + String.valueOf(potencia) + " W");
                holder.txtConsumo.setText("Consumo por hora: "+  String.valueOf(consumo) + " Wh");
            }
        });

        // Agregar el Listener para eliminar con confirmación
        holder.btnEliminar.setOnClickListener(v -> {
            // Crear un AlertDialog para confirmar la eliminación
            new android.app.AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Confirmación")
                    .setMessage("¿Estás seguro de que deseas eliminar este electrodoméstico?")
                    .setPositiveButton("Sí", (dialog, which) -> eliminarClickListener.onEliminarClick(electrodomestico)) // Si el usuario acepta, se elimina
                    .setNegativeButton("No", null) // Si el usuario cancela, no pasa nada
                    .show();
        });
    }



    @Override
    public int getItemCount() {
        return listaElectrodomesticos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtCantidad, txtHoras, txtDias, txtPotnecia, txtConsumo;
        ImageButton btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtHoras = itemView.findViewById(R.id.txtHoras);
            txtDias = itemView.findViewById(R.id.txtDias);
            txtPotnecia = itemView.findViewById(R.id.txtPotenciaPromedio);
            txtConsumo = itemView.findViewById(R.id.txtConsumoPorHora);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }

    public interface OnEliminarClickListener {
        void onEliminarClick(UsuarioElectrodomestico electrodomestico);
    }
}
