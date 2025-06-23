package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.ValidacionDAO;
import javafxapppracticasprofesionales.modelo.pojo.DocumentoEntregado;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;

public class FXMLSeleccionarEntregaParaValidarController implements Initializable, INotificacion {

    @FXML
    private Label lblTitulo;
    @FXML
    private TableView<DocumentoEntregado> tvEntregas;
    @FXML
    private TableColumn<DocumentoEntregado, String> colNombreEstudiante;
    @FXML
    private TableColumn<DocumentoEntregado, String> colMatricula;
    @FXML
    private TableColumn<DocumentoEntregado, String> colFechaEntrega;

    private Entrega entregaSeleccionada;
    private String tipoDocumentoNombre; 
    private ObservableList<DocumentoEntregado> listaDocumentos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }    
    
    public void inicializar(Entrega entrega, String tipoDocNombre) {
        this.entregaSeleccionada = entrega;
        this.tipoDocumentoNombre = tipoDocNombre; 
        lblTitulo.setText(entrega.getNombre());
        cargarEntregas();
    }

    private void configurarTabla() {
        colNombreEstudiante.setCellValueFactory(new PropertyValueFactory<>("nombreEstudiante"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colFechaEntrega.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFechaEntregadoFormateada()));
    }
    
    private void cargarEntregas() {
        try {
            ArrayList<DocumentoEntregado> documentosBD = ValidacionDAO.obtenerEntregasParaValidar(entregaSeleccionada.getIdEntrega(), tipoDocumentoNombre);
            listaDocumentos = FXCollections.observableArrayList(documentosBD);
            tvEntregas.setItems(listaDocumentos);
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnClicContinuar(ActionEvent event) {
        DocumentoEntregado docSeleccionado = tvEntregas.getSelectionModel().getSelectedItem();
        if (docSeleccionado != null) {
            abrirVentanaValidacion(docSeleccionado);
        } else {
            AlertaUtilidad.mostrarAlertaSimple("Sin selección", "Debes seleccionar una entrega para poder validarla.", javafx.scene.control.Alert.AlertType.WARNING);
        }
    }

    private void abrirVentanaValidacion(DocumentoEntregado doc) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLValidarEntrega.fxml"));
            Parent vista = loader.load();
            FXMLValidarEntregaController controlador = loader.getController();
            controlador.inicializar(doc, this);
            
            Stage escenario = new Stage();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Validar Entrega");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error",e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage escenario = (Stage) lblTitulo.getScene().getWindow();
        escenario.close();
    }

    @Override
    public void operacionExitosa() {
        cargarEntregas();
    }
}