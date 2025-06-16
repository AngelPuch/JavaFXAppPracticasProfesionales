package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import javafxapppracticasprofesionales.modelo.pojo.Proyecto;
import javafxapppracticasprofesionales.modelo.pojo.ResponsableProyecto;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;

public class ResponsableProyectoDAO {
    
    public static ResultadoOperacion registrarResponsable(ResponsableProyecto responsable) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "INSERT INTO responsableproyecto (nombre, cargo, correo, OrganizacionVinculada_idOrganizacionVinculada, telefono)"
                    + " VALUES (?, ?, ?, ?, ?);";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            asignarParametrosResponsable(sentencia, responsable);
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
    
    public static ArrayList<ResponsableProyecto> obtenerResponsables() throws SQLException {
        ArrayList<ResponsableProyecto> responsables = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT rp.idResponsableProyecto, rp.nombre AS nombre, rp.cargo AS cargo, rp.correo AS correo, rp.telefono AS telefono, ov.nombre AS nombreOrganizacion "
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
            String consulta = "SELECT idResponsableProyecto, nombre, cargo, correo, telefono "
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
    

    private static void asignarParametrosResponsable(PreparedStatement ps, ResponsableProyecto responsable) throws SQLException {
        ps.setString(1, responsable.getNombre());
        ps.setString(2, responsable.getCargo());
        ps.setString(3, responsable.getCorreo());
        ps.setInt(4, responsable.getOrganizacionVinculada().getIdOrganizacion());
        ps.setString(5, responsable.getTelefono());

    }
    
    private static ResponsableProyecto convertirRegistroResponsable(ResultSet resultado, boolean isleerTodo) throws SQLException {
        ResponsableProyecto responsable = new ResponsableProyecto();
        responsable.setIdResponsable(resultado.getInt("idResponsableProyecto"));
        responsable.setNombre(resultado.getString("nombre"));
        responsable.setCargo(resultado.getString("cargo"));
        responsable.setCorreo(resultado.getString("correo"));
        responsable.setTelefono(resultado.getString("telefono"));
        
        if (isleerTodo) {
            OrganizacionVinculada organizacion = new OrganizacionVinculada();
            organizacion.setNombre(resultado.getString("nombreOrganizacion"));
            responsable.setOrganizacionVinculada(organizacion);
        }
        return responsable;
    }
}