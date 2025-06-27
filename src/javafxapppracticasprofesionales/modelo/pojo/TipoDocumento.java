package javafxapppracticasprofesionales.modelo.pojo;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: TipoDocumento.java 
    * Autor: Angel Jonathan Puch Hernández
    * Fecha: 13/06/2025
*/
public class TipoDocumento {
    private int idTipoDocumento;
    private String nombre;

    public TipoDocumento() {
    }

    public TipoDocumento(int idTipoDocumento, String nombre) {
        this.idTipoDocumento = idTipoDocumento;
        this.nombre = nombre;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
   
    

    @Override
    public String toString() {
        return nombre;
    }   
}