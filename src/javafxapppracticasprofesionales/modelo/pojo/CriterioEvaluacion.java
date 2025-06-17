
package javafxapppracticasprofesionales.modelo.pojo;

public class CriterioEvaluacion {

    private String criterio;
    private String competente;
    private String independiente;
    private String basicoAvanzado;
    private String basicoMinimo;
    private String noCompetente;

    public CriterioEvaluacion() {
    }
    
    public CriterioEvaluacion(String criterio, String competente, String independiente, String basicoAvanzado, String basicoMinimo, String noCompetente) {
        this.criterio = criterio;
        this.competente = competente;
        this.independiente = independiente;
        this.basicoAvanzado = basicoAvanzado;
        this.basicoMinimo = basicoMinimo;
        this.noCompetente = noCompetente;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public String getCompetente() {
        return competente;
    }

    public void setCompetente(String competente) {
        this.competente = competente;
    }

    public String getIndependiente() {
        return independiente;
    }

    public void setIndependiente(String independiente) {
        this.independiente = independiente;
    }

    public String getBasicoAvanzado() {
        return basicoAvanzado;
    }

    public void setBasicoAvanzado(String basicoAvanzado) {
        this.basicoAvanzado = basicoAvanzado;
    }

    public String getBasicoMinimo() {
        return basicoMinimo;
    }

    public void setBasicoMinimo(String basicoMinimo) {
        this.basicoMinimo = basicoMinimo;
    }

    public String getNoCompetente() {
        return noCompetente;
    }

    public void setNoCompetente(String noCompetente) {
        this.noCompetente = noCompetente;
    }

    
}
