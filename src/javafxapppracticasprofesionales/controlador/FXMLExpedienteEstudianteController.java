package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.modelo.dao.AvanceDAO;
import javafxapppracticasprofesionales.modelo.dao.EstudianteDAO;
import javafxapppracticasprofesionales.modelo.dao.EvaluacionDAO;
import javafxapppracticasprofesionales.modelo.dao.ExpedienteDAO;
import javafxapppracticasprofesionales.modelo.pojo.Avance;
import javafxapppracticasprofesionales.modelo.pojo.Estudiante;
import javafxapppracticasprofesionales.modelo.pojo.EstudianteConProyecto;
import javafxapppracticasprofesionales.modelo.pojo.Evaluacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import static javafxapppracticasprofesionales.utilidad.Utilidad.abrirDocumento;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: FXMLExpedienteEstudianteController.java 
    * Autor: Rodrigo Luna Vázquez
    * Fecha: 15/06/2025
*/
public class FXMLExpedienteEstudianteController implements Initializable {

    @FXML
    private Label lbNombre;
    @FXML
    private Label lbMatricula;
    @FXML
    private Label lbSemestre;
    @FXML
    private Label lbCorreo;
    @FXML
    private TabPane tpExpediente;
    @FXML
    private TableView<Avance> tvDocumentosIniciales;
    @FXML
    private TableColumn colNombreDI;
    @FXML
    private TableColumn colFechaDI;
    @FXML
    private TableColumn colEstadoDI;
    @FXML
    private TableView<Avance> tvReportes;
    @FXML
    private TableColumn colNombreR;
    @FXML
    private TableColumn colFechaR;
    @FXML
    private TableColumn colEstadoR;
    @FXML
    private TableView<Avance> tvDocumentosFinales;
    @FXML
    private TableColumn colNombreDF;
    @FXML
    private TableColumn colFechaDF;
    @FXML
    private TableColumn colEstadoDF;
    @FXML
    private TableView<Evaluacion> tvEvaluaciones;
    @FXML
    private TableColumn colFechaEval;
    @FXML
    private TableColumn colCalificacionEval;
    @FXML
    private TableColumn colMotivoEval;
    @FXML
    private Tab tabEvaluaciones;
    @FXML
    private Button btnConsultar;
    
    private EstudianteConProyecto estudianteConProyecto;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tpExpediente.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
                if (newTab != null) {
                    btnConsultar.setDisable(newTab.equals(tabEvaluaciones));
                }
            });
    }

    public void inicializarInformacion(EstudianteConProyecto estudiante) {
        this.estudianteConProyecto = estudiante;
        configurarTablas();
        cargarDatosEstudiante();
        cargarExpediente();
    }

    private void configurarTablas() {
        colNombreDI.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaDI.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstadoDI.setCellValueFactory(new PropertyValueFactory<>("estado"));

        colNombreR.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaR.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstadoR.setCellValueFactory(new PropertyValueFactory<>("estado"));

        colNombreDF.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaDF.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstadoDF.setCellValueFactory(new PropertyValueFactory<>("estado"));

        colFechaEval.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colCalificacionEval.setCellValueFactory(new PropertyValueFactory<>("calificacionTotal"));
        colMotivoEval.setCellValueFactory(new PropertyValueFactory<>("motivo"));
    }

    private void cargarDatosEstudiante() {
        if (this.estudianteConProyecto != null) {
            lbNombre.setText(this.estudianteConProyecto.getNombreEstudiante());
            lbMatricula.setText(this.estudianteConProyecto.getMatricula());
            lbSemestre.setText(String.valueOf(this.estudianteConProyecto.getSemestre()));
            lbCorreo.setText(this.estudianteConProyecto.getCorreo());
        }
    }

    private void cargarExpediente() {
        try {
            Estudiante estudianteCompleto = EstudianteDAO.getEstudiantePorMatricula(this.estudianteConProyecto.getMatricula());
            if (estudianteCompleto != null) {
                int idExpediente = ExpedienteDAO.obtenerIdExpedienteActivo(estudianteCompleto.getIdEstudiante());

                if (idExpediente > 0) {
                    tvDocumentosIniciales.setItems(FXCollections.observableArrayList(AvanceDAO.obtenerDocumentosInicio(idExpediente)));
                    tvReportes.setItems(FXCollections.observableArrayList(AvanceDAO.obtenerReportes(idExpediente)));
                    tvDocumentosFinales.setItems(FXCollections.observableArrayList(AvanceDAO.obtenerDocumentosFinales(idExpediente)));
                    tvEvaluaciones.setItems(FXCollections.observableArrayList(EvaluacionDAO.obtenerEvaluacionesPorExpediente(idExpediente)));
                } else {
                    AlertaUtilidad.mostrarAlertaSimple("Información no disponible", "No se encontró un expediente activo para este estudiante.", Alert.AlertType.INFORMATION);
                }
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de conexión", "No se pudo conectar con la base de datos para cargar el expediente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnClicCerrar(ActionEvent event) {
        Stage escenarioActual = (Stage) lbNombre.getScene().getWindow();
        escenarioActual.close();
    }

    @FXML
    private void btnClicConsultar(ActionEvent event) {
        obtenerAvanceSeleccionadoDeTablaActiva().ifPresent(avance -> {
            String rutaArchivo = avance.getRutaArchivo();
            if (rutaArchivo != null && !rutaArchivo.trim().isEmpty()) {
                abrirDocumento(rutaArchivo);
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Archivo no disponible", 
                    "El documento seleccionado no tiene una ruta de archivo registrada.", Alert.AlertType.ERROR);
            }
        });
    }


    private Optional<Avance> obtenerAvanceSeleccionadoDeTablaActiva() {
        Tab pestañaSeleccionada = tpExpediente.getSelectionModel().getSelectedItem();
        if (pestañaSeleccionada == null) {
            AlertaUtilidad.mostrarAlertaSimple("Sin Selección", 
                "Debes seleccionar una pestaña (Documentos Iniciales, Reportes, etc.).", Alert.AlertType.WARNING);
            return Optional.empty();
        }

        Node contenidoPestaña = pestañaSeleccionada.getContent();
        if (!(contenidoPestaña instanceof TableView)) {
            contenidoPestaña = contenidoPestaña.lookup("TableView");
        }

        if (contenidoPestaña instanceof TableView) {
            @SuppressWarnings("unchecked")
            TableView<Avance> tablaActiva = (TableView<Avance>) contenidoPestaña;
            Avance avanceSeleccionado = tablaActiva.getSelectionModel().getSelectedItem();

            if (avanceSeleccionado == null) {
                AlertaUtilidad.mostrarAlertaSimple("Sin Selección", 
                    "Debes seleccionar un documento de la lista para consultarlo.", Alert.AlertType.WARNING);
                return Optional.empty();
            }
            return Optional.of(avanceSeleccionado);
        }

        return Optional.empty();
    }
}