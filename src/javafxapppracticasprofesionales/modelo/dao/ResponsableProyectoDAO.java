package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.ResponsableProyecto;

public class ResponsableProyectoDAO {
    
    public static ArrayList<ResponsableProyecto> obtenerResponsables() throws SQLException {
        ArrayList<ResponsableProyecto> responsables = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT rp.nombre AS Nombre, rp.cargo AS Cargo, rp.correo AS Correo, rp.telefono AS Telefono, ov.nombre AS OrganizacionVinculada "
                + "FROM responsableproyecto rp "
                + "INNER JOIN organizacionvinculada ov ON rp.OrganizacionVinculada_idOrganizacionVinculada = ov.idOrganizacionVinculada;";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            
            while (resultado.next()) {
                responsables.add(convertirRegistroResponsable(resultado));
            }
            
            conexionBD.close();
            sentencia.close();
            resultado.close();
            
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos.");
        }
        return responsables;
    }

    public static ArrayList<ResponsableProyecto> obtenerResponsablesPorOrganizacion(int idOrganizacion) throws SQLException {
        ArrayList<ResponsableProyecto> responsables = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT idResponsableProyecto, nombre, cargo, correo, telefono "
                    + "FROM responsableproyecto WHERE OrganizacionVinculada_idOrganizacionVinculada = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idOrganizacion);
            ResultSet resultado = sentencia.executeQuery();
            
            while (resultado.next()) {
                responsables.add(convertirRegistroResponsable(resultado));
            }
            
            conexionBD.close();
            sentencia.close();
            resultado.close();
            
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos.");
        }
        return responsables;
    }
    
    private static ResponsableProyecto convertirRegistroResponsable(ResultSet resultado) throws SQLException {
        ResponsableProyecto responsable = new ResponsableProyecto();
        responsable.setIdResponsable(resultado.getInt("idResponsableProyecto"));
        responsable.setNombre(resultado.getString("nombre"));
        responsable.setCargo(resultado.getString("cargo"));
        responsable.setCorreo(resultado.getString("correo"));
        responsable.setTelefono(resultado.getString("telefono"));
        
        return responsable;
    }
}