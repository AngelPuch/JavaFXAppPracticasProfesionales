package javafxapppracticasprofesionales.modelo.pojo;

public class Grupo {
    private int idGrupo;
    private String seccion;
    private String bloque;
    private Academico academico;
    private ExperienciaEducativa experienciaEducativa;
    private Periodo periodo;

    public Grupo() {
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getBloque() {
        return bloque;
    }

    public void setBloque(String bloque) {
        this.bloque = bloque;
    }

    public Academico getAcademico() {
        return academico;
    }

    public void setAcademico(Academico academico) {
        this.academico = academico;
    }

    public ExperienciaEducativa getExperienciaEducativa() {
        return experienciaEducativa;
    }

    public void setExperienciaEducativa(ExperienciaEducativa experienciaEducativa) {
        this.experienciaEducativa = experienciaEducativa;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    @Override
    public String toString() {
        return "Sección " + seccion + " - NRC " + experienciaEducativa.getNrc();
    }
}