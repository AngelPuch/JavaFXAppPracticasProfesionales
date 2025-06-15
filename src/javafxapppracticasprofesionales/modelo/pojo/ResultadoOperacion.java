
package javafxapppracticasprofesionales.modelo.pojo;

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
