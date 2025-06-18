package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.EvaluacionDAO;
import javafxapppracticasprofesionales.modelo.pojo.AfirmacionOV;
import javafxapppracticasprofesionales.modelo.pojo.RespuestaGuardadaOV;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLConfirmarDatosController implements Initializable {

    @FXML
    private TableView<RespuestaGuardadaOV> tvResumen;
    @FXML
    private TableColumn<RespuestaGuardadaOV, String> colAfirmacion;
    @FXML
    private TableColumn<RespuestaGuardadaOV, Integer> colRespuesta;
    @FXML
    private TextArea taComentarios;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private List<AfirmacionOV> listaAfirmaciones;
    private String comentarios;
    private int idUsuario;
    private int idExpediente;
    private INotificacion observador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }    
    
    public void inicializarDatos(List<AfirmacionOV> afirmaciones, String comentarios, int idUsuario, int idExpediente, INotificacion observador) {
        this.listaAfirmaciones = afirmaciones;
        this.comentarios = comentarios;
        this.idUsuario = idUsuario;
        this.idExpediente = idExpediente;
        this.observador = observador;
        
        cargarDatosResumen();
    }
    
    private void configurarTabla() {
        colAfirmacion.setCellValueFactory(new PropertyValueFactory<>("afirmacion"));
        colRespuesta.setCellValueFactory(new PropertyValueFactory<>("respuesta"));
    }
    
    private void cargarDatosResumen() {
        taComentarios.setText(this.comentarios);
        ArrayList<RespuestaGuardadaOV> resumenRespuestas = new ArrayList<>();
        for (AfirmacionOV afirmacion : listaAfirmaciones) {
            RespuestaGuardadaOV respuesta = new RespuestaGuardadaOV();
            respuesta.setAfirmacion(afirmacion.getDescripcion());
            respuesta.setRespuesta(afirmacion.getRespuestaSeleccionada());
            resumenRespuestas.add(respuesta);
        }
        tvResumen.setItems(FXCollections.observableArrayList(resumenRespuestas));
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        try {
            double sumaRespuestas = 0;
            for (AfirmacionOV afirmacion : listaAfirmaciones) {
                sumaRespuestas += afirmacion.getRespuestaSeleccionada();
            }
            double calificacionPromedio = 0;
            if (!listaAfirmaciones.isEmpty()) {
                calificacionPromedio = sumaRespuestas / listaAfirmaciones.size();
            }
            ResultadoOperacion resultado = EvaluacionDAO.guardarEvaluacionOV(idUsuario, idExpediente, comentarios, listaAfirmaciones, calificacionPromedio);
            
            if (!resultado.isError()) {
                AlertaUtilidad.mostrarAlertaSimple("Operación Exitosa", "Operación realizada correctamente.", Alert.AlertType.INFORMATION);
                observador.operacionExitosa();
                cerrarVentana();
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error al Guardar", "No se ha podido guardar su evaluación. Inténtelo más tarde.", Alert.AlertType.ERROR);
            }
            
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "Se perdió la conexión. Inténtalo de nuevo", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        boolean confirmado = AlertaUtilidad.mostrarAlertaConfirmacion("Cancelar", 
                "¿Estás seguro de que quieres cancelar?",
                "Si cancelas, la información no se guardará.");
        if (confirmado) {
            cerrarVentana();
        }
    }
    
    private void cerrarVentana() {
        Utilidad.getEscenarioComponente(tvResumen).close();
    }
}
