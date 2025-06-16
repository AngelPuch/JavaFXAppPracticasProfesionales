/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafxapppracticasprofesionales.modelo.pojo.TipoDocumento;

/**
 * FXML Controller class
 *
 * @author grill
 */
public class FXMLDocumentoInicialController implements Initializable {

    @FXML
    private ListView<TipoDocumento> lvTipoDocumento;
    private String tipoEntrega;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    public void inicializarInformacion(String tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
        cargarDocumentos();
    }
    
     private void cargarDocumentos() {
        System.out.println("Cargando documentos para: " + tipoEntrega); // debug
        ArrayList<TipoDocumento> documentosPojo = new ArrayList<>();
        
        if ("DOCUMENTOS INICIALES".equals(tipoEntrega)) {
            documentosPojo.addAll(TipoDocumento.obtenerTiposDocumentoInicial());
        }
        
        lvTipoDocumento.setItems(FXCollections.observableArrayList(documentosPojo));
        lvTipoDocumento.requestFocus();

    }
}
