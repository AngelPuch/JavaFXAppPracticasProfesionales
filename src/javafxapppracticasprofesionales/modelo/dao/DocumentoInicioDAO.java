package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;

public class DocumentoInicioDAO {

    public static ResultadoOperacion guardarDocumentoInicio(String nombreDocumento, String rutaArchivo, String nombreArchivo, int idEntrega, int idExpediente) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String sql = "INSERT INTO documentoinicio " +
                             "(nombre, fechaEntregado, comentarios_validacion, nombreArchivo, rutaArchivo, " +
                             "EntregaDocumentoInicio_idEntregaDocumentoInicio, Expediente_idExpediente, EstadoDocumento_idEstadoDocumento) " +
                             "VALUES (?, ?, NULL, ?, ?, ?, ?, ?)";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            asignarParametrosDocumentoInicio(sentencia, nombreDocumento, rutaArchivo, nombreArchivo, idEntrega, idExpediente);
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas == 1) {
                resultado.setIsError(false);
                resultado.setMensaje("Documento guardado correctamente.");
            } else {
                resultado.setIsError(true);
                resultado.setMensaje("Error al guardar el documento");
            }
            conexionBD.close();
            sentencia.close();
        } else {
            throw new SQLException("Sin conexión a la base de datos");
        }
        return resultado;
    }
    
    private static void asignarParametrosDocumentoInicio(PreparedStatement sentencia, String nombreDocumento, String rutaArchivo, String nombreArchivo, int idEntrega, int idExpediente) throws SQLException {
        sentencia.setString(1, nombreDocumento);
        sentencia.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        sentencia.setString(3, nombreArchivo);
        sentencia.setString(4, rutaArchivo);
        sentencia.setInt(5, idEntrega);
        sentencia.setInt(6, idExpediente);
        sentencia.setInt(7, 2);
    }
}
