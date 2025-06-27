package javafxapppracticasprofesionales.modelo;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;

public class ConexionBD {

    private static final String ARCHIVO_PROPIEDADES = "database.properties";

    public static Connection abrirConexion() throws SQLException {
        try {
            Properties prop = cargarPropiedades();
            String url = prop.getProperty("db.url");
            String driver = prop.getProperty("db.driver");
            
            Credenciales credenciales = obtenerCredencialesPorRol(prop);
            
            Class.forName(driver);
            return DriverManager.getConnection(url, credenciales.getUsuario(), credenciales.getPassword());

        } catch (Exception e) {
            throw new SQLException("Error al conectar con la base de datos: " + e.getMessage(), e);
        }
    }
    
    private static Properties cargarPropiedades() throws IOException {
        try (InputStream input = ConexionBD.class.getClassLoader().getResourceAsStream(ARCHIVO_PROPIEDADES)) {
            if (input == null) {
                throw new IOException("No se encontró el archivo " + ARCHIVO_PROPIEDADES);
            }
            Properties prop = new Properties();
            prop.load(input);
            return prop;
        }
    }

    private static Credenciales obtenerCredencialesPorRol(Properties prop) throws SQLException {
        String rol = SesionUsuario.getInstancia().getRolUsuario();
        String userKey = "db.user." + rol;
        String passKey = "db.password." + rol;

        String usuarioDB = prop.getProperty(userKey);
        String passwordDB = prop.getProperty(passKey);

        if (usuarioDB == null || passwordDB == null) {
            throw new SQLException("No se encontraron las credenciales para el rol: " + rol + " en el archivo de propiedades.");
        }
        
        return new Credenciales(usuarioDB, passwordDB);
    }
    
    private static class Credenciales {
        private final String usuario;
        private final String password;

        public Credenciales(String usuario, String password) {
            this.usuario = usuario;
            this.password = password;
        }

        public String getUsuario() { return usuario; }
        public String getPassword() { return password; }
    }
}