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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.JavaFXAppPracticasProfesionales;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLPrincipalEvaluadorController implements Initializable {

    @FXML
    private Label lblNombreEvaluador;
    @FXML
    private Button btnEvaluarEstudiante;
    private Usuario usuario;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    public void inicializarInformacion(Usuario usuario) {
        this.usuario = usuario;
        lblNombreEvaluador.setText("Bienvenido(a), " + usuario.getNombre());
    }

    @FXML
    private void btnClicEvaluarEstudiante(ActionEvent event) {
        try {
            Stage escenario = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLSeleccionarEstudianteParaEvaluar.fxml"));
            Parent vista = loader.load();
            
            Scene escena = new Scene(vista);
            escenario.setTitle("Evaluar Estudiante");
            escenario.setScene(escena);
            escenario.show();
            
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de UI", "No se pudo cargar la ventana para evaluar estudiantes.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void btnClicCerrarSesion(ActionEvent event) {
        try {
            SesionUsuario.getInstancia().cerrarSesion();
            FXMLLoader cargador = new FXMLLoader(JavaFXAppPracticasProfesionales.class.getResource("vista/FXMLInicioSesion.fxml"));
            Parent vistaInicioSesion = cargador.load();
            Scene escenaInicio = new Scene(vistaInicioSesion);
            Stage escenarioActual = Utilidad.getEscenarioComponente(lblNombreEvaluador);
            escenarioActual.setScene(escenaInicio);
            escenarioActual.setTitle("Inicio de sesión");
            escenarioActual.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
