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
import javafxapppracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import javafxapppracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import javafxapppracticasprofesionales.modelo.pojo.ResponsableProyecto;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

/**
 * FXML Controller class
 *
 * @author grill
 */
public class FXMLRegistrarOrganizacionVinculadaController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfDireccion;
    @FXML
    private Label lbTelefonoError;
    private INotificacion observador;
    private OrganizacionVinculada organizacionVinculada;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarInformacion(OrganizacionVinculada organizacionVinculada, INotificacion observador) {
        this.organizacionVinculada = organizacionVinculada;
        this.observador = observador;
}

    @FXML
    private void btnAceptar(ActionEvent event) {
        if (validarCampos()) {
            registrarOrganizacion(obtenerOrganizacionNueva());
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

        // Validar que ningún campo de texto principal esté vacío
        if (tfNombre.getText().isEmpty() || tfTelefono.getText().isEmpty() || tfDireccion.getText().isEmpty()) {
            AlertaUtilidad.mostrarAlertaSimple("Campos vacíos",
                    "Existen campos vacíos. Por favor, complétalos para continuar.", Alert.AlertType.WARNING);
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

        return esValido;
    }
    
    private OrganizacionVinculada obtenerOrganizacionNueva() {
        OrganizacionVinculada nuevaOrganizacion = new OrganizacionVinculada();
        nuevaOrganizacion.setNombre(tfNombre.getText());
        nuevaOrganizacion.setDireccion(tfDireccion.getText());
        nuevaOrganizacion.setTelefono(tfTelefono.getText());
        
        return nuevaOrganizacion;
    }
    
    private void registrarOrganizacion(OrganizacionVinculada organizacionVinculada) {
        try {
            ResultadoOperacion resultado = OrganizacionVinculadaDAO.registrarOrganizacion(organizacionVinculada);
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
