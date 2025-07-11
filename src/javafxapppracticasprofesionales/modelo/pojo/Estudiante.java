package javafxapppracticasprofesionales.modelo.pojo;


/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: FXMLProgramarEntregaController.java 
    * Autor: Jose Luis Silva Gómez
    * Fecha: 13/06/2025
*/
public class Estudiante {
    private int idEstudiante;
    private String nombre;
    private String matricula;
    private int semestre;
    private String correo;

    public Estudiante() {
    }

    public Estudiante(int idEstudiante, String nombre, String matricula, int semestre, String correo) {
        this.idEstudiante = idEstudiante;
        this.nombre = nombre;
        this.matricula = matricula;
        this.semestre = semestre;
        this.correo = correo;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }  
}
