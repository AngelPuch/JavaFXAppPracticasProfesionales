
package javafxapppracticasprofesionales.modelo.pojo;

public class ExperienciaEducativa {
    private int idExperienciaEducativa;
    private String nombre;
    private String nrc;
    private int creditos;

    public ExperienciaEducativa() {
    }

    public ExperienciaEducativa(int idExperienciaEducativa, String nombre, String nrc, int creditos) {
        this.idExperienciaEducativa = idExperienciaEducativa;
        this.nombre = nombre;
        this.nrc = nrc;
        this.creditos = creditos;
    }

    public int getIdExperienciaEducativa() {
        return idExperienciaEducativa;
    }

    public void setIdExperienciaEducativa(int idExperienciaEducativa) {
        this.idExperienciaEducativa = idExperienciaEducativa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNrc() {
        return nrc;
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }
    
    
    
}
