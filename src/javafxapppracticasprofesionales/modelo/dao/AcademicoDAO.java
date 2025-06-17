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
        Connection conexion = ConexionBD.abrirConexion();
        if (conexion != null) {
            String consulta = "SELECT idAcademico, numeroPersonal, nombre, correo, idUsuario FROM academico WHERE idUsuario = ?";
            try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
                sentencia.setInt(1, idUsuario);
                ResultSet resultado = sentencia.executeQuery();
                if (resultado.next()) {
                    academico = new Academico();
                    academico.setIdAcademico(resultado.getInt("idAcademico"));
                    academico.setNumeroPersonal(resultado.getString("numeroPersonal"));
                    academico.setNombre(resultado.getString("nombre"));
                    academico.setCorreo(resultado.getString("correo"));
                    academico.setIdUsuario(resultado.getInt("idUsuario"));
                }
            } finally {
                conexion.close();
            }
        }
        return academico;
    }
}