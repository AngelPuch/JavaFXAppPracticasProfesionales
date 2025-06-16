package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Estudiante;
import javafxapppracticasprofesionales.modelo.pojo.EstudianteConProyecto;

/** 
* Project: JavaFX Sales System 
* File: ClassName.java 
* Author: Jose Luis Silva Gomez 
* Date: YYYY-MM-DD 
* Description: Brief description of the file's purpose. 
*/
public class EstudianteDAO {

    public static ArrayList<EstudianteConProyecto> obtenerEstudiantesConProyecto() throws SQLException {
        ArrayList<EstudianteConProyecto> estudiantes = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT e.nombre AS nombreEstudiante, e.matricula, p.nombre AS nombreProyecto " +
                         "FROM estudiante e " +
                         "JOIN inscripcion i ON e.idEstudiante = i.Estudiante_idEstudiante " +
                         "JOIN expediente ex ON i.idInscripcion = ex.Inscripcion_idInscripcion " +
                         "JOIN proyecto p ON ex.Proyecto_idProyecto = p.idProyecto " +
                         "WHERE p.idProyecto IS NOT NULL";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                estudiantes.add(convertirRegistroEstudianteConProyecto(resultado));
            }
            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return estudiantes;
    }

    public static ArrayList<Estudiante> obtenerEstudiantesSinProyecto() throws SQLException {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT e.idEstudiante, e.nombre, e.matricula, e.semestre, e.correo " +
                         "FROM estudiante e " +
                         "LEFT JOIN inscripcion i ON e.idEstudiante = i.Estudiante_idEstudiante " +
                         "LEFT JOIN expediente ex ON i.idInscripcion = ex.Inscripcion_idInscripcion " +
                         "WHERE ex.Proyecto_idProyecto IS NULL";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                estudiantes.add(convertirRegistroEstudiante(resultado));
            }
            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return estudiantes;
    }

    private static Estudiante convertirRegistroEstudiante(ResultSet resultado) throws SQLException {
        Estudiante estudiante = new Estudiante();
        estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
        estudiante.setNombre(resultado.getString("nombre"));
        estudiante.setMatricula(resultado.getString("matricula"));
        estudiante.setSemestre(resultado.getInt("semestre"));
        estudiante.setCorreo(resultado.getString("correo"));
        return estudiante;
    }
    
    private static EstudianteConProyecto convertirRegistroEstudianteConProyecto(ResultSet resultado) throws SQLException {
        EstudianteConProyecto estudianteConProyecto = new EstudianteConProyecto();
        estudianteConProyecto.setNombreEstudiante(resultado.getString("nombreEstudiante"));
        estudianteConProyecto.setMatricula(resultado.getString("matricula"));
        estudianteConProyecto.setNombreProyecto(resultado.getString("nombreProyecto"));
        return estudianteConProyecto;
    }
}
