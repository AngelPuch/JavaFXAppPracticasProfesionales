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
        // Se seleccionan todas las columnas de la rúbrica.
        String consulta = "SELECT idCriterio, nombreCriterio, competente, independiente, basicoAvanzado, basicoUmbral, noCompetente " +
                          "FROM criterio_evaluacion ORDER BY idCriterio ASC;";
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                CriterioEvaluacion criterio = new CriterioEvaluacion();
                criterio.setIdCriterio(resultado.getInt("idCriterio"));
                criterio.setCriterio(resultado.getString("nombreCriterio"));
                // Se cargan las descripciones de cada nivel desde la BD.
                criterio.setCompetente(resultado.getString("competente"));
                criterio.setIndependiente(resultado.getString("independiente"));
                criterio.setBasicoAvanzado(resultado.getString("basicoAvanzado"));
                criterio.setBasicoMinimo(resultado.getString("basicoUmbral"));
                criterio.setNoCompetente(resultado.getString("noCompetente"));
                criterios.add(criterio);
            }
        } finally {
            conexion.close();
        }
    }
    return criterios;
}
}