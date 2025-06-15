/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import javafxapppracticasprofesionales.modelo.pojo.Periodo;
import javafxapppracticasprofesionales.modelo.pojo.ResponsableProyecto;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

/**
 * FXML Controller class
 *
 * @author grill
 */
public class FXMLResponsablesController implements Initializable {

    @FXML
    private TableView<ResponsableProyecto> tvResponsables;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colCargo;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private TableColumn<ResponsableProyecto, String> colOrganizacion;
    @FXML
    private TableColumn coltelefono;
    
    private ObservableList<ResponsableProyecto> responsableProyecto;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        colOrganizacion.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getOrganizacionVinculada().getNombre()));        
        coltelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        colCargo.setCellValueFactory(new PropertyValueFactory("cargo"));
    }
    
    private void cargarInformacionTabla() {
        try {
            responsableProyecto = FXCollections.observableArrayList();
            ArrayList<ResponsableProyecto> responsableProyectoDAO = ResponsableProyectoDAO.obtenerResponsables();
            responsableProyecto.addAll(responsableProyectoDAO);
            tvResponsables.setItems(responsableProyecto);
        } catch (SQLException ex) {
            AlertaUtilidad.mostrarAlertaSimple("Error al cargar", "Hubo un error al cargar los responsables. "
                    + "Por favor intentelo más tarde", Alert.AlertType.WARNING);
            Utilidad.getEscenarioComponente(tvResponsables).close();
        }
        
    }

    @FXML
    private void btnClicRegistrar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLOrganizacionDelProyecto.fxml"));
            Parent vista = loader.load();
            
            Stage escenario = new Stage();
            escenario.setTitle("Registrar Nuevo Proyecto - Paso 1");
            escenario.setScene(new Scene(vista));
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
            
            
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo abrir la ventana de registro.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    
    
}
