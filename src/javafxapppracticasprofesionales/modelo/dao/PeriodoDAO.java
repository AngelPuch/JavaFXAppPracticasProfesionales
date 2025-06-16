
package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Periodo;

public class PeriodoDAO {
    public static ArrayList<Periodo> obtenerTodosLosPeriodos() throws SQLException {
        ArrayList<Periodo> periodos = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT idPeriodo, nombrePeriodo, fechaInicio, fechaFin FROM periodo ORDER BY fechaInicio DESC";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Periodo periodo = new Periodo();
                periodo.setIdPeriodo(resultado.getInt("idPeriodo"));
                periodo.setNombrePeriodo(resultado.getString("nombrePeriodo"));
                periodo.setFechaInicio(resultado.getString("fechaInicio"));
                periodo.setFechaFin(resultado.getString("fechaFin"));
                periodos.add(periodo);
            }
            conexionBD.close();
            sentencia.close();
            resultado.close();
            
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos.");
        }
        return periodos;
    }
    
    public static Periodo obtenerPeriodoActual() throws SQLException {
        Periodo periodo = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT idPeriodo, nombrePeriodo, fechaInicio, fechaFin FROM periodo ORDER BY fechaInicio DESC LIMIT 1";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                periodo = new Periodo();
                periodo.setIdPeriodo(resultado.getInt("idPeriodo"));
                periodo.setNombrePeriodo(resultado.getString("nombrePeriodo"));
                periodo.setFechaInicio(resultado.getString("fechaInicio"));
                periodo.setFechaFin(resultado.getString("fechaFin"));
            }
            conexionBD.close();
            sentencia.close();
            resultado.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return periodo;
    }
}