package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.EntregaDAO;
import javafxapppracticasprofesionales.modelo.dao.PeriodoDAO;
import javafxapppracticasprofesionales.modelo.dao.ProgramarEntregaDAO;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;
import javafxapppracticasprofesionales.modelo.pojo.Grupo;
import javafxapppracticasprofesionales.modelo.pojo.Periodo;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLProgramarEntregaController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextArea taDescripcion;
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaFin;
    private String tipoEntrega;
    private INotificacion observador;
    private String nombreDocumentoPrefijo;
    @FXML
    private Label lbContadorCaracteres;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarInformacion(String tipoEntrega, String nombreDocumento, INotificacion observador) {
        this.tipoEntrega = tipoEntrega;
        this.observador = observador;

        this.nombreDocumentoPrefijo = nombreDocumento + "_";
        this.tfNombre.setText(nombreDocumentoPrefijo);

        tfNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || !newValue.startsWith(nombreDocumentoPrefijo)) {
                javafx.application.Platform.runLater(() -> {
                    tfNombre.setText(oldValue);
                });
            }
        });
        tfNombre.caretPositionProperty().addListener((observable, oldPosition, newPosition) -> {
            if (newPosition.intValue() < nombreDocumentoPrefijo.length()) {
                javafx.application.Platform.runLater(() -> {
                    tfNombre.positionCaret(tfNombre.getText().length());
                });
            }
        });
        final int MAX_CHARS = 100;

        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() > MAX_CHARS) {
                return null; 
            } else {
                return change; 
            }
        });
        taDescripcion.setTextFormatter(textFormatter);

        if (lbContadorCaracteres != null) {
            lbContadorCaracteres.setText("0/" + MAX_CHARS); 
            taDescripcion.textProperty().addListener((observable, oldValue, newValue) -> {
                lbContadorCaracteres.setText(newValue.length() + "/" + MAX_CHARS);
            });
        }
    }
    
    private boolean validarCampos(){
        if(tfNombre.getText().isEmpty() || dpFechaInicio.getValue() == null || dpFechaFin.getValue() == null){
            AlertaUtilidad.mostrarAlertaSimple("Campos vacíos", "Los campos marcados con un (*) no deben de ser vacíos. Por favor, complétalos para continuar.", Alert.AlertType.WARNING);
            return false;
        }
        LocalDate fechaActual = LocalDate.now();
        if (dpFechaInicio.getValue().isBefore(fechaActual)) {
            AlertaUtilidad.mostrarAlertaSimple("Fecha incorrecta", "La fecha de inicio no puede ser anterior a la fecha actual.", Alert.AlertType.WARNING);
            return false;
        }
        if(dpFechaFin.getValue().isBefore(dpFechaInicio.getValue())){
            AlertaUtilidad.mostrarAlertaSimple("Fechas incorrectas", "La fecha de fin no puede ser anterior a la fecha de inicio.", Alert.AlertType.WARNING);
            return false;
        }
        
        try {
            Periodo periodoActual = PeriodoDAO.obtenerPeriodoActual();
            if (periodoActual != null && periodoActual.getFechaFin() != null) {
                LocalDate fechaFinPeriodo = LocalDate.parse(periodoActual.getFechaFin());
                if (dpFechaFin.getValue().isAfter(fechaFinPeriodo)) {
                    AlertaUtilidad.mostrarAlertaSimple("Fecha Fuera de Periodo", 
                        "La fecha de fin de la entrega no puede ser posterior a la fecha de finalización del periodo escolar actual (" 
                        + fechaFinPeriodo.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ").", 
                        Alert.AlertType.WARNING);
                    return false;
                }
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error de Periodo", "No se pudo obtener la información del periodo escolar actual. No se puede programar la entrega.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "No se pudo verificar la fecha del periodo escolar. Error: " + e.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
        
        return true;
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        boolean confirmado = AlertaUtilidad.mostrarAlertaConfirmacion("Cancelar Operación", 
                "¿Estás seguro de que quieres cancelar?", "Cualquier dato no guardado se perderá.");
        if (confirmado) {
            cerrarVentana();
        }
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
        if(validarCampos()){
            String tablaDestino = "";
            switch(tipoEntrega){
                case "DOCUMENTOS INICIALES": tablaDestino = "entregadocumentoinicio"; break;
                case "REPORTES": tablaDestino = "entregareporte"; break;
                case "DOCUMENTOS FINALES": tablaDestino = "entregadocumentofinal"; break;
            }

            Entrega nuevaEntrega = new Entrega();
            nuevaEntrega.setNombre(tfNombre.getText());
            nuevaEntrega.setDescripcion(taDescripcion.getText());
            nuevaEntrega.setFechaInicio(dpFechaInicio.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            nuevaEntrega.setFechaFin(dpFechaFin.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            try{
                ResultadoOperacion resultado = ProgramarEntregaDAO.programarEntregaPeriodoActual(nuevaEntrega, tablaDestino);

                if(!resultado.isError()){
                    AlertaUtilidad.mostrarAlertaSimple("Operación Exitosa", "Operación realizada correctamente.", Alert.AlertType.INFORMATION);
                    observador.operacionExitosa();
                    cerrarVentana();
                } else {
                    AlertaUtilidad.mostrarAlertaSimple("Error", resultado.getMensaje(), Alert.AlertType.ERROR);
                }

            } catch(SQLException e){
                AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "No fue posible conectar con la base de datos.", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }
    
    private void cerrarVentana(){
        Utilidad.getEscenarioComponente(tfNombre).close();
    }
    
}
