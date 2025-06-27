package javafxapppracticasprofesionales.utilidad;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.function.UnaryOperator;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
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
    
    public static void abrirDocumento(String rutaArchivo) {
        try {
            File archivo = new File(rutaArchivo);
            if (archivo.exists() && archivo.isFile()) {
                Desktop.getDesktop().open(archivo);
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Archivo no encontrado", 
                    "El archivo no pudo ser localizado en la ruta: \n" + rutaArchivo, Alert.AlertType.ERROR);
            }
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error al abrir", 
                "No se pudo abrir el archivo. Asegúrate de tener un programa compatible para visualizarlo.", Alert.AlertType.ERROR);
        } catch (SecurityException e) {
            AlertaUtilidad.mostrarAlertaSimple("Sin permisos", 
                "No se tienen los permisos necesarios para acceder al archivo.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            AlertaUtilidad.mostrarAlertaSimple("Error inesperado", 
                "Ocurrió un error general al intentar abrir el archivo: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
}
