package frgp.utn.edu.com.conexion;

import android.content.Context;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import frgp.utn.edu.com.entidad.Electrodomestico;
import frgp.utn.edu.com.entidad.UsuarioElectrodomestico;

public class UsuarioElectrodomesticoDB {

    private Context context;

    public UsuarioElectrodomesticoDB(Context context) {
        this.context = context;
    }

    public void actualizarOInsertarElectrodomesticos(int usuarioId, ArrayList<UsuarioElectrodomestico> seleccionados, InsertarElectrodomesticosCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            boolean success = true;
            try {
                // Cargar el driver JDBC
                Class.forName("com.mysql.jdbc.Driver");

                // Establecer conexión con la base de datos
                Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                // Consultar si el electrodoméstico ya está guardado para el usuario
                String querySelect = "SELECT * FROM UsuarioElectrodomestico WHERE usuario_id = ? AND electrodomestico_id = ?";
                PreparedStatement pstSelect = con.prepareStatement(querySelect);

                // Preparar la consulta para insertar o actualizar los electrodomésticos
                String queryInsert = "INSERT INTO UsuarioElectrodomestico (usuario_id, electrodomestico_id, cantidad, horas, dias) VALUES (?, ?, ?, ?, ?)";
                String queryUpdate = "UPDATE UsuarioElectrodomestico SET cantidad = ?, horas = ?, dias = ? WHERE usuario_id = ? AND electrodomestico_id = ?";

                for (UsuarioElectrodomestico usuarioElectrodomestico : seleccionados) {
                    pstSelect.setInt(1, usuarioId);
                    pstSelect.setInt(2, usuarioElectrodomestico.getElectrodomesticoId());

                    ResultSet rs = pstSelect.executeQuery();

                    // Si ya existe, actualiza
                    if (rs.next()) {
                        PreparedStatement pstUpdate = con.prepareStatement(queryUpdate);
                        pstUpdate.setInt(1, usuarioElectrodomestico.getCantidad());
                        pstUpdate.setInt(2, usuarioElectrodomestico.getHoras());
                        pstUpdate.setInt(3, usuarioElectrodomestico.getDias());
                        pstUpdate.setInt(4, usuarioId);
                        pstUpdate.setInt(5, usuarioElectrodomestico.getElectrodomesticoId());
                        pstUpdate.executeUpdate();
                        pstUpdate.close();
                    } else {
                        // Si no existe, inserta
                        PreparedStatement pstInsert = con.prepareStatement(queryInsert);
                        pstInsert.setInt(1, usuarioId);
                        pstInsert.setInt(2, usuarioElectrodomestico.getElectrodomesticoId());
                        pstInsert.setInt(3, usuarioElectrodomestico.getCantidad());
                        pstInsert.setInt(4, usuarioElectrodomestico.getHoras());
                        pstInsert.setInt(5, usuarioElectrodomestico.getDias());
                        pstInsert.executeUpdate();
                        pstInsert.close();
                    }
                }

                con.close();
            } catch (Exception e) {
                success = false;
                e.printStackTrace();
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "Error al insertar o actualizar electrodomésticos", Toast.LENGTH_SHORT).show();
                });
            }

            boolean finalSuccess = success;
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> callback.onElectrodomesticosInsertados(finalSuccess));
        });
    }

    public void obtenerElectrodomesticosGuardadosPorCategoria(int usuarioId, int categoriaId, ObtenerElectrodomesticosCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            ArrayList<UsuarioElectrodomestico> lista = new ArrayList<>();
            try {
                // Conectar a la base de datos y obtener los electrodomésticos guardados
                Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);
                String query = "SELECT * \n" +
                        "FROM UsuarioElectrodomestico a\n" +
                        "INNER JOIN Electrodomestico e ON a.electrodomestico_id = e.id_electrodomestico\n" +
                        "INNER JOIN Categoria c ON e.id_categoria = c.id_categoria\n" +
                        "WHERE c.id_categoria = ? AND a.usuario_id = ?"; // Corregir el orden de los parámetros
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, categoriaId);  // Establecer el parámetro categoría en la posición 1
                pst.setInt(2, usuarioId);    // Establecer el parámetro usuario en la posición 2
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    int cantidad = rs.getInt("cantidad");
                    int horas = rs.getInt("horas");
                    int dias = rs.getInt("dias");
                    UsuarioElectrodomestico usuarioElectrodomestico = new UsuarioElectrodomestico(id, usuarioId, rs.getInt("electrodomestico_id"), cantidad, horas, dias);
                    lista.add(usuarioElectrodomestico);
                }
                rs.close();
                pst.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Llamar al callback con la lista de electrodomésticos obtenidos
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> callback.onElectrodomesticosObtenidos(lista));
        });
    }

    public void eliminarElectrodomestico(int usuarioId, int electrodomesticoId, EliminarElectrodomesticoCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            boolean success = true;
            try {
                // Cargar el driver JDBC
                Class.forName("com.mysql.jdbc.Driver");

                // Establecer conexión con la base de datos
                Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                // Consulta para eliminar el electrodoméstico
                String queryDelete = "DELETE FROM UsuarioElectrodomestico WHERE usuario_id = ? AND electrodomestico_id = ?";

                PreparedStatement pstDelete = con.prepareStatement(queryDelete);
                pstDelete.setInt(1, usuarioId);
                pstDelete.setInt(2, electrodomesticoId);
                int rowsAffected = pstDelete.executeUpdate();

                if (rowsAffected > 0) {
                    // Si se eliminó correctamente
                    success = true;
                } else {
                    // Si no se encontró el registro para eliminar
                    success = false;
                }

                pstDelete.close();
                con.close();
            } catch (Exception e) {
                success = false;
                e.printStackTrace();
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "Error al eliminar el electrodoméstico", Toast.LENGTH_SHORT).show();
                });
            }

            // Llamar al callback con el resultado
            boolean finalSuccess = success;
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> callback.onElectrodomesticoEliminado(finalSuccess));
        });
    }

    // Interfaz para el callback de eliminación
    public interface EliminarElectrodomesticoCallback {
        void onElectrodomesticoEliminado(boolean success);
    }

    // Interfaz para el callback de inserción
    public interface InsertarElectrodomesticosCallback {
        void onElectrodomesticosInsertados(boolean success);
    }

    public interface ObtenerElectrodomesticosCallback {
        void onElectrodomesticosObtenidos(ArrayList<UsuarioElectrodomestico> electrodomesticos);
    }


    public void obtenerElectrodomesticosPorUsuario(int usuarioId, CallbackElectrodomesticos callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            ArrayList<UsuarioElectrodomestico> electrodomesticos = new ArrayList<>();

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                String query = "SELECT " +
                        "ue.id AS usuario_electrodomestico_id, " +
                        "ue.usuario_id, " +
                        "ue.electrodomestico_id, " +
                        "e.nombre AS electrodomestico_nombre, " +
                        "e.potencia_promedio_watts, " +
                        "e.consumo_hora_wh, " +
                        "ue.cantidad, " +
                        "ue.horas, " +
                        "ue.dias " +
                        "FROM UsuarioElectrodomestico ue " +
                        "INNER JOIN Electrodomestico e ON ue.electrodomestico_id = e.id_electrodomestico " +
                        "WHERE ue.usuario_id = ?";

                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, usuarioId);

                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    // Aquí, creas un objeto UsuarioElectrodomestico y lo llenas con los datos de la consulta
                    UsuarioElectrodomestico usuarioElectrodomestico = new UsuarioElectrodomestico();
                    usuarioElectrodomestico.setId(rs.getInt("usuario_electrodomestico_id"));
                    usuarioElectrodomestico.setUsuarioId(rs.getInt("usuario_id"));
                    usuarioElectrodomestico.setElectrodomesticoId(rs.getInt("electrodomestico_id"));
                    usuarioElectrodomestico.setCantidad(rs.getInt("cantidad"));
                    usuarioElectrodomestico.setHoras(rs.getInt("horas"));
                    usuarioElectrodomestico.setDias(rs.getInt("dias"));

                    electrodomesticos.add(usuarioElectrodomestico);
                }

                rs.close();
                pst.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Devuelves la lista con los objetos UsuarioElectrodomestico
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> callback.onComplete(electrodomesticos));
        });
    }

    public interface CallbackElectrodomesticos {
        void onComplete(ArrayList<UsuarioElectrodomestico> electrodomesticos);
    }

    public void obtenerDetallesElectrodomestico(int electrodomesticoId, final CallbackDetalles callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Conexión a la base de datos
                    Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                    // Actualizamos la consulta para incluir la potencia y el consumo
                    String query = "SELECT nombre, potencia_promedio_watts, consumo_hora_wh FROM Electrodomestico WHERE id_electrodomestico = ?";
                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setInt(1, electrodomesticoId);
                    ResultSet rs = pst.executeQuery();

                    String nombre = null;
                    int potenciaPromedio = 0;
                    int consumoPorHora = 0;

                    if (rs.next()) {
                        nombre = rs.getString("nombre");
                        potenciaPromedio = rs.getInt("potencia_promedio_watts");
                        consumoPorHora = rs.getInt("consumo_hora_wh");
                    }

                    rs.close();
                    pst.close();
                    con.close();

                    // Aseguramos que el callback se llama en el hilo principal
                    final String finalNombre = nombre;
                    final int finalPotenciaPromedio = potenciaPromedio;
                    final int finalConsumoPorHora = consumoPorHora;

                    new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // Llamamos al callback con los valores obtenidos
                            callback.onDetallesObtenidos(finalNombre, finalPotenciaPromedio, finalConsumoPorHora);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface CallbackDetalles {
        void onDetallesObtenidos(String nombre, int potenciaPromedio, int consumoPorHora);
    }

}
