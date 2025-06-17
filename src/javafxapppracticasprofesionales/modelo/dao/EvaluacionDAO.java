package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Evaluacion;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;

public class EvaluacionDAO {

    public static ResultadoOperacion registrarEvaluacion(Evaluacion evaluacion) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setIsError(true);
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "INSERT INTO evaluacion (calificacionTotal, fecha, motivo, comentarios, " +
                         "Usuario_idUsuario, TipoEvaluacion_idTipoEvaluacion, Expediente_idExpediente) " +
                         "VALUES (?, CURDATE(), ?, ?, ?, ?, ?)";
            try (PreparedStatement sentencia = conexionBD.prepareStatement(sql)) {
                sentencia.setFloat(1, evaluacion.getCalificacionTotal());
                sentencia.setString(2, evaluacion.getMotivo());
                sentencia.setString(3, evaluacion.getComentarios());
                sentencia.setInt(4, evaluacion.getIdUsuario());
                sentencia.setInt(5, evaluacion.getIdTipoEvaluacion());
                sentencia.setInt(6, evaluacion.getIdExpediente());

                int filasAfectadas = sentencia.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setIsError(false);
                    resultado.setMensaje("Evaluación guardada correctamente.");
                } else {
                    resultado.setMensaje("No se pudo registrar la evaluación.");
                }
            } finally {
                conexionBD.close();
            }
        } else {
            resultado.setMensaje("No hay conexión con la base de datos.");
        }
        return resultado;
    }
}