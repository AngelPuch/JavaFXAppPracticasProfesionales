/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
                // 1. Obtener el periodo actual
                Periodo periodoActual = PeriodoDAO.obtenerPeriodoActual();
                if (periodoActual == null) {
                    resultado.setMensaje("No se encontró un periodo escolar activo para programar la entrega.");
                    return resultado;
                }

                // 2. Obtener todos los grupos de ese periodo
                ArrayList<Grupo> grupos = GrupoDAO.obtenerGruposPorPeriodo(periodoActual.getIdPeriodo());
                if (grupos.isEmpty()) {
                    resultado.setMensaje("No se encontraron grupos en el periodo actual para asignar la entrega.");
                    return resultado;
                }

                // 3. Iniciar Transacción
                conexionBD.setAutoCommit(false);

                String sql = String.format("INSERT INTO %s (nombre, descripcion, fechaInicio, fechaFin, grupoEE_idgrupoEE) VALUES (?, ?, ?, ?, ?)", tabla);
                PreparedStatement sentencia = conexionBD.prepareStatement(sql);

                // 4. Registrar la entrega para cada grupo
                for (Grupo grupo : grupos) {
                    sentencia.setString(1, nuevaEntrega.getNombre());
                    sentencia.setString(2, nuevaEntrega.getDescripcion());
                    sentencia.setString(3, nuevaEntrega.getFechaInicio());
                    sentencia.setString(4, nuevaEntrega.getFechaFin());
                    sentencia.setInt(5, grupo.getIdGrupo());
                    sentencia.executeUpdate();
                }

                // 5. Confirmar Transacción
                conexionBD.commit();
                resultado.setIsError(false);
                resultado.setMensaje("La entrega ha sido programada correctamente para todos los grupos del periodo actual.");

            } catch (SQLException e) {
                // 6. Revertir en caso de error
                resultado.setMensaje("Ocurrió un error y no se pudo completar la operación.");
                conexionBD.rollback();
            } finally {
                conexionBD.close();
            }
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return resultado;
    }
}
