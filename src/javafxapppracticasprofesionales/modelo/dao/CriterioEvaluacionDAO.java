package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.CriterioEvaluacion;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: CriterioEvaluacionDAO.java 
    * Autor: Angel Jonathan Puch Hernández 
    * Fecha: 15/06/2025
*/
public class CriterioEvaluacionDAO {

    public static ArrayList<CriterioEvaluacion> obtenerCriteriosRubrica() throws SQLException {
        ArrayList<CriterioEvaluacion> criterios = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT idCriterio, nombreCriterio, competente, independiente, basicoAvanzado, basicoUmbral, noCompetente " +
                          "FROM criterio_evaluacion ORDER BY idCriterio ASC;";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                criterios.add(convertirRegistroCriterioEvaluacion(resultado));
            }
            conexionBD.close();
            sentencia.close();
            resultado.close();
        } else {
            throw new SQLException("Sin conexión a la Base de Datos");
        }
        return criterios;
    }
    
    private static CriterioEvaluacion convertirRegistroCriterioEvaluacion(ResultSet resultado) throws SQLException{
        CriterioEvaluacion criterio = new CriterioEvaluacion();
        criterio.setIdCriterio(resultado.getInt("idCriterio"));
        criterio.setCriterio(resultado.getString("nombreCriterio"));
        criterio.setCompetente(resultado.getString("competente"));
        criterio.setIndependiente(resultado.getString("independiente"));
        criterio.setBasicoAvanzado(resultado.getString("basicoAvanzado"));
        criterio.setBasicoMinimo(resultado.getString("basicoUmbral"));
        criterio.setNoCompetente(resultado.getString("noCompetente"));
        return criterio;
    }
}