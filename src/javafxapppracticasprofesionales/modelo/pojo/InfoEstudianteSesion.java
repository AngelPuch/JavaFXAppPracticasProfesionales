package javafxapppracticasprofesionales.modelo.pojo;

/** 
* Project: JavaFXAppPracticasProfesionales 
* File: InfoEstudianteSesion.java 
* Author: Jose Luis Silva Gomez 
* Date: 2025-06-16 
* Description: Brief description of the file's purpose. 
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
