package frgp.utn.edu.com.ui.back;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.CategoriaDB;
import frgp.utn.edu.com.conexion.DataUsuario;
import frgp.utn.edu.com.conexion.ElectrodomesticoDB;
import frgp.utn.edu.com.conexion.UsuarioElectrodomesticoDB;
import frgp.utn.edu.com.entidad.Categoria;
import frgp.utn.edu.com.entidad.Electrodomestico;
import frgp.utn.edu.com.entidad.Usuario;
import frgp.utn.edu.com.entidad.UsuarioElectrodomestico;
import frgp.utn.edu.com.ui.electrodomesticos.CategoriaAdapter;
import frgp.utn.edu.com.ui.electrodomesticos.ElectrodomesticoAdapter;
import frgp.utn.edu.com.ui.myaccount.fragmentMiPerfil;
import frgp.utn.edu.com.utils.SessionManager;

public class ABMLElectrodomesticosActivity extends AppCompatActivity {

    private RecyclerView recyclerCategorias;
    private CategoriaAdapter categoriaAdapter;
    private CategoriaDB categoriaDB;
    private ElectrodomesticoDB electrodomesticoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(findViewById(R.id.toolbar));
        setContentView(R.layout.activity_abml_electrodomesticos);



        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Gestión de Electrodomésticos");
        }

        configurarPerfil();
        configurarRecyclerCategorias();
        inicializarBaseDeDatos();
        cargarCategorias();
    }

    private void configurarPerfil() {
        ImageView btnProfile = findViewById(R.id.icon_user);
        btnProfile.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(android.R.id.content, new fragmentMiPerfil());
            fragmentTransaction.commit();
        });
    }

    private void configurarRecyclerCategorias() {
        recyclerCategorias = findViewById(R.id.recyclerCategorias);
        recyclerCategorias.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void inicializarBaseDeDatos() {
        electrodomesticoDB = new ElectrodomesticoDB(this);
        categoriaDB = new CategoriaDB(this);
    }

    private void cargarCategorias() {
        categoriaDB.obtenerCategoriasAsync(categorias -> {
            if (categorias == null || categorias.isEmpty()) {
                Toast.makeText(ABMLElectrodomesticosActivity.this, "No se encontraron categorías", Toast.LENGTH_SHORT).show();
            } else {
                categoriaAdapter = new CategoriaAdapter(categorias, this::mostrarElectrodomesticosDialog);
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

        RecyclerView dialogRecyclerElectrodomesticos = view.findViewById(R.id.recyclerElectrodomesticos);
        dialogRecyclerElectrodomesticos.setLayoutManager(new GridLayoutManager(this, 1));

        electrodomesticoDB.obtenerElectrodomesticosAsync(categoria.getId_categoria(), electrodomesticos -> {
            obtenerUsuarioId(idUsuario -> {
                if (idUsuario != -1) {
                    UsuarioElectrodomesticoDB db = new UsuarioElectrodomesticoDB(this);
                    db.obtenerElectrodomesticosGuardadosPorCategoria(idUsuario, categoria.getId_categoria(), electrodomesticosGuardados -> {
                        ElectrodomesticoAdapter electrodomesticoAdapter = new ElectrodomesticoAdapter(this, electrodomesticos, electrodomesticosGuardados);
                        dialogRecyclerElectrodomesticos.setAdapter(electrodomesticoAdapter);

                    });
                } else {
                    Toast.makeText(this, "Error al obtener el usuario", Toast.LENGTH_SHORT).show();
                }
            });
        });

        Button btnConfirmarSeleccion = view.findViewById(R.id.btnConfirmarSeleccion);
        btnConfirmarSeleccion.setVisibility(View.VISIBLE);
        btnConfirmarSeleccion.setOnClickListener(v -> {
            ElectrodomesticoAdapter adapter = (ElectrodomesticoAdapter) dialogRecyclerElectrodomesticos.getAdapter();
            if (adapter != null) {
                ArrayList<Electrodomestico> seleccionados = adapter.getSeleccionados();
                guardarSeleccionElectrodomesticos(seleccionados, dialogRecyclerElectrodomesticos, categoria, dialog);
            }
        });

        dialog.show();
    }



    private void guardarSeleccionElectrodomesticos(ArrayList<Electrodomestico> seleccionados, RecyclerView recyclerView, Categoria categoria, BottomSheetDialog dialog) {
        obtenerUsuarioId(idUsuario -> {
            if (idUsuario != -1) {
                ElectrodomesticoAdapter adapter = (ElectrodomesticoAdapter) recyclerView.getAdapter();
                if (adapter != null) {
                    ArrayList<UsuarioElectrodomestico> usuarioElectrodomesticos = adapter.getSeleccionadosParaGuardar(idUsuario);

                    UsuarioElectrodomesticoDB db = new UsuarioElectrodomesticoDB(this);
                    db.actualizarOInsertarElectrodomesticos(idUsuario, usuarioElectrodomesticos, success -> {
                        Toast.makeText(this, success ? "Selección guardada correctamente" : "Error al guardar la selección", Toast.LENGTH_SHORT).show();
                        if (success) dialog.dismiss();
                    });
                }
            }
        });
    }

    private int getValorSpinner(RecyclerView recyclerView, int position, int spinnerId) {
        try {
            Spinner spinner = recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(spinnerId);
            return Integer.parseInt(spinner.getSelectedItem().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    private void obtenerUsuarioId(CallbackUsuarioId callback) {
        String userEmail = SessionManager.getUserEmail(this);
        DataUsuario dataUsuario = new DataUsuario(this);
        dataUsuario.obtenerUsuarioPorEmail(userEmail, usuario -> {
            if (usuario != null) {
                callback.onIdUsuarioObtenido(usuario.getIdUsuario());
            } else {
                callback.onIdUsuarioObtenido(-1);
            }
        });
    }

    public interface CallbackUsuarioId {
        void onIdUsuarioObtenido(int idUsuario);
    }
}
