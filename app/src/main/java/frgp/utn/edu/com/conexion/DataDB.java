package frgp.utn.edu.com.conexion;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class DataDB {

    public static String host = "tp-consumo-electrico-tp-consumo-electrico.e.aivencloud.com";
    public static String port = "22562";
    public static String nameBD = "tp_consumo_electrico";
    public static String user = "avnadmin";
    public static String pass = "AVNS_45Mc6NjIWIoHvbmxQz";

    // Cambia la URL para que use SSL
    public static String urlMySQL = "jdbc:mysql://" + host + ":" + port + "/" + nameBD + "?useSSL=true&requireSSL=true&enabledTLSProtocols=TLSv1.2";

    public static String driver = "com.mysql.jdbc.Driver";

    public static Connection getConnection() throws Exception {
        // Cargar el certificado CA desde la carpeta raw
        InputStream caInput = DataDB.class.getResourceAsStream("/raw/ca.pem"); // Cambia 'ca.pem' por el nombre real de tu archivo

        // Crear un KeyStore para el certificado
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(caInput, null); // Si tu certificado tiene contraseña, reemplaza null con la contraseña

        // Crear un TrustManager que confíe en el KeyStore
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        // Crear un contexto SSL que use el TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        // Establecer la conexión a la base de datos
        return DriverManager.getConnection(urlMySQL, user, pass);
    }
}




/* public static String host="sql10.freesqldatabase.com";
    public static String port="3306";
    public static String nameBD="sql10735229";
    public static String user="sql10735229";
    public static String pass="HrFp6wvm16";


    public static String url = "jdbc:mysql://52.67.231.97:3306/sql10735229";
    public static String driver = "com.mysql.jdbc.Driver";*/
