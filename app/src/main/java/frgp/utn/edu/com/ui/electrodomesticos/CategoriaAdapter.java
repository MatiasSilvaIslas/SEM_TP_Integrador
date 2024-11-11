package frgp.utn.edu.com.ui.electrodomesticos;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.entidad.Categoria;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {
    private List<Categoria> categoriaList;
    private OnCategoriaClickListener listener;

    public CategoriaAdapter(List<Categoria> categorias, OnCategoriaClickListener listener) {
        this.categoriaList = categorias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        // Obtener la categoría en la posición actual
        Categoria categoria = categoriaList.get(position);

        // Verificar con un Log si se está obteniendo correctamente
        Log.d("CategoriaAdapter", "Categoria: " + categoria.getNombre());  // Esto es lo que debes agregar

        // Enlazar la categoría con el ViewHolder
        holder.bind(categoria);
    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }

    public class CategoriaViewHolder extends RecyclerView.ViewHolder {
        private TextView textCategoria;
        private ImageView imageCategoria;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategoria = itemView.findViewById(R.id.textCategoria);
            imageCategoria = itemView.findViewById(R.id.imageCategoria);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onCategoriaClick(categoriaList.get(position));
                    }
                }
            });
        }

        public void bind(Categoria categoria) {
            textCategoria.setText(categoria.getNombre());

            // Cambiar imagen y descripción según la categoría
            switch (categoria.getNombre()) {
                case "Iluminación":
                    imageCategoria.setImageResource(R.drawable.ic_lampara);
                    imageCategoria.setContentDescription("Iluminación");
                    break;
                case "Refrigeración":
                    imageCategoria.setImageResource(R.drawable.ic_refrigerador);
                    imageCategoria.setContentDescription("Refrigeración");
                    break;
                case "Línea Blanca":
                    imageCategoria.setImageResource(R.drawable.ic_lavadora);
                    imageCategoria.setContentDescription("Línea Blanca");
                    break;
                case "Cocina":
                    imageCategoria.setImageResource(R.drawable.ic_microondas);
                    imageCategoria.setContentDescription("Cocina");
                    break;
                case "Climatización":
                    imageCategoria.setImageResource(R.drawable.ic_aire_acondicionado);
                    imageCategoria.setContentDescription("Climatización");
                    break;
                case "Electrónica":
                    imageCategoria.setImageResource(R.drawable.ic_pc);
                    imageCategoria.setContentDescription("Electrónica");
                    break;
                case "Cuidado Personal":
                    imageCategoria.setImageResource(R.drawable.ic_secadora);
                    imageCategoria.setContentDescription("Cuidado Personal");
                    break;
                case "Agua":
                    imageCategoria.setImageResource(R.drawable.ic_agua);
                    imageCategoria.setContentDescription("Agua");
                    break;
                default:
                    imageCategoria.setImageResource(R.drawable.ic_default);
                    imageCategoria.setContentDescription("Categoría desconocida");
                    break;
            }
        }
    }

    public interface OnCategoriaClickListener {
        void onCategoriaClick(Categoria categoria);
    }
}
