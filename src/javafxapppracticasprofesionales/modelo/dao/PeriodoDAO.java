package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Periodo;

public class PeriodoDAO {
    
    public static Periodo obtenerPeriodoActual() throws SQLException {
        Periodo periodo = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT idPeriodo, nombrePeriodo, fechaInicio, fechaFin FROM periodo WHERE CURDATE() BETWEEN fechaInicio AND fechaFin";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                periodo = convertirRegistroPeriodo(resultado);
            }
            conexionBD.close();
            sentencia.close();
            resultado.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return periodo;
    }
    
    private static Periodo convertirRegistroPeriodo(ResultSet resultado) throws SQLException {
        Periodo periodo = new Periodo();
        periodo.setIdPeriodo(resultado.getInt("idPeriodo"));
        periodo.setNombrePeriodo(resultado.getString("nombrePeriodo"));
        periodo.setFechaInicio(resultado.getString("fechaInicio"));
        periodo.setFechaFin(resultado.getString("fechaFin"));
        return periodo;
    }
}