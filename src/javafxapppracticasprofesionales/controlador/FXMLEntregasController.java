package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.modelo.dao.EntregaDAO;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;

public class FXMLEntregasController implements Initializable {

    @FXML private TableView<Entrega> tvEntregasIniciales;
    @FXML private TableColumn colNombreInicial, colFechaInicioInicial, colFechaFinInicial;
    
    @FXML private TableView<Entrega> tvEntregasReportes;
    @FXML private TableColumn colNombreReporte, colFechaInicioReporte, colFechaFinReporte;

    @FXML private TableView<Entrega> tvEntregasFinales;
    @FXML private TableColumn colNombreFinal, colFechaInicioFinal, colFechaFinFinal;

    @Override
    public void initialize(java.net.URL url, java.util.ResourceBundle rb) {
        configurarTablas();
        cargarTodasLasEntregas();
    }

    private void configurarTablas() {
        colNombreInicial.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaInicioInicial.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFinInicial.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        
        colNombreReporte.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaInicioReporte.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFinReporte.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));

        colNombreFinal.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaInicioFinal.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFinFinal.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
    }
    
    private void cargarTodasLasEntregas() {
        try {
            tvEntregasIniciales.setItems(FXCollections.observableArrayList(EntregaDAO.obtenerTodasLasEntregas("entregadocumentoinicio")));
            tvEntregasReportes.setItems(FXCollections.observableArrayList(EntregaDAO.obtenerTodasLasEntregas("entregareporte")));
            tvEntregasFinales.setItems(FXCollections.observableArrayList(EntregaDAO.obtenerTodasLasEntregas("entregadocumentofinal")));
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "No fue posible cargar las entregas." + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnClicProgramarEntrega(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLTipoDeEntrega.fxml"));
            Parent vista = loader.load();
            
            Stage escenario = new Stage();
            escenario.setTitle("Programar Nueva Entrega");
            escenario.setScene(new Scene(vista));
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
            
            cargarTodasLasEntregas();

        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se puede mostrar la ventana.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}