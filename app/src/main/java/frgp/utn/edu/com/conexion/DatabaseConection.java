package frgp.utn.edu.com.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConection {

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
    private static final String URL = "jdbc:mysql://mysql-18ee0642-laymro-adac.a.aivencloud.com:20756/energia?useSSL=true&requireSSL=true&enabledTLSProtocols=TLSv1.2";
    private static final String USER = "energia";
    private static final String PASSWORD = "energia";

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;

        }
    }
}