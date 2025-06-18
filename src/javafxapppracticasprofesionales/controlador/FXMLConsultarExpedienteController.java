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
import javafxapppracticasprofesionales.modelo.pojo.ResponsableProyecto;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLConsultarExpedienteController implements Initializable {

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
    private void btnClicContinuar(ActionEvent event) {
        EstudianteConProyecto estudianteSeleccionado = tvEstudiantesConProyecto.getSelectionModel().getSelectedItem();
        if(estudianteSeleccionado != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLExpedienteEstudiante.fxml"));
                Parent vista = loader.load();

                FXMLExpedienteEstudianteController controller = loader.getController();
                controller.inicializarInformacion(estudianteSeleccionado);

                Stage escenario = new Stage();
                escenario.setTitle("Expediente Estudiante");
                escenario.setScene(new Scene(vista));
                escenario.initModality(Modality.APPLICATION_MODAL);
                
                escenario.showAndWait();
                
            } catch (IOException e) {
                AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo abrir la siguiente ventana.", Alert.AlertType.ERROR);
            }
        } else {
            AlertaUtilidad.mostrarAlertaSimple("Selección requerida", "Debes seleccionar un responsable para continuar.", Alert.AlertType.WARNING);
        }
    }
    
     private void configurarTabla() {
        colEstudiante.setCellValueFactory(new PropertyValueFactory<>("nombreEstudiante"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colProyecto.setCellValueFactory(new PropertyValueFactory<>("nombreProyecto"));
    }
     
     private void cargarEstudiantesConProyecto() {
        Usuario usuarioLogueado = SesionUsuario.getInstancia().getUsuarioLogueado();
        if (usuarioLogueado == null || usuarioLogueado.getIdAcademico() == null) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Sesión", "No se pudo verificar la información del profesor.", Alert.AlertType.ERROR);
            return;
        }
        int idAcademico = usuarioLogueado.getIdAcademico();

        listaEstudiantesConProyecto = FXCollections.observableArrayList();
        try {
            listaEstudiantesConProyecto.addAll(EstudianteDAO.obtenerEstudiantesPorProfesor(idAcademico));
            tvEstudiantesConProyecto.setItems(listaEstudiantesConProyecto);
        } catch (SQLException e) {
            e.printStackTrace();
            AlertaUtilidad.mostrarAlertaSimple("Error de conexión", "No se pudo cargar la lista de estudiantes.", Alert.AlertType.ERROR);
        }
    }

    private void cerrarVentana(){
        Utilidad.getEscenarioComponente(tvEstudiantesConProyecto).close();
    }
    
}
