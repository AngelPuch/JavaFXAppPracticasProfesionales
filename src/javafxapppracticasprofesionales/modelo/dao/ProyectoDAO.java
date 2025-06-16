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

public class ProyectoDAO {

    /**
     * Obtiene todos los proyectos cuyo estado es 'Disponible'.
     * Ya no filtra por periodo escolar.
     */
    public static ArrayList<Proyecto> obtenerTodosLosProyectosActivos() throws SQLException {
        ArrayList<Proyecto> proyectos = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT p.idProyecto, p.nombre, p.descripcion, p.objetivo, p.numeroCupos, " +
                         "ov.idOrganizacionVinculada, ov.nombre AS nombreOrganizacion, " +
                         "r.idResponsableProyecto, r.nombre AS nombreResponsable, " +
                         "pe.idProyectoEstado, pe.nombreEstado " +
                         "FROM proyecto p " +
                         "JOIN organizacionvinculada ov ON p.OrganizacionVinculada_idOrganizacionVinculada = ov.idOrganizacionVinculada " +
                         "JOIN responsableproyecto r ON p.ResponsableProyecto_idResponsableProyecto = r.idResponsableProyecto " +
                         "JOIN proyecto_estado pe ON p.ProyectoEstado_idProyectoEstado = pe.idProyectoEstado " +
                         "WHERE pe.nombreEstado = 'Disponible'";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();
            
            while(resultado.next()){
                proyectos.add(convertirRegistroProyecto(resultado));
            }
            conexionBD.close();
            sentencia.close();
            resultado.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return proyectos;
    }
    
    /**
     * Obtiene los proyectos que están 'Disponibles' y tienen cupos libres para ser asignados.
     */
    public static ArrayList<Proyecto> obtenerProyectosDisponiblesParaAsignar() throws SQLException {
        ArrayList<Proyecto> proyectos = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT p.idProyecto, p.nombre, p.descripcion, p.numeroCupos, p.objetivo, " +
                         "ov.idOrganizacionVinculada, ov.nombre AS nombreOrganizacion, " +
                         "r.idResponsableProyecto, r.nombre AS nombreResponsable, " +
                         "pe.idProyectoEstado, pe.nombreEstado " +
                         "FROM proyecto p " +
                         "JOIN organizacionvinculada ov ON p.OrganizacionVinculada_idOrganizacionVinculada = ov.idOrganizacionVinculada " +
                         "JOIN responsableproyecto r ON p.ResponsableProyecto_idResponsableProyecto = r.idResponsableProyecto " +
                         "JOIN proyecto_estado pe ON p.ProyectoEstado_idProyectoEstado = pe.idProyectoEstado " +
                         "WHERE pe.nombreEstado = 'Disponible' AND p.numeroCupos > (" +
                         "    SELECT COUNT(*) FROM expediente ex2 WHERE ex2.Proyecto_idProyecto = p.idProyecto" +
                         ")";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                proyectos.add(convertirRegistroProyecto(resultado));
            }
            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return proyectos;
    }

    public static ResultadoOperacion registrarProyecto(Proyecto proyectoNuevo) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "INSERT INTO proyecto (nombre, descripcion, numeroCupos, objetivo, " +
                             "OrganizacionVinculada_idOrganizacionVinculada, " +
                             "ResponsableProyecto_idResponsableProyecto) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            asignarParametrosProyecto(sentencia, proyectoNuevo);
            int filasAfectadas = sentencia.executeUpdate();
            
            if (filasAfectadas == 1) {
                resultado.setIsError(false);
                resultado.setMensaje("Proyecto registrado correctamente.");
            } else {
                resultado.setIsError(true);
                resultado.setMensaje("Lo sentimos, por el momento no se puede registrar el proyecto, por favor inténtelo más tarde");
            }
          
            conexionBD.close();
            sentencia.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return resultado;
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
    
    private static void asignarParametrosProyecto(PreparedStatement sentencia, Proyecto proyectoNuevo) throws SQLException{
        sentencia.setString(1, proyectoNuevo.getNombre());
        sentencia.setString(2, proyectoNuevo.getDescripcion());
        sentencia.setInt(3, proyectoNuevo.getNumeroCupos());
        sentencia.setString(4, proyectoNuevo.getObjetivo());
        sentencia.setInt(5, proyectoNuevo.getOrganizacion().getIdOrganizacion());
        sentencia.setInt(6, proyectoNuevo.getResponsable().getIdResponsable());
    }
}