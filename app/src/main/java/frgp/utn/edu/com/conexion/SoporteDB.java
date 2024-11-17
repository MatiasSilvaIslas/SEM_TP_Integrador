package frgp.utn.edu.com.conexion;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class SoporteDB {
    private Context context;
    private static final String TAG = "SoporteDB";

    public SoporteDB(Context context) {
        this.context = context;
    }

    public interface OperacionCallback {
        void onExito();
        void onError(String mensaje);
    }

    public void obtenerIdUsuario(String email, IdUsuarioCallback callback) {
        new ObtenerIdUsuarioTask(email, callback).execute();
    }

    public void insertarCasoSoporte(int idUsuario, String resumen, String detalle, OperacionCallback callback) {
        new InsertarCasoSoporteTask(idUsuario, resumen, detalle, callback).execute();
    }

    private static class ObtenerIdUsuarioTask extends AsyncTask<Void, Void, Integer> {
        private String email;
        private IdUsuarioCallback callback;
        private String mensajeError;

        public ObtenerIdUsuarioTask(String email, IdUsuarioCallback callback) {
            this.email = email;
            this.callback = callback;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int idUsuario = -1;
            Connection con = null;
            PreparedStatement pst = null;
            ResultSet rs = null;

            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                String query = "SELECT ID_usuario FROM Usuarios WHERE email_usuario = ?";
                pst = con.prepareStatement(query);
                pst.setString(1, email);
                rs = pst.executeQuery();

                if (rs.next()) {
                    idUsuario = rs.getInt("ID_usuario");
                }

            } catch (Exception e) {
                Log.e(TAG, "Error al obtener ID usuario: " + e.getMessage());
                mensajeError = "Error al consultar la base de datos: " + e.getMessage();
                return -1;
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (pst != null) pst.close();
                    if (con != null) con.close();
                } catch (SQLException | java.sql.SQLException e) {
                    Log.e(TAG, "Error al cerrar conexiones: " + e.getMessage());
                }
            }
            return idUsuario;
        }

        @Override
        protected void onPostExecute(Integer idUsuario) {
            if (callback != null) {
                if (idUsuario != -1) {
                    callback.onIdUsuarioObtenido(idUsuario);
                } else {
                    callback.onError(mensajeError != null ? mensajeError : "No se encontró el usuario");
                }
            }
        }
    }

    private static class InsertarCasoSoporteTask extends AsyncTask<Void, Void, Boolean> {
        private int userId;
        private String resumen;
        private String detalle;
        private OperacionCallback callback;
        private String mensajeError;

        public InsertarCasoSoporteTask(int userId, String resumen, String detalle, OperacionCallback callback) {
            this.userId = userId;
            this.resumen = resumen;
            this.detalle = detalle;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection con = null;
            PreparedStatement pst = null;

            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                String query = "INSERT INTO Soporte (ID_usuario, caso_resumen, caso_detalle, caso_estado, timestamp) " +
                        "VALUES (?, ?, ?, 'Pendiente', CURRENT_TIMESTAMP)";
                pst = con.prepareStatement(query);
                pst.setInt(1, userId);
                pst.setString(2, resumen);
                pst.setString(3, detalle);

                int filasAfectadas = pst.executeUpdate();
                return filasAfectadas > 0;

            } catch (Exception e) {
                Log.e(TAG, "Error al insertar caso: " + e.getMessage());
                mensajeError = "Error al registrar el caso: " + e.getMessage();
                return false;
            } finally {
                try {
                    if (pst != null) pst.close();
                    if (con != null) con.close();
                } catch (SQLException | java.sql.SQLException e) {
                    Log.e(TAG, "Error al cerrar conexiones: " + e.getMessage());
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean exitoso) {
            if (callback != null) {
                if (exitoso) {
                    callback.onExito();
                } else {
                    callback.onError(mensajeError != null ? mensajeError : "Error al registrar el caso");
                }
            }
        }
    }

    public interface IdUsuarioCallback {
        void onIdUsuarioObtenido(int idUsuario);
        void onError(String mensaje);
    }
}