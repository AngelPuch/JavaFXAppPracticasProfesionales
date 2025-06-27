package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import javafxapppracticasprofesionales.modelo.pojo.ResponsableProyecto;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;
import javafxapppracticasprofesionales.utilidad.ValidacionUtilidad;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: FXMLRegistrarResponsableController.java 
    * Autor: Rodrigo Luna Vázquez, Jose Luis Silva Gómez
    * Fecha: 12/06/2025
*/
public class FXMLRegistrarResponsableController implements Initializable {

    @FXML
    private Label lbTitulo;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfCargo;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfCorreo;
    @FXML
    private Label lbTelefonoError;
    @FXML
    private Label lbCorreoError;
    @FXML
    private Label lbContadorCaracteresNombre;
    @FXML
    private Label lbContadorCaracteresApPaterno;
    @FXML
    private Label lbContadorCaracteresApMaterno;
    @FXML
    private Label lbContadorCaracteresCargo;
    
    private OrganizacionVinculada organizacionSeleccionada;
    private INotificacion observador;
    private ResponsableProyecto responsableActualizar;
    private boolean esEdicion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Utilidad.configurarTextAreaConContador(tfNombre, lbContadorCaracteresNombre, 70);
        Utilidad.configurarTextAreaConContador(tfApellidoPaterno, lbContadorCaracteresApPaterno, 50);
        Utilidad.configurarTextAreaConContador(tfApellidoMaterno, lbContadorCaracteresApMaterno, 50);
        Utilidad.configurarTextAreaConContador(tfCargo, lbContadorCaracteresCargo, 45);
    }    
    
    public void inicializarInformacion(OrganizacionVinculada organizacionSeleccionada, INotificacion observador, ResponsableProyecto responsableActualizar, boolean esEdicion) {
        this.organizacionSeleccionada = organizacionSeleccionada;
        this.observador = observador;
        this.responsableActualizar = responsableActualizar;
        this.esEdicion = esEdicion;
        
        if (esEdicion && responsableActualizar != null) {
            lbTitulo.setText("Actualizar Responsable del Proyecto");
            tfNombre.setText(responsableActualizar.getNombre());
            tfApellidoPaterno.setText(responsableActualizar.getApellidoPaterno());
            tfApellidoMaterno.setText(responsableActualizar.getApellidoMaterno());
            tfCargo.setText(responsableActualizar.getCargo());
            tfTelefono.setText(responsableActualizar.getTelefono());
            tfCorreo.setText(responsableActualizar.getCorreo());
        } else {
            lbTitulo.setText("Registrar Responsable del Proyecto");
        }
    }
    
    @FXML
    private void btnClicGuardar(ActionEvent event) {
        if (validarCampos()) {
            ResponsableProyecto responsable = obtenerResponsableFormulario();
            if (esEdicion) {
                actualizarResponsable(responsable);
            } else {
                registrarResponsable(responsable);
            }
        }
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private boolean validarCampos() {
        lbTelefonoError.setText("");
        lbCorreoError.setText("");
        boolean esValido = true;

        if (!ValidacionUtilidad.isCampoTextoValido(tfNombre, tfApellidoPaterno, tfCargo)) {
            return false;
        }

        if (!tfTelefono.getText().trim().isEmpty() && !ValidacionUtilidad.validarTelefono(tfTelefono.getText().trim())) {
            lbTelefonoError.setText("Teléfono inválido (10 dígitos)");
            esValido = false;
        }
        
        if (!tfCorreo.getText().trim().isEmpty() && !ValidacionUtilidad.validarCorreo(tfCorreo.getText().trim())) {
            lbCorreoError.setText("Correo inválido");
            esValido = false;
        }

        return esValido;
    }
    
    private ResponsableProyecto obtenerResponsableFormulario() {
        ResponsableProyecto responsable = new ResponsableProyecto();
        responsable.setNombre(tfNombre.getText().trim());
        responsable.setApellidoPaterno(tfApellidoPaterno.getText().trim());
        responsable.setApellidoMaterno(tfApellidoMaterno.getText().trim());
        responsable.setCargo(tfCargo.getText().trim());
        responsable.setTelefono(tfTelefono.getText().trim());
        responsable.setCorreo(tfCorreo.getText().trim());
        if (!esEdicion) {
            responsable.setIdOrganizacion(organizacionSeleccionada.getIdOrganizacion());
        }
        return responsable;
    }
    
    private void registrarResponsable(ResponsableProyecto responsable) {
        try {
            ResultadoOperacion resultado = ResponsableProyectoDAO.registrarResponsable(responsable);
            if (!resultado.isError()) {
                AlertaUtilidad.mostrarAlertaSimple("Registro Exitoso", resultado.getMensaje(), Alert.AlertType.INFORMATION);
                observador.operacionExitosa();
                cerrarVentana();
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error en el Registro", resultado.getMensaje(), Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "Hubo un error en la conexión con la base de datos. Intente más tarde.", Alert.AlertType.ERROR);
        }
    }

    private void actualizarResponsable(ResponsableProyecto responsable) {
        try {
            ResultadoOperacion resultado = ResponsableProyectoDAO.actualizarResponsable(responsable, responsableActualizar.getIdResponsable());
            if (!resultado.isError()) {
                AlertaUtilidad.mostrarAlertaSimple("Actualización Exitosa", resultado.getMensaje(), Alert.AlertType.INFORMATION);
                observador.operacionExitosa();
                cerrarVentana();
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error en la Actualización", resultado.getMensaje(), Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "Hubo un error en la conexión con la base de datos. Intente más tarde.", Alert.AlertType.ERROR);
        }
    }
    
    private void cerrarVentana() {
        Stage escenario = (Stage) lbTitulo.getScene().getWindow();
        escenario.close();
    }
}