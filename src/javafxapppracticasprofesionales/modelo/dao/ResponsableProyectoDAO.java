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

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: ResponsableProyecto.java 
    * Autor: Rodrigo Luna Vázquez, Jose Luis Silva Gómez, Angel Jonathan Puch Hernández
    * Fecha: 12/06/2025
*/
public class ResponsableProyectoDAO {
    
    public static ResultadoOperacion registrarResponsable(ResponsableProyecto responsable) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "INSERT INTO responsableproyecto (nombre, apellidoPaterno, apellidoMaterno, "
                    + "cargo, correo,telefono, OrganizacionVinculada_idOrganizacionVinculada)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            asignarParametrosResponsable(sentencia, responsable, true);
            int filasAfectadas = sentencia.executeUpdate();
            
            if (filasAfectadas == 1) {
                resultado.setIsError(false);
                resultado.setMensaje("Responsable registrado correctamente.");
            } else {
                resultado.setIsError(true);
                resultado.setMensaje("Lo sentimos, por el momento no se puede registrar el responsable, "
                        + "por favor inténtelo más tarde");
            }
          
            conexionBD.close();
            sentencia.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return resultado;
    }
    
    public static ArrayList<ResponsableProyecto> obtenerResponsables() throws SQLException {
        ArrayList<ResponsableProyecto> responsables = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT rp.idResponsableProyecto, rp.nombre, rp.apellidoPaterno, "
                    + "rp.apellidoMaterno, rp.cargo, rp.correo, rp.telefono, ov.nombre AS nombreOrganizacion "
                + "FROM responsableproyecto rp "
                + "INNER JOIN organizacionvinculada ov ON rp.OrganizacionVinculada_idOrganizacionVinculada = ov.idOrganizacionVinculada";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            
            while (resultado.next()) {
                responsables.add(convertirRegistroResponsable(resultado, true));
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
            String consulta = "SELECT idResponsableProyecto, nombre, apellidoPaterno, apellidoMaterno, "
                    + "cargo, correo, telefono "
                    + "FROM responsableproyecto WHERE OrganizacionVinculada_idOrganizacionVinculada = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idOrganizacion);
            ResultSet resultado = sentencia.executeQuery();
            
            while (resultado.next()) {
                responsables.add(convertirRegistroResponsable(resultado, false));
            }
            conexionBD.close();
            sentencia.close();
            resultado.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos.");
        }
        return responsables;
    }
    
    public static ResultadoOperacion actualizarResponsable(ResponsableProyecto responsable, int idResponsable) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String sql = "UPDATE responsableproyecto SET nombre = ?, apellidoPaterno = ?, "
                    + "apellidoMaterno = ?, cargo = ?, correo = ?, telefono = ? " +
                         "WHERE idResponsableProyecto = ?";
            
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            asignarParametrosResponsable(sentencia, responsable, false);
            sentencia.setInt(7, idResponsable);
            
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas == 1) {
                resultado.setIsError(false);
                resultado.setMensaje("Responsable actualizado correctamente.");
            } else {
                resultado.setIsError(true);
                resultado.setMensaje("No se pudo actualizar el responsable. "
                        + "Verifique que el ID sea correcto.");
            }
            
            conexionBD.close();
            sentencia.close();
            
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        
        return resultado;
    }

    private static void asignarParametrosResponsable(PreparedStatement ps, 
            ResponsableProyecto responsable, boolean isRegistrar) throws SQLException {
        ps.setString(1, responsable.getNombre());
        ps.setString(2, responsable.getApellidoPaterno());
        ps.setString(3, responsable.getApellidoMaterno());
        ps.setString(4, responsable.getCargo());
        ps.setString(5, responsable.getCorreo());
        ps.setString(6, responsable.getTelefono());
        if (isRegistrar) {
            ps.setInt(7, responsable.getIdOrganizacion());
        }
    }
    
    private static ResponsableProyecto convertirRegistroResponsable(ResultSet resultado, boolean leerOrganizacion) throws SQLException {
        ResponsableProyecto responsable = new ResponsableProyecto();
        responsable.setIdResponsable(resultado.getInt("idResponsableProyecto"));
        responsable.setNombre(resultado.getString("nombre"));
        responsable.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        responsable.setApellidoMaterno(resultado.getString("apellidoMaterno"));
        responsable.setCargo(resultado.getString("cargo"));
        responsable.setCorreo(resultado.getString("correo"));
        responsable.setTelefono(resultado.getString("telefono"));
        
        if (leerOrganizacion) {
            OrganizacionVinculada organizacion = new OrganizacionVinculada();
            organizacion.setNombre(resultado.getString("nombreOrganizacion"));
            responsable.setOrganizacionVinculada(organizacion);
        }
        return responsable;
    }
}