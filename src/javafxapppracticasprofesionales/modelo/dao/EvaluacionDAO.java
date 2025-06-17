package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.AfirmacionOV;
import javafxapppracticasprofesionales.modelo.pojo.Evaluacion;
import javafxapppracticasprofesionales.modelo.pojo.EvaluacionOV;
import javafxapppracticasprofesionales.modelo.pojo.RespuestaGuardadaOV;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;

public class EvaluacionDAO {

    public static ResultadoOperacion registrarEvaluacion(Evaluacion evaluacion) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setIsError(true);
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "INSERT INTO evaluacion (calificacionTotal, fecha, motivo, comentarios, " +
                         "Usuario_idUsuario, TipoEvaluacion_idTipoEvaluacion, Expediente_idExpediente) " +
                         "VALUES (?, CURDATE(), ?, ?, ?, ?, ?)";
            try (PreparedStatement sentencia = conexionBD.prepareStatement(sql)) {
                sentencia.setFloat(1, evaluacion.getCalificacionTotal());
                sentencia.setString(2, evaluacion.getMotivo());
                sentencia.setString(3, evaluacion.getComentarios());
                sentencia.setInt(4, evaluacion.getIdUsuario());
                sentencia.setInt(5, evaluacion.getIdTipoEvaluacion());
                sentencia.setInt(6, evaluacion.getIdExpediente());

                int filasAfectadas = sentencia.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setIsError(false);
                    resultado.setMensaje("Evaluación guardada correctamente.");
                } else {
                    resultado.setMensaje("No se pudo registrar la evaluación.");
                }
            } finally {
                conexionBD.close();
            }
        } else {
            resultado.setMensaje("No hay conexión con la base de datos.");
        }
        return resultado;
    }
    
    public static ArrayList<AfirmacionOV> obtenerAfirmacionesOV() throws SQLException {
        ArrayList<AfirmacionOV> afirmaciones = new ArrayList<>();
        Connection conexion = ConexionBD.abrirConexion();
        if (conexion != null) {
            String consulta = "SELECT idAfirmacion, descripcion, categoria FROM afirmacion_ov ORDER BY categoria, idAfirmacion";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                AfirmacionOV afirmacion = new AfirmacionOV();
                afirmacion.setIdAfirmacion(resultado.getInt("idAfirmacion"));
                afirmacion.setDescripcion(resultado.getString("descripcion"));
                afirmacion.setCategoria(resultado.getString("categoria"));
                afirmaciones.add(afirmacion);
            }
            conexion.close();
        }
        return afirmaciones;
    }
    
    public static EvaluacionOV obtenerInfoParaEvaluacion(int idExpediente) throws SQLException {
        EvaluacionOV info = null;
        Connection conexion = ConexionBD.abrirConexion();
        if (conexion != null) {
            String consulta = "SELECT est.nombre AS nombre_alumno, est.matricula, ov.nombre AS nombre_ov, " +
                              "pro.nombre AS nombre_proyecto, res.nombre AS nombre_responsable, exp.horasAcumuladas " +
                              "FROM expediente exp " +
                              "JOIN inscripcion i ON exp.Inscripcion_idInscripcion = i.idInscripcion " +
                              "JOIN estudiante est ON i.Estudiante_idEstudiante = est.idEstudiante " +
                              "JOIN proyecto pro ON exp.Proyecto_idProyecto = pro.idProyecto " +
                              "JOIN organizacionvinculada ov ON pro.OrganizacionVinculada_idOrganizacionVinculada = ov.idOrganizacionVinculada " +
                              "JOIN responsableproyecto res ON pro.ResponsableProyecto_idResponsableProyecto = res.idResponsableProyecto " +
                              "WHERE exp.idExpediente = ?";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, idExpediente);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                info = new EvaluacionOV();
                // ... Aquí debes crear los setters en EvaluacionOV y usarlos:
                // info.setNombreAlumno(resultado.getString("nombre_alumno"));
                // etc...
            }
            conexion.close();
        }
        return info;
    }
    
    public static ResultadoOperacion guardarEvaluacionOV(int idUsuario, int idExpediente, String observaciones, List<AfirmacionOV> afirmaciones) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setIsError(true);
        Connection conexion = ConexionBD.abrirConexion();
        if (conexion != null) {
            // ID 1 Corresponde a "Evaluacion de OV por Estudiante" en tu tabla 'tipoevaluacion'
            int idTipoEvaluacion = 1; 
            
            try {
                conexion.setAutoCommit(false); // Iniciar transacción
                
                // 1. Insertar en la tabla 'evaluacion'
                String sqlEvaluacion = "INSERT INTO evaluacion (fecha, comentarios, Usuario_idUsuario, TipoEvaluacion_idTipoEvaluacion, Expediente_idExpediente) " +
                                       "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement psEvaluacion = conexion.prepareStatement(sqlEvaluacion, Statement.RETURN_GENERATED_KEYS);
                psEvaluacion.setDate(1, Date.valueOf(LocalDate.now()));
                psEvaluacion.setString(2, observaciones);
                psEvaluacion.setInt(3, idUsuario);
                psEvaluacion.setInt(4, idTipoEvaluacion);
                psEvaluacion.setInt(5, idExpediente);
                psEvaluacion.executeUpdate();
                
                ResultSet generatedKeys = psEvaluacion.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idEvaluacionGenerado = generatedKeys.getInt(1);
                    
                    // 2. Insertar cada respuesta en 'evaluacion_ov_detalle'
                    String sqlDetalle = "INSERT INTO evaluacion_ov_detalle (idEvaluacion, idAfirmacion, respuesta) VALUES (?, ?, ?)";
                    PreparedStatement psDetalle = conexion.prepareStatement(sqlDetalle);
                    
                    for (AfirmacionOV afirmacion : afirmaciones) {
                        psDetalle.setInt(1, idEvaluacionGenerado);
                        psDetalle.setInt(2, afirmacion.getIdAfirmacion());
                        psDetalle.setInt(3, afirmacion.getRespuestaSeleccionada());
                        psDetalle.addBatch();
                    }
                    psDetalle.executeBatch();
                    
                    conexion.commit(); // Confirmar transacción
                    resultado.setIsError(false);
                    resultado.setMensaje("Evaluación guardada correctamente.");
                } else {
                    throw new SQLException("Fallo al crear la evaluación, no se obtuvo ID.");
                }

            } catch (SQLException e) {
                conexion.rollback(); // Revertir en caso de error
                throw e; // Lanzar la excepción para que el controlador la maneje
            } finally {
                conexion.close();
            }
        }
        return resultado;
    }

    public static ArrayList<RespuestaGuardadaOV> obtenerDetalleEvaluacionGuardada(int idExpediente) throws SQLException {
        ArrayList<RespuestaGuardadaOV> respuestas = new ArrayList<>();
        Connection conexion = ConexionBD.abrirConexion();
        if (conexion != null) {
            String consulta = "SELECT af.descripcion, det.respuesta " +
                              "FROM evaluacion e " +
                              "JOIN evaluacion_ov_detalle det ON e.idEvaluacion = det.idEvaluacion " +
                              "JOIN afirmacion_ov af ON det.idAfirmacion = af.idAfirmacion " +
                              "WHERE e.Expediente_idExpediente = ? AND e.TipoEvaluacion_idTipoEvaluacion = 1 " +
                              "ORDER BY af.idAfirmacion";
        
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, idExpediente);
            ResultSet resultado = sentencia.executeQuery();

            while(resultado.next()) {
                RespuestaGuardadaOV respuesta = new RespuestaGuardadaOV();
                respuesta.setAfirmacion(resultado.getString("descripcion"));
                respuesta.setRespuesta(resultado.getInt("respuesta"));
                respuestas.add(respuesta);
            }
            conexion.close();
        }
        return respuestas;
    }
    
    public static boolean haEvaluadoOVPreviamente(int idExpediente) throws SQLException {
        boolean yaEvaluado = false;
        Connection conexion = ConexionBD.abrirConexion();
        if (conexion != null) {
            // ID 1 corresponde a "Evaluacion de OV por Estudiante" en tu tabla 'tipoevaluacion'
            String consulta = "SELECT COUNT(*) AS total FROM evaluacion WHERE Expediente_idExpediente = ? AND TipoEvaluacion_idTipoEvaluacion = 1";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, idExpediente);
            ResultSet resultado = sentencia.executeQuery();
        
            if (resultado.next()) {
                if (resultado.getInt("total") > 0) {
                    yaEvaluado = true;
                }
            }
            conexion.close();
        }
        return yaEvaluado;
    }

    public static ArrayList<Evaluacion> obtenerEvaluacionesPorExpediente(int idExpediente) throws SQLException {
        ArrayList<Evaluacion> evaluaciones = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = "SELECT idEvaluacion, calificacionTotal, fecha, motivo, comentarios, " +
                         "Usuario_idUsuario, TipoEvaluacion_idTipoEvaluacion, Expediente_idExpediente " +
                         "FROM evaluacion WHERE Expediente_idExpediente = ?";
            try (PreparedStatement sentencia = conexionBD.prepareStatement(sql)) {
                sentencia.setInt(1, idExpediente);
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    Evaluacion evaluacion = new Evaluacion();
                    evaluacion.setIdEvaluacion(resultado.getInt("idEvaluacion"));
                    evaluacion.setCalificacionTotal(resultado.getFloat("calificacionTotal"));
                    evaluacion.setFecha(resultado.getString("fecha"));
                    evaluacion.setMotivo(resultado.getString("motivo"));
                    evaluacion.setComentarios(resultado.getString("comentarios"));
                    evaluacion.setIdUsuario(resultado.getInt("Usuario_idUsuario"));
                    evaluacion.setIdTipoEvaluacion(resultado.getInt("TipoEvaluacion_idTipoEvaluacion"));
                    evaluacion.setIdExpediente(resultado.getInt("Expediente_idExpediente"));
                    evaluaciones.add(evaluacion);
                }
            } finally {
                conexionBD.close();
            }
        }
        return evaluaciones;
    }
}