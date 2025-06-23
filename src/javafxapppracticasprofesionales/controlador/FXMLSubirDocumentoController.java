package javafxapppracticasprofesionales.controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.DocumentoInicioDAO;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLSubirDocumentoController implements Initializable {

    
    
    @FXML
    private Button btnSeleccionarArchivo;
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnCancelar;
    private Entrega entregaSeleccionada;
    private int idExpediente;
    private File archivoSeleccionado;
    private static final String DIRECTORIO_PRINCIPAL_APP = "PracticasProfesionales_Documentos";
    private static final String SUBDIRECTORIO_DOCUMENTOS = "DocumentosIniciales";
    private INotificacion observador;
    @FXML
    private Label lbTipoDocumento;
    private String tipoDocumento;
    private String nombreUsuario = SesionUsuario.getInstancia().getUsuarioLogueado().getNombre();
    @FXML
    private TextArea taDescripcion;
    @FXML
    private Label lbNombreArchivo;
    @FXML
    private TextArea taRutaArchivo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void inicializarDatos(Entrega entregaSeleccionada, int idExpediente, String tipoDocumento, INotificacion observador) {
        this.entregaSeleccionada = entregaSeleccionada;
        this.idExpediente = idExpediente;
        this.tipoDocumento = tipoDocumento;
        this.observador = observador;
        lbTipoDocumento.setText(tipoDocumento);
        taDescripcion.setText(entregaSeleccionada.getDescripcion());
    }

    @FXML
    private void clicSeleccionarArchivo(ActionEvent event) {
        FileChooser dialogo = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
            "Documentos Soportados (*.pdf, *.doc, *.docx)", "*.pdf", "*.doc", "*.docx");
        dialogo.getExtensionFilters().add(extFilter);
        Stage escenario = (Stage) btnSeleccionarArchivo.getScene().getWindow();
        archivoSeleccionado = dialogo.showOpenDialog(escenario);

        if (archivoSeleccionado != null) {
            taRutaArchivo.setText(archivoSeleccionado.getAbsolutePath());
            lbNombreArchivo.setText(tipoDocumento + "_" + nombreUsuario);
        }
    }

     @FXML
    private void clicAceptar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }
        String nombreFinalArchivo;
        Path rutaDestino;

        try {
            String nombreOriginal = archivoSeleccionado.getName();
            String extension = "";
            int i = nombreOriginal.lastIndexOf('.');
            if (i > 0) {
                extension = nombreOriginal.substring(i); 
            }
            nombreFinalArchivo = tipoDocumento + "_" + nombreUsuario + extension;
            
            String userHome = System.getProperty("user.home");
            Path directorioPath = Paths.get(userHome, DIRECTORIO_PRINCIPAL_APP, SUBDIRECTORIO_DOCUMENTOS);
            rutaDestino = directorioPath.resolve(nombreFinalArchivo);

            if (Files.exists(rutaDestino)) {
                AlertaUtilidad.mostrarAlertaSimple("Nombre duplicado", 
                        "Ya existe un archivo con el nombre '" + nombreFinalArchivo + "'. Por favor, elige otro nombre.", 
                        Alert.AlertType.WARNING);
                return; 
            }
            
            String rutaParaBD = rutaDestino.toAbsolutePath().toString();
            ResultadoOperacion resultado = DocumentoInicioDAO.guardarDocumentoInicio(tipoDocumento, rutaParaBD, nombreFinalArchivo, entregaSeleccionada.getIdEntrega(), idExpediente);

            if (!resultado.isError()) {
                Files.createDirectories(directorioPath); 
                Files.copy(archivoSeleccionado.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
                
                AlertaUtilidad.mostrarAlertaSimple("Operación exitosa", "Operación realizada correctamente.", Alert.AlertType.INFORMATION);
                observador.operacionExitosa();
                cerrarVentana();
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error", "No se puede realizar la entrega de tu documento debido a que no tienes un expediente asignado.", Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Base de Datos", "No se puede realizar la entrega de tu documento debido a que no tienes un expediente asignado." , Alert.AlertType.ERROR);
            cerrarVentana();
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Archivo", "No se pudo preparar o guardar el archivo en el directorio: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private boolean validarCampos() {
        if (archivoSeleccionado == null) {
            AlertaUtilidad.mostrarAlertaSimple("Archivo requerido", "Debes seleccionar un archivo para subir.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        boolean confirmado = AlertaUtilidad.mostrarAlertaConfirmacion("Cancelar", null,
                "¿Estás seguro de que quieres cancelar?");
        if (confirmado) {
            cerrarVentana();
        }
    }
    
    private void cerrarVentana() {
        Utilidad.getEscenarioComponente(lbNombreArchivo).close();
    }
}
