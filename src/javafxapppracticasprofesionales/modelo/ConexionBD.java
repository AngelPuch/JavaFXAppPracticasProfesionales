package javafxapppracticasprofesionales.modelo;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;

public class ConexionBD {
    
    private static final String ARCHIVO_PROPIEDADES = "database.properties";

    public static Connection abrirConexion() throws SQLException {
        Connection conexion = null;
        try (InputStream input = ConexionBD.class.getClassLoader().getResourceAsStream(ARCHIVO_PROPIEDADES)) {
            if (input == null) {
                throw new SQLException("No se encontró el archivo " + ARCHIVO_PROPIEDADES);
            }
            
            Properties prop = new Properties();
            prop.load(input);

            String url = prop.getProperty("db.url");
            String driver = prop.getProperty("db.driver");
            
            String rol = SesionUsuario.getInstancia().getRolUsuario();
            
            String userKey = "db.user." + rol;
            String passKey = "db.password." + rol;

            String usuarioDB = prop.getProperty(userKey);
            String passwordDB = prop.getProperty(passKey);
            
            if (usuarioDB == null || passwordDB == null) {
                throw new SQLException("No se encontraron las credenciales para el rol: " + rol + " en el archivo de propiedades.");
            }
            
            Class.forName(driver);
            conexion = DriverManager.getConnection(url, usuarioDB, passwordDB);

        } catch (Exception e) {
            throw new SQLException("Error al conectar con la base de datos: " + e.getMessage(), e);
        }
        return conexion;
    }
}