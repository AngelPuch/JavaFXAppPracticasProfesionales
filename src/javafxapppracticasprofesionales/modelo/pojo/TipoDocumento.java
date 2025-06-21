package javafxapppracticasprofesionales.modelo.pojo;

import java.util.ArrayList;
import java.util.List;

public class TipoDocumento {
    private int idTipoDocumento;
    private String nombre;

    public TipoDocumento() {
    }
    
    public TipoDocumento(String nombre) {
        this.nombre = nombre;
    }

    public TipoDocumento(int idTipoDocumento, String nombre) {
        this.idTipoDocumento = idTipoDocumento;
        this.nombre = nombre;
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

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }    
}