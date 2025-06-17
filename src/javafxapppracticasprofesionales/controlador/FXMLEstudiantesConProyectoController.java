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
import javafxapppracticasprofesionales.modelo.pojo.EstudianteConProyecto;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;

public class FXMLEstudiantesConProyectoController implements Initializable, INotificacion {

    @FXML
    private TableView<EstudianteConProyecto> tvEstudiantesConProyecto;
    @FXML
    private TableColumn colEstudiante;
    @FXML
    private TableColumn colMatricula;
    @FXML
    private TableColumn colProyecto;
    private ObservableList<EstudianteConProyecto> listaEstudiantesConProyecto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarEstudiantesConProyecto();
    }    

    @FXML
    private void btnClicAsignarProyecto(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLSeleccionarEstudiante.fxml"));
            Parent vista = loader.load();
            
            FXMLSeleccionarEstudianteController controller = loader.getController();
            controller.inicializarInformacion(this);
            
            Stage escenario = new Stage();
            escenario.setTitle("Asignar Proyecto a Estudiante - Paso 1");
            escenario.setScene(new Scene(vista));
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
            
            cargarEstudiantesConProyecto();
            
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo abrir la ventana de registro.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    private void configurarTabla() {
        colEstudiante.setCellValueFactory(new PropertyValueFactory<>("nombreEstudiante"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colProyecto.setCellValueFactory(new PropertyValueFactory<>("nombreProyecto"));
    }
    
    private void cargarEstudiantesConProyecto() {
        listaEstudiantesConProyecto = FXCollections.observableArrayList();
        try {
            listaEstudiantesConProyecto.addAll(EstudianteDAO.obtenerEstudiantesConProyecto());
            tvEstudiantesConProyecto.setItems(listaEstudiantesConProyecto);
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de conexión", "No se pudo cargar la lista de estudiantes con proyecto.", Alert.AlertType.ERROR);
        }
    }

    @Override
    public void operacionExitosa() {
        cargarEstudiantesConProyecto();
    }
}
