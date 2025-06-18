
package javafxapppracticasprofesionales.modelo.pojo;

public class Avance {

    private int id;
    private String nombre;
    private String fechaEntrega;
    private String estado;
    private String rutaArchivo;
    private String comentarios;

    public Avance() {
    }

    public Avance(int id, String nombre, String fechaEntrega, String estado, String rutaArchivo, String comentarios) {
        this.id = id;
        this.nombre = nombre;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.rutaArchivo = rutaArchivo;
        this.comentarios = comentarios;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
    
    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
    
    
    @Override
    public String toString() {
        return nombre;
    }
}