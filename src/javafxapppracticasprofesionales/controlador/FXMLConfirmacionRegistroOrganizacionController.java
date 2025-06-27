package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: FXMLConfirmacionRegistroOrganizacionController.java 
    * Autor: Rodrigo Luna Vázquez 
    * Fecha: 12/06/2025
*/
public class FXMLConfirmacionRegistroOrganizacionController implements Initializable {

    @FXML
    private Label lbNombre;
    @FXML
    private Label lbTelefono;
    @FXML
    private Label lbCalle;
    @FXML
    private Label lbNumero;
    @FXML
    private Label lbColonia;
    @FXML
    private Label lbCodigoPostal;
    @FXML
    private Label lbMunicipio;
    @FXML
    private Label lbEstado;
    
    private boolean confirmado = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void inicializarDatos(OrganizacionVinculada organizacion) {
        lbNombre.setText(organizacion.getNombre());
        lbTelefono.setText(organizacion.getTelefono());
        lbCalle.setText(organizacion.getCalle());
        lbNumero.setText(organizacion.getNumero());
        lbColonia.setText(organizacion.getColonia());
        lbCodigoPostal.setText(organizacion.getCodigoPostal());
        lbMunicipio.setText(organizacion.getMunicipio());
        lbEstado.setText(organizacion.getEstado());
    }
    
    public boolean isConfirmado() {
        return confirmado;
    }

    @FXML
    private void btnClicConfirmar(ActionEvent event) {
        confirmado = true;
        cerrarVentana();
    }

    @FXML
    private void btnClicModificarDatos(ActionEvent event) {
        confirmado = false;
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Stage escenario = (Stage) lbNombre.getScene().getWindow();
        escenario.close();
    }
}