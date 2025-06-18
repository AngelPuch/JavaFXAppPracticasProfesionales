package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.ProyectoDAO;
import javafxapppracticasprofesionales.modelo.pojo.Estudiante;
import javafxapppracticasprofesionales.modelo.pojo.Proyecto;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLSeleccionarProyectoController implements Initializable {

    @FXML
    private TableView<Proyecto> tvProyectos;
    @FXML
    private TableColumn<Proyecto, String> colNombre;
    @FXML
    private TableColumn<Proyecto, String> colOrganizacion;
    @FXML
    private TableColumn<Proyecto, Integer> colCupos;
    @FXML
    private TableColumn<Proyecto, String> colObjetivo;

    private ObservableList<Proyecto> listaProyectos;
    private Estudiante estudianteSeleccionado;
    private INotificacion observador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarProyectosDisponibles();
    }    

    public void inicializarInformacion(Estudiante estudiante, INotificacion observador) {
        this.estudianteSeleccionado = estudiante;
        this.observador = observador;
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void btnClicContinuar(ActionEvent event) {
        Proyecto proyectoSeleccionado = tvProyectos.getSelectionModel().getSelectedItem();
        if (proyectoSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLConfirmarAsignacionProyecto.fxml"));
                Parent vista = loader.load();
            
                FXMLConfirmarAsignacionProyectoController controller = loader.getController();
                controller.inicializarInformacion(proyectoSeleccionado, estudianteSeleccionado, observador);
                
                Stage escenario = new Stage();
                escenario.setTitle("Asignar Proyecto a Estudiante - Paso 3");
                escenario.setScene(new Scene(vista));
                escenario.initModality(Modality.APPLICATION_MODAL);
                cerrarVentana();
                escenario.show();
            } catch (IOException e) {
                AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo abrir la ventana de confirmación de asignación.", Alert.AlertType.ERROR);
            }
        } else {
            AlertaUtilidad.mostrarAlertaSimple("Selección requerida", "Debes seleccionar un proyecto para continuar.", Alert.AlertType.WARNING);
        }
    }
    
    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colOrganizacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrganizacion().getNombre()));
        colCupos.setCellValueFactory(new PropertyValueFactory<>("numeroCupos"));
        colObjetivo.setCellValueFactory(new PropertyValueFactory<>("objetivo"));
    }

    private void cargarProyectosDisponibles() {
        try {
            listaProyectos = FXCollections.observableArrayList(ProyectoDAO.obtenerProyectosDisponiblesParaAsignar());
            tvProyectos.setItems(listaProyectos);
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "No se pudo cargar la lista de proyectos disponibles.", Alert.AlertType.ERROR);
        }
    }
    
    private void cerrarVentana(){
        Utilidad.getEscenarioComponente(tvProyectos).close();
    }
}
