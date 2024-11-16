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
import frgp.utn.edu.com.entidad.Electrodomestico;

public class ElectrodomesticoDB {

    private Context context;

    public ElectrodomesticoDB(Context context) {
        this.context = context;
    }

    public void obtenerElectrodomesticosAsync(int categoriaId, ElectrodomesticoCallback callback) {
        // Ejecutar la consulta en segundo plano
        new ObtenerElectrodomesticosTask(categoriaId, callback).execute();
    }

    // AsyncTask para obtener los electrodomésticos
    private static class ObtenerElectrodomesticosTask extends AsyncTask<Void, Void, ArrayList<Electrodomestico>> {

        private int categoriaId;
        private ElectrodomesticoCallback callback;

        public ObtenerElectrodomesticosTask(int categoriaId, ElectrodomesticoCallback callback) {
            this.categoriaId = categoriaId;
            this.callback = callback;
        }

        @Override
        protected ArrayList<Electrodomestico> doInBackground(Void... voids) {
            ArrayList<Electrodomestico> electrodomesticos = new ArrayList<>();
            try {
                // Cargar el driver JDBC
                Class.forName("com.mysql.jdbc.Driver");

                // Establecer conexión con la base de datos
                Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                // Consulta SQL para obtener los electrodomésticos según la categoría
                String query = "SELECT id_electrodomestico, nombre, potencia_promedio_watts, consumo_hora_wh, id_categoria FROM Electrodomestico WHERE id_categoria = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, categoriaId); // Usar el id de la categoría
                ResultSet rs = pst.executeQuery();

                // Procesar los resultados
                while (rs.next()) {
                    int id_electrodomestico = rs.getInt("id_electrodomestico");
                    String nombre = rs.getString("nombre");
                    int potencia = rs.getInt("potencia_promedio_watts");
                    int consumoHoraWh = rs.getInt("consumo_hora_wh");
                    int categoriaId = rs.getInt("id_categoria");

                    // Aquí asumimos que está el método para obtener la categoría asociada al ID
                    // Puede ser una consulta adicional a la base de datos o un objeto previamente cargado
                    Categoria categoria = obtenerCategoriaPorId(categoriaId, con);

                    electrodomesticos.add(new Electrodomestico(id_electrodomestico, nombre, potencia, consumoHoraWh, categoria));
                }

                // Cerrar recursos
                rs.close();
                pst.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return electrodomesticos;
        }

        @Override
        protected void  onPostExecute(ArrayList<Electrodomestico> electrodomesticos) {
            super.onPostExecute(electrodomesticos);
            // Llamar al callback con los resultados
            callback.onElectrodomesticosObtenidos(electrodomesticos);
        }

        private Categoria obtenerCategoriaPorId(int categoriaId, Connection con) {
            Categoria categoria = null;
            try {
                String query = "SELECT id_categoria, nombre FROM Categoria WHERE id_categoria = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, categoriaId);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    categoria = new Categoria(rs.getInt("id_categoria"), rs.getString("nombre"));
                }

                rs.close();
                pst.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return categoria;
        }
    }

    public interface ElectrodomesticoCallback {
        void onElectrodomesticosObtenidos(ArrayList<Electrodomestico> electrodomesticos);
    }
}
