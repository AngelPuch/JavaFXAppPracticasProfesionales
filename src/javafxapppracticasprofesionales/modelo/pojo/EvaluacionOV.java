package javafxapppracticasprofesionales.modelo.pojo;


/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: EvaluacionOV.java 
    * Autor: Jose Luis Silva Gómez
    * Fecha: 15/06/2025
*/
public class EvaluacionOV {
     private String nombreAlumno;
    private String matricula;
    private String nombreOrganizacion;
    private String nombreProyecto;
    private String responsableProyecto;
    private int horasCubiertas;

    public EvaluacionOV() {
    }

    public EvaluacionOV(String nombreAlumno, String matricula, String nombreOrganizacion, String nombreProyecto, String responsableProyecto, int horasCubiertas) {
        this.nombreAlumno = nombreAlumno;
        this.matricula = matricula;
        this.nombreOrganizacion = nombreOrganizacion;
        this.nombreProyecto = nombreProyecto;
        this.responsableProyecto = responsableProyecto;
        this.horasCubiertas = horasCubiertas;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombreOrganizacion() {
        return nombreOrganizacion;
    }

    public void setNombreOrganizacion(String nombreOrganizacion) {
        this.nombreOrganizacion = nombreOrganizacion;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getResponsableProyecto() {
        return responsableProyecto;
    }

    public void setResponsableProyecto(String responsableProyecto) {
        this.responsableProyecto = responsableProyecto;
    }

    public int getHorasCubiertas() {
        return horasCubiertas;
    }

    public void setHorasCubiertas(int horasCubiertas) {
        this.horasCubiertas = horasCubiertas;
    }
    
    
}
