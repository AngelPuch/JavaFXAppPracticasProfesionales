
package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafxapppracticasprofesionales.modelo.dao.EntregaDAO;
import javafxapppracticasprofesionales.modelo.dao.ProgramarEntregaDAO;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;
import javafxapppracticasprofesionales.modelo.pojo.Grupo;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarInformacion(String tipoEntrega, String nombreDocumento){
        this.tipoEntrega = tipoEntrega;
        this.tfNombre.setText(nombreDocumento);
    }
    
    private boolean validarCampos(){
        if(tfNombre.getText().isEmpty() || taDescripcion.getText().isEmpty() || dpFechaInicio.getValue() == null || dpFechaFin.getValue() == null){
            AlertaUtilidad.mostrarAlertaSimple("Campos inválidos", "Existen campos vacíos. Por favor, completa toda la información.", Alert.AlertType.WARNING);
            return false;
        }
        if(dpFechaFin.getValue().isBefore(dpFechaInicio.getValue())){
            AlertaUtilidad.mostrarAlertaSimple("Fechas incorrectas", "La fecha de fin no puede ser anterior a la fecha de inicio.", Alert.AlertType.WARNING);
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
    
    private void cerrarVentana(){
        Utilidad.getEscenarioComponente(tfNombre).close();
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
                // Se llama al nuevo método transaccional
                ResultadoOperacion resultado = ProgramarEntregaDAO.programarEntregaPeriodoActual(nuevaEntrega, tablaDestino);

                if(!resultado.isError()){
                    AlertaUtilidad.mostrarAlertaSimple("Operación Exitosa", resultado.getMensaje(), Alert.AlertType.INFORMATION);
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
    
}
