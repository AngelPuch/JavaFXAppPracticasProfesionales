package javafxapppracticasprofesionales.modelo.pojo;

import java.util.Objects;

public class AfirmacionOV {

    private int idAfirmacion;
    private String descripcion;
    private String categoria;
    private int respuestaSeleccionada; 

    public AfirmacionOV() {
    }

    public AfirmacionOV(int idAfirmacion, String descripcion, String categoria, int respuestaSeleccionada) {
        this.idAfirmacion = idAfirmacion;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.respuestaSeleccionada = respuestaSeleccionada;
    }

    public int getIdAfirmacion() {
        return idAfirmacion;
    }

    public void setIdAfirmacion(int idAfirmacion) {
        this.idAfirmacion = idAfirmacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getRespuestaSeleccionada() {
        return respuestaSeleccionada;
    }

    public void setRespuestaSeleccionada(int respuestaSeleccionada) {
        this.respuestaSeleccionada = respuestaSeleccionada;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAfirmacion);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AfirmacionOV other = (AfirmacionOV) obj;
        return this.idAfirmacion == other.idAfirmacion;
    }
}