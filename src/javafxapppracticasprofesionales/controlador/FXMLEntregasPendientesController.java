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
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.EntregaDAO;
import javafxapppracticasprofesionales.modelo.dao.EstudianteDAO;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;
import javafxapppracticasprofesionales.modelo.pojo.InfoEstudianteSesion;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLEntregasPendientesController implements Initializable, INotificacion {

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
    @FXML
    private TableColumn<Entrega, String> colEstadoInicial;
    @FXML
    private TableColumn<Entrega, String> colEstadoReporte;
    @FXML
    private TableColumn<Entrega, String> colEstadoFinal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablas();
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
        colNombreInicial.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaInicioInicial.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFinInicial.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colEstadoInicial.setCellValueFactory(new PropertyValueFactory<>("estado")); 

        colNombreReporte.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaInicioReporte.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFinReporte.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colEstadoReporte.setCellValueFactory(new PropertyValueFactory<>("estado")); 

        colNombreFinal.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaInicioFinal.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFinFinal.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colEstadoFinal.setCellValueFactory(new PropertyValueFactory<>("estado")); 
    }

    private void cargarTodasLasEntregas() {
        try {
            int idExp = infoSesion.getIdExpediente();
            int idGrp = infoSesion.getIdGrupo();

            ArrayList<Entrega> entregasIniciales = EntregaDAO.obtenerEntregasPendientesEstudiante(idGrp, idExp, "entregadocumentoinicio");
            tvEntregasIniciales.setItems(FXCollections.observableArrayList(entregasIniciales));

            ArrayList<Entrega> entregasReportes = EntregaDAO.obtenerEntregasPendientesEstudiante(idGrp, idExp, "entregareporte");
            tvEntregasReportes.setItems(FXCollections.observableArrayList(entregasReportes));

            ArrayList<Entrega> entregasFinales = EntregaDAO.obtenerEntregasPendientesEstudiante(idGrp, idExp, "entregadocumentofinal");
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
            
            FXMLSeleccionarEntregaController controller = loader.getController();
            controller.inicializarDatos(this);
            
            Stage escenario = new Stage();
            escenario.setTitle("Subir documento inicial - Paso 1");
            escenario.setScene(new Scene(vista));
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo abrir la ventana de selección de entrega.", Alert.AlertType.ERROR);
        }
    }

    @Override
    public void operacionExitosa() {
        cargarTodasLasEntregas();
    }
    
}
