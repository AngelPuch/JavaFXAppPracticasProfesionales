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
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.ExpedienteDAO;
import javafxapppracticasprofesionales.modelo.pojo.Estudiante;
import javafxapppracticasprofesionales.modelo.pojo.Proyecto;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: FXMLConfirmarAsignacionProyectoController.java 
    * Autor: Jose Luis Silva Gómez
    * Fecha: 12/06/2025
*/
public class FXMLConfirmarAsignacionProyectoController implements Initializable {

    @FXML
    private Label lbNombreEstudiante;
    @FXML
    private Label lbSemestreEstudiante;
    @FXML
    private Label lbCorreoEstudiante;
    @FXML
    private Label lbMatriculaEstudiante;
    @FXML
    private Label lbNumeroCuposProyecto;
    @FXML
    private TextArea taObjetivo;
    @FXML
    private TextArea taNombreProyecto;
    @FXML
    private TextArea taOrganizacion;
    
    Proyecto proyectoSeleccionado;
    Estudiante estudianteSeleccionado;
    INotificacion observador;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void inicializarInformacion(Proyecto proyectoSeleccionado, Estudiante estudianteSeleccionado, INotificacion observador) {
        this.proyectoSeleccionado = proyectoSeleccionado;
        this.estudianteSeleccionado = estudianteSeleccionado;
        this.observador = observador;
        configurarInformacionEstudiante();
        configurarInformacionProyecto();
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        boolean confirmado = AlertaUtilidad.mostrarAlertaConfirmacion("Cancelar", null,
                "¿Estás seguro de que quieres cancelar?");
        if (confirmado) {
            cerrarVentana();
        }
    }

    @FXML
    private void btnClicConfirmar(ActionEvent event) {
        try {
            ResultadoOperacion resultado = ExpedienteDAO.asignarProyectoAEstudiante(proyectoSeleccionado.getIdProyecto(), estudianteSeleccionado.getIdEstudiante());
            if (!resultado.isError()) {
                AlertaUtilidad.mostrarAlertaSimple("Operación exitosa",
                        "Operación realizada correctamente.", Alert.AlertType.INFORMATION);
                observador.operacionExitosa();
                cerrarVentana();
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error en el registro",
                        resultado.getMensaje(), Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Sin Conexión",
                    "Se perdió la conexión. Inténtalo de nuevo." + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void configurarInformacionEstudiante() {
        lbNombreEstudiante.setText(estudianteSeleccionado.getNombre());
        lbCorreoEstudiante.setText(estudianteSeleccionado.getCorreo());
        lbMatriculaEstudiante.setText(estudianteSeleccionado.getMatricula());
        lbSemestreEstudiante.setText(String.valueOf(estudianteSeleccionado.getSemestre()));
    }
    
    private void configurarInformacionProyecto() {
        taNombreProyecto.setText(proyectoSeleccionado.getNombre());
        taOrganizacion.setText(proyectoSeleccionado.getOrganizacion().getNombre());
        lbNumeroCuposProyecto.setText(String.valueOf(proyectoSeleccionado.getNumeroCupos()));
        taObjetivo.setText(proyectoSeleccionado.getObjetivo());
    }
    
    private void cerrarVentana(){
        Utilidad.getEscenarioComponente(lbCorreoEstudiante).close();
    }
    
}
