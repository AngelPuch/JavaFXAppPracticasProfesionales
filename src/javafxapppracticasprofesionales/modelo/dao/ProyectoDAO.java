package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import javafxapppracticasprofesionales.modelo.pojo.Proyecto;
import javafxapppracticasprofesionales.modelo.pojo.ProyectoEstado;
import javafxapppracticasprofesionales.modelo.pojo.ResponsableProyecto;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: Proyecto.java 
    * Autor: Angel Jonathan Puch Hernández, Jose Luis Silva Gómez
    * Fecha: 12/06/2025
*/
public class ProyectoDAO {

    public static ArrayList<Proyecto> obtenerTodosLosProyectosActivos() throws SQLException{
        ArrayList<Proyecto> proyectos = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        
        if(conexionBD != null){
            String consulta = "SELECT p.idProyecto, p.nombre, p.numeroCupos, p.descripcion, p.objetivo, " +
                              "o.idOrganizacionVinculada, o.nombre AS nombreOrganizacion, " +
                              "r.idResponsableProyecto, CONCAT(r.nombre, ' ', r.apellidoPaterno, ' ', IFNULL(r.apellidoMaterno, '')) AS nombreResponsable, " +
                              "pe.idProyectoEstado, pe.nombreEstado " +
                              "FROM proyecto p " +
                              "INNER JOIN organizacionvinculada o ON p.OrganizacionVinculada_idOrganizacionVinculada = o.idOrganizacionVinculada " +
                              "INNER JOIN responsableproyecto r ON p.ResponsableProyecto_idResponsableProyecto = r.idResponsableProyecto " +
                              "INNER JOIN proyecto_estado pe ON p.ProyectoEstado_idProyectoEstado = pe.idProyectoEstado " +
                              "WHERE pe.nombreEstado != 'Finalizado';";

            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            
            while(resultado.next()){
                proyectos.add(convertirRegistroProyecto(resultado));
            }
            conexionBD.close();
            sentencia.close();
            resultado.close();
        } else{
            throw new SQLException("Error: Sin conexión a la Base de Datos.");
        }
        return proyectos;
    }

    public static ArrayList<Proyecto> obtenerProyectosDisponiblesParaAsignar() throws SQLException {
        ArrayList<Proyecto> proyectos = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String consulta = "SELECT p.idProyecto, p.nombre, p.numeroCupos, p.descripcion, p.objetivo, " +
                              "o.idOrganizacionVinculada, o.nombre AS nombreOrganizacion, " +
                              "r.idResponsableProyecto, CONCAT(r.nombre, ' ', r.apellidoPaterno, ' ', IFNULL(r.apellidoMaterno, '')) AS nombreResponsable, " +
                              "pe.idProyectoEstado, pe.nombreEstado " +
                              "FROM proyecto p " +
                              "INNER JOIN organizacionvinculada o ON p.OrganizacionVinculada_idOrganizacionVinculada = o.idOrganizacionVinculada " +
                              "INNER JOIN responsableproyecto r ON p.ResponsableProyecto_idResponsableProyecto = r.idResponsableProyecto " +
                              "INNER JOIN proyecto_estado pe ON p.ProyectoEstado_idProyectoEstado = pe.idProyectoEstado " +
                              "WHERE p.numeroCupos > 0 AND pe.nombreEstado = 'Disponible';";

            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            
            while (resultado.next()) {
                proyectos.add(convertirRegistroProyecto(resultado));
            }
            conexionBD.close();
            sentencia.close();
            resultado.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos.");
        }
        return proyectos;
    }

    public static ResultadoOperacion registrarProyecto(Proyecto proyecto) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "INSERT INTO proyecto (nombre, descripcion, numeroCupos, objetivo, ProyectoEstado_idProyectoEstado, "
                    + "OrganizacionVinculada_idOrganizacionVinculada, ResponsableProyecto_idResponsableProyecto) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            asignarParametrosProyecto(sentencia, proyecto);
            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {
                resultado.setIsError(false);
                resultado.setMensaje("Proyecto registrado correctamente.");
            } else {
                resultado.setIsError(true);
                resultado.setMensaje("Error al registrar el proyecto.");
            }
            conexionBD.close();
            sentencia.close();
        } else {
            throw new SQLException("Error de conexión");
        }
        return resultado;
    }
    
    public static Proyecto obtenerProyectoPorIdExpediente(int idExpediente) throws SQLException {
        Proyecto proyecto = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT p.idProyecto, p.nombre, p.descripcion, p.numeroCupos, p.objetivo " +
                         "FROM gestionpracticas.proyecto p " +
                         "JOIN gestionpracticas.expediente e ON p.idProyecto = e.Proyecto_idProyecto " +
                         "WHERE e.idExpediente = ?;";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            sentencia.setInt(1, idExpediente);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                proyecto = new Proyecto();
                proyecto.setIdProyecto(resultado.getInt("idProyecto"));
                proyecto.setNombre(resultado.getString("nombre"));
                proyecto.setDescripcion(resultado.getString("descripcion"));
                proyecto.setNumeroCupos(resultado.getInt("numeroCupos"));
                proyecto.setObjetivo(resultado.getString("objetivo"));
            }
            conexionBD.close();
            sentencia.close();
            resultado.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return proyecto;
    }

    private static Proyecto convertirRegistroProyecto(ResultSet resultado) throws SQLException {
        Proyecto proyecto = new Proyecto();
        proyecto.setIdProyecto(resultado.getInt("idProyecto"));
        proyecto.setNombre(resultado.getString("nombre"));
        proyecto.setNumeroCupos(resultado.getInt("numeroCupos"));
        proyecto.setDescripcion(resultado.getString("descripcion"));
        proyecto.setObjetivo(resultado.getString("objetivo"));
        
        OrganizacionVinculada ov = new OrganizacionVinculada();
        ov.setIdOrganizacion(resultado.getInt("idOrganizacionVinculada"));
        ov.setNombre(resultado.getString("nombreOrganizacion"));
        proyecto.setOrganizacion(ov);
        
        ResponsableProyecto responsable = new ResponsableProyecto();
        responsable.setIdResponsable(resultado.getInt("idResponsableProyecto"));
        responsable.setNombre(resultado.getString("nombreResponsable"));
        proyecto.setResponsable(responsable);
        
        ProyectoEstado estado = new ProyectoEstado();
        estado.setIdProyectoEstado(resultado.getInt("idProyectoEstado"));
        estado.setNombreEstado(resultado.getString("nombreEstado"));
        proyecto.setEstado(estado);
        
        return proyecto;
    }
    
    private static void asignarParametrosProyecto(PreparedStatement sentencia, Proyecto proyecto) throws SQLException {
        sentencia.setString(1, proyecto.getNombre());
        sentencia.setString(2, proyecto.getDescripcion());
        sentencia.setInt(3, proyecto.getNumeroCupos());
        sentencia.setString(4, proyecto.getObjetivo());
        sentencia.setInt(5, 1);//Disponible por defecto
        sentencia.setInt(6, proyecto.getOrganizacion().getIdOrganizacion());
        sentencia.setInt(7, proyecto.getResponsable().getIdResponsable());
    }
}