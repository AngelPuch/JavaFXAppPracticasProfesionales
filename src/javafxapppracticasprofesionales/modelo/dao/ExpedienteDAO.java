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
                // Iniciar transacción para asegurar que ambas operaciones (asignar y actualizar cupo) se completen
                conexionBD.setAutoCommit(false);

                // Sentencia 1: Asignar el proyecto al expediente del estudiante
                String sqlAsignar = "UPDATE expediente SET Proyecto_idProyecto = ? WHERE idExpediente = (" +
                                    "SELECT idExpediente FROM (SELECT ex.idExpediente FROM expediente ex " +
                                    "JOIN inscripcion i ON ex.Inscripcion_idInscripcion = i.idInscripcion " +
                                    "WHERE i.Estudiante_idEstudiante = ? AND ex.Proyecto_idProyecto IS NULL " +
                                    "ORDER BY ex.idExpediente DESC LIMIT 1) AS temp)";
                PreparedStatement psAsignar = conexionBD.prepareStatement(sqlAsignar);
                psAsignar.setInt(1, idProyecto);
                psAsignar.setInt(2, idEstudiante);
                int filasAfectadas = psAsignar.executeUpdate();

                if (filasAfectadas > 0) {
                    // Sentencia 2: Actualizar el número de cupos del proyecto
                    String sqlActualizarCupo = "UPDATE proyecto SET numeroCupos = numeroCupos - 1 WHERE idProyecto = ?";
                    PreparedStatement psActualizarCupo = conexionBD.prepareStatement(sqlActualizarCupo);
                    psActualizarCupo.setInt(1, idProyecto);
                    psActualizarCupo.executeUpdate();
                    
                    // Si todo fue bien, confirmar la transacción
                    conexionBD.commit();
                    resultado.setIsError(false);
                    resultado.setMensaje("Proyecto asignado correctamente.");

                } else {
                    // Si no se afectaron filas, significa que no se encontró un expediente válido para asignar
                    resultado.setIsError(true);
                    resultado.setMensaje("No se encontró un expediente activo y sin proyecto para este estudiante.");
                    // Revertir la transacción por si acaso
                    conexionBD.rollback();
                }

                psAsignar.close();

            } catch (SQLException e) {
                // Si algo falla, revertir todos los cambios de la transacción
                conexionBD.rollback();
                throw e; // Relanzar la excepción para que la capa superior la maneje
            } finally {
                conexionBD.close();
            }
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return resultado;
    }

    public static ResultadoOperacion crearExpedienteVacio(int idInscripcion) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            // El estado '1' corresponde a 'Activo' según el script de la BD
            String sql = "INSERT INTO expediente (Estado_idEstado, Inscripcion_idInscripcion, Proyecto_idProyecto) VALUES (1, ?, NULL)";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            sentencia.setInt(1, idInscripcion);
            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas == 1) {
                resultado.setIsError(false);
                resultado.setMensaje("Expediente inicial creado para la inscripción.");
            } else {
                resultado.setIsError(true);
                resultado.setMensaje("No se pudo crear el expediente inicial.");
            }

            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return resultado;
    }
    
    public static int obtenerIdExpedientePorEstudiante(int idEstudiante) throws SQLException {
        int idExpediente = 0;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            // La consulta navega desde estudiante -> inscripcion -> expediente para encontrar el ID
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
}