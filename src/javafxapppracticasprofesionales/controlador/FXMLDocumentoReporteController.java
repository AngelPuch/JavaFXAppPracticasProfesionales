/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.modelo.pojo.TipoDocumento;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

/**
 * FXML Controller class
 *
 * @author grill
 */
public class FXMLDocumentoReporteController implements Initializable {

    @FXML
    private ListView<TipoDocumento> lvTipoDocumento;
    private String tipoEntrega;
    private Usuario usuarioSesion;



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
   public void inicializarInformacion(String tipoEntrega, Usuario usuarioSesion) {
        this.tipoEntrega = tipoEntrega;
        this.usuarioSesion = usuarioSesion;
        cargarDocumentos();
}
    
    private void cargarDocumentos() {
        ArrayList<TipoDocumento> documentosPojo = new ArrayList<>();
        
        if ("REPORTES".equals(tipoEntrega)) {
            documentosPojo.addAll(TipoDocumento.obtenerTiposDocumentoReporte());
        }
        
        lvTipoDocumento.setItems(FXCollections.observableArrayList(documentosPojo));
        lvTipoDocumento.requestFocus();

    }

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLSeleccionarTipoEntrega.fxml"));
            Parent vista = loader.load();
            
            FXMLSeleccionarTipoEntregaController controller = loader.getController();
            controller.inicializarInformacion(usuarioSesion);

            Stage escenario = new Stage();
            escenario.setTitle("Seleccionar tipo de entrega");
            escenario.setScene(new Scene(vista));
            cerrarVentana();
            escenario.show();

        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo volver a la ventana anterior.", Alert.AlertType.ERROR);
        }
    }
    private void cerrarVentana() {
        Utilidad.getEscenarioComponente(lvTipoDocumento).close();
    }
    
}
