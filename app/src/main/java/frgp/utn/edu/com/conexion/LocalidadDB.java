package frgp.utn.edu.com.conexion;

import android.content.Context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import frgp.utn.edu.com.entidad.Localidad;

public class LocalidadDB {
    private Context context;

    public LocalidadDB( Context ct) {
        context = ct;
    }

    public ArrayList<Localidad> obtenerLocalidades(int id_prov) {
        ArrayList<Localidad> localidades = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

            String query = "SELECT l.codLocalidad, l.codProvincia, localidad, l.estado FROM sql10735229.Localidades as l inner join sql10735229.Provincias as p on l.codProvincia = p.codProvincia where l.codProvincia = " + id_prov;
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id_localidad = rs.getInt("codLocalidad");
                int id_provincia = rs.getInt("codProvincia");
                String descripcion = rs.getString("localidad");
                boolean estado = rs.getBoolean("estado");
                localidades.add(new Localidad(id_localidad,id_provincia ,descripcion,estado));
            }

            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localidades;
    }
}
