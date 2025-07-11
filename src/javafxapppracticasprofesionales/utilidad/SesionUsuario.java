package javafxapppracticasprofesionales.utilidad;

import javafxapppracticasprofesionales.modelo.pojo.Usuario;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: SesionUsuario.java 
    * Autor: Angel Jonathan Puch Hernández
    * Fecha: 12/06/2025
*/
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

    public String getRolUsuario() {
        if (usuarioLogueado != null && usuarioLogueado.getRolPrincipal() != null) {
            return usuarioLogueado.getRolPrincipal();
        }
        return "login_checker";
    }
}