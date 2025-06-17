package javafxapppracticasprofesionales.modelo.pojo;

public class EstudianteConProyecto {
    private String nombreEstudiante;
    private String matricula;
    private String nombreProyecto;
    private int semestre;
    private String correo;

    public EstudianteConProyecto() {
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

    public EstudianteConProyecto(String nombreEstudiante, String matricula, String nombreProyecto, int semestre, String correo) {
        this.nombreEstudiante = nombreEstudiante;
        this.matricula = matricula;
        this.nombreProyecto = nombreProyecto;
        this.semestre = semestre;
        this.correo = correo;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }
}
