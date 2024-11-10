package frgp.utn.edu.com.conexion;

import android.os.AsyncTask;
import android.content.Context;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import frgp.utn.edu.com.entidad.Categoria;

public class CategoriaDB {

    private Context context;

    public CategoriaDB(Context context) {
        this.context = context;
    }

    public void obtenerCategoriasAsync(CategoriaCallback callback) {
        // Ejecutar la consulta en segundo plano
        new ObtenerCategoriasTask(callback).execute();
    }

    // AsyncTask para obtener las categorías
    private static class ObtenerCategoriasTask extends AsyncTask<Void, Void, ArrayList<Categoria>> {

        private CategoriaCallback callback;

        public ObtenerCategoriasTask(CategoriaCallback callback) {
            this.callback = callback;
        }

        @Override
        protected ArrayList<Categoria> doInBackground(Void... voids) {
            ArrayList<Categoria> categorias = new ArrayList<>();
            try {
                // Cargar el driver JDBC
                Class.forName("com.mysql.jdbc.Driver");

                // Establecer conexión con la base de datos
                Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                // Consulta SQL
                String query = "SELECT id_categoria, nombre FROM Categoria";
                PreparedStatement pst = con.prepareStatement(query);
                ResultSet rs = pst.executeQuery();

                // Procesar resultados
                while (rs.next()) {
                    int id_categoria = rs.getInt("id_categoria");
                    String nombre = rs.getString("nombre");
                    categorias.add(new Categoria(id_categoria, nombre));
                }

                // Cerrar recursos
                rs.close();
                pst.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return categorias;
        }

        @Override
        protected void onPostExecute(ArrayList<Categoria> categorias) {
            super.onPostExecute(categorias);
            // Llamar al callback con el resultado
            callback.onCategoriasObtenidas(categorias);
        }
    }

    // Interfaz de callback para manejar los resultados
    public interface CategoriaCallback {
        void onCategoriasObtenidas(ArrayList<Categoria> categorias);
    }
}
