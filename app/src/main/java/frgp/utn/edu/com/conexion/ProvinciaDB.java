package frgp.utn.edu.com.conexion;

import android.content.Context;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import frgp.utn.edu.com.entidad.Provincia;

public class ProvinciaDB {
    private Context context;

    public ProvinciaDB( Context ct) {
        context = ct;
    }

    public ArrayList<Provincia> obtenerProvincias() {
        ArrayList<Provincia> provincias = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

            String query = "SELECT * FROM Provincias";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("codProvincia");
                String descripcion = rs.getString("provincia");
                boolean estado = rs.getBoolean("estado");
                provincias.add(new Provincia(id, descripcion,estado));
            }

            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provincias;
    }
    public Provincia obtenerProvincia(String provincia) {
        Provincia provincias = new Provincia();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);
            //busca una provincia
            String query = "SELECT * FROM Provincias WHERE provincia = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, provincia);
            ResultSet rs = pst.executeQuery();

                int id = rs.getInt("codProvincia");
                String descripcion = rs.getString("provincia");
                boolean estado = rs.getBoolean("estado");
                provincias = new Provincia(id, descripcion,estado);


            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provincias;
    }
}