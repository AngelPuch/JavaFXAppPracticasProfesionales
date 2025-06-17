package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Estudiante;
import javafxapppracticasprofesionales.modelo.pojo.EstudianteConProyecto;
import javafxapppracticasprofesionales.modelo.pojo.InfoEstudianteSesion;

public class EstudianteDAO {

    public static ArrayList<EstudianteConProyecto> obtenerEstudiantesConProyecto() throws SQLException {
        ArrayList<EstudianteConProyecto> estudiantes = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT e.nombre AS nombreEstudiante, e.matricula, p.nombre AS nombreProyecto, e.semestre, e.correo " +
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
    
    public static ArrayList<Estudiante> obtenerEstudiantesParaEvaluar() throws SQLException {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT DISTINCT e.idEstudiante, e.nombre, e.matricula, e.semestre, e.correo, u.idUsuario " +
                         "FROM estudiante e " +
                         "JOIN usuario u ON e.idUsuario = u.idUsuario " +
                         "JOIN inscripcion i ON e.idEstudiante = i.Estudiante_idEstudiante " +
                         "JOIN expediente ex ON i.idInscripcion = ex.Inscripcion_idInscripcion " +
                         "WHERE ex.Proyecto_idProyecto IS NOT NULL;";
            try (PreparedStatement sentencia = conexionBD.prepareStatement(sql);
                 ResultSet resultado = sentencia.executeQuery()) {

                while (resultado.next()) {
                    Estudiante estudiante = new Estudiante();
                    estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
                    estudiante.setNombre(resultado.getString("nombre"));
                    estudiante.setMatricula(resultado.getString("matricula"));
                    estudiante.setSemestre(resultado.getInt("semestre"));
                    estudiante.setCorreo(resultado.getString("correo"));
                    estudiantes.add(estudiante);
                }
            } finally {
                conexionBD.close();
            }
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return estudiantes;
    }
    
    public static Estudiante obtenerEstudiantePorIdUsuario(int idUsuario) throws SQLException {
        Estudiante estudiante = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT idEstudiante, nombre, matricula, semestre, correo, idUsuario " +
                         "FROM estudiante WHERE idUsuario = ?";
            try (PreparedStatement sentencia = conexionBD.prepareStatement(sql)) {
                sentencia.setInt(1, idUsuario);
                ResultSet resultado = sentencia.executeQuery();
                if (resultado.next()) {
                    estudiante = convertirRegistroEstudiante(resultado);
                }
            } finally {
                conexionBD.close();
            }
        }
        return estudiante;
    }

    public static InfoEstudianteSesion obtenerInfoEstudianteParaSesion(int idUsuario) throws SQLException {
        InfoEstudianteSesion info = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            try {
                String sql = "SELECT e.idEstudiante, i.grupoEE_idgrupoEE, ex.idExpediente " +
                             "FROM estudiante e " +
                             "JOIN inscripcion i ON e.idEstudiante = i.Estudiante_idEstudiante " +
                             "LEFT JOIN expediente ex ON i.idInscripcion = ex.Inscripcion_idInscripcion " +
                             "WHERE e.idUsuario = ?";
                
                PreparedStatement sentencia = conexionBD.prepareStatement(sql);
                sentencia.setInt(1, idUsuario);
                ResultSet resultado = sentencia.executeQuery();
                
                if (resultado.next()) {
                    info = new InfoEstudianteSesion();
                    info.setIdEstudiante(resultado.getInt("idEstudiante"));
                    info.setIdGrupo(resultado.getInt("grupoEE_idgrupoEE"));
                    info.setIdExpediente(resultado.getInt("idExpediente"));
                }
            } finally {
                conexionBD.close();
            }
        }
        return info;
    }
    
    public static boolean verificarProyectoAsignado(int idEstudiante) throws SQLException {
        boolean tieneProyecto = false;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT COUNT(ex.Proyecto_idProyecto) AS totalProyectos " +
                         "FROM estudiante e " +
                         "JOIN inscripcion i ON e.idEstudiante = i.Estudiante_idEstudiante " +
                         "JOIN expediente ex ON i.idInscripcion = ex.Inscripcion_idInscripcion " +
                         "WHERE e.idEstudiante = ? AND ex.Proyecto_idProyecto IS NOT NULL";
            try (PreparedStatement sentencia = conexionBD.prepareStatement(sql)) {
                sentencia.setInt(1, idEstudiante);
                ResultSet resultado = sentencia.executeQuery();
                if (resultado.next()) {
                    if (resultado.getInt("totalProyectos") > 0) {
                        tieneProyecto = true;
                    }
                }
            } finally {
                conexionBD.close();
            }
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return tieneProyecto;
    }

    public static Estudiante getEstudiantePorMatricula(String matricula) throws SQLException {
        Estudiante estudiante = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT idEstudiante, nombre, matricula, semestre, correo, idUsuario " +
                         "FROM estudiante WHERE matricula = ?";
            try (PreparedStatement sentencia = conexionBD.prepareStatement(sql)) {
                sentencia.setString(1, matricula);
                ResultSet resultado = sentencia.executeQuery();
                if (resultado.next()) {
                    estudiante = new Estudiante();
                    estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
                    estudiante.setNombre(resultado.getString("nombre"));
                    estudiante.setMatricula(resultado.getString("matricula"));
                    estudiante.setSemestre(resultado.getInt("semestre"));
                    estudiante.setCorreo(resultado.getString("correo"));
                }
            } finally {
                conexionBD.close();
            }
        }
        return estudiante;
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
        estudianteConProyecto.setSemestre(resultado.getInt("semestre"));
        estudianteConProyecto.setCorreo(resultado.getString("correo"));
        return estudianteConProyecto;
    }
}
