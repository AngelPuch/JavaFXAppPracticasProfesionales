package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;

public class OrganizacionVinculadaDAO {
    
    public static ResultadoOperacion registrarOrganizacion(OrganizacionVinculada organizacionVinculada) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "INSERT INTO organizacionvinculada (nombre, telefono, calle, numero, colonia, codigoPostal, municipio, estado)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            asignarParametrosOrganizacion(sentencia, organizacionVinculada);
            int filasAfectadas = sentencia.executeUpdate();
            
            if (filasAfectadas == 1) {
                resultado.setIsError(false);
                resultado.setMensaje("Organización registrada correctamente.");
            } else {
                resultado.setIsError(true);
                resultado.setMensaje("Lo sentimos, por el momento no se puede registrar la organización, por favor inténtelo más tarde");
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
            String sql = "SELECT idOrganizacionVinculada, nombre, telefono, calle, numero, colonia, codigoPostal, municipio, estado FROM organizacionvinculada";
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
        ps.setString(2, organizacionVinculada.getTelefono());
        ps.setString(3, organizacionVinculada.getCalle());
        ps.setString(4, organizacionVinculada.getNumero());
        ps.setString(5, organizacionVinculada.getColonia());
        ps.setString(6, organizacionVinculada.getCodigoPostal());
        ps.setString(7, organizacionVinculada.getMunicipio());
        ps.setString(8, organizacionVinculada.getEstado());
    }
    
    private static OrganizacionVinculada convertirRegistroOV(ResultSet resultado) throws SQLException{
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setIdOrganizacion(resultado.getInt("idOrganizacionVinculada"));
        organizacion.setNombre(resultado.getString("nombre"));
        organizacion.setTelefono(resultado.getString("telefono"));
        organizacion.setCalle(resultado.getString("calle"));
        organizacion.setNumero(resultado.getString("numero"));
        organizacion.setColonia(resultado.getString("colonia"));
        organizacion.setCodigoPostal(resultado.getString("codigoPostal"));
        organizacion.setMunicipio(resultado.getString("municipio"));
        organizacion.setEstado(resultado.getString("estado"));
        
        return organizacion;
    }
}