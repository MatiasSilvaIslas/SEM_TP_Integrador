package frgp.utn.edu.com.conexion;

import android.content.Context;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;

import frgp.utn.edu.com.entidad.Categoria;
import frgp.utn.edu.com.entidad.Electrodomestico;

public class ElectrodomesticoDB {
    private Context context;

    public ElectrodomesticoDB(Context context) {
        this.context = context;
    }

    public ArrayList<Electrodomestico> obtenerElectrodomesticos() {
        ArrayList<Electrodomestico> electrodomesticos = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

            String query = "SELECT e.id_electrodomestico, e.nombre, e.potencia_promedio_watts, e.consumo_hora_wh, c.id_categoria, c.nombre AS categoria_nombre " +
                    "FROM Electrodomestico AS e " +
                    "INNER JOIN Categoria AS c ON e.id_categoria = c.id_categoria";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id_electrodomestico = rs.getInt("id_electrodomestico");
                String nombre = rs.getString("nombre");
                int potenciaPromedioWatts = rs.getInt("potencia_promedio_watts");
                int consumoHoraWh = rs.getInt("consumo_hora_wh");

                int id_categoria = rs.getInt("id_categoria");
                String categoriaNombre = rs.getString("categoria_nombre");
                Categoria categoria = new Categoria(id_categoria, categoriaNombre);

                electrodomesticos.add(new Electrodomestico(id_electrodomestico, nombre, potenciaPromedioWatts, consumoHoraWh, categoria));
            }

            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return electrodomesticos;
    }
}