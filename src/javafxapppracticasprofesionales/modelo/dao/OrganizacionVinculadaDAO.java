package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import javafxapppracticasprofesionales.modelo.pojo.ResponsableProyecto;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;

public class OrganizacionVinculadaDAO {
    
    public static ResultadoOperacion registrarOrganizacion(OrganizacionVinculada organizacionVinculada) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "INSERT INTO organizacionvinculada (nombre, direccion, telefono)"
                    + " VALUES (?, ?, ?);";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            asignarParametrosOrganizacion(sentencia, organizacionVinculada);
            int filasAfectadas = sentencia.executeUpdate();
            
            if (filasAfectadas == 1) {
                resultado.setIsError(false);
                resultado.setMensaje("Responsable registrado correctamente.");
            } else {
                resultado.setIsError(true);
                resultado.setMensaje("Lo sentimos, por el momento no se puede registrar el responsable, por favor inténtelo más tarde");
            }
          
            conexionBD.close();
            sentencia.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return resultado;
    }

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
    
    private static void asignarParametrosOrganizacion(PreparedStatement ps, OrganizacionVinculada organizacionVinculada) throws SQLException {
        ps.setString(1, organizacionVinculada.getNombre());
        ps.setString(2, organizacionVinculada.getDireccion());
        ps.setString(3, organizacionVinculada.getTelefono());

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


