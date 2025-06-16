/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;

/**
 * FXML Controller class
 *
 * @author grill
 */
public class FXMLRegistrarResponsableController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfCargo;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfCorreo;
    
    private OrganizacionVinculada organizacionSeleccionada;
    private INotificacion observador;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarInformacion(OrganizacionVinculada organizacionSeleccionada, INotificacion observador) {
        this.organizacionSeleccionada = organizacionSeleccionada;
        this.observador = observador;
}


    @FXML
    private void btnAceptar(ActionEvent event) {
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
    }
    
}
