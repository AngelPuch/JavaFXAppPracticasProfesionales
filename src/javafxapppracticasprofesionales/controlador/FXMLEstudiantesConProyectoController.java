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
public class FXMLEstudiantesConProyectoController implements Initializable {

    @FXML
    private TableView<?> tvEstudiantesConProyecto;
    @FXML
    private TableColumn<?, ?> colEstudiante;
    @FXML
    private TableColumn<?, ?> colMatricula;
    @FXML
    private TableColumn<?, ?> colProyecto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnClicAsignarProyecto(ActionEvent event) {
    }
    
}
