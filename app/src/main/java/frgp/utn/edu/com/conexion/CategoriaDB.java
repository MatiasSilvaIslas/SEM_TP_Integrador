package frgp.utn.edu.com.conexion;

import android.content.Context;

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

    public ArrayList<Categoria> obtenerCategorias() {
        ArrayList<Categoria> categorias = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

            String query = "SELECT id_categoria, nombre FROM Categoria";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id_categoria = rs.getInt("id_categoria");
                String nombre = rs.getString("nombre");
                categorias.add(new Categoria(id_categoria, nombre));
            }

            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categorias;
    }
}