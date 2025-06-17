package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;

/** 
* Project: JavaFXAppPracticasProfesionales 
* File: DocumentoInicio.java 
* Author: Jose Luis Silva Gomez 
* Date: 2025-06-16 
* Description: Brief description of the file's purpose. 
*/
public class DocumentoInicioDAO {

    public static ResultadoOperacion guardarDocumentoInicio(String nombreDocumento, String rutaArchivo, String nombreArchivo, int idEntrega, int idExpediente) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            try {
                // El ID 2 corresponde a 'Entregado' en la tabla 'estadodocumento'
                int idEstadoDocumentoInicial = 2; 
                String sql = "INSERT INTO documentoinicio " +
                             "(nombre, fechaEntregado, comentarios_validacion, nombreArchivo, rutaArchivo, " +
                             "EntregaDocumentoInicio_idEntregaDocumentoInicio, Expediente_idExpediente, EstadoDocumento_idEstadoDocumento) " +
                             "VALUES (?, ?, NULL, ?, ?, ?, ?, ?)";

                PreparedStatement sentencia = conexionBD.prepareStatement(sql);
                sentencia.setString(1, nombreDocumento);
                // Usamos Timestamp para guardar fecha y hora actual
                sentencia.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now())); 
                sentencia.setString(3, nombreArchivo);
                sentencia.setString(4, rutaArchivo);
                sentencia.setInt(5, idEntrega);
                sentencia.setInt(6, idExpediente);
                sentencia.setInt(7, idEstadoDocumentoInicial);

                int filasAfectadas = sentencia.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setIsError(false);
                    resultado.setMensaje("Documento guardado correctamente.");
                } else {
                    resultado.setIsError(true);
                    resultado.setMensaje("Error al guardar el documento");
                }
                
                sentencia.close();
            } finally {
                conexionBD.close();
            }
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }

        return resultado;
    }
}
