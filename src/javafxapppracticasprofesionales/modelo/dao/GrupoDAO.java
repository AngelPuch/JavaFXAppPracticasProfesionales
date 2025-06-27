package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Academico;
import javafxapppracticasprofesionales.modelo.pojo.ExperienciaEducativa;
import javafxapppracticasprofesionales.modelo.pojo.Grupo;
import javafxapppracticasprofesionales.modelo.pojo.Periodo;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: GrupoDAO.java 
    * Autor: Angel Jonathan Puch Hernández
    * Fecha: 13/06/2025
*/
public class GrupoDAO {
    
    public static ArrayList<Grupo> obtenerGruposPorPeriodo(int idPeriodo) throws SQLException {
        ArrayList<Grupo> grupos = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT g.idgrupoEE, g.seccion, g.bloque, " +
                         "a.idAcademico, a.nombre AS nombreAcademico, " +
                         "ee.idExperienciaEducativa, ee.nombre AS nombreEE, ee.nrc, " +
                         "p.idPeriodo, p.nombrePeriodo " +
                         "FROM grupoee g " +
                         "JOIN academico a ON g.Academico_idAcademico = a.idAcademico " +
                         "JOIN experienciaeducativa ee ON g.ExperienciaEducativa_idExperienciaEducativa = ee.idExperienciaEducativa " +
                         "JOIN periodo p ON g.Periodo_idPeriodo = p.idPeriodo " +
                         "WHERE g.Periodo_idPeriodo = ?";
            
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            sentencia.setInt(1, idPeriodo);
            ResultSet resultado = sentencia.executeQuery();
            
            while(resultado.next()){;
                grupos.add(convertirRegistroGrupo(resultado));
            }
            
            conexionBD.close();
            sentencia.close();
            resultado.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return grupos;
    }
    
    private static Grupo convertirRegistroGrupo(ResultSet resultado) throws SQLException {
        Grupo grupo = new Grupo();
        grupo.setIdGrupo(resultado.getInt("idgrupoEE"));
        grupo.setSeccion(resultado.getString("seccion"));
        grupo.setBloque(resultado.getString("bloque"));

        Academico academico = new Academico();
        academico.setIdAcademico(resultado.getInt("idAcademico"));
        academico.setNombre(resultado.getString("nombreAcademico"));
        grupo.setAcademico(academico);

        ExperienciaEducativa ee = new ExperienciaEducativa();
        ee.setIdExperienciaEducativa(resultado.getInt("idExperienciaEducativa"));
        ee.setNombre(resultado.getString("nombreEE"));
        ee.setNrc(resultado.getString("nrc"));
        grupo.setExperienciaEducativa(ee);
        
        Periodo periodo = new Periodo();
        periodo.setIdPeriodo(resultado.getInt("idPeriodo"));
        periodo.setNombrePeriodo(resultado.getString("nombrePeriodo"));
        grupo.setPeriodo(periodo);
        
        return grupo;
    }
}