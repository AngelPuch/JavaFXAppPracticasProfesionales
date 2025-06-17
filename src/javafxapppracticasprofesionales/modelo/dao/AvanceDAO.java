// Archivo: JavaFXAppPracticasProfesionales/src/javafxapppracticasprofesionales/modelo/dao/AvanceDAO.java
package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Avance;

public class AvanceDAO {

    public static ArrayList<Avance> obtenerDocumentosInicio(int idExpediente) throws SQLException {
        // CORRECCIÓN: Se añade 'di.rutaArchivo' a la consulta
        String sql = "SELECT di.idDocumentoInicio AS id, di.nombre, DATE_FORMAT(di.fechaEntregado, '%d-%m-%Y') AS fecha, " +
                     "ed.nombreEstado AS estado, di.rutaArchivo " +
                     "FROM documentoinicio di " +
                     "JOIN estadodocumento ed ON di.EstadoDocumento_idEstadoDocumento = ed.idEstadoDocumento " +
                     "WHERE di.Expediente_idExpediente = ?";
        return consultarAvances(sql, idExpediente);
    }

    public static ArrayList<Avance> obtenerReportes(int idExpediente) throws SQLException {
        // CORRECCIÓN: Se añade 'r.rutaArchivo' a la consulta
        String sql = "SELECT r.idReporte AS id, r.nombreArchivo AS nombre, DATE_FORMAT(r.fechaEntregado, '%d-%m-%Y') AS fecha, " +
                     "ed.nombreEstado AS estado, r.rutaArchivo " +
                     "FROM reporte r " +
                     "JOIN estadodocumento ed ON r.EstadoDocumento_idEstadoDocumento = ed.idEstadoDocumento " +
                     "WHERE r.Expediente_idExpediente = ?";
        return consultarAvances(sql, idExpediente);
    }
    
    public static ArrayList<Avance> obtenerDocumentosFinales(int idExpediente) throws SQLException {
        // CORRECCIÓN: Se añade 'df.rutaArchivo' a la consulta
        String sql = "SELECT df.idDocumentoFinal AS id, df.nombreDocumento AS nombre, DATE_FORMAT(df.fechaEntregado, '%d-%m-%Y') AS fecha, " +
                     "ed.nombreEstado AS estado, df.rutaArchivo " +
                     "FROM documentofinal df " +
                     "JOIN estadodocumento ed ON df.EstadoDocumento_idEstadoDocumento = ed.idEstadoDocumento " +
                     "WHERE df.Expediente_idExpediente = ?";
        return consultarAvances(sql, idExpediente);
    }

    private static ArrayList<Avance> consultarAvances(String sql, int idExpediente) throws SQLException {
        ArrayList<Avance> avances = new ArrayList<>();
        Connection conexion = ConexionBD.abrirConexion();
        if (conexion != null) {
            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
                consulta.setInt(1, idExpediente);
                ResultSet resultado = consulta.executeQuery();
                while(resultado.next()) {
                    Avance avance = new Avance();
                    avance.setId(resultado.getInt("id"));
                    avance.setNombre(resultado.getString("nombre"));
                    avance.setFechaEntrega(resultado.getString("fecha"));
                    avance.setEstado(resultado.getString("estado"));
                    avance.setRutaArchivo(resultado.getString("rutaArchivo")); // <--- CORRECCIÓN: Se obtiene la ruta
                    avances.add(avance);
                }
            } finally {
                conexion.close();
            }
        }
        return avances;
    }
}