package javafxapppracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxapppracticasprofesionales.modelo.ConexionBD;
import javafxapppracticasprofesionales.modelo.pojo.TipoDocumento;

public class TipoDocumentoDAO {
    
    public static ArrayList<TipoDocumento> obtenerTiposDeDocumento(String tipoEntrega) throws SQLException {
        ArrayList<TipoDocumento> tiposDocumento = new ArrayList<>();
        TipoEntregaDB tipoEntregaDB;
        
        try {
            tipoEntregaDB = TipoEntregaDB.fromString(tipoEntrega);
        } catch (IllegalArgumentException e) {
            throw new SQLException(e.getMessage());
        }
        String consulta = String.format("SELECT %s, nombre FROM %s",
                tipoEntregaDB.getColumnaId(), tipoEntregaDB.getTabla());
        
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                tiposDocumento.add(convertirRegistroTipoDocumento(resultado, tipoEntregaDB));
            }
            conexionBD.close();
            sentencia.close();
            resultado.close();
        } else {
            throw new SQLException("Sin conexión a la Base de Datos");
        }
        return tiposDocumento;
    }
    
    public enum TipoEntregaDB {
        DOCUMENTOS_INICIALES("tipodocumentoinicio", "idTipoDocumentoInicio"),
        REPORTES("tipodocumentoreporte", "idTipoDocumentoReporte"),
        DOCUMENTOS_FINALES("tipodocumentofinal", "idTipoDocumentoFinal");

        private final String tabla;
        private final String columnaId;

        TipoEntregaDB(String tabla, String columnaId) {
            this.tabla = tabla;
            this.columnaId = columnaId;
        }

        public String getTabla() {
            return tabla;
        }

        public String getColumnaId() {
            return columnaId;
        }

        public static TipoEntregaDB fromString(String text) {
            for (TipoEntregaDB b : TipoEntregaDB.values()) {
                if (b.name().equalsIgnoreCase(text.replace(" ", "_"))) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Tipo de entrega no válido: " + text);
        }
    }
    
    private static TipoDocumento convertirRegistroTipoDocumento(ResultSet resultado, 
            TipoEntregaDB tipoEntregaDB) throws SQLException {
        TipoDocumento tipo = new TipoDocumento();
        tipo.setIdTipoDocumento(resultado.getInt(tipoEntregaDB.getColumnaId()));
        tipo.setNombre(resultado.getString("nombre"));
        return tipo;
    }
}