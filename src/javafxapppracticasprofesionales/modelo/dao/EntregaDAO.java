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
}
