
package javafxapppracticasprofesionales.controlador;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafxapppracticasprofesionales.modelo.dao.AvanceDAO;
import javafxapppracticasprofesionales.modelo.dao.EstudianteDAO;
import javafxapppracticasprofesionales.modelo.dao.ExpedienteDAO;
import javafxapppracticasprofesionales.modelo.pojo.Avance;
import javafxapppracticasprofesionales.modelo.pojo.Estudiante;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLMiExpedienteController implements Initializable {

    @FXML
    private Label lblNombreEstudiante;
    @FXML
    private Label lblMatricula;
    @FXML
    private Label lblCorreo;
    @FXML
    private Label lblHoras;
    @FXML
    private TabPane tpExpediente;
    @FXML
    private TableView<Avance> tvDocumentosIniciales;
    @FXML
    private TableColumn<?, ?> colNombreDI;
    @FXML
    private TableColumn<?, ?> colFechaDI;
    @FXML
    private TableColumn<?, ?> colEstadoDI;
    @FXML
    private TableView<Avance> tvReportes;
    @FXML
    private TableColumn<?, ?> colNombreR;
    @FXML
    private TableColumn<?, ?> colFechaR;
    @FXML
    private TableColumn<?, ?> colEstadoR;
    @FXML
    private TableView<Avance> tvDocumentosFinales;
    @FXML
    private TableColumn<?, ?> colNombreDF;
    @FXML
    private TableColumn<?, ?> colFechaDF;
    @FXML
    private TableColumn<?, ?> colEstadoDF;
    @FXML
    private Button btnConsultar;
    @FXML
    private Button btnCancelar;

    private int idExpediente;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            int idUsuario = SesionUsuario.getInstancia().getUsuarioLogueado().getIdUsuario();
            // CORRECCIÓN: Se obtiene el idEstudiante a través del idUsuario de la sesión
            Estudiante estudianteLogueado = EstudianteDAO.obtenerEstudiantePorIdUsuario(idUsuario);
            if (estudianteLogueado != null) {
                //this.idExpediente = ExpedienteDAO.obtenerIdExpedienteActivo(estudianteLogueado.getIdEstudiante());
            }
            
            configurarTablas();
            cargarDatosEstudiante(estudianteLogueado); // Se pasa el objeto estudiante
            cargarTodosLosAvances();
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", 
                "No fue posible conectar con la base de datos. Inténtelo de nuevo o contacte al administrador.", 
                Alert.AlertType.ERROR);
        }
    }    

    @FXML
    private void btnClicConsultar(ActionEvent event) {
        Tab pestañaSeleccionada = tpExpediente.getSelectionModel().getSelectedItem();
        if (pestañaSeleccionada == null) {
            AlertaUtilidad.mostrarAlertaSimple("Sin Selección", "Debes seleccionar una pestaña (Documentos Iniciales, Reportes, etc.).", Alert.AlertType.WARNING);
            return;
        }

        TableView<Avance> tablaActiva = (TableView<Avance>) pestañaSeleccionada.getContent();
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

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        Utilidad.getEscenarioComponente(btnCancelar).close();
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
            ArrayList<Avance> docsInicialesList = AvanceDAO.obtenerDocumentosInicio(idExpediente);
            ObservableList<Avance> docsInicialesObservable = FXCollections.observableArrayList(docsInicialesList);
            tvDocumentosIniciales.setItems(docsInicialesObservable);
            
            ArrayList<Avance> reportesList = AvanceDAO.obtenerReportes(idExpediente);
            ObservableList<Avance> reportesObservable = FXCollections.observableArrayList(reportesList);
            tvReportes.setItems(reportesObservable);

            ArrayList<Avance> docsFinalesList = AvanceDAO.obtenerDocumentosFinales(idExpediente);
            ObservableList<Avance> docsFinalesObservable = FXCollections.observableArrayList(docsFinalesList);
            tvDocumentosFinales.setItems(docsFinalesObservable);
        }
    }
    
}
