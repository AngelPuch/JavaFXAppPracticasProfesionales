package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Avance;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: AvanceDAO.java 
    * Autor: Angel Jonathan Puch Hernández, Rodrigo Luna Vázquez
    * Fecha: 15/06/2025
*/
public class AvanceDAO {

    public static ArrayList<Avance> obtenerDocumentosInicio(int idExpediente) throws SQLException {
        String sql = "SELECT di.idDocumentoInicio AS id, di.nombre, DATE_FORMAT(di.fechaEntregado, '%d-%m-%Y') AS fecha, " +
                     "ed.nombreEstado AS estado, di.rutaArchivo, di.comentarios_validacion " +
                     "FROM documentoinicio di " +
                     "JOIN estadodocumento ed ON di.EstadoDocumento_idEstadoDocumento = ed.idEstadoDocumento " +
                     "WHERE di.Expediente_idExpediente = ?";
        return consultarAvances(sql, idExpediente, "comentarios_validacion");
    }

    public static ArrayList<Avance> obtenerReportes(int idExpediente) throws SQLException {
        String sql = "SELECT r.idReporte AS id, r.nombreArchivo AS nombre, DATE_FORMAT(r.fechaEntregado, '%d-%m-%Y') AS fecha, " +
                     "ed.nombreEstado AS estado, r.rutaArchivo, r.comentarios_validacion " +
                     "FROM reporte r " +
                     "JOIN estadodocumento ed ON r.EstadoDocumento_idEstadoDocumento = ed.idEstadoDocumento " +
                     "WHERE r.Expediente_idExpediente = ?";
        return consultarAvances(sql, idExpediente, "comentarios_validacion");
    }
    
    public static ArrayList<Avance> obtenerDocumentosFinales(int idExpediente) throws SQLException {
        String sql = "SELECT df.idDocumentoFinal AS id, df.nombreDocumento AS nombre, DATE_FORMAT(df.fechaEntregado, '%d-%m-%Y') AS fecha, " +
                     "ed.nombreEstado AS estado, df.rutaArchivo, df.comentarios_validacion " +
                     "FROM documentofinal df " +
                     "JOIN estadodocumento ed ON df.EstadoDocumento_idEstadoDocumento = ed.idEstadoDocumento " +
                     "WHERE df.Expediente_idExpediente = ?";
        return consultarAvances(sql, idExpediente, "comentarios_validacion");
    }

    private static ArrayList<Avance> consultarAvances(String sql, int idExpediente, String nombreColumnaComentarios) throws SQLException {
        ArrayList<Avance> avances = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            sentencia.setInt(1, idExpediente);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()) {
                avances.add(convertirRegistroAvance(resultado, nombreColumnaComentarios));
            }
            conexionBD.close();
            sentencia.close();
            resultado.close();
        } else {
            throw new SQLException("Sin conexión a la Base de Datos");
        }
        return avances;
    }
    
    private static Avance convertirRegistroAvance(ResultSet resultado, String nombreColumnaComentarios) throws SQLException {
        Avance avance = new Avance();
        avance.setId(resultado.getInt("id"));
        avance.setNombre(resultado.getString("nombre"));
        avance.setFechaEntrega(resultado.getString("fecha"));
        avance.setEstado(resultado.getString("estado"));
        avance.setRutaArchivo(resultado.getString("rutaArchivo"));
        
        String comentarios = resultado.getString(nombreColumnaComentarios);
        if (comentarios == null || comentarios.trim().isEmpty()){
            avance.setComentarios("Sin comentarios");
        } else{
            avance.setComentarios(comentarios);
        }
        
        return avance;
    }
}