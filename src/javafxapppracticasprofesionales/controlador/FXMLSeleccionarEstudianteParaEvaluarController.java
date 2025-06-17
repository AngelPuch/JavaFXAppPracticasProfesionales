
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.modelo.dao.EstudianteDAO;
import javafxapppracticasprofesionales.modelo.pojo.Estudiante;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLSeleccionarEstudianteParaEvaluarController implements Initializable {

    @FXML
    private TableView<Estudiante> tvEstudiantes;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colMatricula;
    @FXML
    private TableColumn colSemestre;

    private ObservableList<Estudiante> listaEstudiantes;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarEstudiantes();
    }    

    @FXML
    private void btnClicContinuar(ActionEvent event) {
        Estudiante estudianteSeleccionado = tvEstudiantes.getSelectionModel().getSelectedItem();
        if (estudianteSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLCalificarPresentacion.fxml"));
                Parent vista = loader.load();

                FXMLCalificarPresentacionController controller = loader.getController();
                controller.inicializarInformacion(estudianteSeleccionado);

                Stage escenario = new Stage();
                escenario.setTitle("Calificar Presentación");
                escenario.setScene(new Scene(vista));
                Utilidad.getEscenarioComponente(tvEstudiantes).close();
                escenario.show();
            } catch (IOException e) {
                AlertaUtilidad.mostrarAlertaSimple("Error de UI", "No se pudo cargar la ventana de calificación.", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        } else {
            AlertaUtilidad.mostrarAlertaSimple("Selección requerida", "Debe seleccionar un estudiante para continuar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        // Lógica para regresar al menú principal del evaluador
    }
    
    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colSemestre.setCellValueFactory(new PropertyValueFactory<>("semestre"));
    }

    private void cargarEstudiantes() {
        try {
            listaEstudiantes = FXCollections.observableArrayList(EstudianteDAO.obtenerEstudiantesParaEvaluar());
            tvEstudiantes.setItems(listaEstudiantes);
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
