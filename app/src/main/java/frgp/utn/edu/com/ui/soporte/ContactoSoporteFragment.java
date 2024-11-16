package frgp.utn.edu.com.ui.soporte;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import frgp.utn.edu.com.R;
import frgp.utn.edu.com.conexion.DataDB;
import frgp.utn.edu.com.conexion.SoporteDB;

public class ContactoSoporteFragment extends Fragment {
    private EditText editCasoResumen, editCasoDetalle;
    private Button btnEnviarSoporte;
    private SoporteDB soporteDB;
    private int userId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacto_soporte, container, false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));

        editCasoResumen = view.findViewById(R.id.editCasoResumen);
        editCasoDetalle = view.findViewById(R.id.editCasoDetalle);
        btnEnviarSoporte = view.findViewById(R.id.btnEnviarSoporte);

        soporteDB = new SoporteDB(requireContext());

        obtenerUserId();

        btnEnviarSoporte.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(requireContext(), "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
                return;
            }

            String resumen = editCasoResumen.getText().toString().trim();
            String detalle = editCasoDetalle.getText().toString().trim();

            if (!resumen.isEmpty() && !detalle.isEmpty()) {
                soporteDB.insertarCasoSoporte(userId, resumen, detalle);
                Toast.makeText(requireContext(), "Se ha enviado con éxito. Pronto nos comunicaremos con usted", Toast.LENGTH_SHORT).show();
                // Usar navigate back en lugar de onBackPressed
                getParentFragmentManager().popBackStack();
            } else {
                Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void obtenerUserId() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);

        if (email != null) {
            new ObtenerUserIdTask(email).execute();
        } else {
            Toast.makeText(requireContext(), "No se encontró un usuario registrado.", Toast.LENGTH_SHORT).show();
            // Usar navigate back en lugar de onBackPressed
            getParentFragmentManager().popBackStack();
        }
    }

    // AsyncTask para obtener el ID de usuario a partir del email
    @SuppressWarnings("deprecation")
    private class ObtenerUserIdTask extends AsyncTask<Void, Void, Integer> {
        private final String email;
        private Exception error;

        public ObtenerUserIdTask(String email) {
            this.email = email;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            Connection con = null;
            PreparedStatement pst = null;
            ResultSet rs = null;
            try {
                // Conexión con la base de datos
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                // Consulta SQL para obtener el ID del usuario
                String query = "SELECT ID_usuario FROM Usuarios WHERE email = ?";
                pst = con.prepareStatement(query);
                pst.setString(1, email);
                rs = pst.executeQuery();

                if (rs.next()) {
                    return rs.getInt("ID_usuario");
                }
            } catch (Exception e) {
                error = e;
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (pst != null) pst.close();
                    if (con != null) con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (error != null) {
                Toast.makeText(requireContext(),
                        "Error al obtener datos: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                // Usar navigate back en lugar de onBackPressed
                getParentFragmentManager().popBackStack();
                return;
            }

            if (result != -1) {
                userId = result;
            } else {
                Toast.makeText(requireContext(),
                        "Usuario no encontrado.",
                        Toast.LENGTH_SHORT).show();
                // Usar navigate back en lugar de onBackPressed
                getParentFragmentManager().popBackStack();
            }
        }
    }
}