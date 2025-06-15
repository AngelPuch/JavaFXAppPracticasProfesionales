package javafxapppracticasprofesionales.modelo.pojo;

public class Usuario {
    private int idUsuario;
    private String username;
    private String password;
    private int idAcademico;
    private int idEstudiante;
    private String roles;

    public Usuario() {
    }

    public Usuario(int idUsuario, String username, String password, int idAcademico, int idEstudiante, String roles) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.idAcademico = idAcademico;
        this.idEstudiante = idEstudiante;
        this.roles = roles;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdAcademico() {
        return idAcademico;
    }

    public void setIdAcademico(int idAcademico) {
        this.idAcademico = idAcademico;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return username;
    }
}