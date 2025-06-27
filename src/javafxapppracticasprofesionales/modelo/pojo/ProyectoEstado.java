package javafxapppracticasprofesionales.modelo.pojo;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: ProyectoEstado.java 
    * Autor: Jose Luis Silva Gómez
    * Fecha: 15/06/2025
*/
public class ProyectoEstado {
    private int idProyectoEstado;
    private String nombreEstado;

    public ProyectoEstado() {
    }

    public ProyectoEstado(int idProyectoEstado, String nombreEstado) {
        this.idProyectoEstado = idProyectoEstado;
        this.nombreEstado = nombreEstado;
    }

    public int getIdProyectoEstado() {
        return idProyectoEstado;
    }

    public void setIdProyectoEstado(int idProyectoEstado) {
        this.idProyectoEstado = idProyectoEstado;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }
}
