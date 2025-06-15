package javafxapppracticasprofesionales.modelo.pojo;

/** 
* Project: JavaFX Sales System 
* File: ClassName.java 
* Author: Jose Luis Silva Gomez 
* Date: YYYY-MM-DD 
* Description: Brief description of the file's purpose. 
*/
public class Estudiante {
    private int idEstudiante;
    private String nombre;
    private String matricula;
    private int semestre;
    private String corre;

    public Estudiante() {
    }

    public Estudiante(int idEstudiante, String nombre, String matricula, int semestre, String corre) {
        this.idEstudiante = idEstudiante;
        this.nombre = nombre;
        this.matricula = matricula;
        this.semestre = semestre;
        this.corre = corre;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public String getCorre() {
        return corre;
    }

    public void setCorre(String corre) {
        this.corre = corre;
    }
    
}
