/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafxapppracticasprofesionales.modelo.ConexionBD;
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
}
