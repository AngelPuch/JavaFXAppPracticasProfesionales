package javafxapppracticasprofesionales.utilidad;

import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.stage.Stage;

public class Utilidad {
    
    public static void mostrarAlertaSimple(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
    public static Stage getEscenarioComponente(Control componente) {
        return (Stage)componente.getScene().getWindow();
    }
    
}
