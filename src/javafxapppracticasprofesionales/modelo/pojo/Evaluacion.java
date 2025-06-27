
package javafxapppracticasprofesionales.modelo.pojo;

import java.util.List;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: Evaluacion.java 
    * Autor: Angel Jonathan Puch Hernández 
    * Fecha: 15/06/2025
*/
public class Evaluacion {
    private int idEvaluacion;
    private float calificacionTotal;
    private String fecha;
    private String motivo;
    private String comentarios;
    private int idUsuario;
    private int idTipoEvaluacion;
    private int idExpediente;
    private List<EvaluacionDetalle> detalles;
    private String nombreTipoEvaluacion;
    private String nombreEvaluador;
    
    public Evaluacion() {
    }

    public Evaluacion(int idEvaluacion, float calificacionTotal, String fecha, String motivo, 
            String comentarios, int idUsuario, int idTipoEvaluacion, int idExpediente) {
        this.idEvaluacion = idEvaluacion;
        this.calificacionTotal = calificacionTotal;
        this.fecha = fecha;
        this.motivo = motivo;
        this.comentarios = comentarios;
        this.idUsuario = idUsuario;
        this.idTipoEvaluacion = idTipoEvaluacion;
        this.idExpediente = idExpediente;
    }

    public int getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(int idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public float getCalificacionTotal() {
        return calificacionTotal;
    }

    public void setCalificacionTotal(float calificacionTotal) {
        this.calificacionTotal = calificacionTotal;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTipoEvaluacion() {
        return idTipoEvaluacion;
    }

    public void setIdTipoEvaluacion(int idTipoEvaluacion) {
        this.idTipoEvaluacion = idTipoEvaluacion;
    }

    public int getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }

    public List<EvaluacionDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<EvaluacionDetalle> detalles) {
        this.detalles = detalles;
    }

    public String getNombreTipoEvaluacion() {
        return nombreTipoEvaluacion;
    }

    public void setNombreTipoEvaluacion(String nombreTipoEvaluacion) {
        this.nombreTipoEvaluacion = nombreTipoEvaluacion;
    }

    public String getNombreEvaluador() {
        return nombreEvaluador;
    }

    public void setNombreEvaluador(String nombreEvaluador) {
        this.nombreEvaluador = nombreEvaluador;
    }
    
}