package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;

/**
 * Project: JavaFXAppPracticasProfesionales
 * File: FXMLPreEvaluarOVController.java
 * Author: Jose Luis Silva Gomez
 * Date: 2025-06-16
 * Description: Brief description of the file's purpose.
 */
public class FXMLPreEvaluarOVController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnClicEvaluar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLEvaluarOrganizacionVinculada.fxml"));
            Parent vista = loader.load();

            Stage escenario = new Stage();
            escenario.setTitle("Evaluar Organización Vinculada - Paso 1");
            escenario.setScene(new Scene(vista));
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo cargar la ventana de evaluación.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
}
