package javafxapppracticasprofesionales.modelo.pojo;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: EvaluacionOV.java 
    * Autor: Jose Luis Silva Gómez
    * Fecha: 12/06/2025
*/
public class InfoEstudianteSesion {
    
    private int idEstudiante;
    private int idGrupo;
    private int idExpediente;

    public InfoEstudianteSesion() {}

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public int getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }
}
