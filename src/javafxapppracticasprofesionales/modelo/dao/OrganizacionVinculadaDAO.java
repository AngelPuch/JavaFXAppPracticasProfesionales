package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;

public class OrganizacionVinculadaDAO {

    public static ArrayList<OrganizacionVinculada> obtenerOrganizacionesVinculadas() throws SQLException {
        ArrayList<OrganizacionVinculada> organizaciones = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT idOrganizacionVinculada, nombre, direccion, telefono FROM organizacionvinculada";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();
            
            while (resultado.next()) {
                organizaciones.add(convertirRegistroOV(resultado));
            }
            
            conexionBD.close();
            sentencia.close();
            resultado.close();
            
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos.");
        }
        return organizaciones;
    }
    
    private static OrganizacionVinculada convertirRegistroOV(ResultSet resultado) throws SQLException{
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setIdOrganizacion(resultado.getInt("idOrganizacionVinculada"));
        organizacion.setNombre(resultado.getString("nombre"));
        organizacion.setDireccion(resultado.getString("direccion"));
        organizacion.setTelefono(resultado.getString("telefono"));
        
        return organizacion;
    }
}