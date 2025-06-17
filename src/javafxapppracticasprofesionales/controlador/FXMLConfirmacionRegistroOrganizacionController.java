package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;

/** 
* Project: JavaFX Sales System 
* File: ClassName.java 
* Author: Jose Luis Silva Gomez 
* Date: YYYY-MM-DD 
* Description: Brief description of the file's purpose. 
*/
public class FXMLConfirmacionRegistroOrganizacionController implements Initializable {

    @FXML
    private Label lbNombre;
    @FXML
    private Label lbDireccion;
    @FXML
    private Label lbTelefono;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnConfirmar;
    private boolean confirmado = false;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarDatos(OrganizacionVinculada organizacion) {
        if (organizacion != null) {
            lbNombre.setText(organizacion.getNombre());
            lbDireccion.setText(organizacion.getDireccion());
            lbTelefono.setText(organizacion.getTelefono());
        }
    }
    
    public boolean isConfirmado() {
        return confirmado;
    }

    private void cerrarVentana() {
        Stage escenario = (Stage) btnConfirmar.getScene().getWindow();
        escenario.close();
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        confirmado = false;
        cerrarVentana();
    }

    @FXML
    private void btnClicConfirmar(ActionEvent event) {
        confirmado = true;
        cerrarVentana();
    }
    
}
