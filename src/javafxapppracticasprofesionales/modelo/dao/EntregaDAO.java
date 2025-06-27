package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: EntregaDAO.java 
    * Autor: Angel Jonathan Puch Hernández, Jose Luis Silva Gómez, Rodrigo Luna Vázquez
    * Fecha: 13/06/2025
*/
public class EntregaDAO {
    
    public static ArrayList<Entrega> obtenerTodasLasEntregas(String tabla) throws SQLException {
        ArrayList<Entrega> entregas = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = String.format(
                "SELECT e.*, CONCAT(g.seccion, ' - Bloque: ', g.bloque) AS nombreGrupo " +
                "FROM %s e " +
                "JOIN grupoee g ON e.grupoEE_idgrupoEE = g.idgrupoEE", tabla
            );
            
            try (PreparedStatement preparedStatement = conexionBD.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                
                while (resultSet.next()) {
                    Entrega entrega = new Entrega();
                    entrega.setIdEntrega(resultSet.getInt("id" + tabla.substring(0, 1).toUpperCase() + tabla.substring(1)));
                    entrega.setNombre(resultSet.getString("nombre"));
                    
                    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
                    Date fechaInicio = resultSet.getDate("fechaInicio");
                    if (fechaInicio != null) {
                        entrega.setFechaInicio(fechaInicio.toLocalDate().format(formatoFecha));
                    } else {
                        entrega.setFechaInicio("N/A");
                    }
                    Date fechaFin = resultSet.getDate("fechaFin");
                    if (fechaFin != null) {
                        entrega.setFechaFin(fechaFin.toLocalDate().format(formatoFecha));
                    } else {
                        entrega.setFechaFin("N/A");
                    }
                    Time horaInicio = resultSet.getTime("horaInicio");
                    if (horaInicio != null) {
                        entrega.setHoraInicio(horaInicio.toLocalTime().format(formatoHora));
                    } else {
                        entrega.setHoraInicio("N/A");
                    }
                    Time horaFin = resultSet.getTime("horaFin");
                    if (horaFin != null) {
                        entrega.setHoraFin(horaFin.toLocalTime().format(formatoHora));
                    } else {
                        entrega.setHoraFin("N/A");
                    }
                    entrega.setNombreGrupo(resultSet.getString("nombreGrupo"));
                    
                    entregas.add(entrega);
                }
            } finally {
                conexionBD.close();
            }
        }
        return entregas;
    }
    
    public static ArrayList<Entrega> obtenerEntregasPendientesEstudiante(int idGrupo, int idExpediente, String tablaEntrega) throws SQLException {
        ArrayList<Entrega> entregas = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String tablaDocumento;
            String campoIdEntregaFK;
            String campoIdEntregaPK;
            String campoIdDocumentoPK;

            if (tablaEntrega.equals("entregadocumentoinicio")) {
                tablaDocumento = "documentoinicio";
                campoIdEntregaPK = "idEntregaDocumentoInicio";
                campoIdDocumentoPK = "idDocumentoInicio";
                campoIdEntregaFK = "EntregaDocumentoInicio_idEntregaDocumentoInicio";
            } else if (tablaEntrega.equals("entregareporte")) {
                tablaDocumento = "reporte";
                campoIdEntregaPK = "idEntregaReporte";
                campoIdDocumentoPK = "idReporte";
                campoIdEntregaFK = "EntregaReporte_idEntregaReporte";
            } else { 
                tablaDocumento = "documentofinal";
                campoIdEntregaPK = "idEntregaDocumentoFinal";
                campoIdDocumentoPK = "idDocumentoFinal";
                campoIdEntregaFK = "EntregaDocumentoFinal_idEntregaDocumentoFinal";
            }

            String sql = String.format(
                "SELECT e.*, d.%s AS documento_id " +
                "FROM %s e " +
                "LEFT JOIN %s d ON e.%s = d.%s AND d.Expediente_idExpediente = ? " +
                "WHERE e.grupoEE_idgrupoEE = ?",
                campoIdDocumentoPK, tablaEntrega, tablaDocumento, campoIdEntregaPK, campoIdEntregaFK
            );

            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            sentencia.setInt(1, idExpediente); 
            sentencia.setInt(2, idGrupo);      
            
            ResultSet resultado = sentencia.executeQuery();

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            
            while(resultado.next()){
                Entrega entrega = new Entrega();
                entrega.setIdEntrega(resultado.getInt(campoIdEntregaPK));
                entrega.setNombre(resultado.getString("nombre"));
                entrega.setDescripcion(resultado.getString("descripcion"));
                entrega.setFechaInicio(resultado.getString("fechaInicio"));
                entrega.setFechaFin(resultado.getString("fechaFin"));
                
                LocalTime horaInicio = resultado.getObject("horaInicio", LocalTime.class);
                LocalTime horaFin = resultado.getObject("horaFin", LocalTime.class);
                
                if (horaInicio != null) {
                    entrega.setHoraInicio(horaInicio.format(timeFormatter));
                }
                if (horaFin != null) {
                    entrega.setHoraFin(horaFin.format(timeFormatter));
                }   
                
                if (resultado.getObject("documento_id") != null) {
                    entrega.setEstado("Entregado");
                } else {
                    entrega.setEstado("Sin Entregar");
                }
                
                entregas.add(entrega);
            }

            conexionBD.close();
        }
        return entregas;
    }
    
   
    public static List<Entrega> obtenerEntregasPorTipo(String tipoEntrega, int idAcademico) throws SQLException {
        List<Entrega> entregas = new ArrayList<>();
        String query = "";
        Connection conexion = null;

        switch (tipoEntrega) {
            case "Documentos Iniciales":
                query = "SELECT e.idEntregaDocumentoInicio, e.nombre, e.descripcion, e.fechaInicio, e.fechaFin, "
                        + "g.seccion, ee.nombre AS nombreEE, tdi.nombre AS tipoDocumento "
                        + "FROM entregadocumentoinicio AS e "
                        + "INNER JOIN tipodocumentoinicio AS tdi ON e.TipoDocumentoInicio_idTipoDocumentoInicio = tdi.idTipoDocumentoInicio "
                        + "INNER JOIN grupoee AS g ON e.grupoEE_idgrupoEE = g.idgrupoEE "
                        + "INNER JOIN experienciaeducativa AS ee ON g.ExperienciaEducativa_idExperienciaEducativa = ee.idExperienciaEducativa "
                        + "WHERE g.Academico_idAcademico = ?";
                break;
            case "Reportes":
                query = "SELECT er.idEntregaReporte, er.nombre, er.descripcion, er.fechaInicio, er.fechaFin, "
                        + "g.seccion, ee.nombre AS nombreEE, tdr.nombre AS tipoDocumento "
                        + "FROM entregareporte AS er "
                        + "INNER JOIN tipodocumentoreporte AS tdr ON er.TipoDocumentoReporte_idTipoDocumentoReporte = tdr.idTipoDocumentoReporte "
                        + "INNER JOIN grupoee AS g ON er.grupoEE_idgrupoEE = g.idgrupoEE "
                        + "INNER JOIN experienciaeducativa AS ee ON g.ExperienciaEducativa_idExperienciaEducativa = ee.idExperienciaEducativa "
                        + "WHERE g.Academico_idAcademico = ?";
                break;
            case "Documentos Finales":
                query = "SELECT ef.idEntregaDocumentoFinal, ef.nombre, ef.descripcion, ef.fechaInicio, ef.fechaFin, "
                        + "g.seccion, ee.nombre AS nombreEE, tdf.nombre AS tipoDocumento "
                        + "FROM entregadocumentofinal AS ef "
                        + "INNER JOIN tipodocumentofinal AS tdf ON ef.TipoDocumentoFinal_idTipoDocumentoFinal = tdf.idTipoDocumentoFinal "
                        + "INNER JOIN grupoee AS g ON ef.grupoEE_idgrupoEE = g.idgrupoEE "
                        + "INNER JOIN experienciaeducativa AS ee ON g.ExperienciaEducativa_idExperienciaEducativa = ee.idExperienciaEducativa "
                        + "WHERE g.Academico_idAcademico = ?";
                break;
            default:
                return entregas;
        }

        try {
            conexion = ConexionBD.abrirConexion();
            try (PreparedStatement ps = conexion.prepareStatement(query)) {
                ps.setInt(1, idAcademico);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Entrega entrega = new Entrega();
                    entrega.setIdEntrega(rs.getInt(1));
                    entrega.setNombre(rs.getString("nombre"));
                    entrega.setDescripcion(rs.getString("descripcion"));
                    entrega.setTipoDocumento(rs.getString("tipoDocumento"));
                    Date fechaInicioDb = rs.getDate("fechaInicio");
                    Date fechaFinDb = rs.getDate("fechaFin");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    if (fechaInicioDb != null) {
                        entrega.setFechaInicio(sdf.format(fechaInicioDb));
                    }
                    if (fechaFinDb != null) {
                        entrega.setFechaFin(sdf.format(fechaFinDb));
                    }

                    entregas.add(entrega);
                }
            }
        } catch (SQLException ex) {
            System.err.println("ERROR al consultar entregas por tipo: " + ex.getMessage());
            throw ex;
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("ERROR al cerrar la conexión: " + e.getMessage());
                }
            }
        }

        return entregas;
    }
}