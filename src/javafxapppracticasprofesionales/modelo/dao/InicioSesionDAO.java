package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;

public class InicioSesionDAO {

    public static Usuario verificarUsuario(String username, String password) throws SQLException {
        Usuario usuarioSesion = null;
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String sql = "SELECT u.idUsuario, u.username, u.password, u.Academico_idAcademico, u.Estudiante_idEstudiante, " +
                                  "GROUP_CONCAT(r.nombreRol SEPARATOR ', ') AS roles " +
                                  "FROM usuario u " +
                                  "JOIN usuario_rol ur ON u.idUsuario = ur.Usuario_idUsuario " +
                                  "JOIN rol r ON ur.Rol_idRol = r.idRol " +
                                  "WHERE u.username = ? AND u.password = ? " +
                                  "GROUP BY u.idUsuario";
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            sentencia.setString(1, username);
            sentencia.setString(2, password);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                usuarioSesion = convertirRegistroUsuario(resultado);
            }
            conexionBD.close();
            sentencia.close();
            resultado.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return usuarioSesion;
    }
    
    private static Usuario convertirRegistroUsuario(ResultSet resultado) throws SQLException{
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(resultado.getInt("idUsuario"));
        usuario.setUsername(resultado.getString("username"));
        usuario.setPassword(resultado.getString("password"));
        usuario.setIdAcademico(resultado.getInt("Academico_idAcademico"));
        usuario.setIdEstudiante(resultado.getInt("Estudiante_idEstudiante"));
        usuario.setRoles(resultado.getString("roles"));
        
        return usuario;
    }
}