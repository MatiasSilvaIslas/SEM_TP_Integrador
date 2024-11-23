package frgp.utn.edu.com.ui.proyeccion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.UsuarioElectrodomesticoDB;
import frgp.utn.edu.com.entidad.Electrodomestico;
import frgp.utn.edu.com.entidad.UsuarioElectrodomestico;

import java.util.ArrayList;
import java.util.List;

public class UsuarioElectrodomesticoProyeccionAdapter extends RecyclerView.Adapter<UsuarioElectrodomesticoProyeccionAdapter.ViewHolder> {

    private final List<UsuarioElectrodomestico> listaElectrodomesticos;
    private OnTotalChangeListener totalChangeListener;


/*, OnTotalChangeListener totalChangeListener*/
    public UsuarioElectrodomesticoProyeccionAdapter(ArrayList<UsuarioElectrodomestico> listaElectrodomesticos) {
        this.listaElectrodomesticos = listaElectrodomesticos;
      /*  this.totalChangeListener = totalChangeListener;*/

    }


    public interface OnTotalChangeListener {
        void onTotalChange();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario_electrodomesticoproyeccion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)  {
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


    }



    @Override
    public int getItemCount() {
        return listaElectrodomesticos.size();
    }

    public interface Callback {
        void onCalculatedValue(float getCalculatedValue);


    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtCantidad, txtHoras, txtDias, txtPotnecia, txtConsumo;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtHoras = itemView.findViewById(R.id.txtHoras);
            txtDias = itemView.findViewById(R.id.txtDias);
            txtPotnecia = itemView.findViewById(R.id.txtPotenciaPromedio);
            txtConsumo = itemView.findViewById(R.id.txtConsumoPorHora);


        }
    }

/*    public void filtrar(String texto, List<Electrodomestico> listaElectrodomesticos, int Us) {
         // Convertir a minúsculas para comparación

        listaElectrodomesticos.stream().filter(electrodomestico -> electrodomestico.getNombre().toLowerCase().contains(texto.toLowerCase())).forEach(listaElectrodomesticos::add);

        notifyDataSetChanged(); // Actualizar la vista
    }*/


}
