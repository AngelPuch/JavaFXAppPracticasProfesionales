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
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String tabla = "";
            String columnaId = "";
            switch (tipoEntrega) {
                case "DOCUMENTOS INICIALES":
                    tabla = "tipodocumentoinicio";
                    columnaId = "idTipoDocumentoInicio";
                    break;
                case "REPORTES":
                    tabla = "tipodocumentoreporte";
                    columnaId = "idTipoDocumentoReporte";
                    break;
                case "DOCUMENTOS FINALES":
                    tabla = "tipodocumentofinal";
                    columnaId = "idTipoDocumentoFinal";
                    break;
                default:
                    throw new SQLException("Tipo de entrega no válido: " + tipoEntrega);
            }

            String consulta = String.format("SELECT %s, nombre FROM %s", columnaId, tabla);

            try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
                 ResultSet resultado = sentencia.executeQuery()) {

                while (resultado.next()) {
                    TipoDocumento tipo = new TipoDocumento();
                    tipo.setIdTipoDocumento(resultado.getInt(columnaId));
                    tipo.setNombre(resultado.getString("nombre"));
                    tiposDocumento.add(tipo);
                }
            } finally {
                conexionBD.close();
            }
        }
        return tiposDocumento;
    }
}