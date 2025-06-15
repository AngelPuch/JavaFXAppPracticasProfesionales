
package javafxapppracticasprofesionales.modelo.pojo;

public class OrganizacionVinculada {
    private int idOrganizacion;
    private String nombre;
    private String direccion;
    private String telefono;
    
    public OrganizacionVinculada() {
    }

    public OrganizacionVinculada(int idOrganizacion, String nombre, String direccion, String telefono) {
        this.idOrganizacion = idOrganizacion;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public int getIdOrganizacion() {
        return idOrganizacion;
    }

    public void setIdOrganizacion(int idOrganizacion) {
        this.idOrganizacion = idOrganizacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    
}