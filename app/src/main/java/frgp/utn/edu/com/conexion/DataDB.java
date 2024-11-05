package frgp.utn.edu.com.conexion;

public class DataDB {

    public static String host="mysql-18ee0642-laymro-adac.a.aivencloud.com";
    public static String port="20756";
    public static String nameBD="energia";
    public static String user="grupo7";
    public static String pass="grupo7";

    public static String urlMySQL = "jdbc:mysql://" + host + ":" + port + "/"+ nameBD + "?sslmode=require"+ user+ pass;
    public static String driver = "com.mysql.jdbc.Driver";

}
