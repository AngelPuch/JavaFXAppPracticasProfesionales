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
import javafx.stage.Stage;
import javafxapppracticasprofesionales.JavaFXAppPracticasProfesionales;
import javafxapppracticasprofesionales.interfaz.IControladorPrincipal;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.EstudianteDAO;
import javafxapppracticasprofesionales.modelo.dao.EvaluacionDAO;
import javafxapppracticasprofesionales.modelo.pojo.InfoEstudianteSesion;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLPrincipalEstudianteController implements Initializable, INotificacion, IControladorPrincipal {

    @FXML
    private Label lbNombreVentana;
    @FXML
    private Label lbNombreUsuario;
    @FXML
    private AnchorPane apCentral;
    
    private Usuario usuarioSesion;
    private int idEstudiante;
    private int idExpediente;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @Override
    public void inicializarInformacion(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
        try {
            InfoEstudianteSesion info = EstudianteDAO.obtenerInfoEstudianteParaSesion(this.usuarioSesion.getIdUsuario());
            if (info != null && info.getIdExpediente() > 0) {
                this.idEstudiante = info.getIdEstudiante();
                this.idExpediente = info.getIdExpediente();
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "No se pudo cargar la información del estudiante.", Alert.AlertType.ERROR);
        }
        cargarInformacion();
    }
    
    private void cargarInformacion() {
        if (usuarioSesion != null) {
            lbNombreUsuario.setText(usuarioSesion.toString());
        }
    }

    @FXML
    private void btnClicEvaluarOrganizacionVinculada(ActionEvent event) {
        try {
            boolean tieneProyecto = EstudianteDAO.verificarProyectoAsignado(idEstudiante);
            if (!tieneProyecto) {
                AlertaUtilidad.mostrarAlertaSimple("Sin Proyecto Asignado",
                        "No tienes proyecto asignado, por lo tanto no puedes evaluar a una organización vinculada.",
                        Alert.AlertType.INFORMATION);
                return; 
            }
            int horasAcumuladas = EstudianteDAO.obtenerHorasAcumuladas(this.idExpediente);

            if (horasAcumuladas < 480) {
                AlertaUtilidad.mostrarAlertaSimple("Horas Insuficientes",
                        "Debes haber cumplido con al menos 480 horas para poder evaluar a la organización vinculada. Llevas " + horasAcumuladas + " horas.",
                        Alert.AlertType.WARNING);
                return; 
            }

            boolean yaEvaluado = EvaluacionDAO.haEvaluadoOVPreviamente(this.idExpediente);

            if (yaEvaluado) {
                cargarEvaluacionRealizada();
            } else {
                lbNombreVentana.setText("Evaluación de OV (Pendiente)");
                FXMLLoader loader = new FXMLLoader(JavaFXAppPracticasProfesionales.class.getResource("vista/FXMLPreEvaluarOV.fxml"));
                Parent root = loader.load();
                FXMLPreEvaluarOVController controller = loader.getController();
                controller.inicializarDatos(this);
                apCentral.getChildren().setAll(root);
            }
            
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "No se pudo verificar el estado de la evaluación: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo cargar la vista correspondiente: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void btnClicEntregasPendientes(ActionEvent event) {
        try {
            boolean tieneProyecto = EstudianteDAO.verificarProyectoAsignado(this.idEstudiante);

            if (tieneProyecto) {
                cargarEscenas("vista/FXMLEntregasPendientes.fxml");
                lbNombreVentana.setText("Entregas Pendientes");
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Proyecto no Asignado",
                    "No puedes entregar documentos porque aún no se te ha asignado un proyecto. " +
                    "Por favor, contacta al coordinador.",
                    Alert.AlertType.WARNING);
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", 
                "No se pudo verificar el estado de tu proyecto. Inténtalo de nuevo.", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void btnClicMiExpediente(ActionEvent event) {
        try {
            boolean tieneProyecto = EstudianteDAO.verificarProyectoAsignado(idEstudiante);

            if (tieneProyecto) {
                cargarEscenas("vista/FXMLMiExpediente.fxml");
                lbNombreVentana.setText("Mi expediente");
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Sin Avances",
                    "No tienes proyecto asignado, por lo tanto no hay avances que mostrar.",
                    Alert.AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Sin Conexión", "Se perdió la conexión. Inténtalo de nuevo", Alert.AlertType.ERROR);
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

    @Override
    public void operacionExitosa() {
        cargarEvaluacionRealizada();
    }
    
    private void cargarEvaluacionRealizada() {
        try {
            lbNombreVentana.setText("Evaluación de OV (Realizada)");
            FXMLLoader loader = new FXMLLoader(JavaFXAppPracticasProfesionales.class.getResource("vista/FXMLVerEvaluacionOV.fxml"));
            Parent root = loader.load();
            FXMLVerEvaluacionOVController controller = loader.getController();
            controller.inicializarDatos(this.idExpediente);
            apCentral.getChildren().setAll(root);
        } catch (IOException ex) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo cargar la vista correspondiente: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
