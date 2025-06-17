package javafxapppracticasprofesionales.modelo.pojo;

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
