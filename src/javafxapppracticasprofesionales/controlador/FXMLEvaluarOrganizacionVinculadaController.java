package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

/** 
* Project: JavaFXAppPracticasProfesionales 
* File: FXMLEvaluarOrganizacionVinculadaController.java 
* Author: Jose Luis Silva Gomez 
* Date: 2025-06-16 
* Description: Brief description of the file's purpose. 
*/
public class FXMLEvaluarOrganizacionVinculadaController implements Initializable {

    @FXML
    private Label lbNombreAlumno;
    @FXML
    private Label lbMatricula;
    @FXML
    private Label lbOrganizacion;
    @FXML
    private Label lbProyecto;
    @FXML
    private TableView<?> tvAfirmaciones;
    @FXML
    private TableColumn<?, ?> colCategoria;
    @FXML
    private TableColumn<?, ?> colAfirmacion;
    @FXML
    private TableColumn<?, ?> colNunca;
    @FXML
    private TableColumn<?, ?> colPocasVeces;
    @FXML
    private TableColumn<?, ?> colAlgunasVeces;
    @FXML
    private TableColumn<?, ?> colMuchasVeces;
    @FXML
    private TableColumn<?, ?> colSiempre;
    @FXML
    private TextArea taObservaciones;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicCancelar(ActionEvent event) {
    }

    @FXML
    private void clicAceptar(ActionEvent event) {
    }
    
}
