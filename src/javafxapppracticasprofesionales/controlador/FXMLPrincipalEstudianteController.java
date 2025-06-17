/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.JavaFXAppPracticasProfesionales;
import javafxapppracticasprofesionales.modelo.dao.EstudianteDAO;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;

/** 
* Project: JavaFXAppPracticasProfesionales 
* File: FXMLPrincipalEstudianteController.java 
* Author: Jose Luis Silva Gomez 
* Date: 2025-06-16 
* Description: Brief description of the file's purpose. 
*/
public class FXMLPrincipalEstudianteController implements Initializable {

    @FXML
    private Label lbNombreVentana;
    @FXML
    private Label lbNombreUsuario;
    @FXML
    private VBox apCentral;
    private Usuario usuarioSesion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarInformacion(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
        cargarInformacion();
    }
    
    private void cargarInformacion() {
        if (usuarioSesion != null) {
            lbNombreUsuario.setText(usuarioSesion.toString());
        }
    }

    @FXML
    private void btnClicEvaluarOrganizacionVinculada(ActionEvent event) {
    }

    @FXML
    private void btnClicEntregasPendientes(ActionEvent event) {
        cargarEscenas("vista/FXMLEntregasPendientes.fxml");
        lbNombreVentana.setText("Entregas Pendientes");
    }
    
    @FXML
    private void btnClicMiExpediente(ActionEvent event) {
        try {
            int idEstudiante = SesionUsuario.getInstancia().getUsuarioLogueado().getIdEstudiante();
            boolean tieneProyecto = EstudianteDAO.verificarProyectoAsignado(idEstudiante);

            if (tieneProyecto) {
                // Flujo Normal: Cargar la ventana del expediente
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLMiExpediente.fxml"));
                Parent vista = loader.load();
                
                FXMLMiExpedienteController controller = loader.getController();
                // Aquí se podría pasar información si fuera necesario.
                
                Scene escena = new Scene(vista);
                Stage escenario = new Stage();
                escenario.setTitle("Mi Expediente");
                escenario.setScene(escena);
                escenario.show();
                // Opcional: cerrar la ventana principal
                // Utilidad.getEscenarioComponente(algúnControlEnLaVista).close();

            } else {
                // No se cumple PRE-1: Mostrar alerta.
                AlertaUtilidad.mostrarAlertaSimple("Sin Avances",
                    "No tienes proyecto asignado, por lo tanto no hay avances que mostrar.",
                    Alert.AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Sin Conexión", "Se perdió la conexión. Inténtalo de nuevo", Alert.AlertType.ERROR);
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de UI", "No se pudo cargar la ventana del expediente.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnClicCerrarSesion(ActionEvent event) {
        try {
            SesionUsuario.getInstancia().cerrarSesion();
            FXMLLoader cargador = new FXMLLoader(JavaFXAppPracticasProfesionales.class.getResource("vista/FXMLInicioSesion.fxml"));
            Parent vistaInicioSesion = cargador.load();
            Scene escenaInicio = new Scene(vistaInicioSesion);
            Stage escenarioActual = Utilidad.getEscenarioComponente(lbNombreVentana);
            escenarioActual.setScene(escenaInicio);
            escenarioActual.setTitle("Inicio de sesión");
            escenarioActual.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void cargarEscenas(String ruta) {
        try {
            FXMLLoader loader = new FXMLLoader(JavaFXAppPracticasProfesionales.class.getResource(ruta));
            Parent root = loader.load();
            apCentral.getChildren().setAll(root);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    
}
