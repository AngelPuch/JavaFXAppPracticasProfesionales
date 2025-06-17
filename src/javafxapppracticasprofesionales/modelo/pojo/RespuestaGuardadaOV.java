package javafxapppracticasprofesionales.modelo.pojo;

/** 
* Project: JavaFXAppPracticasProfesionales 
* File: RespuestaGuardadaOV.java 
* Author: Jose Luis Silva Gomez 
* Date: 2025-06-16 
* Description: Brief description of the file's purpose. 
*/
public class RespuestaGuardadaOV {
    private String afirmacion;
    private int respuesta;

    public RespuestaGuardadaOV() {
    }

    public RespuestaGuardadaOV(String afirmacion, int respuesta) {
        this.afirmacion = afirmacion;
        this.respuesta = respuesta;
    }

    public String getAfirmacion() {
        return afirmacion;
    }

    public void setAfirmacion(String afirmacion) {
        this.afirmacion = afirmacion;
    }

    public int getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(int respuesta) {
        this.respuesta = respuesta;
    }
    
    
}
