package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.EstudianteDAO;
import javafxapppracticasprofesionales.modelo.dao.EvaluacionDAO;
import javafxapppracticasprofesionales.modelo.pojo.AfirmacionOV;
import javafxapppracticasprofesionales.modelo.pojo.EvaluacionOV;
import javafxapppracticasprofesionales.modelo.pojo.InfoEstudianteSesion;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLEvaluarOrganizacionVinculadaController implements Initializable {
    
    @FXML
    private Label lbNombreAlumno;
    @FXML
    private Label lbMatricula;
    @FXML
    private Label lbOrganizacion;
    @FXML
    private Label lbProyecto;
    @FXML
    private TableView<AfirmacionOV> tvAfirmaciones;
    @FXML
    private TableColumn<AfirmacionOV, String> colCategoria;
    @FXML
    private TableColumn<AfirmacionOV, String> colAfirmacion;
    @FXML
    private TableColumn<AfirmacionOV, HBox> colNunca;
    @FXML
    private TableColumn<AfirmacionOV, HBox> colPocasVeces;
    @FXML
    private TableColumn<AfirmacionOV, HBox> colAlgunasVeces;
    @FXML
    private TableColumn<AfirmacionOV, HBox> colMuchasVeces;
    @FXML
    private TableColumn<AfirmacionOV, HBox> colSiempre;
    @FXML
    private TextArea taObservaciones;
    private InfoEstudianteSesion infoSesion;
    private ObservableList<AfirmacionOV> listaAfirmaciones;
    private int idExpediente;
    private int idUsuario;
    private final Map<AfirmacionOV, ToggleGroup> gruposPorAfirmacion = new HashMap<>();
    @FXML
    private Label lbContadorCaracteres;
    private INotificacion observador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarAfirmaciones();
        Usuario usuarioLogueado = SesionUsuario.getInstancia().getUsuarioLogueado();
        try {
            infoSesion = EstudianteDAO.obtenerInfoEstudianteParaSesion(usuarioLogueado.getIdUsuario());
            if (infoSesion != null) {
                this.idUsuario = usuarioLogueado.getIdUsuario();
                this.idExpediente = infoSesion.getIdExpediente();
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error de Información", "No se pudo recuperar la información del estudiante.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "Se perdió la conexión. Inténtalo de nuevo", Alert.AlertType.ERROR);
        }
        cargarDatosGenerales();
        Utilidad.configurarTextAreaConContador(taObservaciones, lbContadorCaracteres, 150);
    }
    
    public void inicializarDatos(INotificacion observador) {
        this.observador = observador;
    }

    private void configurarTabla() {
        colCategoria.setCellValueFactory(cellData -> new SimpleObjectProperty<>(String.valueOf(cellData.getValue().getIdAfirmacion()) + "."));
        colAfirmacion.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDescripcion()));

        colNunca.setCellValueFactory(cellData -> createRadioButtonCell(cellData.getValue(), 1));
        colPocasVeces.setCellValueFactory(cellData -> createRadioButtonCell(cellData.getValue(), 2));
        colAlgunasVeces.setCellValueFactory(cellData -> createRadioButtonCell(cellData.getValue(), 3));
        colMuchasVeces.setCellValueFactory(cellData -> createRadioButtonCell(cellData.getValue(), 4));
        colSiempre.setCellValueFactory(cellData -> createRadioButtonCell(cellData.getValue(), 5));
    }

    private SimpleObjectProperty<HBox> createRadioButtonCell(AfirmacionOV afirmacion, int valorRespuesta) {
        RadioButton rb = new RadioButton();
        rb.setUserData(valorRespuesta);

        ToggleGroup grupo = gruposPorAfirmacion.computeIfAbsent(afirmacion, af -> {
            ToggleGroup nuevoGrupo = new ToggleGroup();
            nuevoGrupo.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    afirmacion.setRespuestaSeleccionada((int) newValue.getUserData());
                }
            });
            return nuevoGrupo;
        });

        rb.setToggleGroup(grupo);

        if (afirmacion.getRespuestaSeleccionada() == valorRespuesta) {
            rb.setSelected(true);
        }

        HBox hbox = new HBox(rb);
        hbox.setAlignment(Pos.CENTER);
        return new SimpleObjectProperty<>(hbox);
    }

    private void cargarAfirmaciones() {
        try {
            listaAfirmaciones = FXCollections.observableArrayList(EvaluacionDAO.obtenerAfirmacionesOV());
            tvAfirmaciones.setItems(listaAfirmaciones);
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "No se pudieron cargar las afirmaciones.", Alert.AlertType.ERROR);
        }
    }
    
    private void cargarDatosGenerales() {
        try {
            EvaluacionOV datos = EvaluacionDAO.obtenerInfoParaEvaluacion(this.idExpediente);
            if (datos != null) {
                lbNombreAlumno.setText(datos.getNombreAlumno());
                lbMatricula.setText(datos.getMatricula());
                lbOrganizacion.setText(datos.getNombreOrganizacion());
                lbProyecto.setText(datos.getNombreProyecto());
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "No se pudieron cargar los datos de la evaluación.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicAceptar(ActionEvent event) {
        if (!validarRespuestasCompletas()) {
            AlertaUtilidad.mostrarAlertaSimple("Evaluación Incompleta", "Por favor, responde a todas las afirmaciones.", Alert.AlertType.WARNING);
            return;
        }
        abrirVentanaConfirmacion();
    }
    
    private boolean validarRespuestasCompletas() {
        for (AfirmacionOV afirmacion : listaAfirmaciones) {
            if (afirmacion.getRespuestaSeleccionada() == 0) {  
                return false;  
            }
        }
        return true;
    }
    
    private void abrirVentanaConfirmacion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLConfirmarDatos.fxml"));
            Parent vista = loader.load();
            
            FXMLConfirmarDatosController controller = loader.getController();
            
            String obs = taObservaciones.getText().trim();
            controller.inicializarDatos(this.listaAfirmaciones, obs, this.idUsuario, this.idExpediente, observador); 
            
            Stage escenario = new Stage();
            escenario.setTitle("Confirmar Evaluación");
            escenario.setScene(new Scene(vista));
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.show();
            cerrarVentana();
            
            
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Interfaz", "No se pudo cargar la ventana de confirmación.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        boolean confirmado = AlertaUtilidad.mostrarAlertaConfirmacion("Cancelar", 
                "¿Estás seguro de que quieres cancelar?",
                "Si cancelas, la información no se guardará.");
        if (confirmado) {
            cerrarVentana();
        }
    }
    
    private void cerrarVentana() {
        Stage escenario = (Stage) lbProyecto.getScene().getWindow();
        if (escenario != null) {
            escenario.close();
        }
    }
}