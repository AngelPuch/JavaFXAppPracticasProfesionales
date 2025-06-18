package javafxapppracticasprofesionales.utilidad;

import java.util.function.UnaryOperator;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

public class Utilidad {
    
    public static Stage getEscenarioComponente(Control componente) {
        return (Stage)componente.getScene().getWindow();
    }
    
    public static void configurarTextAreaConContador(TextInputControl componenteText, Label contadorLabel, int maxLength) {
        if (componenteText == null || contadorLabel == null) {
            return;
        }

        UnaryOperator<TextFormatter.Change> filtro = change -> {
            String newText = change.getControlNewText();
            return newText.length() > maxLength ? null : change;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filtro);
        componenteText.setTextFormatter(textFormatter);

        contadorLabel.setText("0/" + maxLength + " Max.");
        componenteText.textProperty().addListener((observable, oldValue, newValue) -> {
            contadorLabel.setText(newValue.length() + "/" + maxLength + " Max.");
        });
    }
    
}
