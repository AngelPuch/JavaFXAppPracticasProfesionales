
package javafxapppracticasprofesionales.modelo.pojo;

public class ConfigTabla {
    private final String tablaDocumento;
    private final String idEntregaPK;
    private final String idDocumentoPK;
    private final String idEntregaFK;

    public ConfigTabla(String tablaDocumento, String idEntregaPK, String idDocumentoPK, String idEntregaFK) {
        this.tablaDocumento = tablaDocumento;
        this.idEntregaPK = idEntregaPK;
        this.idDocumentoPK = idDocumentoPK;
        this.idEntregaFK = idEntregaFK;
    }

    public String getTablaDocumento() {
        return tablaDocumento;
    }

    public String getIdEntregaPK() {
        return idEntregaPK;
    }

    public String getIdDocumentoPK() {
        return idDocumentoPK;
    }

    public String getIdEntregaFK() {
        return idEntregaFK;
    }
}
