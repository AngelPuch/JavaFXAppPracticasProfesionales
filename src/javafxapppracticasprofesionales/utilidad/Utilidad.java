package javafxapppracticasprofesionales.utilidad;

import javafx.scene.control.Control;
import javafx.stage.Stage;

public class Utilidad {
    
    public static Stage getEscenarioComponente(Control componente) {
        return (Stage)componente.getScene().getWindow();
    }
    
}
