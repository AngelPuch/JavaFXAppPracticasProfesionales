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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.modelo.pojo.TipoDocumento;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: FXMLValidarSeleccionarTipoEntregaController.java 
    * Autor: Rodrigo Luna Vázquez
    * Fecha: 15/06/2025
*/
public class FXMLValidarSeleccionarTipoEntregaController implements Initializable {

    @FXML
    private RadioButton rbDocumentosIniciales;
    @FXML
    private RadioButton rbReportes;
    @FXML
    private RadioButton rbDocumentosFinales;
    @FXML
    private ToggleGroup tgTipoEntrega;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}    

    @FXML
    private void btnClicContinuar(ActionEvent event) {
        TipoDocumento tipoSeleccionado = null;
        if (rbDocumentosIniciales.isSelected()) {
            tipoSeleccionado = new TipoDocumento(1, "Documentos Iniciales");
        } else if (rbReportes.isSelected()) {
            tipoSeleccionado = new TipoDocumento(2, "Reportes");
        } else if (rbDocumentosFinales.isSelected()) {
            tipoSeleccionado = new TipoDocumento(3, "Documentos Finales");
        }
        
        if (tipoSeleccionado != null) {
            abrirSiguienteVentana(tipoSeleccionado);
        } else {
            AlertaUtilidad.mostrarAlertaSimple("Sin selección", "Debes seleccionar un tipo de entrega.", Alert.AlertType.WARNING);
        }
    }
    
    private void abrirSiguienteVentana(TipoDocumento tipoDoc) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLValidarSeleccionarDocumento.fxml"));
            Parent vista = loader.load();
            FXMLValidarSeleccionarDocumentoController controlador = loader.getController();
            controlador.inicializar(tipoDoc);
            
            Stage escenario = (Stage) rbReportes.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Validar Entregas - Paso 2");
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error","No se pudo cargar la siguiente ventana: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}