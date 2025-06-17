package javafxapppracticasprofesionales.controlador;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private TableColumn<Avance, String> colNombreDI;
    @FXML
    private TableColumn<Avance, String> colFechaDI;
    @FXML
    private TableColumn<Avance, String> colEstadoDI;
    @FXML
    private TableView<Avance> tvReportes;
    @FXML
    private TableColumn<Avance, String> colNombreR;
    @FXML
    private TableColumn<Avance, String> colFechaR;
    @FXML
    private TableColumn<Avance, String> colEstadoR;
    @FXML
    private TableView<Avance> tvDocumentosFinales;
    @FXML
    private TableColumn<Avance, String> colNombreDF;
    @FXML
    private TableColumn<Avance, String> colFechaDF;
    @FXML
    private TableColumn<Avance, String> colEstadoDF;
    @FXML
    private TableView<Evaluacion> tvEvaluaciones;
    @FXML
    private TableColumn<Evaluacion, String> colFechaEval;
    @FXML
    private TableColumn<Evaluacion, Float> colCalificacionEval;
    @FXML
    private TableColumn<Evaluacion, String> colMotivoEval;
    
    //Se agregan los fx:id a las pestañas para identificarlas en el método de consulta
    @FXML
    private Tab tabDocIniciales;
    @FXML
    private Tab tabReportes;
    @FXML
    private Tab tabDocFinales;
    @FXML
    private Tab tabEvaluaciones;

    private EstudianteConProyecto estudianteConProyecto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // La configuración se invoca desde inicializarInformacion
    }

    public void inicializarInformacion(EstudianteConProyecto estudiante) {
        this.estudianteConProyecto = estudiante;
        configurarTablas();
        cargarDatosEstudiante();
        cargarExpediente();
    }

    private void configurarTablas() {
        // Configuración para Documentos Iniciales
        colNombreDI.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaDI.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstadoDI.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Configuración para Reportes
        colNombreR.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaR.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstadoR.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Configuración para Documentos Finales
        colNombreDF.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaDF.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstadoDF.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Configuración para Evaluaciones
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
    private void btnClicRegresar(ActionEvent event) {
        Stage escenarioActual = (Stage) lbNombre.getScene().getWindow();
        escenarioActual.close();
    }

    @FXML
    private void btnClicConsultar(ActionEvent event) {
        Tab pestañaSeleccionada = tpExpediente.getSelectionModel().getSelectedItem();
        if (pestañaSeleccionada == null) {
            AlertaUtilidad.mostrarAlertaSimple("Sin Selección", "Debes seleccionar una pestaña (Documentos Iniciales, Reportes, etc.).", Alert.AlertType.WARNING);
            return;
        }

        // Verifica si la pestaña seleccionada es la de evaluaciones, que no tiene archivos para abrir.
        if (pestañaSeleccionada.equals(tabEvaluaciones)) {
            AlertaUtilidad.mostrarAlertaSimple("No aplicable", "Las evaluaciones no son documentos que se puedan abrir.", Alert.AlertType.INFORMATION);
            return;
        }

        // Si no es la de evaluaciones, es una de las de documentos (Avance).
        TableView<Avance> tablaActiva = (TableView<Avance>) pestañaSeleccionada.getContent();
        Avance avanceSeleccionado = tablaActiva.getSelectionModel().getSelectedItem();

        if (avanceSeleccionado == null) {
            AlertaUtilidad.mostrarAlertaSimple("Sin Selección", "Debes seleccionar un documento de la lista para consultarlo.", Alert.AlertType.WARNING);
            return;
        }

        String rutaArchivo = avanceSeleccionado.getRutaArchivo();
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            AlertaUtilidad.mostrarAlertaSimple("Archivo no disponible", "El documento seleccionado no tiene una ruta de archivo registrada.", Alert.AlertType.ERROR);
            return;
        }

        try {
            File archivo = new File(rutaArchivo);
            if (archivo.exists()) {
                Desktop.getDesktop().open(archivo);
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Archivo no encontrado", "El archivo no pudo ser localizado en la ruta: \n" + rutaArchivo, Alert.AlertType.ERROR);
            }
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error al abrir", "No se pudo abrir el archivo. Asegúrate de tener un programa compatible instalado.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            AlertaUtilidad.mostrarAlertaSimple("Error inesperado", "Ocurrió un error al intentar abrir el archivo: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}