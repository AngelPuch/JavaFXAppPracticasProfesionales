package javafxapppracticasprofesionales.modelo.pojo;

public class Usuario {
    private int idUsuario;
    private String username;
    private String password;
    private String nombre;
    private String rolPrincipal;
    private Integer idEstudiante;
    private Integer idAcademico;

    public Usuario() {
    }

    public Usuario(int idUsuario, String username, String password, String nombre, String rolPrincipal, Integer idEstudiante, Integer idAcademico) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.rolPrincipal = rolPrincipal;
        this.idEstudiante = idEstudiante;
        this.idAcademico = idAcademico;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRolPrincipal() {
        return rolPrincipal;
    }

    public void setRolPrincipal(String rolPrincipal) {
        this.rolPrincipal = rolPrincipal;
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Integer getIdAcademico() {
        return idAcademico;
    }

    public void setIdAcademico(Integer idAcademico) {
        this.idAcademico = idAcademico;
    }



    @Override
    public String toString() {
        return nombre;
    }
}