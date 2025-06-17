/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.ProyectoDAO;
import javafxapppracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import javafxapppracticasprofesionales.modelo.pojo.Proyecto;
import javafxapppracticasprofesionales.modelo.pojo.ResponsableProyecto;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

/**
 * FXML Controller class
 *
 * @author grill
 */
public class FXMLRegistrarResponsableController implements Initializable {

    @FXML
    private TextField tfNombre;
    
    private OrganizacionVinculada organizacionSeleccionada;
    private INotificacion observador;
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
    private Label lbTitulo;
    private ResponsableProyecto responsableActualizar;
    private boolean esEdicion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarInformacion(OrganizacionVinculada organizacionSeleccionada, INotificacion observador, ResponsableProyecto responsableActualizar, boolean esEdicion) {
        this.organizacionSeleccionada = organizacionSeleccionada;
        this.observador = observador;
        this.responsableActualizar = responsableActualizar;
        this.esEdicion = esEdicion;
        if (esEdicion && responsableActualizar != null) {
            tfNombre.setText(responsableActualizar.getNombre());
            tfCargo.setText(responsableActualizar.getCargo());
            tfTelefono.setText(responsableActualizar.getTelefono());
            tfCorreo.setText(responsableActualizar.getCorreo());
            lbTitulo.setText("Actualizar Responsable del Proyecto");
        }
    }


    @FXML
    private void btnAceptar(ActionEvent event) {
        if (validarCampos() && !esEdicion) {
            registrarResponsable(obtenerResponsableNuevo());
        } else if (validarCampos() && esEdicion) {
            actualizarResponsable(obtenerResponsableNuevo());
        }
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
        boolean confirmado = AlertaUtilidad.mostrarAlertaConfirmacion("Cancelar", null,
                "¿Estás seguro de que quieres cancelar?");
        if (confirmado) {
            cerrarVentana();
        }
    }
    
    private boolean validarCampos() {
        boolean esValido = true;

        if (tfNombre.getText().isEmpty() || tfCargo.getText().isEmpty()) {
            AlertaUtilidad.mostrarAlertaSimple("Campos vacíos",
                    "Los campos marcados con un (*) no deben de ser vacíos. Por favor, complétalos para continuar.", Alert.AlertType.WARNING);
            return false;
        }

        String telefono = tfTelefono.getText().trim();
        if (!telefono.isEmpty()) {
            if (!telefono.matches("\\d{10}")) {
                lbTelefonoError.setText("*Teléfono inválido");
                esValido = false;
            } else {
                lbTelefonoError.setText("");
            }
        } else {
            lbTelefonoError.setText("");
        }
        
        String correo = tfCorreo.getText().trim();
        if (!correo.isEmpty()) {
            if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
                lbCorreoError.setText("*Correo inválido");
                esValido = false;
            } else {
                lbCorreoError.setText("");
            }
        } else {
            lbCorreoError.setText("");
        }

        return esValido;
    }
    
    private ResponsableProyecto obtenerResponsableNuevo() {
        ResponsableProyecto nuevoResponsable = new ResponsableProyecto();
        nuevoResponsable.setNombre(tfNombre.getText());
        nuevoResponsable.setCargo(tfCargo.getText());
        nuevoResponsable.setCorreo(tfCorreo.getText());
        nuevoResponsable.setTelefono(tfTelefono.getText());
        if (!esEdicion) {
            nuevoResponsable.setIdOrganizacion(organizacionSeleccionada.getIdOrganizacion());
        }
        
        return nuevoResponsable;
    }
    
    private void registrarResponsable(ResponsableProyecto responsable) {
        try {
            ResultadoOperacion resultado = ResponsableProyectoDAO.registrarResponsable(responsable);
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
                    "Se perdió la conexión. Inténtalo de nuevo. Causa: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    private void actualizarResponsable(ResponsableProyecto responsable) {
        try {
            ResultadoOperacion resultado = ResponsableProyectoDAO.actualizarResponsable(responsable, responsableActualizar.getIdResponsable());
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
                    "Se perdió la conexión. Inténtalo de nuevo. Causa: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    private void cerrarVentana() {
        Utilidad.getEscenarioComponente(tfNombre).close();
    }
    
}
