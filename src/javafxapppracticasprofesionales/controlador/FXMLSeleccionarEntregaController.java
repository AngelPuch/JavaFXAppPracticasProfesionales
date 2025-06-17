package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.EntregaDAO;
import javafxapppracticasprofesionales.modelo.dao.EstudianteDAO;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;
import javafxapppracticasprofesionales.modelo.pojo.InfoEstudianteSesion;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLSeleccionarEntregaController implements Initializable {

    @FXML
    private TableView<Entrega> tvEntregas;
    @FXML
    private TableColumn<Entrega, String> colNombre;
    @FXML
    private TableColumn<Entrega, String> colFechaInicio;
    @FXML
    private TableColumn<Entrega, String> colFechaFin;
    @FXML
    private TableColumn<Entrega, String> colDescripcion;
    @FXML
    private Button btnContinuar;
    @FXML
    private Button btnCancelar;
    private INotificacion observador;
    private InfoEstudianteSesion infoSesion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        Usuario usuarioLogueado = SesionUsuario.getInstancia().getUsuarioLogueado();
        cargarInformacionEstudiante(usuarioLogueado.getIdUsuario());
    }
    
    public void inicializarDatos(INotificacion observador) {
        this.observador = observador;
    }
    
    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }
    
    private void cargarInformacionEstudiante(int idUsuario) {
        try {
            this.infoSesion = EstudianteDAO.obtenerInfoEstudianteParaSesion(idUsuario);
            if (this.infoSesion != null) {
                cargarEntregas(this.infoSesion.getIdGrupo(), this.infoSesion.getIdExpediente());
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Información no encontrada", 
                    "No se pudo encontrar la información de inscripción para el estudiante.", Alert.AlertType.ERROR);
                btnContinuar.setDisable(true); 
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "No se pudo cargar la información del estudiante.", Alert.AlertType.ERROR);
        }
    }
    
    private void cargarEntregas(int idGrupo, int idExpediente) {
        try {
            
            ArrayList<Entrega> entregasBD = EntregaDAO.obtenerEntregasPendientesEstudiante(idGrupo, idExpediente, "entregadocumentoinicio");
            
            final LocalDateTime fechaYHoraActual = LocalDateTime.now();
            
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            ArrayList<Entrega> entregasPendientes = entregasBD.stream()
                    .filter(entrega -> "Sin Entregar".equals(entrega.getEstado()))
                    .filter(entrega -> {
                        try {
                            LocalDateTime fechaFinEntrega = LocalDateTime.parse(entrega.getFechaFin(), formatter);
                            return fechaFinEntrega.isAfter(fechaYHoraActual);
                        } catch (DateTimeParseException e) {
                            
                            return false;
                        }
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
            
            ObservableList<Entrega> entregasObservable = FXCollections.observableArrayList(entregasPendientes);
            tvEntregas.setItems(entregasObservable);
            
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "No se pudieron cargar las entregas.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicContinuar(ActionEvent event) {
        Entrega entregaSeleccionada = tvEntregas.getSelectionModel().getSelectedItem();
        if (entregaSeleccionada == null) {
            AlertaUtilidad.mostrarAlertaSimple("Selección requerida", "Debes seleccionar una entrega para continuar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLSubirDocumento.fxml"));
            Parent vista = loader.load();

            FXMLSubirDocumentoController controller = loader.getController();
            controller.inicializarDatos(entregaSeleccionada.getIdEntrega(), this.infoSesion.getIdExpediente(), entregaSeleccionada.getNombre(), observador);

            Stage escenario = new Stage();
            escenario.setTitle("Subir documento inicial - Paso 2");
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Subir Documento");
            cerrarVentana();
            escenario.show();
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo cargar la siguiente ventana.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Utilidad.getEscenarioComponente(tvEntregas).close();
    }
    
}
