package frgp.utn.edu.com.ui.electrodomesticos;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import frgp.utn.edu.com.R;
import java.util.ArrayList;
import frgp.utn.edu.com.entidad.Categoria;
import frgp.utn.edu.com.entidad.Electrodomestico;
import frgp.utn.edu.com.conexion.CategoriaDB;
import frgp.utn.edu.com.conexion.ElectrodomesticoDB;

public class ABMLElectrodomesticosActivity extends AppCompatActivity {

    private RecyclerView recyclerCategorias;
    private CategoriaAdapter categoriaAdapter;
    private CategoriaDB categoriaDB;
    private ElectrodomesticoDB electrodomesticoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abml_electrodomesticos);

        recyclerCategorias = findViewById(R.id.recyclerCategorias);
        recyclerCategorias.setLayoutManager(new LinearLayoutManager(this));

        electrodomesticoDB = new ElectrodomesticoDB(this);  // Inicializa electrodomesticoDB

        CategoriaDB categoriaDB = new CategoriaDB(this);
        categoriaDB.obtenerCategoriasAsync(new CategoriaDB.CategoriaCallback() {
            @Override
            public void onCategoriasObtenidas(ArrayList<Categoria> categorias) {
                // Crear el adaptador pasando el listener
                categoriaAdapter = new CategoriaAdapter(categorias, new CategoriaAdapter.OnCategoriaClickListener() {
                    @Override
                    public void onCategoriaClick(Categoria categoria) {
                        mostrarElectrodomesticosDialog(categoria);
                    }
                });

                recyclerCategorias.setAdapter(categoriaAdapter);
            }
        });
    }

    private void mostrarElectrodomesticosDialog(Categoria categoria) {
        View view = getLayoutInflater().inflate(R.layout.dialog_seleccionar_electrodomesticos, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        TextView textTitulo = view.findViewById(R.id.textTituloElectrodomesticos);
        textTitulo.setText("Electrodomésticos en " + categoria.getNombre());

        RecyclerView recyclerElectrodomesticos = view.findViewById(R.id.recyclerElectrodomesticos);
        recyclerElectrodomesticos.setLayoutManager(new LinearLayoutManager(this));

        // Llamada asincrónica para obtener los electrodomésticos con el callback
        electrodomesticoDB.obtenerElectrodomesticosAsync(categoria.getId_categoria(), new ElectrodomesticoDB.ElectrodomesticoCallback() {
            @Override
            public void onElectrodomesticosObtenidos(ArrayList<Electrodomestico> electrodomesticos) {
                // Aquí los electrodomésticos son obtenidos correctamente
                ElectrodomesticoAdapter electrodomesticoAdapter = new ElectrodomesticoAdapter(ABMLElectrodomesticosActivity.this, electrodomesticos);  // Usamos ABMLElectrodomesticosActivity
                recyclerElectrodomesticos.setAdapter(electrodomesticoAdapter);

                // Configuración del botón de confirmación
                Button btnConfirmarSeleccion = view.findViewById(R.id.btnConfirmarSeleccion);
                btnConfirmarSeleccion.setOnClickListener(v -> {
                    // Lógica para guardar los electrodomésticos seleccionados
                    ArrayList<Electrodomestico> seleccionados = electrodomesticoAdapter.getSeleccionados();
                    // Procesar los seleccionados y guardar en la base de datos
                    dialog.dismiss();
                });
            }
        });

        dialog.show();
    }
}
                                             