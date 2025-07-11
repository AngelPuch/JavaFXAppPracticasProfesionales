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

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: ValidacionDAO.java 
    * Autor: Rodrigo Luna Vázquez
    * Fecha: 15/06/2025
*/
public class ValidacionDAO {

    public static ArrayList<DocumentoEntregado> obtenerEntregasParaValidar(int idEntrega, String tipoDocumento) throws SQLException {
        ArrayList<DocumentoEntregado> documentos = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD == null) {
            throw new SQLException("No hay conexión con la base de datos.");
        }

        int idUsuarioLogueado = SesionUsuario.getInstancia().getUsuarioLogueado().getIdUsuario();
        Academico profesorLogueado = AcademicoDAO.obtenerAcademicoPorIdUsuario(idUsuarioLogueado);
        if (profesorLogueado == null) {
            conexionBD.close();
            throw new SQLException("El usuario actual no es un académico registrado.");
        }

        String sql = "";
        String nombreIdColumna = "";
        String nombreTablaReal = "";

        switch (tipoDocumento) {
            case "Documentos Iniciales":
                nombreIdColumna = "idDocumentoInicio";
                nombreTablaReal = "documentoinicio";
                sql = "SELECT doc.idDocumentoInicio, est.nombre AS nombreEstudiante, est.matricula, doc.fechaEntregado, doc.rutaArchivo " +
                      "FROM documentoinicio doc " +
                      "JOIN expediente exp ON doc.Expediente_idExpediente = exp.idExpediente " +
                      "JOIN inscripcion i ON exp.Inscripcion_idInscripcion = i.idInscripcion " +
                      "JOIN estudiante est ON i.Estudiante_idEstudiante = est.idEstudiante " +
                      "JOIN grupoee g ON i.grupoEE_idgrupoEE = g.idgrupoEE " +
                      "JOIN estadodocumento ed ON doc.EstadoDocumento_idEstadoDocumento = ed.idEstadoDocumento " +
                      "WHERE g.Academico_idAcademico = ? AND doc.EntregaDocumentoInicio_idEntregaDocumentoInicio = ? AND ed.nombreEstado = 'Entregado'";
                break;
            case "Reportes":
                nombreIdColumna = "idReporte";
                nombreTablaReal = "reporte";
                sql = "SELECT doc.idReporte, est.nombre AS nombreEstudiante, est.matricula, doc.fechaEntregado, doc.rutaArchivo " +
                      "FROM reporte doc " +
                      "JOIN expediente exp ON doc.Expediente_idExpediente = exp.idExpediente " +
                      "JOIN inscripcion i ON exp.Inscripcion_idInscripcion = i.idInscripcion " +
                      "JOIN estudiante est ON i.Estudiante_idEstudiante = est.idEstudiante " +
                      "JOIN grupoee g ON i.grupoEE_idgrupoEE = g.idgrupoEE " +
                      "JOIN estadodocumento ed ON doc.EstadoDocumento_idEstadoDocumento = ed.idEstadoDocumento " +
                      "WHERE g.Academico_idAcademico = ? AND doc.EntregaReporte_idEntregaReporte = ? AND ed.nombreEstado = 'Entregado'";
                break;
            case "Documentos Finales":
                nombreIdColumna = "idDocumentoFinal";
                nombreTablaReal = "documentofinal";
                sql = "SELECT doc.idDocumentoFinal, est.nombre AS nombreEstudiante, est.matricula, doc.fechaEntregado, doc.rutaArchivo " +
                      "FROM documentofinal doc " +
                      "JOIN expediente exp ON doc.Expediente_idExpediente = exp.idExpediente " +
                      "JOIN inscripcion i ON exp.Inscripcion_idInscripcion = i.idInscripcion " +
                      "JOIN estudiante est ON i.Estudiante_idEstudiante = est.idEstudiante " +
                      "JOIN grupoee g ON i.grupoEE_idgrupoEE = g.idgrupoEE " +
                      "JOIN estadodocumento ed ON doc.EstadoDocumento_idEstadoDocumento = ed.idEstadoDocumento " +
                      "WHERE g.Academico_idAcademico = ? AND doc.EntregaDocumentoFinal_idEntregaDocumentoFinal = ? AND ed.nombreEstado = 'Entregado'";
                break;
            default:
                conexionBD.close();
                throw new SQLException("Tipo de documento no válido: " + tipoDocumento);
        }

        try (PreparedStatement consulta = conexionBD.prepareStatement(sql)) {
            consulta.setInt(1, profesorLogueado.getIdAcademico());
            consulta.setInt(2, idEntrega);

            ResultSet resultado = consulta.executeQuery();
            while (resultado.next()) {
                DocumentoEntregado doc = new DocumentoEntregado();
                doc.setIdDocumento(resultado.getInt(nombreIdColumna));
                doc.setNombreEstudiante(resultado.getString("nombreEstudiante"));
                doc.setMatricula(resultado.getString("matricula"));
                doc.setFechaEntregado(resultado.getTimestamp("fechaEntregado").toLocalDateTime());
                doc.setRutaArchivo(resultado.getString("rutaArchivo"));
                doc.setTipoDocumento(nombreTablaReal);
                documentos.add(doc);
            }
        } finally {
            conexionBD.close();
        }

        return documentos;
    }
    
    public static ResultadoOperacion validarEntrega(DocumentoEntregado docAValidar, int idNuevoEstado, String comentarios) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD == null) {
            resultado.setIsError(true);
            resultado.setMensaje("No hay conexión con la base de datos.");
            return resultado;
        }

        String sql = "";
        String tipoDocumento = docAValidar.getTipoDocumento();

        switch (tipoDocumento) {
            case "documentoinicio":
                sql = "UPDATE documentoinicio SET EstadoDocumento_idEstadoDocumento = ?, comentarios_validacion = ? WHERE idDocumentoInicio = ?";
                break;
            case "reporte":
                sql = "UPDATE reporte SET EstadoDocumento_idEstadoDocumento = ?, comentarios_validacion = ? WHERE idReporte = ?";
                break;
            case "documentofinal":
                sql = "UPDATE documentofinal SET EstadoDocumento_idEstadoDocumento = ?, comentarios_validacion = ? WHERE idDocumentoFinal = ?";
                break;
            default:
                conexionBD.close();
                resultado.setIsError(true);
                resultado.setMensaje("Tipo de documento desconocido, no se puede validar.");
                return resultado;
        }

        try (PreparedStatement sentencia = conexionBD.prepareStatement(sql)) {
            sentencia.setInt(1, idNuevoEstado);
            sentencia.setString(2, comentarios);
            sentencia.setInt(3, docAValidar.getIdDocumento());

            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas > 0) {
                resultado.setIsError(false);
                resultado.setMensaje("Operación realizada correctamente.");
            } else {
                resultado.setIsError(true);
                resultado.setMensaje("No se pudo actualizar el estado del documento.");
            }
        } finally {
            conexionBD.close();
        }

        return resultado;
    }
}