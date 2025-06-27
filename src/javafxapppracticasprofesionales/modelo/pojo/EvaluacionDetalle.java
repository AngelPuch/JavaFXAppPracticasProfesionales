package javafxapppracticasprofesionales.modelo.pojo;


/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: EvaluacionDetalle.java 
    * Autor: Angel Jonathan Puch Hernández 
    * Fecha: 15/06/2025
*/
public class EvaluacionDetalle {
    private int idEvaluacionDetalle;
    private int idEvaluacion;
    private int idCriterio;
    private float calificacion;

    public EvaluacionDetalle() {}

    public EvaluacionDetalle(int idEvaluacionDetalle, int idEvaluacion, int idCriterio, float calificacion) {
        this.idEvaluacionDetalle = idEvaluacionDetalle;
        this.idEvaluacion = idEvaluacion;
        this.idCriterio = idCriterio;
        this.calificacion = calificacion;
    }

    public int getIdEvaluacionDetalle() {
        return idEvaluacionDetalle;
    }

    public void setIdEvaluacionDetalle(int idEvaluacionDetalle) {
        this.idEvaluacionDetalle = idEvaluacionDetalle;
    }

    public int getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(int idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public int getIdCriterio() {
        return idCriterio;
    }

    public void setIdCriterio(int idCriterio) {
        this.idCriterio = idCriterio;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    
}