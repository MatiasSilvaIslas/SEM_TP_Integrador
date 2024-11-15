package frgp.utn.edu.com.conexion;

import android.content.Context;
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SoporteDB {
    private Context context;

    public SoporteDB(Context context) {
        this.context = context;
    }

    // Método para insertar un caso de soporte con un ID de usuario
    public void insertarCasoSoporte(int idUsuario, String resumen, String detalle) {
        new InsertarCasoSoporteTask(idUsuario, resumen, detalle).execute();
    }

    // AsyncTask para insertar el caso en la base de datos
    private static class InsertarCasoSoporteTask extends AsyncTask<Void, Void, Void> {
        private int userId;
        private String resumen;
        private String detalle;

        public InsertarCasoSoporteTask(int userId, String resumen, String detalle) {
            this.userId = this.userId;
            this.resumen = resumen;
            this.detalle = detalle;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                String query = "INSERT INTO Soporte (ID_usuario, caso_resumen, caso_detalle, caso_estado, timestamp) VALUES (?, ?, ?, 'Pendiente', CURRENT_TIMESTAMP)";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, userId);
                pst.setString(2, resumen);
                pst.setString(3, detalle);
                pst.executeUpdate();

                pst.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    // Método para obtener el ID de usuario basado en el email
    public void obtenerIdUsuario(String email, IdUsuarioCallback callback) {
        new ObtenerIdUsuarioTask(email, callback).execute();
    }

    // AsyncTask para obtener el ID de usuario
    private static class ObtenerIdUsuarioTask extends AsyncTask<Void, Void, Integer> {
        private String email;
        private IdUsuarioCallback callback;

        public ObtenerIdUsuarioTask(String email, IdUsuarioCallback callback) {
            this.email = email;
            this.callback = callback;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int idUsuario = -1;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                String query = "SELECT ID_usuario FROM Usuario WHERE email = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, email);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    idUsuario = rs.getInt("ID_usuario");
                }

                rs.close();
                pst.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return idUsuario;
        }

        @Override
        protected void onPostExecute(Integer idUsuario) {
            super.onPostExecute(idUsuario);
            callback.onIdUsuarioObtenido(idUsuario);
        }
    }

    // Interfaz para el callback de ID de usuario
    public interface IdUsuarioCallback {
        void onIdUsuarioObtenido(int idUsuario);
    }
}
