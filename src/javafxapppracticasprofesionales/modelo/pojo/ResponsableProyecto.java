package javafxapppracticasprofesionales.modelo.pojo;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: ResponsableProyecto.java 
    * Autor: Rodrigo Luna Vázquez 
    * Fecha: 12/06/2025
*/
public class ResponsableProyecto {
    private int idResponsable;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String cargo;
    private String correo;
    private String telefono;
    private int idOrganizacion; 

    private OrganizacionVinculada organizacionVinculada;

    public ResponsableProyecto() {
    }

    public ResponsableProyecto(int idResponsable, String nombre, String apellidoPaterno, String apellidoMaterno, String cargo, String correo, OrganizacionVinculada organizacionVinculada) {
        this.idResponsable = idResponsable;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.cargo = cargo;
        this.correo = correo;
        this.organizacionVinculada = organizacionVinculada;
    }

    public int getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(int idResponsable) {
        this.idResponsable = idResponsable;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public OrganizacionVinculada getOrganizacionVinculada() {
        return organizacionVinculada;
    }

    public void setOrganizacionVinculada(OrganizacionVinculada organizacionVinculada) {
        this.organizacionVinculada = organizacionVinculada;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
     public int getIdOrganizacion() {
        return idOrganizacion;
    }

    public void setIdOrganizacion(int idOrganizacion) {
        this.idOrganizacion = idOrganizacion;
    }
    
    public String getNombreCompleto() {
        return nombre + " " + apellidoPaterno + (apellidoMaterno != null && !apellidoMaterno.isEmpty() ? " " + apellidoMaterno : "");
    }

    @Override
    public String toString() {
        return getNombreCompleto();
    }
}