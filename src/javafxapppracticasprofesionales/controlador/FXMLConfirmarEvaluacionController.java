package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafxapppracticasprofesionales.modelo.dao.EvaluacionDAO;
import javafxapppracticasprofesionales.modelo.pojo.Estudiante;
import javafxapppracticasprofesionales.modelo.pojo.Evaluacion;
import javafxapppracticasprofesionales.modelo.pojo.Proyecto;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: FXMLConfirmarEvaluacionController.java 
    * Autor: Angel Jonathan Puch Hernández 
    * Fecha: 15/06/2025
*/
public class FXMLConfirmarEvaluacionController implements Initializable {

    @FXML
    private Label lblNombreEstudiante;
    @FXML
    private Label lblMatricula;
    @FXML
    private Label lblProyecto;
    @FXML
    private TextArea txtComentarios;
    @FXML
    private Label lblCalificacionFinal;

    private Evaluacion evaluacion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void inicializarDatos(Estudiante estudiante, Proyecto proyecto, Evaluacion evaluacion) {
        this.evaluacion = evaluacion;
        lblNombreEstudiante.setText(estudiante.getNombre());
        lblMatricula.setText(estudiante.getMatricula());
        lblProyecto.setText(proyecto.getNombre());
        txtComentarios.setText(evaluacion.getComentarios());
        lblCalificacionFinal.setText(String.valueOf(evaluacion.getCalificacionTotal()));
        
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        boolean confirmacion = AlertaUtilidad.mostrarAlertaConfirmacion("Cancelar", null ,"¿Estás seguro de que quieres cancelar? No se guardará la evaluación.");
        if (confirmacion) {
            Utilidad.getEscenarioComponente(lblNombreEstudiante).close();
        }
    }

    @FXML
    private void btnClicConfirmar(ActionEvent event) {
        try {
            ResultadoOperacion resultado = EvaluacionDAO.registrarEvaluacion(this.evaluacion);
            if (!resultado.isError()) {
                AlertaUtilidad.mostrarAlertaSimple("Operación Exitosa", "La evaluación ha sido guardada correctamente.", Alert.AlertType.INFORMATION);
                Utilidad.getEscenarioComponente(lblNombreEstudiante).close();
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error al Guardar", resultado.getMensaje(), Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
             AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", e.getMessage(), Alert.AlertType.ERROR);
             e.printStackTrace(); 
        }
    }
}