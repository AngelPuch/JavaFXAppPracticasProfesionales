package javafxapppracticasprofesionales.controlador;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.ValidacionDAO;
import javafxapppracticasprofesionales.modelo.pojo.DocumentoEntregado;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;

public class FXMLValidarEntregaController implements Initializable {

    @FXML
    private Label lblNombreEstudiante;
    @FXML
    private Label lblMatricula;
    @FXML
    private TextArea taComentarios;
    @FXML
    private Button btnRechazar;
    @FXML
    private Button btnAprobar;

    private DocumentoEntregado docAValidar;
    private INotificacion notificador;
    private String[] tablaInfo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void inicializar(DocumentoEntregado doc, INotificacion notificacion, String[] info) {
        this.docAValidar = doc;
        this.notificador = notificacion;
        this.tablaInfo = info;
        
        lblNombreEstudiante.setText(doc.getNombreEstudiante());
        lblMatricula.setText(doc.getMatricula());
    }

    @FXML
    private void btnClicAbrirDocumento(ActionEvent event) {
        String ruta = docAValidar.getRutaArchivo();
        if (ruta != null && !ruta.isEmpty()) {
            try {
                File archivo = new File(ruta);
                if (archivo.exists()) {
                    Desktop.getDesktop().open(archivo);
                    AlertaUtilidad.mostrarAlertaSimple("Información", "Documento abierto en la aplicación predeterminada.", javafx.scene.control.Alert.AlertType.INFORMATION);
                } else {
                    AlertaUtilidad.mostrarAlertaSimple("Error", "El archivo no se encontró en la ruta: " + ruta, javafx.scene.control.Alert.AlertType.ERROR);
                }
            } catch (IOException e) {
                AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo abrir el archivo. " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            AlertaUtilidad.mostrarAlertaSimple("Sin archivo", "Este registro no tiene una ruta de archivo asociada.", javafx.scene.control.Alert.AlertType.WARNING);
        }
    }
    
    @FXML
    private void btnClicAprobar(ActionEvent event) {
        validarDocumento(3); // 3 es el ID para "Aprobado" en la tabla estadodocumento
    }
    
    @FXML
    private void btnClicRechazar(ActionEvent event) {
        if (taComentarios.getText().trim().isEmpty()) {
            AlertaUtilidad.mostrarAlertaSimple("Comentario Requerido", "Para rechazar una entrega es obligatorio agregar un comentario de retroalimentación.", javafx.scene.control.Alert.AlertType.WARNING);
            return;
        }
        validarDocumento(4); // 4 es el ID para "Rechazado con Comentarios"
    }
    
    private void validarDocumento(int nuevoEstadoId) {
        String comentarios = taComentarios.getText();
        String nombreTabla = tablaInfo[0];
        String nombreColumnaId = tablaInfo[1];
        
        try {
            ResultadoOperacion resultado = ValidacionDAO.validarEntrega(docAValidar.getIdDocumento(), nuevoEstadoId, comentarios, nombreTabla, nombreColumnaId);
            if (!resultado.isError()) {
                AlertaUtilidad.mostrarAlertaSimple("Éxito", resultado.getMensaje(), javafx.scene.control.Alert.AlertType.INFORMATION);
                notificador.operacionExitosa();
                cerrarVentana();
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error", resultado.getMensaje(), Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Stage escenario = (Stage) lblMatricula.getScene().getWindow();
        escenario.close();
    }
}