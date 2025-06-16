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
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

/**
 * FXML Controller class
 *
 * @author grill
 */
public class FXMLSeleccionarTipoEntregaController implements Initializable {

    @FXML
    private Label lbTitulo;
    private String tipoEntregaSeleccionado;




    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tipoEntregaSeleccionado = null;
    }    

    @FXML
    private void btnClicDocIniciales(ActionEvent event) {
        tipoEntregaSeleccionado = "DOCUMENTOS INICIALES";
        lbTitulo.setText("Seleccionado: Documentos Iniciales");
    }

    @FXML
    private void btnClicDocFinales(ActionEvent event) {
        tipoEntregaSeleccionado = "DOCUMENTOS FINALES";
        lbTitulo.setText("Seleccionado: Documentos Finales");
    }

    @FXML
    private void btnClicReportes(ActionEvent event) {
        tipoEntregaSeleccionado = "REPORTES";
        lbTitulo.setText("Seleccionado: Reportes");
    }

    @FXML
    private void btnClicContinuar(ActionEvent event) {
        // Primero, se valida que se haya seleccionado una opción.
        if (tipoEntregaSeleccionado == null || tipoEntregaSeleccionado.isEmpty()) {
            AlertaUtilidad.mostrarAlertaSimple("Sin selección", 
                "Por favor, selecciona un tipo de entrega antes de continuar.", Alert.AlertType.WARNING);
            return; // Detiene la ejecución si no hay nada seleccionado.
        }

        // El switch actúa según la opción guardada en la variable.
        switch (tipoEntregaSeleccionado) {
            case "DOCUMENTOS INICIALES":
                irSiguientePantallaDocumentoInicial();
                // Aquí iría la lógica para abrir la ventana de documentos iniciales.
                // Ejemplo: irPantallaDocumentosIniciales();
                break;

            case "DOCUMENTOS FINALES":
                System.out.println("Acción para Documentos Finales...");
                // Aquí iría la lógica para abrir la ventana de documentos finales.
                // Ejemplo: irPantallaDocumentosFinales();
                break;

            case "REPORTES":
                System.out.println("Acción para Reportes...");
                // Aquí iría la lógica para abrir la ventana de reportes.
                // Ejemplo: irPantallaReportes();
                break;
                
            default:
                AlertaUtilidad.mostrarAlertaSimple("Error", 
                    "Opción no reconocida.", Alert.AlertType.ERROR);
                break;
        }
    }


    @FXML
    private void btnClicCancelar(ActionEvent event) {
        boolean confirmado = AlertaUtilidad.mostrarAlertaConfirmacion("Cancelar", null,
                "¿Estás seguro de que quieres cancelar?");
        if (confirmado) {
            cerrarVentana();
        }
    }
    
    private void cerrarVentana() {
        Utilidad.getEscenarioComponente(lbTitulo).close();
    }
    
    private void irSiguientePantallaDocumentoInicial() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLDocumentoInicial.fxml"));
            Parent vista = loader.load();

            FXMLDocumentoInicialController controller = loader.getController();

            Stage escenario = new Stage();
            escenario.setTitle("Registrar Nuevo Proyecto - Paso 2");
            escenario.setScene(new Scene(vista));
            cerrarVentana();
            escenario.show();

        } catch (IOException ex) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo abrir la siguiente ventana." + ex.getMessage(), Alert.AlertType.ERROR);
        }
    } 
}
