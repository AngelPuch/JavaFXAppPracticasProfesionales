package javafxapppracticasprofesionales.modelo.pojo;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: ResultadoOperacion.java 
    * Autor: Angel Jonathan Puch Hernández
    * Fecha: 12/06/2025
*/
public class ResultadoOperacion {
    private boolean isError;
    private String mensaje;

    public ResultadoOperacion() {
    }

    public ResultadoOperacion(boolean isError, String mensaje) {
        this.isError = isError;
        this.mensaje = mensaje;
    }

    public boolean isError() {
        return isError;
    }

    public void setIsError(boolean isError) {
        this.isError = isError;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }    
}
