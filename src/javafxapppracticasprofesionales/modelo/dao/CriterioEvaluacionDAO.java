package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.CriterioEvaluacion;

public class CriterioEvaluacionDAO {

    public static ArrayList<CriterioEvaluacion> obtenerCriteriosRubrica() throws SQLException {
        ArrayList<CriterioEvaluacion> criterios = new ArrayList<>();
        Connection conexion = ConexionBD.abrirConexion();
        if (conexion != null) {
            String consulta = "SELECT idCriterio, nombreCriterio FROM criterio_evaluacion ORDER BY idCriterio ASC;";
            try (PreparedStatement sentencia = conexion.prepareStatement(consulta);
                 ResultSet resultado = sentencia.executeQuery()) {

                while (resultado.next()) {
                    CriterioEvaluacion criterio = new CriterioEvaluacion();
                    criterio.setIdCriterio(resultado.getInt("idCriterio"));
                    criterio.setCriterio(resultado.getString("nombreCriterio"));
                    criterios.add(criterio);
                }
            } finally {
                conexion.close();
            }
        }
        return criterios;
    }
}