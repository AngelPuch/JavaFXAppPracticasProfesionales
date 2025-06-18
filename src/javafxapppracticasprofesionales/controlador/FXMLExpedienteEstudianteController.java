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
import javafxapppracticasprofesionales.modelo.dao.EvaluacionDAO;
import javafxapppracticasprofesionales.modelo.pojo.Avance;
import javafxapppracticasprofesionales.modelo.pojo.Estudiante;
import javafxapppracticasprofesionales.modelo.pojo.Evaluacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;

public class FXMLExpedienteEstudianteController implements Initializable {

    private Label lblNombreEstudiante;
    private Label lblMatricula;
    private Label lblCorreo;
    @FXML
    private TableView<Avance> tvDocumentosIniciales;
    @FXML
    private TableColumn<Avance, String> colNombreDI;
    @FXML
    private TableColumn<Avance, String> colFechaDI;
    @FXML
    private TableColumn<Avance, String> colEstadoDI;
    @FXML
    private TableColumn<Avance, String> colComentariosDI; // NUEVA COLUMNA
    @FXML
    private TableView<Avance> tvReportes;
    @FXML
    private TableColumn<Avance, String> colNombreR;
    @FXML
    private TableColumn<Avance, String> colFechaR;
    @FXML
    private TableColumn<Avance, String> colEstadoR;
    @FXML
    private TableColumn<Avance, String> colComentariosR; // NUEVA COLUMNA
    @FXML
    private TableView<Avance> tvDocumentosFinales;
    @FXML
    private TableColumn<Avance, String> colNombreDF;
    @FXML
    private TableColumn<Avance, String> colFechaDF;
    @FXML
    private TableColumn<Avance, String> colEstadoDF;
    @FXML
    private TableColumn<Avance, String> colComentariosDF; // NUEVA COLUMNA
    @FXML
    private Button btnConsultar;
    private Button btnRegresar;
    @FXML
    private TabPane tpExpediente;
    @FXML
    private Tab tabEvaluaciones;
    @FXML
    private TableView<Evaluacion> tvEvaluaciones;
    @FXML
    private TableColumn<Evaluacion, String> colFechaEval;
    @FXML
    private TableColumn<Evaluacion, Float> colCalificacionEval;
    private TableColumn<Evaluacion, String> colTipoEval;
    private TableColumn<Evaluacion, String> colEvaluador;

    private int idExpediente;
    private Estudiante estudiante;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbMatricula;
    @FXML
    private Label lbSemestre;
    @FXML
    private Label lbCorreo;
    @FXML
    private Tab tabDocIniciales;
    @FXML
    private Tab tabReportes;
    @FXML
    private Tab tabDocFinales;
    @FXML
    private TableColumn<?, ?> colMotivoEval;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablas();
    }    

    public void inicializarValores(Estudiante estudiante, int idExpediente) {
        this.estudiante = estudiante;
        this.idExpediente = idExpediente;
        cargarDatosEstudiante();
        try {
            cargarTodosLosAvances();
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", 
                "No fue posible conectar con la base de datos. Inténtelo de nuevo o contacte al administrador.", 
                Alert.AlertType.ERROR);
        }
        
        tpExpediente.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null) {
                btnConsultar.setDisable(newTab.equals(tabEvaluaciones));
            }
        });
    }

    private void configurarTablas() {
        // Tabla Documentos Iniciales
        colNombreDI.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaDI.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstadoDI.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colComentariosDI.setCellValueFactory(new PropertyValueFactory<>("comentarios")); // NUEVA LÍNEA
        
        // Tabla Reportes
        colNombreR.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaR.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstadoR.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colComentariosR.setCellValueFactory(new PropertyValueFactory<>("comentarios")); // NUEVA LÍNEA
        
        // Tabla Documentos Finales
        colNombreDF.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaDF.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstadoDF.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colComentariosDF.setCellValueFactory(new PropertyValueFactory<>("comentarios")); // NUEVA LÍNEA

        // Tabla Evaluaciones (sin cambios)
        colFechaEval.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colCalificacionEval.setCellValueFactory(new PropertyValueFactory<>("calificacionTotal"));
        colTipoEval.setCellValueFactory(new PropertyValueFactory<>("nombreTipoEvaluacion"));
        colEvaluador.setCellValueFactory(new PropertyValueFactory<>("nombreEvaluador"));
    }

    private void cargarDatosEstudiante() {
        if (estudiante != null) {
             lblNombreEstudiante.setText(estudiante.getNombre());
             lblMatricula.setText(estudiante.getMatricula());
             lblCorreo.setText(estudiante.getCorreo());
        }
    }
    
    private void cargarTodosLosAvances() throws SQLException {
        if (idExpediente > 0) {
            tvDocumentosIniciales.setItems(FXCollections.observableArrayList(AvanceDAO.obtenerDocumentosInicio(idExpediente)));
            tvReportes.setItems(FXCollections.observableArrayList(AvanceDAO.obtenerReportes(idExpediente)));
            tvDocumentosFinales.setItems(FXCollections.observableArrayList(AvanceDAO.obtenerDocumentosFinales(idExpediente)));
            tvEvaluaciones.setItems(FXCollections.observableArrayList(EvaluacionDAO.obtenerEvaluacionesPorExpediente(idExpediente)));
        }
    }
    
    @FXML
    private void btnClicRegresar(ActionEvent event) {
        Stage escenario = (Stage) btnRegresar.getScene().getWindow();
        escenario.close();
    }
    
    @FXML
    private void btnClicConsultar(ActionEvent event) {
        Tab pestañaSeleccionada = tpExpediente.getSelectionModel().getSelectedItem();
        if (pestañaSeleccionada == null) {
            AlertaUtilidad.mostrarAlertaSimple("Sin Selección", "Debes seleccionar una pestaña (Documentos Iniciales, Reportes, etc.).", Alert.AlertType.WARNING);
            return;
        }

        TableView<Avance> tablaActiva = (TableView<Avance>) pestañaSeleccionada.getContent().lookup("TableView");
        Avance avanceSeleccionado = tablaActiva.getSelectionModel().getSelectedItem();

        if (avanceSeleccionado == null) {
            AlertaUtilidad.mostrarAlertaSimple("Sin Selección", "Debes seleccionar un documento de la lista para consultarlo.", Alert.AlertType.WARNING);
            return;
        }

        String rutaArchivo = avanceSeleccionado.getRutaArchivo();
        if (rutaArchivo == null || rutaArchivo.isEmpty()) {
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
            AlertaUtilidad.mostrarAlertaSimple("Error al abrir", "No se pudo abrir el archivo. Es posible que no tengas un programa instalado para visualizarlo.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            AlertaUtilidad.mostrarAlertaSimple("Error inesperado", "Ocurrió un error al intentar abrir el archivo.", Alert.AlertType.ERROR);
        }
    }
}