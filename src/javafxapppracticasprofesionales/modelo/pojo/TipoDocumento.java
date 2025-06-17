package javafxapppracticasprofesionales.modelo.pojo;

import java.util.ArrayList;
import java.util.List;

public class TipoDocumento {
    private int idTipoDocumento;
    private String nombre;

    public TipoDocumento() {
    }

    
    public TipoDocumento(String nombre) {
        this.nombre = nombre;
    }

    public TipoDocumento(int idTipoDocumento, String nombre) {
        this.idTipoDocumento = idTipoDocumento;
        this.nombre = nombre;
    }

    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public static List<TipoDocumento> obtenerTiposDocumentoInicial() {
        ArrayList<TipoDocumento> tipos = new ArrayList<>();
        tipos.add(new TipoDocumento("Carta de aceptación"));
        tipos.add(new TipoDocumento("Constancia de seguro"));
        tipos.add(new TipoDocumento("Cronograma"));
        tipos.add(new TipoDocumento("Horario"));
        tipos.add(new TipoDocumento("Oficio asignación"));
        return tipos;
    }

    public static List<TipoDocumento> obtenerTiposDocumentoReporte() {
        ArrayList<TipoDocumento> tipos = new ArrayList<>();
        tipos.add(new TipoDocumento("Reporte Mensual"));
        tipos.add(new TipoDocumento("Reporte 210 horas"));
        tipos.add(new TipoDocumento("Reporte final"));
        return tipos;
    }

    public static List<TipoDocumento> obtenerTiposDocumentoFinal() {
        ArrayList<TipoDocumento> tipos = new ArrayList<>();
        tipos.add(new TipoDocumento("Reporte Final"));
        tipos.add(new TipoDocumento("Constancia de Liberación"));
        tipos.add(new TipoDocumento("Evaluación de OV"));
        tipos.add(new TipoDocumento("Autoevaluación"));
        return tipos;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }
    
}