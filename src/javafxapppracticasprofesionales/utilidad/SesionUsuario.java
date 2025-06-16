package javafxapppracticasprofesionales.utilidad;


import javafxapppracticasprofesionales.modelo.pojo.Usuario;

public class SesionUsuario {
    private static SesionUsuario instancia;
    private Usuario usuarioLogueado;

    private SesionUsuario() {
    }

    public static SesionUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SesionUsuario();
        }
        return instancia;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }
    
    public void cerrarSesion() {
        this.usuarioLogueado = null;
    }

    /**
     * Devuelve el primer rol del usuario logueado. Si no hay sesión,
     * devuelve "login_checker" para la conexión inicial.
     * @return String con el nombre del rol.
     */
    public String getRolUsuario() {
        if (usuarioLogueado != null && usuarioLogueado.getRolPrincipal() != null) {
            return usuarioLogueado.getRolPrincipal();
        }
        // Si no hay nadie logueado, se usa el rol por defecto para el login.
        return "login_checker";
    }
}