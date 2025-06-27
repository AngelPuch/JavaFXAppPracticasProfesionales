package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.JavaFXAppPracticasProfesionales;
import javafxapppracticasprofesionales.interfaz.IControladorPrincipal;
import javafxapppracticasprofesionales.modelo.dao.EstudianteDAO;
import javafxapppracticasprofesionales.modelo.pojo.Estudiante;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLPrincipalEvaluadorController implements Initializable, IControladorPrincipal {

    @FXML
    private Label lblNombreEvaluador;
    @FXML
    private TableView<Estudiante> tvEstudiantes;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colMatricula;
    @FXML
    private TableColumn colSemestre;
    
    private Usuario usuario;
    private ObservableList<Estudiante> listaEstudiantes;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarEstudiantes();
    }   
    
    @Override
    public void inicializarInformacion(Usuario usuario) {
        this.usuario = usuario;
        lblNombreEvaluador.setText("Bienvenido(a), " + usuario.getNombre());
    }

    @FXML
    private void btnClicContinuar(ActionEvent event) {
        Estudiante estudianteSeleccionado = tvEstudiantes.getSelectionModel().getSelectedItem();
        if (estudianteSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLCalificarPresentacion.fxml"));
                Parent vista = loader.load();

                FXMLCalificarPresentacionController controller = loader.getController();
                controller.inicializarInformacion(estudianteSeleccionado);

                Stage escenario = new Stage();
                escenario.setTitle("Calificar Presentación");
                escenario.setScene(new Scene(vista));
                escenario.show();
                
            } catch (IOException e) {
                AlertaUtilidad.mostrarAlertaSimple("Error de UI", "No se pudo cargar la ventana de calificación.", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        } else {
            AlertaUtilidad.mostrarAlertaSimple("Selección requerida", "Debe seleccionar un estudiante para continuar.", Alert.AlertType.WARNING);
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

    
    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colSemestre.setCellValueFactory(new PropertyValueFactory<>("semestre"));
    }

    private void cargarEstudiantes() {
        try {
            listaEstudiantes = FXCollections.observableArrayList(EstudianteDAO.obtenerEstudiantesParaEvaluar());
            tvEstudiantes.setItems(listaEstudiantes);
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
}
