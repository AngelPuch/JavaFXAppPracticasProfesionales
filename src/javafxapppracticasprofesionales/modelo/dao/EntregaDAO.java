package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
            String sql = String.format("SELECT * FROM %s", tabla);
            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
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
    
    public static ArrayList<Entrega> obtenerEntregasPorTipo(int idTipoDocumento, int idAcademico) throws SQLException {
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
    }
}
