package javafxapppracticasprofesionales.modelo.pojo;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: Academico.java 
    * Autor: Rodrigo Luna Vázquez
    * Fecha: 13/06/2025
*/
public class Academico {
    private int idAcademico;
    private String numeroPersonal;
    private String nombre;
    private String correo;
    private int idUsuario;

    public Academico() {
    }

    public Academico(int idAcademico, String numeroPersonal, String nombre, String correo) {
        this.idAcademico = idAcademico;
        this.numeroPersonal = numeroPersonal;
        this.nombre = nombre;
        this.correo = correo;
    }

    public int getIdAcademico() {
        return idAcademico;
    }

    public void setIdAcademico(int idAcademico) {
        this.idAcademico = idAcademico;
    }

    public String getNumeroPersonal() {
        return numeroPersonal;
    }

    public void setNumeroPersonal(String numeroPersonal) {
        this.numeroPersonal = numeroPersonal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    
    
}
