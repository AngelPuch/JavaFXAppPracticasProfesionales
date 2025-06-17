package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Academico;
import javafxapppracticasprofesionales.modelo.pojo.DocumentoEntregado;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;

public class ValidacionDAO {
    
    public static ArrayList<DocumentoEntregado> obtenerEntregasParaValidar(int idEntrega, String[] tablaInfo) throws SQLException {
        ArrayList<DocumentoEntregado> documentos = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();

        int idUsuarioLogueado = SesionUsuario.getInstancia().getUsuarioLogueado().getIdUsuario();
        Academico profesorLogueado = AcademicoDAO.obtenerAcademicoPorIdUsuario(idUsuarioLogueado);

        if (profesorLogueado == null) {
            throw new SQLException("El usuario actual no es un académico registrado y no puede validar entregas.");
        }

        if (conexionBD != null) {
            
            String nombreTabla = tablaInfo[0];
            String nombreColumnaId = tablaInfo[1];
            String nombreColumnaFkEntrega = tablaInfo[2];
            int idAcademico = profesorLogueado.getIdAcademico(); 

            String sql = String.format(
                "SELECT doc.%s, est.nombre AS nombreEstudiante, est.matricula, doc.fechaEntregado, doc.rutaArchivo " +
                "FROM %s doc " +
                "JOIN expediente exp ON doc.Expediente_idExpediente = exp.idExpediente " +
                "JOIN inscripcion i ON exp.Inscripcion_idInscripcion = i.idInscripcion " +
                "JOIN estudiante est ON i.Estudiante_idEstudiante = est.idEstudiante " +
                "JOIN grupoee g ON i.grupoEE_idgrupoEE = g.idgrupoEE " +
                "JOIN estadodocumento ed ON doc.EstadoDocumento_idEstadoDocumento = ed.idEstadoDocumento " +
                "WHERE g.Academico_idAcademico = ? AND doc.%s = ? AND ed.nombreEstado = 'Entregado'",
                nombreColumnaId, nombreTabla, nombreColumnaFkEntrega
            );

            try (PreparedStatement consulta = conexionBD.prepareStatement(sql)) {
                consulta.setInt(1, idAcademico);
                consulta.setInt(2, idEntrega);
                
                ResultSet resultado = consulta.executeQuery();
                while (resultado.next()) {
                    DocumentoEntregado doc = new DocumentoEntregado();
                    doc.setIdDocumento(resultado.getInt(nombreColumnaId));
                    doc.setNombreEstudiante(resultado.getString("nombreEstudiante"));
                    doc.setMatricula(resultado.getString("matricula"));
                    doc.setFechaEntregado(resultado.getTimestamp("fechaEntregado").toLocalDateTime());
                    doc.setRutaArchivo(resultado.getString("rutaArchivo"));
                    doc.setTipoDocumento(nombreTabla);
                    documentos.add(doc);
                }
            } finally {
                conexionBD.close();
            }
        }
        return documentos;
    }

    
    
    public static ResultadoOperacion validarEntrega(int idDocumento, int idNuevoEstado, String comentarios, String nombreTabla, String nombreIdColumna) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = String.format(
                "UPDATE %s SET EstadoDocumento_idEstadoDocumento = ?, comentarios_validacion = ? WHERE %s = ?",
                nombreTabla, nombreIdColumna
            );
            try (PreparedStatement sentencia = conexionBD.prepareStatement(sql)) {
                sentencia.setInt(1, idNuevoEstado);
                sentencia.setString(2, comentarios);
                sentencia.setInt(3, idDocumento);

                int filasAfectadas = sentencia.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setIsError(false);
                    resultado.setMensaje("Documento validado correctamente.");
                } else {
                    resultado.setIsError(true);
                    resultado.setMensaje("No se pudo actualizar el estado del documento.");
                }
            } finally {
                conexionBD.close();
            }
        } else {
            resultado.setIsError(true);
            resultado.setMensaje("No hay conexión con la base de datos.");
        }
        return resultado;
    }
}