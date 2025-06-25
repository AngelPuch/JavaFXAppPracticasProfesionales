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

public class ProgramarEntregaDAO {

    /**
     * Verifica si ya existe una entrega programada para un tipo de documento y grupo específicos.
     * * @param tabla La tabla de entrega a consultar.
     * @param columnaFkTipoDoc El nombre de la columna de la FK del tipo de documento.
     * @param idGrupoEE El ID del grupo.
     * @param idTipoDocumento El ID del tipo de documento.
     * @param conexion La conexión a la BD.
     * @return true si ya existe, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    private static boolean verificarEntregaExistente(String tabla, String columnaFkTipoDoc, 
            int idGrupoEE, int idTipoDocumento, Connection conexion) throws SQLException {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE grupoEE_idgrupoEE = ? AND %s = ?", tabla, columnaFkTipoDoc);
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idGrupoEE);
            sentencia.setInt(2, idTipoDocumento);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public static ResultadoOperacion programarNuevaEntrega(Entrega nuevaEntrega, String tabla, int idGrupoEE, int idTipoDocumento) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setIsError(true);
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            try {
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

                // VALIDACIÓN: Verificar si la entrega ya fue programada
                if (verificarEntregaExistente(tabla, columnaFkTipoDoc, idGrupoEE, idTipoDocumento, conexionBD)) {
                    resultado.setIsError(true);
                    resultado.setMensaje("Ya existe una entrega programada para este tipo de documento y grupo. No se puede duplicar.");
                    return resultado;
                }

                String sql = String.format(
                    "INSERT INTO %s (nombre, descripcion, fechaInicio, fechaFin, horaInicio, horaFin, grupoEE_idgrupoEE, %s) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", tabla, columnaFkTipoDoc
                );
            
                try (PreparedStatement sentencia = conexionBD.prepareStatement(sql)) {
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

                    int filasAfectadas = sentencia.executeUpdate();
                    if (filasAfectadas > 0) {
                        resultado.setIsError(false);
                        resultado.setMensaje("La entrega ha sido programada correctamente.");
                    } else {
                        resultado.setMensaje("No se pudo programar la entrega.");
                    }
                }
            } finally {
                conexionBD.close();
            }
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return resultado;
    }
}