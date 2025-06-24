package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;

public class EntregaDAO {
    public static ArrayList<Entrega> obtenerEntregasPorGrupo(int idGrupo, String tabla) throws SQLException {
        ArrayList<Entrega> entregas = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sql = String.format("SELECT * FROM %s WHERE grupoEE_idgrupoEE = ?", tabla);
            
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            sentencia.setInt(1, idGrupo);
            ResultSet resultado = sentencia.executeQuery();

            while(resultado.next()){
                Entrega entrega = new Entrega();
                if (tabla.equals("entregadocumentoinicio")) {
                    entrega.setIdEntrega(resultado.getInt("idEntregaDocumentoInicio"));
                } else if (tabla.equals("entregareporte")) {
                    entrega.setIdEntrega(resultado.getInt("idEntregaReporte"));
                } else {
                    entrega.setIdEntrega(resultado.getInt("idEntregaDocumentoFinal"));
                }
                entrega.setNombre(resultado.getString("nombre"));
                entrega.setDescripcion(resultado.getString("descripcion"));
                entrega.setFechaInicio(resultado.getString("fechaInicio"));
                entrega.setFechaFin(resultado.getString("fechaFin"));
                entregas.add(entrega);
            }

            conexionBD.close();
            sentencia.close();
            resultado.close();
        } else {
            throw new SQLException("Error: Sin conexión a la Base de Datos");
        }
        return entregas;
    }
    
    public static ArrayList<Entrega> obtenerTodasLasEntregas(String tabla) throws SQLException {
        ArrayList<Entrega> entregas = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            
            // Consulta con JOIN para obtener el nombre del grupo
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

                    // Fecha de inicio
                    Date fechaInicio = resultSet.getDate("fechaInicio");
                    if (fechaInicio != null) {
                        entrega.setFechaInicio(fechaInicio.toLocalDate().format(formatoFecha));
                    } else {
                        entrega.setFechaInicio("N/A");
                    }

                    // Fecha de fin
                    Date fechaFin = resultSet.getDate("fechaFin");
                    if (fechaFin != null) {
                        entrega.setFechaFin(fechaFin.toLocalDate().format(formatoFecha));
                    } else {
                        entrega.setFechaFin("N/A");
                    }

                    // Hora de inicio
                    Time horaInicio = resultSet.getTime("horaInicio");
                    if (horaInicio != null) {
                        entrega.setHoraInicio(horaInicio.toLocalTime().format(formatoHora));
                    } else {
                        entrega.setHoraInicio("N/A");
                    }

                    // Hora de fin
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

            while(resultado.next()){
                Entrega entrega = new Entrega();
                entrega.setIdEntrega(resultado.getInt(campoIdEntregaPK));
                entrega.setNombre(resultado.getString("nombre"));
                entrega.setDescripcion(resultado.getString("descripcion"));
                entrega.setFechaInicio(resultado.getString("fechaInicio"));
                entrega.setFechaFin(resultado.getString("fechaFin"));
                entrega.setHoraInicio(resultado.getString("horaInicio"));
                entrega.setHoraFin(resultado.getString("horaFin"));
                
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
    
   /* public static ArrayList<Entrega> obtenerEntregasPorTipo(int idTipoDocumento, int idAcademico) throws SQLException {
        ArrayList<Entrega> entregas = new ArrayList<>();
        Connection conexion = ConexionBD.abrirConexion();

        if (conexion != null) {
            String nombreTabla = "";
            String nombreColumnaId = "";

            switch (idTipoDocumento) {
                case 1:
                    nombreTabla = "entregadocumentoinicio";
                    nombreColumnaId = "idEntregaDocumentoInicio";
                    break;
                case 2:
                    nombreTabla = "entregareporte";
                    nombreColumnaId = "idEntregaReporte";
                    break;
                case 3:
                    nombreTabla = "entregadocumentofinal";
                    nombreColumnaId = "idEntregaDocumentoFinal";
                    break;
                default:
                    conexion.close();
                    return entregas;
            }

            String consulta = String.format("SELECT t.%s, t.nombre, t.descripcion, t.fechaInicio, t.fechaFin " +
                                            "FROM %s t " +
                                            "JOIN grupoee g ON t.grupoEE_idgrupoEE = g.idgrupoEE " +
                                            "WHERE g.Academico_idAcademico = ?",
                                            nombreColumnaId, nombreTabla);

            try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
                sentencia.setInt(1, idAcademico);

                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    Entrega entrega = new Entrega();
                    entrega.setIdEntrega(resultado.getInt(nombreColumnaId));
                    entrega.setNombre(resultado.getString("nombre"));
                    entrega.setDescripcion(resultado.getString("descripcion"));
                    entrega.setFechaInicio(resultado.getTimestamp("fechaInicio").toLocalDateTime().toLocalDate().toString());
                    entrega.setFechaFin(resultado.getTimestamp("fechaFin").toLocalDateTime().toLocalDate().toString());
                    entregas.add(entrega);
                }
            } finally {
                conexion.close();
            }
        }
        return entregas;
    }*/
    
    public static List<Entrega> obtenerEntregasPorTipo(String tipoEntrega, int idAcademico) throws SQLException {
    // Es buena práctica usar la interfaz (List) en lugar de la implementación (ArrayList).
    List<Entrega> entregas = new ArrayList<>();
    String query = "";
    Connection conexion = null;

    // 1. Se define la consulta SQL correcta basado en el String del tipo de entrega.
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
             // Si el tipo de entrega no es válido, se devuelve una lista vacía.
            return entregas;
    }

    try {
        // 2. Se abre la conexión usando tu clase `ConexionBD`.
        conexion = ConexionBD.abrirConexion();

        // 3. Se prepara y ejecuta la consulta.
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, idAcademico);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Entrega entrega = new Entrega();
                
                // Se usa el índice 1 para la columna de ID, ya que cambia de nombre.
                entrega.setIdEntrega(rs.getInt(1));
                entrega.setNombre(rs.getString("nombre"));
                entrega.setDescripcion(rs.getString("descripcion"));
                  
                // ¡Se obtiene la columna del nuevo campo!
                entrega.setTipoDocumento(rs.getString("tipoDocumento"));
                
                // Se usa getDate() para columnas DATE y se formatea. Es más seguro.
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
        // Se imprime el error y se relanza para que la capa superior lo maneje.
        System.err.println("ERROR al consultar entregas por tipo: " + ex.getMessage());
        throw ex;
    } finally {
        // 4. Se asegura de cerrar la conexión en el bloque finally.
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