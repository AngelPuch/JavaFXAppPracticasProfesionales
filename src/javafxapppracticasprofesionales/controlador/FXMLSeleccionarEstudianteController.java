package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.EstudianteDAO;
import javafxapppracticasprofesionales.modelo.pojo.Estudiante;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLSeleccionarEstudianteController implements Initializable {

    @FXML
    private TableView<Estudiante> tvEstudiantes;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colSemestre;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private TableColumn colMatricula;

    private ObservableList<Estudiante> listaEstudiantes;
    private INotificacion observador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarEstudiantesSinProyecto();
    }    
    
    public void inicializarInformacion(INotificacion observador) {
        this.observador = observador;
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void btnClicContinuar(ActionEvent event) {
        Estudiante estudianteSeleccionado = tvEstudiantes.getSelectionModel().getSelectedItem();
        if (estudianteSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLSeleccionarProyecto.fxml"));
                Parent vista = loader.load();
                
                FXMLSeleccionarProyectoController controller = loader.getController();
                controller.inicializarInformacion(estudianteSeleccionado, observador);
            
                Stage escenario = new Stage();
                escenario.setTitle("Asignar Proyecto a Estudiante - Paso 2");
                escenario.setScene(new Scene(vista));
                escenario.initModality(Modality.APPLICATION_MODAL);
                escenario.showAndWait();
                
                cerrarVentana();
            } catch (IOException e) {
                AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo abrir la ventana de selección de registro.", Alert.AlertType.ERROR);
            }
        } else {
            AlertaUtilidad.mostrarAlertaSimple("Selección requerida", "Debes seleccionar un estudiante para continuar.", Alert.AlertType.WARNING);
        }
    }
    
    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colSemestre.setCellValueFactory(new PropertyValueFactory<>("semestre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
    }
    
    private void cargarEstudiantesSinProyecto() {
        listaEstudiantes = FXCollections.observableArrayList();
        try {
            listaEstudiantes.addAll(EstudianteDAO.obtenerEstudiantesSinProyecto());
            tvEstudiantes.setItems(listaEstudiantes);
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de conexión", "No se pudo cargar la lista de estudiantes.", Alert.AlertType.ERROR);
        }
    }
    
    private void cerrarVentana() {
        Utilidad.getEscenarioComponente(tvEstudiantes).close();
    }
}
