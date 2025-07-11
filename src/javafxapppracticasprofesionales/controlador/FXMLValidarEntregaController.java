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
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.ValidacionDAO;
import javafxapppracticasprofesionales.modelo.pojo.DocumentoEntregado;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: FXMLValidarEntregaController.java 
    * Autor: Rodrigo Luna Vázquez
    * Fecha: 15/06/2025
*/
public class FXMLValidarEntregaController implements Initializable {

    @FXML
    private Label lblNombreEstudiante;
    @FXML
    private Label lblMatricula;
    @FXML
    private TextArea taComentarios;
    @FXML
    private Label lbContadorCaracteres;
    
    private DocumentoEntregado docAValidar;
    private INotificacion notificador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void inicializar(DocumentoEntregado doc, INotificacion notificacion) {
        this.docAValidar = doc;
        this.notificador = notificacion;
        
        lblNombreEstudiante.setText(doc.getNombreEstudiante());
        lblMatricula.setText(doc.getMatricula());
        Utilidad.configurarTextAreaConContador(taComentarios, lbContadorCaracteres, 150);
    }

    @FXML
    private void btnClicAbrirDocumento(ActionEvent event) {
        String ruta = docAValidar.getRutaArchivo();
        if (ruta != null && !ruta.isEmpty()) {
            Utilidad.abrirDocumento(ruta);
        } else {
            AlertaUtilidad.mostrarAlertaSimple("Sin archivo", "Este registro no tiene una ruta de archivo asociada.", javafx.scene.control.Alert.AlertType.WARNING);
        }
    }
    
    @FXML
    private void btnClicAprobar(ActionEvent event) {
        validarDocumento(3); 
    }
    
    @FXML
    private void btnClicRechazar(ActionEvent event) {
        if (taComentarios.getText().trim().isEmpty()) {
            AlertaUtilidad.mostrarAlertaSimple("Comentario Requerido", "Para rechazar una entrega es obligatorio agregar un comentario de retroalimentación.", javafx.scene.control.Alert.AlertType.WARNING);
            return;
        }
        validarDocumento(4); 
    }
    
    @FXML
    private void btnClicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void validarDocumento(int nuevoEstadoId) {
        String comentarios = taComentarios.getText();
        
        try {
            ResultadoOperacion resultado = ValidacionDAO.validarEntrega(docAValidar, nuevoEstadoId, comentarios);
            if (!resultado.isError()) {
                AlertaUtilidad.mostrarAlertaSimple("Éxito", resultado.getMensaje(), Alert.AlertType.INFORMATION);
                notificador.operacionExitosa();
                cerrarVentana();
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error", resultado.getMensaje(), Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void cerrarVentana() {
        Stage escenario = (Stage) lblMatricula.getScene().getWindow();
        escenario.close();
    }
}