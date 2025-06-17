package javafxapppracticasprofesionales.modelo.pojo;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;


public class AfirmacionOV {
    
    private int idAfirmacion;
    private String categoria;
    private String descripcion;
    private ToggleGroup grupoOpciones;
    
    public AfirmacionOV() {
        this.grupoOpciones = new ToggleGroup();
    }

    public AfirmacionOV(int idAfirmacion, String categoria, String descripcion, ToggleGroup grupoOpciones) {
        this.idAfirmacion = idAfirmacion;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.grupoOpciones = grupoOpciones;
    }
    
    public int getIdAfirmacion() { return idAfirmacion; }
    
    public void setIdAfirmacion(int idAfirmacion) { this.idAfirmacion = idAfirmacion; }

    public String getCategoria() { return categoria; }
    
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDescripcion() { return descripcion; }
    
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public ToggleGroup getGrupoOpciones() { return grupoOpciones; }
    
    public void setGrupoOpciones(ToggleGroup grupoOpciones) { this.grupoOpciones = grupoOpciones; }
    
    public int getRespuestaSeleccionada() {
        RadioButton seleccionado = (RadioButton) grupoOpciones.getSelectedToggle();
        if (seleccionado != null) {
            return Integer.parseInt(seleccionado.getUserData().toString());
        }
        return 0; 
    }
}