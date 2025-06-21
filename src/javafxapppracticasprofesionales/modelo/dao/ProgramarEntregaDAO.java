package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;

public class ProgramarEntregaDAO {

    public static ResultadoOperacion programarNuevaEntrega(Entrega nuevaEntrega, String tabla, int idGrupoEE, int idTipoDocumento) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setIsError(true);
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            
            // Determinar el nombre de la columna de la llave foránea dinámicamente
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
                sentencia.setInt(8, idTipoDocumento); // Se inserta el ID del tipo de documento

                int filasAfectadas = sentencia.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setIsError(false);
                    resultado.setMensaje("La entrega ha sido programada correctamente.");
                } else {
                    resultado.setMensaje("No se pudo programar la entrega.");
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