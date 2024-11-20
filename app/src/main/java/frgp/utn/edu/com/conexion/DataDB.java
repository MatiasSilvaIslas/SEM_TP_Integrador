package frgp.utn.edu.com.conexion;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class DataDB {

  /*  public static String host = "tp-consumo-electrico-tp-consumo-electrico.e.aivencloud.com";
    public static String port = "22562";
    public static String nameBD = "tp_consumo_electrico";
    public static String user = "avnadmin";
    public static String pass = "AVNS_0LF3FRyk1od9x2la6L0";

    public static String urlMySQL = "jdbc:mysql://" + host + ":" + port + "/" + nameBD + "?useSSL=true&requireSSL=true&enabledTLSProtocols=TLSv1.2";

    public static String driver = "com.mysql.jdbc.Driver";

    public static Connection getConnection() throws Exception {
        InputStream caInput = DataDB.class.getResourceAsStream("/raw/ca.pem");

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(caInput, null);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        return DriverManager.getConnection(urlMySQL, user, pass);
    }
    */

    public static String host="sql10.freesqldatabase.com";
    public static String port="3306";
    public static String nameBD="sql10735229";
    public static String user="sql10735229";
    public static String pass="HrFp6wvm16";


    public static String url = "jdbc:mysql://52.67.231.97:3306/sql10735229";
    public static String driver = "com.mysql.jdbc.Driver";




}

