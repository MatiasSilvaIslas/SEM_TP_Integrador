package frgp.utn.edu.com.conexion;

import android.content.Context;
import android.os.AsyncTask;

import frgp.utn.edu.com.entidad.Categoria;
import frgp.utn.edu.com.entidad.Electrodomestico;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ElectroDB {

    private Context context;

    public ElectroDB(Context context) {
        this.context = context;
    }

    // Método para obtener un electrodoméstico por su ID de forma asíncrona
    public void obtenerElectrodomesticoPorIdAsync(int electrodomesticoId, ElectrodomesticoCallback callback) {
        new ObtenerElectrodomesticoPorIdTask(electrodomesticoId, callback).execute();
    }


    private static class ObtenerElectrodomesticoPorIdTask extends AsyncTask<Void, Void, Electrodomestico> {

        private int electrodomesticoId;
        private ElectrodomesticoCallback callback;

        public ObtenerElectrodomesticoPorIdTask(int electrodomesticoId, ElectrodomesticoCallback callback) {
            this.electrodomesticoId = electrodomesticoId;
            this.callback = callback;
        }

        @Override
        protected Electrodomestico doInBackground(Void... voids) {
            Electrodomestico electrodomestico = null;
            try {

                Class.forName("com.mysql.jdbc.Driver");


                Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);


                String query = "SELECT id_electrodomestico, nombre, potencia_promedio_watts, consumo_hora_wh, id_categoria FROM Electrodomestico WHERE id_electrodomestico = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, electrodomesticoId);
                ResultSet rs = pst.executeQuery();


                if (rs.next()) {
                    int id_electrodomestico = rs.getInt("id_electrodomestico");
                    String nombre = rs.getString("nombre");
                    int potencia = rs.getInt("potencia_promedio_watts");
                    int consumoHoraWh = rs.getInt("consumo_hora_wh");
                    int categoriaId = rs.getInt("id_categoria");


                    Categoria categoria = obtenerCategoriaPorId(categoriaId, con);

                    electrodomestico = new Electrodomestico(id_electrodomestico, nombre, potencia, consumoHoraWh, categoria);
                }


                rs.close();
                pst.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return electrodomestico;
        }

        @Override
        protected void onPostExecute(Electrodomestico electrodomestico) {
            super.onPostExecute(electrodomestico);

            if (electrodomestico != null) {
                callback.onElectrodomesticoObtenido(electrodomestico);
            } else {
                callback.onError(new Exception("Electrodoméstico no encontrado con ID: " + electrodomesticoId));
            }
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
        void onElectrodomesticoObtenido(Electrodomestico electrodomestico);
        void onError(Exception e);
    }
}
