package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: FXMLTipoDeEntregaController.java 
    * Autor: Angel Jonathan Puch Hernández
    * Fecha: 13/06/2025
*/
public class FXMLTipoDeEntregaController implements Initializable {

    @FXML
    private ListView<String> lvTipoEntrega;
    private INotificacion observador;
    private ObservableList<String> listaTipos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaTipos = FXCollections.observableArrayList();
        cargarTiposDeEntrega();
    }    
    
    public void inicializarInformacion(INotificacion observador) {
        this.observador = observador;
    }
    
    private void cargarTiposDeEntrega(){
        listaTipos.add("DOCUMENTOS INICIALES");
        listaTipos.add("REPORTES");
        listaTipos.add("DOCUMENTOS FINALES");
        lvTipoEntrega.setItems(listaTipos);
    }

    @FXML
    private void btnClicContinuar(ActionEvent event) {
        String tipoSeleccionado = lvTipoEntrega.getSelectionModel().getSelectedItem();
        if(tipoSeleccionado != null){
             try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLTipoDeDocumento.fxml"));
                Parent vista = loader.load();
                FXMLTipoDeDocumentoController controller = loader.getController();
                controller.inicializarInformacion(tipoSeleccionado, observador);
                
                Stage escenario = new Stage();
                escenario.setTitle("Seleccionar Tipo de Documento");
                escenario.setScene(new Scene(vista));
                escenario.show();
                cerrarVentana();
            } catch (IOException e) {
                 AlertaUtilidad.mostrarAlertaSimple("Error", "No se puede mostrar la ventana." + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            AlertaUtilidad.mostrarAlertaSimple("Sin selección", "Debes seleccionar un tipo de entrega para continuar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana(){
        Utilidad.getEscenarioComponente(lvTipoEntrega).close();
    }
    
}