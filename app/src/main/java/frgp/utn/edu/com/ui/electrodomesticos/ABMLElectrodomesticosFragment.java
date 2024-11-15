package frgp.utn.edu.com.ui.electrodomesticos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.CategoriaDB;
import frgp.utn.edu.com.conexion.DataUsuario;
import frgp.utn.edu.com.conexion.ElectrodomesticoDB;
import frgp.utn.edu.com.conexion.UsuarioElectrodomesticoDB;
import frgp.utn.edu.com.entidad.Categoria;
import frgp.utn.edu.com.entidad.Electrodomestico;
import frgp.utn.edu.com.entidad.Usuario;
import frgp.utn.edu.com.entidad.UsuarioElectrodomestico;
import frgp.utn.edu.com.utils.SessionManager;

import java.util.ArrayList;

public class ABMLElectrodomesticosFragment extends Fragment {

    private ElectrodomesticoAdapter electrodomesticoAdapter;
    private RecyclerView recyclerElectrodomesticos;
    private RecyclerView recyclerCategorias;
    private CategoriaAdapter categoriaAdapter;
    private CategoriaDB categoriaDB;
    private ElectrodomesticoDB electrodomesticoDB;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_abml_electrodomesticos, container, false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));
       // initViews(view);
        //view.setContentView(R.layout.fragment_abml_electrodomesticos);

        // Inicialización del RecyclerView
        recyclerCategorias = view.findViewById(R.id.recyclerCategorias);

        // Configurar el RecyclerView para usar GridLayoutManager con 2 columnas
        recyclerCategorias.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // Inicialización de las clases de base de datos
        electrodomesticoDB = new ElectrodomesticoDB(getActivity());
        categoriaDB = new CategoriaDB(getActivity());

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
        return view;
    }



    private void mostrarElectrodomesticosDialog(Categoria categoria) {
        // Inflar el layout del BottomSheet
        View view = getLayoutInflater().inflate(R.layout.dialog_seleccionar_electrodomesticos, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(view);

        // Configurar el título del dialog
        TextView textTitulo = view.findViewById(R.id.textTituloElectrodomesticos);
        textTitulo.setText("Electrodomésticos en " + categoria.getNombre());

        // Inicializar el RecyclerView dentro del BottomSheet
        RecyclerView dialogRecyclerElectrodomesticos = view.findViewById(R.id.recyclerElectrodomesticos);
        dialogRecyclerElectrodomesticos.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        // Configurar el botón de confirmación
        Button btnConfirmarSeleccion = view.findViewById(R.id.btnConfirmarSeleccion);
        btnConfirmarSeleccion.setVisibility(View.VISIBLE);

        // Obtener electrodomésticos de la base de datos
        electrodomesticoDB.obtenerElectrodomesticosAsync(categoria.getId_categoria(), new ElectrodomesticoDB.ElectrodomesticoCallback() {
            @Override
            public void onElectrodomesticosObtenidos(ArrayList<Electrodomestico> electrodomesticos) {
                // Llamar al método para obtener los electrodomésticos guardados por el usuario
                obtenerUsuarioId(new CallbackUsuarioId() {
                    @Override
                    public void onIdUsuarioObtenido(int idUsuario) {
                        if (idUsuario != -1) {
                            UsuarioElectrodomesticoDB db = new UsuarioElectrodomesticoDB(getActivity());
                            db.obtenerElectrodomesticosGuardadosPorCategoria(idUsuario, categoria.getId_categoria(), new UsuarioElectrodomesticoDB.ObtenerElectrodomesticosCallback() {
                                @Override
                                public void onElectrodomesticosObtenidos(ArrayList<UsuarioElectrodomestico> electrodomesticosGuardados) {
                                    // Crear el adaptador para los electrodomésticos obtenidos y las configuraciones guardadas
                                    electrodomesticoAdapter = new ElectrodomesticoAdapter(getActivity(), electrodomesticos, electrodomesticosGuardados);

                                    // Asignar el adaptador al RecyclerView dentro del BottomSheet
                                    dialogRecyclerElectrodomesticos.setAdapter(electrodomesticoAdapter);
                                }
                            });
                        } else {
                            // Manejo de error si no se obtuvo el id del usuario
                            Toast.makeText(getActivity(), "Error al obtener el usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Configurar el evento de click para el botón de confirmación
                btnConfirmarSeleccion.setOnClickListener(v -> {
                    ArrayList<Electrodomestico> seleccionados = electrodomesticoAdapter.getSeleccionados();
                    ArrayList<UsuarioElectrodomestico> usuarioElectrodomesticos = new ArrayList<>();

                    obtenerUsuarioId(new CallbackUsuarioId() {
                        @Override
                        public void onIdUsuarioObtenido(int idUsuario) {
                            if (idUsuario != -1) {
                                UsuarioElectrodomesticoDB usuarioElectrodomesticoDB = new UsuarioElectrodomesticoDB(getActivity());

                                for (int position = 0; position < seleccionados.size(); position++) {
                                    Electrodomestico electrodomestico = seleccionados.get(position);

                                    int cantidad = getCantidadSeleccionada(dialogRecyclerElectrodomesticos, position);
                                    int horas = getHorasSeleccionada(dialogRecyclerElectrodomesticos, position);
                                    int dias = getDiasSeleccionada(dialogRecyclerElectrodomesticos, position);

                                    UsuarioElectrodomestico usuarioElectrodomestico = new UsuarioElectrodomestico(
                                            idUsuario,
                                            electrodomestico.getId_electrodomestico(),
                                            cantidad,
                                            horas,
                                            dias
                                    );

                                    usuarioElectrodomesticos.add(usuarioElectrodomestico);
                                }

                                // Llamada a actualizarOInsertarElectrodomesticos con todos los elementos seleccionados
                                usuarioElectrodomesticoDB.actualizarOInsertarElectrodomesticos(idUsuario, usuarioElectrodomesticos, success -> {
                                    if (success) {
                                        Toast.makeText(getActivity(), "Selección guardada correctamente", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Error al guardar la selección", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss();
                                });
                            } else {
                                Toast.makeText(getActivity(), "Error al obtener el usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                });

            }
        });

        // Mostrar el BottomSheet
        dialog.show();
    }


    // Métodos actualizados para obtener cantidad, horas y días usando la posición en lugar del objeto Electrodomestico
    private int getCantidadSeleccionada(RecyclerView recyclerView, int position) {
        Spinner spinnerCantidad = (Spinner) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.spinnerCantidad);
        return Integer.parseInt(spinnerCantidad.getSelectedItem().toString());
    }

    private int getHorasSeleccionada(RecyclerView recyclerView, int position) {
        Spinner spinnerHoras = (Spinner) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.spinnerHoras);
        return Integer.parseInt(spinnerHoras.getSelectedItem().toString());
    }

    private int getDiasSeleccionada(RecyclerView recyclerView, int position) {
        Spinner spinnerDias = (Spinner) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.spinnerDias);
        return Integer.parseInt(spinnerDias.getSelectedItem().toString());
    }


    private void insertarElectrodomesticos(ArrayList<UsuarioElectrodomestico> usuarioElectrodomesticos) {
        obtenerUsuarioId(new CallbackUsuarioId() {
            @Override
            public void onIdUsuarioObtenido(int idUsuario) {
                if (idUsuario != -1) {
                    UsuarioElectrodomesticoDB db = new UsuarioElectrodomesticoDB(getActivity());
                    db.actualizarOInsertarElectrodomesticos(idUsuario, usuarioElectrodomesticos, new UsuarioElectrodomesticoDB.InsertarElectrodomesticosCallback() {
                        @Override
                        public void onElectrodomesticosInsertados(boolean success) {
                            if (success) {
                                Toast.makeText(getActivity(), "Electrodomésticos guardados correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Error al guardar los electrodomésticos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Error al obtener el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void obtenerUsuarioId(CallbackUsuarioId callback) {
        String userEmail = SessionManager.getUserEmail(getActivity());

        DataUsuario dataUsuario = new DataUsuario(getActivity());
        dataUsuario.obtenerUsuarioPorEmail(userEmail, new DataUsuario.CallbackUsuario() {
            @Override
            public void onComplete(Usuario usuario) {
                if (usuario != null) {
                    callback.onIdUsuarioObtenido(usuario.getIdUsuario());
                } else {
                    callback.onIdUsuarioObtenido(-1);
                }
            }
        });
    }



    public interface CallbackUsuarioId {
        void onIdUsuarioObtenido(int idUsuario);
    }
}
