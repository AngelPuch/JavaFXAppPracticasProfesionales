package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;


public class ExpedienteDAO {

    public static ResultadoOperacion asignarProyectoAEstudiante(int idProyecto, int idEstudiante) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            try {
                conexionBD.setAutoCommit(false);

                String sqlAsignar = "UPDATE expediente ex " +
                    "JOIN (SELECT ex.idExpediente FROM expediente ex " +
                    "JOIN inscripcion i ON ex.Inscripcion_idInscripcion = i.idInscripcion " +
                    "WHERE i.Estudiante_idEstudiante = ? AND ex.Proyecto_idProyecto IS NULL " +
                    "ORDER BY ex.idExpediente DESC LIMIT 1) temp " +
                    "ON ex.idExpediente = temp.idExpediente " +
                    "SET ex.Proyecto_idProyecto = ?";
                PreparedStatement psAsignar = conexionBD.prepareStatement(sqlAsignar);
                psAsignar.setInt(1, idEstudiante);
                psAsignar.setInt(2, idProyecto);
                int filasAfectadas = psAsignar.executeUpdate();

                if (filasAfectadas > 0) {
                    String sqlActualizarCupo = "UPDATE proyecto SET numeroCupos = numeroCupos - 1 WHERE idProyecto = ?";
                    PreparedStatement psActualizarCupo = conexionBD.prepareStatement(sqlActualizarCupo);
                    psActualizarCupo.setInt(1, idProyecto);
                    psActualizarCupo.executeUpdate();
                    
                    conexionBD.commit();
                    resultado.setIsError(false);
                    resultado.setMensaje("Proyecto asignado correctamente.");

                } else {
                    resultado.setIsError(true);
                    resultado.setMensaje("No se encontró un expediente activo y sin proyecto para este estudiante.");
                    conexionBD.rollback();
                }

                psAsignar.close();

            } catch (SQLException e) {
                conexionBD.rollback();
            } finally {
                conexionBD.close();
            }
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return resultado;
    }
    
    public static int obtenerIdExpedientePorEstudiante(int idEstudiante) throws SQLException {
        int idExpediente = 0;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT ex.idExpediente " +
                         "FROM gestionpracticas.expediente ex " +
                         "JOIN gestionpracticas.inscripcion i ON ex.Inscripcion_idInscripcion = i.idInscripcion " +
                         "WHERE i.Estudiante_idEstudiante = ?;";
            try (PreparedStatement sentencia = conexionBD.prepareStatement(sql)) {
                sentencia.setInt(1, idEstudiante);
                try (ResultSet resultado = sentencia.executeQuery()) {
                    if (resultado.next()) {
                        idExpediente = resultado.getInt("idExpediente");
                    }
                }
            } finally {
                conexionBD.close();
            }
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return idExpediente;
    }
    
    public static int obtenerIdExpedienteActivo(int idEstudiante) throws SQLException {
        int idExpediente = -1;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT ex.idExpediente FROM expediente ex " +
                         "JOIN inscripcion i ON ex.Inscripcion_idInscripcion = i.idInscripcion " +
                         "WHERE i.Estudiante_idEstudiante = ? AND ex.Estado_idEstado = 1 " + 
                         "ORDER BY ex.idExpediente DESC LIMIT 1";
            try (PreparedStatement sentencia = conexionBD.prepareStatement(sql)) {
                sentencia.setInt(1, idEstudiante);
                ResultSet resultado = sentencia.executeQuery();
                if (resultado.next()) {
                    idExpediente = resultado.getInt("idExpediente");
                }
            } finally {
                conexionBD.close();
            }
        }
        return idExpediente;
    }
}