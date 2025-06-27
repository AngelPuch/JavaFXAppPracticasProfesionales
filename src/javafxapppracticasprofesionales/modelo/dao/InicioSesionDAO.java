package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;
import javafxapppracticasprofesionales.utilidad.UtilidadPassword;

public class InicioSesionDAO {

    public static Usuario verificarUsuario(String username, String password) throws SQLException {
        Usuario usuarioVerificado = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        Usuario usuarioRegistrado = obtenerUsuarioPorUsername(username, conexionBD);
        if (conexionBD != null) {
            if (usuarioRegistrado != null && UtilidadPassword.verificarPassword(password, usuarioRegistrado.getPassword())) {
                usuarioVerificado = usuarioRegistrado;
                String rolPrincipal = obtenerRolPrincipal(usuarioVerificado.getIdUsuario(), conexionBD);
                usuarioVerificado.setRolPrincipal(rolPrincipal);
                buscarYAsignarPerfil(conexionBD, usuarioVerificado);
            }
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión a la Base de Datos");
        }
        return usuarioVerificado;
    }

    private static Usuario obtenerUsuarioPorUsername(String username, Connection conexion) throws SQLException {
        Usuario usuario = null;
        String sql = "SELECT idUsuario, username, password, nombre FROM usuario WHERE username = ?";
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setString(1, username);
        ResultSet resultado = sentencia.executeQuery();
        if (resultado.next()) {
            usuario = new Usuario();
            usuario.setIdUsuario(resultado.getInt("idUsuario"));
            usuario.setUsername(resultado.getString("username"));
            usuario.setPassword(resultado.getString("password"));
            usuario.setNombre(resultado.getString("nombre"));
        }
        sentencia.close();
        resultado.close();
        return usuario;
    }

    private static String obtenerRolPrincipal(int idUsuario, Connection conexion) throws SQLException {
        String rol = null;
        String sql = "SELECT r.nombreRol FROM rol r JOIN usuario_rol ur ON r.idRol = ur.Rol_idRol WHERE ur.Usuario_idUsuario = ?";
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, idUsuario);
        ResultSet resultado = sentencia.executeQuery();
        if (resultado.next()) {
            rol = resultado.getString("nombreRol");
        }
        sentencia.close();
        resultado.close();
        return rol;
    }

    private static void buscarYAsignarPerfil(Connection conexion, Usuario usuario) throws SQLException {
        String sqlPerfil;
        String idCampoPerfil;

        switch (usuario.getRolPrincipal().toLowerCase()) {
            case "estudiante":
                sqlPerfil = "SELECT idEstudiante FROM estudiante WHERE idUsuario = ?";
                idCampoPerfil = "idEstudiante";
                break;
            case "profesor":
            case "coordinador":
            case "evaluador": 
                sqlPerfil = "SELECT idAcademico FROM academico WHERE idUsuario = ?";
                idCampoPerfil = "idAcademico";
                break;
            default:
                return; 
        }
        PreparedStatement sentenciaPerfil = conexion.prepareStatement(sqlPerfil);
        sentenciaPerfil.setInt(1, usuario.getIdUsuario());
        ResultSet resultadoPerfil = sentenciaPerfil.executeQuery();

        if (resultadoPerfil.next()) {
            int idPerfil = resultadoPerfil.getInt(idCampoPerfil);
            switch (usuario.getRolPrincipal().toLowerCase()) {
                case "estudiante":
                    usuario.setIdEstudiante(idPerfil);
                    break;
                case "profesor":
                case "coordinador":
                case "evaluador":
                    usuario.setIdAcademico(idPerfil);
                    break;
            }
        }
        sentenciaPerfil.close();
        resultadoPerfil.close();
    }
}