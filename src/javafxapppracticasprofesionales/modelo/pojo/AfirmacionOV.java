package javafxapppracticasprofesionales.modelo.pojo;

import java.util.Objects;

/**
 * Representa una afirmación (pregunta) en el formulario de evaluación de la OV.
 * Este POGO solo contiene los datos, la lógica de la interfaz (como los ToggleGroup)
 * se maneja por completo en el controlador.
 */
public class AfirmacionOV {

    private int idAfirmacion;
    private String descripcion;
    private String categoria;
    
    // Campo para guardar el valor (1-5) de la respuesta del usuario. Se inicializa en 0.
    private int respuestaSeleccionada; 

    // NO es necesario el ToggleGroup aquí, el controlador lo gestiona.

    public AfirmacionOV() {
        // El constructor está limpio. No hay lógica de interfaz aquí.
    }

    // --- Getters y Setters ---

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

    // --- Métodos hashCode y equals ---
    // Son importantes para que el objeto funcione correctamente como llave en el Map del controlador.
    // Se basan en el identificador único 'idAfirmacion'.

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