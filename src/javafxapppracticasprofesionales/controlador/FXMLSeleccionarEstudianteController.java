package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.EstudianteDAO;
import javafxapppracticasprofesionales.modelo.pojo.Estudiante;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

/** 
* Project: JavaFX Sales System 
* File: ClassName.java 
* Author: Jose Luis Silva Gomez 
* Date: YYYY-MM-DD 
* Description: Brief description of the file's purpose. 
*/
public class FXMLSeleccionarEstudianteController implements Initializable {

    @FXML
    private TableView<Estudiante> tvEstudiantes;
    @FXML
    private TableColumn<Estudiante, String> colNombre;
    @FXML
    private TableColumn<Estudiante, Integer> colSemestre;
    @FXML
    private TableColumn<Estudiante, String> colCorreo;
    @FXML
    private TableColumn<Estudiante, String> colMatricula;

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
            // Aquí puedes agregar la lógica para continuar con el estudiante seleccionado
        } else {
            AlertaUtilidad.mostrarAlertaSimple("Selección requerida", "Debes seleccionar un estudiante para continuar.", Alert.AlertType.WARNING);
        }
    }
    
    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colSemestre.setCellValueFactory(new PropertyValueFactory<>("semestre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("corre"));
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
