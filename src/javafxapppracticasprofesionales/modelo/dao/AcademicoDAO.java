package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Academico;

public class AcademicoDAO {

    public static Academico obtenerAcademicoPorIdUsuario(int idUsuario) throws SQLException {
        Academico academico = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT idAcademico, numeroPersonal, nombre, correo, idUsuario "
                    + "FROM academico WHERE idUsuario = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            sentencia.setInt(1, idUsuario);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()){
                academico = convertirRegistroAcademico(resultado);
            }
            conexionBD.close();
            sentencia.close();
            resultado.close();
        } else {
            throw new SQLException("Sin conexión a la Base de Datos");
        }
        return academico;
    }
    
    private static Academico convertirRegistroAcademico(ResultSet resultado) throws SQLException {
        Academico academico = new Academico();
        academico.setIdAcademico(resultado.getInt("idAcademico"));
        academico.setNumeroPersonal(resultado.getString("numeroPersonal"));
        academico.setNombre(resultado.getString("nombre"));
        academico.setCorreo(resultado.getString("correo"));
        academico.setIdUsuario(resultado.getInt("idUsuario"));
        return academico;
    }
}