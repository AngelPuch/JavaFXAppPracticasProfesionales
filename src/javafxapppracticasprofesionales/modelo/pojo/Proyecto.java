
package javafxapppracticasprofesionales.modelo.pojo;

public class Proyecto {
    private int idProyecto;
    private String nombre;
    private String descripcion;
    private int numeroCupos;
    private String objetivo;
    private OrganizacionVinculada organizacion;
    private ResponsableProyecto responsable;
    private ProyectoEstado estado;

    public Proyecto() {
    }

    public Proyecto(int idProyecto, String nombre, String descripcion, int numeroCupos, String objetivo, 
            OrganizacionVinculada organizacion, ResponsableProyecto responsable, ProyectoEstado estado) {
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.numeroCupos = numeroCupos;
        this.objetivo = objetivo;
        this.organizacion = organizacion;
        this.responsable = responsable;
        this.estado = estado;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getNumeroCupos() {
        return numeroCupos;
    }

    public void setNumeroCupos(int numeroCupos) {
        this.numeroCupos = numeroCupos;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public OrganizacionVinculada getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(OrganizacionVinculada organizacion) {
        this.organizacion = organizacion;
    }

    public ResponsableProyecto getResponsable() {
        return responsable;
    }

    public void setResponsable(ResponsableProyecto responsable) {
        this.responsable = responsable;
    }

    public ProyectoEstado getEstado() {
        return estado;
    }

    public void setEstado(ProyectoEstado estado) {
        this.estado = estado;
    }
    
    
    
        
}