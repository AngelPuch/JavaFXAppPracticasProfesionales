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
import javafxapppracticasprofesionales.modelo.dao.AvanceDAO;
import javafxapppracticasprofesionales.modelo.dao.EstudianteDAO;
import javafxapppracticasprofesionales.modelo.dao.ExpedienteDAO;
import javafxapppracticasprofesionales.modelo.dao.EvaluacionDAO; 
import javafxapppracticasprofesionales.modelo.pojo.Avance;
import javafxapppracticasprofesionales.modelo.pojo.Estudiante;
import javafxapppracticasprofesionales.modelo.pojo.Evaluacion; 
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import static javafxapppracticasprofesionales.utilidad.Utilidad.abrirDocumento;

public class FXMLMiExpedienteController implements Initializable {

    @FXML
    private Label lblNombreEstudiante;
    @FXML
    private Label lblMatricula;
    @FXML
    private Label lblCorreo;
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
    private TableColumn colComentariosDI;
    @FXML
    private TableView<Avance> tvReportes;
    @FXML
    private TableColumn colNombreR;
    @FXML
    private TableColumn colFechaR;
    @FXML
    private TableColumn colEstadoR;
    @FXML
    private TableColumn colComentariosR;
    @FXML
    private TableView<Avance> tvDocumentosFinales;
    @FXML
    private TableColumn colNombreDF;
    @FXML
    private TableColumn colFechaDF;
    @FXML
    private TableColumn colEstadoDF;
    @FXML
    private TableColumn colComentariosDF;
    @FXML
    private Tab tabEvaluaciones;
    @FXML
    private TableView<Evaluacion> tvEvaluaciones;
    @FXML
    private TableColumn colFechaEval;
    @FXML
    private TableColumn colCalificacionEval;
    @FXML
    private TableColumn colTipoEval;
    @FXML
    private TableColumn colEvaluador;
    @FXML
    private Button btnConsultar;
 
    private int idExpediente;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            int idUsuario = SesionUsuario.getInstancia().getUsuarioLogueado().getIdUsuario();
            Estudiante estudianteLogueado = EstudianteDAO.obtenerEstudiantePorIdUsuario(idUsuario);
            if (estudianteLogueado != null) {
                this.idExpediente = ExpedienteDAO.obtenerIdExpedienteActivo(estudianteLogueado.getIdEstudiante());
            }
            
            configurarTablas();
            cargarDatosEstudiante(estudianteLogueado);
            cargarTodosLosAvances();
            
            tpExpediente.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
                if (newTab != null) {
                    btnConsultar.setDisable(newTab.equals(tabEvaluaciones));
                }
            });
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", 
                "No fue posible conectar con la base de datos. Inténtelo de nuevo o contacte al administrador.", 
                Alert.AlertType.ERROR);
        }
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
    
    private void configurarTablas() {
        colNombreDI.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaDI.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstadoDI.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colComentariosDI.setCellValueFactory(new PropertyValueFactory<>("comentarios")); 
        
        
        colNombreR.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaR.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstadoR.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colComentariosR.setCellValueFactory(new PropertyValueFactory<>("comentarios"));
        
        
        colNombreDF.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaDF.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstadoDF.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colComentariosDF.setCellValueFactory(new PropertyValueFactory<>("comentarios"));
        
        colFechaEval.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colCalificacionEval.setCellValueFactory(new PropertyValueFactory<>("calificacionTotal"));
        colTipoEval.setCellValueFactory(new PropertyValueFactory<>("nombreTipoEvaluacion"));
        colEvaluador.setCellValueFactory(new PropertyValueFactory<>("nombreEvaluador"));
    }
    
    private void cargarDatosEstudiante(Estudiante estudianteLogueado) {
        if (estudianteLogueado != null) {
             lblNombreEstudiante.setText(estudianteLogueado.getNombre());
             lblMatricula.setText(estudianteLogueado.getMatricula());
             lblCorreo.setText(estudianteLogueado.getCorreo());
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
}