package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.modelo.dao.EntregaDAO;
import javafxapppracticasprofesionales.modelo.dao.EstudianteDAO;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;
import javafxapppracticasprofesionales.modelo.pojo.InfoEstudianteSesion;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;

/** 
* Project: JavaFXAppPracticasProfesionales 
* File: FXMLEntregasPendientesController.java 
* Author: Jose Luis Silva Gomez 
* Date: 2025-06-16 
* Description: Brief description of the file's purpose. 
*/
public class FXMLEntregasPendientesController implements Initializable {

    @FXML
    private TableView<Entrega> tvEntregasIniciales;
    @FXML
    private TableColumn<Entrega, String> colNombreInicial;
    @FXML
    private TableColumn<Entrega, String> colFechaInicioInicial;
    @FXML
    private TableColumn<Entrega, String> colFechaFinInicial;
    @FXML
    private TableView<Entrega> tvEntregasReportes;
    @FXML
    private TableColumn<Entrega, String> colNombreReporte;
    @FXML
    private TableColumn<Entrega, String> colFechaInicioReporte;
    @FXML
    private TableColumn<Entrega, String> colFechaFinReporte;
    @FXML
    private TableView<Entrega> tvEntregasFinales;
    @FXML
    private TableColumn<Entrega, String> colNombreFinal;
    @FXML
    private TableColumn<Entrega, String> colFechaInicioFinal;
    @FXML
    private TableColumn<Entrega, String> colFechaFinFinal;
    private InfoEstudianteSesion infoSesion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablas();
        // Obtener la información del estudiante logueado
        Usuario usuarioLogueado = SesionUsuario.getInstancia().getUsuarioLogueado();
        try {
            infoSesion = EstudianteDAO.obtenerInfoEstudianteParaSesion(usuarioLogueado.getIdUsuario());
            if (infoSesion != null) {
                cargarTodasLasEntregas();
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error de Información", "No se pudo recuperar la información del estudiante.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", e.getMessage(), Alert.AlertType.ERROR);
        }
    }    

    private void configurarTablas() {
        // Tabla Documentos Iniciales
        colNombreInicial.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaInicioInicial.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFinInicial.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));

        // Tabla Reportes
        colNombreReporte.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaInicioReporte.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFinReporte.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));

        // Tabla Documentos Finales
        colNombreFinal.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaInicioFinal.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFinFinal.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
    }

    private void cargarTodasLasEntregas() {
        try {
            // Cargar entregas de documentos iniciales
            ArrayList<Entrega> entregasIniciales = EntregaDAO.obtenerEntregasPorGrupo(infoSesion.getIdGrupo(), "entregadocumentoinicio");
            tvEntregasIniciales.setItems(FXCollections.observableArrayList(entregasIniciales));

            // Cargar entregas de reportes
            ArrayList<Entrega> entregasReportes = EntregaDAO.obtenerEntregasPorGrupo(infoSesion.getIdGrupo(), "entregareporte");
            tvEntregasReportes.setItems(FXCollections.observableArrayList(entregasReportes));

            // Cargar entregas de documentos finales
            ArrayList<Entrega> entregasFinales = EntregaDAO.obtenerEntregasPorGrupo(infoSesion.getIdGrupo(), "entregadocumentofinal");
            tvEntregasFinales.setItems(FXCollections.observableArrayList(entregasFinales));

        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "No se pudieron cargar las listas de entregas.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnClicEntregarDocIniciales(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLSeleccionarEntrega.fxml"));
            Parent vista = loader.load();
            
            Stage escenario = new Stage();
            escenario.setTitle("Subir documento inicial - Paso 1");
            escenario.setScene(new Scene(vista));
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
                
            cerrarVentana();
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo abrir la ventana de selección de entrega.", Alert.AlertType.ERROR);
        }
    }

    private void cerrarVentana() {
        Utilidad.getEscenarioComponente(tvEntregasFinales).close();
    }
    
}
