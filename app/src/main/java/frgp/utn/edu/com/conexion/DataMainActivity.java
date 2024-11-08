package frgp.utn.edu.com.conexion;

import android.content.Context;
import android.widget.ListView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.widget.Toast;
import frgp.utn.edu.com.entidad.Articulo;

public class DataMainActivity {

    private ListView lvArticulo;
    private Context context;

    public DataMainActivity(ListView lv, Context ct) {
        lvArticulo = lv;
        context = ct;
    }

    public void fetchData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            ArrayList<Articulo> listaArticulo = new ArrayList<>();

            try {

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM articulo");

                while (rs.next()) {
                    Articulo Articulo = new Articulo();
                    Articulo.setId(rs.getInt("id"));
                    Articulo.setNombre(rs.getString("nombre"));
                    Articulo.setStock(rs.getInt("stock"));
                    //Articulo.setCategoria(obtenerCategoriasById(rs.getInt("idCategoria")));
                    listaArticulo.add(Articulo);
                }

                rs.close();
                st.close();
                con.close();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Hay un problema", Toast.LENGTH_SHORT).show();
            }

            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {

                /*ArticuloAdapter adapter = new ArticuloAdapter(context, listaArticulo);
                lvArticulo.setAdapter(adapter);
                Toast.makeText(context, "Conexion exitosa", Toast.LENGTH_SHORT).show();*/
            });
        });
    }

    public void agregarArticulo(int id, String nombre, int stock, int categoriaId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            boolean success = false;

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

                String query = "INSERT INTO articulo (id, nombre, stock, idCategoria) VALUES (?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, id);
                pst.setString(2, nombre);
                pst.setInt(3, stock);
                pst.setInt(4, categoriaId);
                int rowsAffected = pst.executeUpdate();

                success = rowsAffected > 0;

                pst.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "Error al agregar artículo", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            boolean finalSuccess = success;
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                if (finalSuccess) {
                    Toast.makeText(context, "Artículo agregado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al agregar artículo", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    /*public ArrayList<Categoria> obtenerCategorias() {
        ArrayList<Categoria> categorias = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String query = "SELECT id, descripcion FROM categoria";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String descripcion = rs.getString("descripcion");
                categorias.add(new Categoria(id, descripcion));
            }

            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categorias;
    }
    public Categoria obtenerCategoriasById(int id) {
        Categoria categorias = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String query = "SELECT id, descripcion FROM categoria where id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();


            if (rs.next()) {
                categorias = new Categoria();
                categorias.setId(rs.getInt("id"));
                categorias.setDescripcion(rs.getString("descripcion"));
            }
            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categorias;
    }

    public Articulo buscarArticuloPorId(int id) {
        Articulo articulo = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            String query = "SELECT id, nombre, stock, idCategoria FROM articulo WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int articuloId = rs.getInt("id");
                String nombre = rs.getString("nombre");
                int stock = rs.getInt("stock");
                int categoriaId = rs.getInt("idCategoria");
                Categoria categoria = obtenerCategoriasById(categoriaId);
                articulo = new Articulo(articuloId, nombre, stock, categoria);
            }

            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return articulo;
    }*/

    public boolean modificarArticulo(int id, String nombre, int stock, int categoriaId) {
        boolean success = false;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);

            String query = "UPDATE articulo SET nombre = ?, stock = ?, idCategoria = ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, nombre);
            pst.setInt(2, stock);
            pst.setInt(3, categoriaId);
            pst.setInt(4, id);

            int rowsAffected = pst.executeUpdate();
            success = rowsAffected > 0;

            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    public boolean articuloExiste(int id) {
        boolean existe = false;

        String query = "SELECT id FROM articulo WHERE id = ?";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(DataDB.url, DataDB.user, DataDB.pass);
                 PreparedStatement pst = con.prepareStatement(query)) {

                pst.setInt(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return existe;
    }

}
