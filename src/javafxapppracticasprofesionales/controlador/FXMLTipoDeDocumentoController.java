package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.pojo.TipoDocumento;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLTipoDeDocumentoController implements Initializable {

    @FXML
    private ListView<TipoDocumento> lvTipoDocumento;
    private String tipoEntrega;
    private INotificacion observador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // La inicialización se maneja en el método inicializarInformacion
    }

    public void inicializarInformacion(String tipoEntrega, INotificacion observador) {
        this.tipoEntrega = tipoEntrega;
        this.observador = observador;
        cargarDocumentos();
    }

    @FXML
    private void btnClicContinuar(ActionEvent event) {
        TipoDocumento docSeleccionado = lvTipoDocumento.getSelectionModel().getSelectedItem(); // Ahora obtiene un String
        if (docSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLProgramarEntrega.fxml"));
                Parent vista = loader.load();
                FXMLProgramarEntregaController controller = loader.getController();
                controller.inicializarInformacion(tipoEntrega, docSeleccionado.getNombre(), observador);

                Stage escenario = new Stage();
                escenario.setTitle("Programar Entrega");
                escenario.setScene(new Scene(vista));
                escenario.initModality(Modality.APPLICATION_MODAL);
                cerrarVentana();
                escenario.showAndWait();
            } catch (IOException e) {
                AlertaUtilidad.mostrarAlertaSimple("Error", "No se puede mostrar la ventana.", Alert.AlertType.ERROR);
            }
        } else {
            AlertaUtilidad.mostrarAlertaSimple("Sin selección", "Debes seleccionar un tipo de documento para continuar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void BtnClicRegresar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLTipoDeEntrega.fxml"));
            Parent vista = loader.load();
            Stage escenario = new Stage();
            escenario.setTitle("Seleccionar Tipo de Entrega");
            escenario.setScene(new Scene(vista));
            escenario.show();
            cerrarVentana();
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se puede mostrar la ventana.", Alert.AlertType.ERROR);
        }
    }

    private void cargarDocumentos() {
        System.out.println("Cargando documentos para: " + tipoEntrega); // debug
        ArrayList<TipoDocumento> documentosPojo = new ArrayList<>();
        switch (tipoEntrega) {
            case "DOCUMENTOS INICIALES":
                documentosPojo.addAll(TipoDocumento.obtenerTiposDocumentoInicial());
                break;
            case "REPORTES":
                documentosPojo.addAll(TipoDocumento.obtenerTiposDocumentoReporte());
                break;
            case "DOCUMENTOS FINALES":
                documentosPojo.addAll(TipoDocumento.obtenerTiposDocumentoFinal());
                break;
        }
        
        lvTipoDocumento.setItems(FXCollections.observableArrayList(documentosPojo));
        lvTipoDocumento.requestFocus();

    }

    private void cerrarVentana() {
        Utilidad.getEscenarioComponente(lvTipoDocumento).close();
    }
}