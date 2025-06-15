package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/** 
* Project: JavaFX Sales System 
* File: ClassName.java 
* Author: Jose Luis Silva Gomez 
* Date: YYYY-MM-DD 
* Description: Brief description of the file's purpose. 
*/
public class FXMLSeleccionarProyectoController implements Initializable {

    @FXML
    private TableView<?> tvProyectos;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colDescripcion;
    @FXML
    private TableColumn<?, ?> colNumeroCupos;
    @FXML
    private TableColumn<?, ?> colEstado;
    @FXML
    private TableColumn<?, ?> colObjetivo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnClicCancelar(ActionEvent event) {
    }

    @FXML
    private void btnClicContinuar(ActionEvent event) {
    }
    
}
