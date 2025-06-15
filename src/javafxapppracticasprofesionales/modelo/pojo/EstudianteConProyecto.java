package javafxapppracticasprofesionales.modelo.pojo;

/** 
* Project: JavaFX Sales System 
* File: ClassName.java 
* Author: Jose Luis Silva Gomez 
* Date: YYYY-MM-DD 
* Description: Brief description of the file's purpose. 
*/
public class EstudianteConProyecto {
    private String nombreEstudiante;
    private String matricula;
    private String nombreProyecto;

    public EstudianteConProyecto() {
    }

    public EstudianteConProyecto(String nombreEstudiante, String matricula, String nombreProyecto) {
        this.nombreEstudiante = nombreEstudiante;
        this.matricula = matricula;
        this.nombreProyecto = nombreProyecto;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }
}
