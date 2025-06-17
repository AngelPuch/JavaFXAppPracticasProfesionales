package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxapppracticasprofesionales.modelo.dao.EvaluacionDAO;
import javafxapppracticasprofesionales.modelo.pojo.RespuestaGuardadaOV;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;

/**
 * Project: JavaFXAppPracticasProfesionales
 * File: FXMLVerEvaluacionOVController.java
 * Author: Jose Luis Silva Gomez
 * Date: 2025-06-16
 * Description: Brief description of the file's purpose.
 */
public class FXMLVerEvaluacionOVController implements Initializable {

    @FXML
    private TableView<RespuestaGuardadaOV> tvRespuestas;
    @FXML
    private TableColumn<RespuestaGuardadaOV, String> colAfirmacion;
    @FXML
    private TableColumn<RespuestaGuardadaOV, Integer> colRespuesta;
    
    private int idExpediente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }    
    
    public void inicializarDatos(int idExpediente) {
        this.idExpediente = idExpediente;
        cargarRespuestas();
    }
    
    private void configurarTabla() {
        colAfirmacion.setCellValueFactory(new PropertyValueFactory<>("afirmacion"));
        colRespuesta.setCellValueFactory(new PropertyValueFactory<>("respuesta"));
    }
    
    private void cargarRespuestas() {
        try {
            ArrayList<RespuestaGuardadaOV> respuestas = EvaluacionDAO.obtenerDetalleEvaluacionGuardada(this.idExpediente);
            tvRespuestas.setItems(FXCollections.observableArrayList(respuestas));
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "No se pudo cargar el detalle de tu evaluación.", Alert.AlertType.ERROR);
        }
    }
}
