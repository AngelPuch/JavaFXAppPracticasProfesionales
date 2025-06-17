/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.JavaFXAppPracticasProfesionales;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;

/**
 * FXML Controller class
 *
 * @author grill
 */
public class FXMLPrincipalProfesorController implements Initializable {

    @FXML
    private Label lbNombreVentana;
    @FXML
    private Label lbNombreUsuario;
    @FXML
    private AnchorPane apCentral;
    private Usuario usuarioSesion;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarInformacion(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
        cargarInformacion();
    }
    
    private void cargarInformacion() {
        if (usuarioSesion != null) {
            lbNombreUsuario.setText(usuarioSesion.toString());
        }
    }

    @FXML
    private void btnClicConsultarExpediente(ActionEvent event) {
        cargarEscenas("vista/FXMLConsultarExpediente.fxml");
        lbNombreVentana.setText("Consultar expediente de estudiante");
    }

    @FXML
    private void btnClicValidarEntregaDocumentos(ActionEvent event) {
            try {
            // Abrimos la NUEVA ventana, exclusiva para el flujo de validación.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLValidarSeleccionarTipoEntrega.fxml"));
            Parent vista = loader.load();

            Stage escenario = new Stage();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Validar Entregas - Paso 1");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();

        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "Error al cargar la ventana: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnClicCerrarSesion(ActionEvent event) {
        try {
            SesionUsuario.getInstancia().cerrarSesion();
            FXMLLoader cargador = new FXMLLoader(JavaFXAppPracticasProfesionales.class.getResource("vista/FXMLInicioSesion.fxml"));
            Parent vistaInicioSesion = cargador.load();
            Scene escenaInicio = new Scene(vistaInicioSesion);
            Stage escenarioActual = Utilidad.getEscenarioComponente(lbNombreVentana);
            escenarioActual.setScene(escenaInicio);
            escenarioActual.setTitle("Inicio de sesión");
            escenarioActual.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void cargarEscenas(String ruta) {
        try {
            FXMLLoader loader = new FXMLLoader(JavaFXAppPracticasProfesionales.class.getResource(ruta));
            Parent root = loader.load();
            apCentral.getChildren().setAll(root);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
