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
        Usuario usuarioSesion = null;
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            try {
                String sqlUsuario = "SELECT idUsuario, username, password, nombre FROM usuario WHERE username = ?";
                PreparedStatement sentenciaUsuario = conexionBD.prepareStatement(sqlUsuario);
                sentenciaUsuario.setString(1, username);
                ResultSet resultadoUsuario = sentenciaUsuario.executeQuery();

                if (resultadoUsuario.next()) {
                    String hashGuardada = resultadoUsuario.getString("password");
                    if (UtilidadPassword.verificarPassword(password, hashGuardada)) {
                        usuarioSesion = new Usuario();
                        usuarioSesion.setIdUsuario(resultadoUsuario.getInt("idUsuario"));
                        usuarioSesion.setUsername(resultadoUsuario.getString("username"));
                        usuarioSesion.setNombre(resultadoUsuario.getString("nombre"));

                        String sqlRol = "SELECT r.nombreRol FROM rol r JOIN usuario_rol ur ON r.idRol = ur.Rol_idRol WHERE ur.Usuario_idUsuario = ?";
                        PreparedStatement sentenciaRol = conexionBD.prepareStatement(sqlRol);
                        sentenciaRol.setInt(1, usuarioSesion.getIdUsuario());
                        ResultSet resultadoRol = sentenciaRol.executeQuery();

                        if (resultadoRol.next()) {
                            String rol = resultadoRol.getString("nombreRol");
                            usuarioSesion.setRolPrincipal(rol);
                            
                            buscarYAsignarPerfil(conexionBD, usuarioSesion);
                        }
                    }
                }
            } finally {
                if (conexionBD != null) {
                    conexionBD.close();
                }
            }
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return usuarioSesion;
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
    }
}