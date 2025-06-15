
package javafxapppracticasprofesionales.modelo.pojo;

public class ResponsableProyecto {
    private int idResponsable;
    private String nombre;
    private String cargo;
    private String correo;
    private String telefono;
    private OrganizacionVinculada organizacionVinculada;

    public ResponsableProyecto() {
    }

    public ResponsableProyecto(int idResponsable, String nombre, String cargo, String correo, OrganizacionVinculada organizacionVinculada) {
        this.idResponsable = idResponsable;
        this.nombre = nombre;
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
    

    
}