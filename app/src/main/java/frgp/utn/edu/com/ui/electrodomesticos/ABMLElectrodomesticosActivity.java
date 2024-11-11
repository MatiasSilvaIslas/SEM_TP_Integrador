package frgp.utn.edu.com.ui.electrodomesticos;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.entidad.Categoria;
import frgp.utn.edu.com.entidad.Electrodomestico;
import frgp.utn.edu.com.conexion.CategoriaDB;
import frgp.utn.edu.com.conexion.ElectrodomesticoDB;

import java.util.ArrayList;

public class ABMLElectrodomesticosActivity extends AppCompatActivity {

    private RecyclerView recyclerCategorias;
    private CategoriaAdapter categoriaAdapter;
    private CategoriaDB categoriaDB;
    private ElectrodomesticoDB electrodomesticoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abml_electrodomesticos);

        // Inicialización del RecyclerView
        recyclerCategorias = findViewById(R.id.recyclerCategorias);

        // Configurar el RecyclerView para usar GridLayoutManager con 2 columnas
        recyclerCategorias.setLayoutManager(new GridLayoutManager(this, 2));

        // Inicialización de las clases de base de datos
        electrodomesticoDB = new ElectrodomesticoDB(this);
        categoriaDB = new CategoriaDB(this);

        // Obtener las categorías de la base de datos de manera asincrónica
        categoriaDB.obtenerCategoriasAsync(new CategoriaDB.CategoriaCallback() {
            @Override
            public void onCategoriasObtenidas(ArrayList<Categoria> categorias) {
                // Verificamos que las categorías no estén vacías
                if (categorias != null && !categorias.isEmpty()) {
                    // Crear el adaptador con la lista de categorías obtenidas
                    categoriaAdapter = new CategoriaAdapter(categorias, new CategoriaAdapter.OnCategoriaClickListener() {
                        @Override
                        public void onCategoriaClick(Categoria categoria) {
                            mostrarElectrodomesticosDialog(categoria);
                        }
                    });

                    // Asignamos el adaptador solo cuando los datos estén disponibles
                    recyclerCategorias.setAdapter(categoriaAdapter);
                }
            }
        });
    }

    private void mostrarElectrodomesticosDialog(Categoria categoria) {
        // Inflar el layout del BottomSheet
        View view = getLayoutInflater().inflate(R.layout.dialog_seleccionar_electrodomesticos, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        // Configurar el título del dialog
        TextView textTitulo = view.findViewById(R.id.textTituloElectrodomesticos);
        textTitulo.setText("Electrodomésticos en " + categoria.getNombre());

        // Inicializar el RecyclerView dentro del BottomSheet
        RecyclerView recyclerElectrodomesticos = view.findViewById(R.id.recyclerElectrodomesticos);
        recyclerElectrodomesticos.setLayoutManager(new GridLayoutManager(this, 1));

        // Configurar el botón de confirmación (lo obtenemos antes de configurar el adaptador)
        Button btnConfirmarSeleccion = view.findViewById(R.id.btnConfirmarSeleccion);
        btnConfirmarSeleccion.setVisibility(View.VISIBLE); // Aseguramos que el botón esté visible

        // Obtener electrodomésticos de la base de datos
        electrodomesticoDB.obtenerElectrodomesticosAsync(categoria.getId_categoria(), new ElectrodomesticoDB.ElectrodomesticoCallback() {
            @Override
            public void onElectrodomesticosObtenidos(ArrayList<Electrodomestico> electrodomesticos) {
                // Crear el adaptador para los electrodomésticos obtenidos
                ElectrodomesticoAdapter electrodomesticoAdapter = new ElectrodomesticoAdapter(ABMLElectrodomesticosActivity.this, electrodomesticos);

                // Asignar el adaptador al RecyclerView dentro del BottomSheet
                recyclerElectrodomesticos.setAdapter(electrodomesticoAdapter);

                // Configurar el evento de click para el botón de confirmación
                btnConfirmarSeleccion.setOnClickListener(v -> {
                    // Obtener los electrodomésticos seleccionados
                    ArrayList<Electrodomestico> seleccionados = electrodomesticoAdapter.getSeleccionados();

                    // Procesar los electrodomésticos seleccionados, guardarlos en la base de datos, etc.
                    dialog.dismiss();
                });
            }
        });

        // Mostrar el BottomSheet
        dialog.show();
    }
}
