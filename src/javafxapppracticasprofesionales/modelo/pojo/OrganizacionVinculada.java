package javafxapppracticasprofesionales.modelo.pojo;

import java.util.StringJoiner;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: OrganizacionVinculada.java 
    * Autor: Rodrigo Luna Vázquez 
    * Fecha: 12/06/2025
*/
public class OrganizacionVinculada {
    private int idOrganizacion;
    private String nombre;
    private String telefono;
    private String calle;
    private String numero;
    private String colonia;
    private String codigoPostal;
    private String municipio;
    private String estado;

    public OrganizacionVinculada() {
    }

    public OrganizacionVinculada(int idOrganizacion, String nombre, String telefono, String calle, 
            String numero, String colonia, String codigoPostal, String municipio, String estado) {
        this.idOrganizacion = idOrganizacion;
        this.nombre = nombre;
        this.telefono = telefono;
        this.calle = calle;
        this.numero = numero;
        this.colonia = colonia;
        this.codigoPostal = codigoPostal;
        this.municipio = municipio;
        this.estado = estado;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getDireccionCompleta() {
        StringJoiner direccion = new StringJoiner(", ");
        
        if (calle != null && !calle.trim().isEmpty()) {
            String calleYNumero = calle.trim();
            if (numero != null && !numero.trim().isEmpty()) {
                calleYNumero += " #" + numero.trim();
            }
            direccion.add(calleYNumero);
        }

        if (colonia != null && !colonia.trim().isEmpty()) {
            direccion.add(colonia.trim());
        }
        
        if (codigoPostal != null && !codigoPostal.trim().isEmpty()) {
            direccion.add("C.P. " + codigoPostal.trim());
        }
        
        if (municipio != null && !municipio.trim().isEmpty()) {
            direccion.add(municipio.trim());
        }
        
        if (estado != null && !estado.trim().isEmpty()) {
            direccion.add(estado.trim());
        }
        
        return direccion.toString();
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}