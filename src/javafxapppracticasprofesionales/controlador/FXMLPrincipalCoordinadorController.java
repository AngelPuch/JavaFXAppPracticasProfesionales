package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.JavaFXAppPracticasProfesionales;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLPrincipalCoordinadorController implements Initializable {

    @FXML
    private AnchorPane apCentral;
    @FXML
    private Label lbNombreVentana;
    @FXML
    private Label lbNombreUsuario;
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
    private void btnClicProyectos(ActionEvent event) {
        cargarEscenas("vista/FXMLProyectos.fxml");
        lbNombreVentana.setText("Gestión de proyectos");
    }

    @FXML
    private void btnClicOrganizaciones(MouseEvent event) {
        cargarEscenas("vista/FXMLOrganizacionesVinculadas.fxml");
        lbNombreVentana.setText("Gestión de Organizaciones Vinculadas");
    }

    @FXML
    private void btnClicResponsables(MouseEvent event) {
        cargarEscenas("vista/FXMLResponsables.fxml");
        lbNombreVentana.setText("Gestion de responsables");
    }

    @FXML
    private void btnClicAsignarProyectos(ActionEvent event) {
        cargarEscenas("vista/FXMLEstudiantesConProyecto.fxml");
        lbNombreVentana.setText("Estudiantes con Proyectos Asignados");
    }

    @FXML
    private void btnClicEntregas(ActionEvent event) {
        cargarEscenas("vista/FXMLEntregas.fxml");
        lbNombreVentana.setText("Programar entregas");
        
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
