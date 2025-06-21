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
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.EntregaDAO;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;

public class FXMLEntregasController implements Initializable, INotificacion {

    @FXML private TableView<Entrega> tvEntregasIniciales;
    @FXML private TableColumn colNombreInicial, colFechaInicioInicial, colFechaFinInicial, colHoraInicioInicial, colHoraFinInicial, colGrupoInicial;
    
    @FXML private TableView<Entrega> tvEntregasReportes;
    @FXML private TableColumn colNombreReporte, colFechaInicioReporte, colFechaFinReporte, colHoraInicioReporte, colHoraFinReporte, colGrupoReporte;

    @FXML private TableView<Entrega> tvEntregasFinales;
    @FXML private TableColumn colNombreFinal, colFechaInicioFinal, colFechaFinFinal, colHoraInicioFinal, colHoraFinFinal, colGrupoFinal;

    @Override
    public void initialize(java.net.URL url, java.util.ResourceBundle rb) {
        configurarTablas();
        cargarTodasLasEntregas();
    }

    private void configurarTablas() {
        // Tabla de Documentos Iniciales
        colNombreInicial.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaInicioInicial.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFinInicial.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colHoraInicioInicial.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        colHoraFinInicial.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
        colGrupoInicial.setCellValueFactory(new PropertyValueFactory<>("nombreGrupo"));
        
        // Tabla de Reportes
        colNombreReporte.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaInicioReporte.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFinReporte.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colHoraInicioReporte.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        colHoraFinReporte.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
        colGrupoReporte.setCellValueFactory(new PropertyValueFactory<>("nombreGrupo"));

        // Tabla de Documentos Finales
        colNombreFinal.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaInicioFinal.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFinFinal.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colHoraInicioFinal.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        colHoraFinFinal.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
        colGrupoFinal.setCellValueFactory(new PropertyValueFactory<>("nombreGrupo"));
    }
    
    private void cargarTodasLasEntregas() {
        try {
            tvEntregasIniciales.setItems(FXCollections.observableArrayList(EntregaDAO.obtenerTodasLasEntregas("entregadocumentoinicio")));
            tvEntregasReportes.setItems(FXCollections.observableArrayList(EntregaDAO.obtenerTodasLasEntregas("entregareporte")));
            tvEntregasFinales.setItems(FXCollections.observableArrayList(EntregaDAO.obtenerTodasLasEntregas("entregadocumentofinal")));
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "No fue posible cargar las entregas. " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnClicProgramarEntrega(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLTipoDeEntrega.fxml"));
            Parent vista = loader.load();
            
            // Suponiendo que FXMLTipoDeEntregaController necesita una referencia para notificar cambios
            INotificacion notificacion = this;
            FXMLTipoDeEntregaController controlador = loader.getController();
            // controlador.inicializarInformacion(notificacion); // Si el método existe
            
            Stage escenario = new Stage();
            escenario.setTitle("Programar Nueva Entrega");
            escenario.setScene(new Scene(vista));
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();

            // Recargar las tablas después de cerrar la ventana modal
            cargarTodasLasEntregas();

        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se puede mostrar la ventana.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @Override
    public void operacionExitosa() {
        cargarTodasLasEntregas();
    }
}