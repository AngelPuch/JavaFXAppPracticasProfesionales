package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;
import javafxapppracticasprofesionales.modelo.pojo.Grupo;
import javafxapppracticasprofesionales.modelo.pojo.Periodo;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;

public class ProgramarEntregaDAO {
    public static ResultadoOperacion programarEntrega(String nombre, String descripcion, String fechaInicio, String fechaFin, int idGrupoEE, String tabla) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setIsError(true);
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = String.format("INSERT INTO %s (nombre, descripcion, fechaInicio, fechaFin, grupoEE_idgrupoEE) VALUES (?, ?, ?, ?, ?)", tabla);
            
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, descripcion);
            sentencia.setString(3, fechaInicio);
            sentencia.setString(4, fechaFin);
            sentencia.setInt(5, idGrupoEE);
            
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
    
    public static ResultadoOperacion programarEntregaPeriodoActual(Entrega nuevaEntrega, String tabla) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setIsError(true);
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            try {
                Periodo periodoActual = PeriodoDAO.obtenerPeriodoActual();
                if (periodoActual == null) {
                    resultado.setMensaje("No se encontró un periodo escolar activo para programar la entrega.");
                    return resultado;
                }

                ArrayList<Grupo> grupos = GrupoDAO.obtenerGruposPorPeriodo(periodoActual.getIdPeriodo());
                if (grupos.isEmpty()) {
                    resultado.setMensaje("No se encontraron grupos en el periodo actual para asignar la entrega.");
                    return resultado;
                }

                conexionBD.setAutoCommit(false);

                String sql = String.format("INSERT INTO %s (nombre, descripcion, fechaInicio, fechaFin, grupoEE_idgrupoEE) VALUES (?, ?, ?, ?, ?)", tabla);
                PreparedStatement sentencia = conexionBD.prepareStatement(sql);

                for (Grupo grupo : grupos) {
                    sentencia.setString(1, nuevaEntrega.getNombre());
                    sentencia.setString(2, nuevaEntrega.getDescripcion());

                    LocalDate fechaInicio = LocalDate.parse(nuevaEntrega.getFechaInicio());
                    LocalDate fechaFin = LocalDate.parse(nuevaEntrega.getFechaFin());
                    sentencia.setDate(3, java.sql.Date.valueOf(fechaInicio));
                    sentencia.setDate(4, java.sql.Date.valueOf(fechaFin));

                    sentencia.setInt(5, grupo.getIdGrupo());
                    sentencia.executeUpdate();
                }

                conexionBD.commit();
                resultado.setIsError(false);
                resultado.setMensaje("La entrega ha sido programada correctamente para todos los grupos del periodo actual.");

            } catch (SQLException e) {
                resultado.setMensaje("Ocurrió un error y no se pudo completar la operación: " + e.getMessage());
                try {
                    if (conexionBD != null && !conexionBD.getAutoCommit()) {
                        conexionBD.rollback();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } finally {
                if (conexionBD != null) {
                    conexionBD.close();
                }
            }
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return resultado;
    }
}
