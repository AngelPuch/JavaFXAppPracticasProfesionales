package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: ProgramarEntregaDAO.java 
    * Autor: Angel Jonathan Puch Hernández
    * Fecha: 13/06/2025
*/
public class ProgramarEntregaDAO {

    public static ResultadoOperacion programarNuevaEntrega(Entrega nuevaEntrega, String tabla, int idGrupoEE, int idTipoDocumento) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setIsError(true);
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String columnaFkTipoDoc = "";
            switch(tabla) {
                case "entregadocumentoinicio":
                    columnaFkTipoDoc = "TipoDocumentoInicio_idTipoDocumentoInicio";
                    break;
                case "entregareporte":
                    columnaFkTipoDoc = "TipoDocumentoReporte_idTipoDocumentoReporte";
                    break;
                case "entregadocumentofinal":
                    columnaFkTipoDoc = "TipoDocumentoFinal_idTipoDocumentoFinal";
                    break;
                default:
                    throw new SQLException("Nombre de tabla de entrega no válido: " + tabla);
            }
            if (verificarEntregaExistente(tabla, columnaFkTipoDoc, idGrupoEE, idTipoDocumento, conexionBD)) {
                resultado.setIsError(true);
                resultado.setMensaje("Ya existe una entrega programada para este tipo de documento y grupo. No se puede duplicar.");
                return resultado;
            }
            String sql = String.format("INSERT INTO %s (nombre, descripcion, fechaInicio, fechaFin, "
                    + "horaInicio, horaFin, grupoEE_idgrupoEE, %s) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", tabla, columnaFkTipoDoc);
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            asignarParametrosEntrega(sentencia, nuevaEntrega, idGrupoEE, idTipoDocumento);
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas > 0) {
                resultado.setIsError(false);
                resultado.setMensaje("La entrega ha sido programada correctamente.");
            } else {
                resultado.setMensaje("No se pudo programar la entrega.");
            }
            conexionBD.close();
            sentencia.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return resultado;
    }
    
    private static boolean verificarEntregaExistente(String tabla, String columnaFkTipoDoc, 
            int idGrupoEE, int idTipoDocumento, Connection conexion) throws SQLException {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE grupoEE_idgrupoEE = ? AND %s = ?", tabla, columnaFkTipoDoc);
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, idGrupoEE);
        sentencia.setInt(2, idTipoDocumento);
        ResultSet resultado = sentencia.executeQuery();
        if (resultado.next()) {
            return resultado.getInt(1) > 0;
        }
        sentencia.close();
        resultado.close();
        return false;
    }
    
    private static void asignarParametrosEntrega(PreparedStatement sentencia, 
            Entrega nuevaEntrega, int idGrupoEE, int idTipoDocumento) throws SQLException {
        sentencia.setString(1, nuevaEntrega.getNombre());
        sentencia.setString(2, nuevaEntrega.getDescripcion());
        sentencia.setDate(3, java.sql.Date.valueOf(LocalDate.parse(nuevaEntrega.getFechaInicio())));
        sentencia.setDate(4, java.sql.Date.valueOf(LocalDate.parse(nuevaEntrega.getFechaFin())));
        if (nuevaEntrega.getHoraInicio() != null && !nuevaEntrega.getHoraInicio().isEmpty()) {
            sentencia.setTime(5, Time.valueOf(nuevaEntrega.getHoraInicio() + ":00"));
        } else {
            sentencia.setNull(5, java.sql.Types.TIME);
        }
        if (nuevaEntrega.getHoraFin() != null && !nuevaEntrega.getHoraFin().isEmpty()) {
            sentencia.setTime(6, Time.valueOf(nuevaEntrega.getHoraFin() + ":00"));
        } else {
            sentencia.setNull(6, java.sql.Types.TIME);
        }
        sentencia.setInt(7, idGrupoEE);
        sentencia.setInt(8, idTipoDocumento);
    }
}