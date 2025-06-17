/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;

/**
 *
 * @author Dell
 */
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
                // El nombre del campo ID es diferente en cada tabla
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
            
            // --- LÓGICA PARA CONSTRUIR LA CONSULTA DINÁMICA ---
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
            } else { // entregadocumentofinal
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
            // --- FIN DE LA LÓGICA DE CONSTRUCCIÓN ---

            PreparedStatement sentencia = conexionBD.prepareStatement(sql);
            sentencia.setInt(1, idExpediente); // El idExpediente para el JOIN
            sentencia.setInt(2, idGrupo);      // El idGrupo para el WHERE
            
            ResultSet resultado = sentencia.executeQuery();

            while(resultado.next()){
                Entrega entrega = new Entrega();
                entrega.setIdEntrega(resultado.getInt(campoIdEntregaPK));
                entrega.setNombre(resultado.getString("nombre"));
                entrega.setDescripcion(resultado.getString("descripcion"));
                entrega.setFechaInicio(resultado.getString("fechaInicio"));
                entrega.setFechaFin(resultado.getString("fechaFin"));
                
                // Determinar el estado basado en si se encontró un documento
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
    
    public static ArrayList<Entrega> obtenerEntregasPorTipo(int idTipoDocumento) throws SQLException {
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
                    // Si el tipo no es válido, cerramos la conexión y devolvemos una lista vacía.
                    conexion.close();
                    return entregas;
            }

            String consulta = String.format("SELECT %s, nombre, descripcion, fechaInicio, fechaFin FROM %s",
                                            nombreColumnaId, nombreTabla);

            try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    Entrega entrega = new Entrega();
                    entrega.setIdEntrega(resultado.getInt(nombreColumnaId));
                    entrega.setNombre(resultado.getString("nombre"));
                    entrega.setDescripcion(resultado.getString("descripcion"));
                    // Reemplazo para la línea 192
                    entrega.setFechaInicio(resultado.getTimestamp("fechaInicio").toLocalDateTime().toLocalDate().toString());

                    // Reemplazo para la línea 193
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
