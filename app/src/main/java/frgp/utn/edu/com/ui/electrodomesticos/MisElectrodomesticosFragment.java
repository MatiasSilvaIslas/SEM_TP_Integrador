package frgp.utn.edu.com.ui.electrodomesticos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.DataUsuario;
import frgp.utn.edu.com.conexion.UsuarioElectrodomesticoDB;
import frgp.utn.edu.com.entidad.Usuario;
import frgp.utn.edu.com.entidad.UsuarioElectrodomestico;
import frgp.utn.edu.com.ui.myaccount.fragmentMiPerfil;
import frgp.utn.edu.com.utils.SessionManager;

public class MisElectrodomesticosFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsuarioElectrodomesticoAdapter adapter;
    private ArrayList<UsuarioElectrodomestico> listaElectrodomesticos;
    private int usuarioId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_electrodomesticos, container, false);
        ((AppCompatActivity) getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));

        recyclerView = view.findViewById(R.id.rvElectrodomesticos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listaElectrodomesticos = new ArrayList<>();
        adapter = new UsuarioElectrodomesticoAdapter(listaElectrodomesticos, this::eliminarElectrodomestico);
        recyclerView.setAdapter(adapter);

        obtenerUsuarioId(idUsuario -> {
            usuarioId = idUsuario;
            cargarElectrodomesticos();
        });

        ImageView btnProfile = view.findViewById(R.id.icon_user);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity() ).setnavigateToMainMenu(true);
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frgment_frame, new fragmentMiPerfil());
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void cargarElectrodomesticos() {
        if (usuarioId != -1) {
            UsuarioElectrodomesticoDB db = new UsuarioElectrodomesticoDB(getActivity());
            db.obtenerElectrodomesticosPorUsuario(usuarioId, new UsuarioElectrodomesticoDB.CallbackElectrodomesticos() {
                @Override
                public void onComplete(ArrayList<UsuarioElectrodomestico> electrodomesticos) {
                    if (electrodomesticos != null) {
                        listaElectrodomesticos.clear();
                        listaElectrodomesticos.addAll(electrodomesticos);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "No se encontraron electrodomésticos.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), "Error al obtener el usuario.", Toast.LENGTH_SHORT).show();
        }
    }


    private void eliminarElectrodomestico(UsuarioElectrodomestico electrodomestico) {
        UsuarioElectrodomesticoDB db = new UsuarioElectrodomesticoDB(getActivity());
        db.eliminarElectrodomestico(usuarioId, electrodomestico.getElectrodomesticoId(), success -> {
            if (success) {
                listaElectrodomesticos.remove(electrodomestico);
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Electrodoméstico eliminado correctamente.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Error al eliminar el electrodoméstico.", Toast.LENGTH_SHORT).show();
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
