/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.modelo.pojo.EstudianteConProyecto;
import javafxapppracticasprofesionales.modelo.pojo.TipoDocumento;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;

/**
 * FXML Controller class
 *
 * @author grill
 */
public class FXMLExpedienteEstudianteController implements Initializable {

    @FXML
    private ListView<TipoDocumento> lvDocInicio;
    @FXML
    private ListView<TipoDocumento> lvDocFinal;
    @FXML
    private ListView<TipoDocumento> lvReporte;
    private EstudianteConProyecto estudiante; 
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbMatricula;
    @FXML
    private Label lbSemestre;
    @FXML
    private Label lbCorreo;



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTodasLasListas();
    }    
    
    public void inicializarInformacion(EstudianteConProyecto estudiante) {
        this.estudiante = estudiante;
        lbNombre.setText(this.estudiante.getNombreEstudiante());
        lbMatricula.setText(this.estudiante.getMatricula());      
        lbSemestre.setText(String.valueOf(this.estudiante.getSemestre()));
        lbCorreo.setText(this.estudiante.getCorreo());
    }

    @FXML
    private void btnClicConsultar(ActionEvent event) {
    }

    @FXML
private void btnClicRegresar(ActionEvent event) {
    Stage escenarioActual = (Stage) lbNombre.getScene().getWindow();
    escenarioActual.close();
}
    
     private void cargarTodasLasListas() {
        // --- 1. Cargar Documentos Iniciales ---
        ArrayList<TipoDocumento> docsIniciales = new ArrayList<>();
        docsIniciales.addAll(TipoDocumento.obtenerTiposDocumentoInicial());
        lvDocInicio.setItems(FXCollections.observableArrayList(docsIniciales));
        lvDocInicio.setVisible(true); // Aseguramos que sea visible

        // --- 2. Cargar Documentos Finales ---
        ArrayList<TipoDocumento> docsFinales = new ArrayList<>();
        docsFinales.addAll(TipoDocumento.obtenerTiposDocumentoFinal());
        lvDocFinal.setItems(FXCollections.observableArrayList(docsFinales));
        lvDocFinal.setVisible(true);

        // --- 3. Cargar Reportes ---
        ArrayList<TipoDocumento> reportes = new ArrayList<>();
        reportes.addAll(TipoDocumento.obtenerTiposDocumentoReporte());
        lvReporte.setItems(FXCollections.observableArrayList(reportes));
        lvReporte.setVisible(true);
    }
    
}
