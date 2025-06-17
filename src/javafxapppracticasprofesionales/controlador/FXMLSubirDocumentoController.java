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
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.DocumentoInicioDAO;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.modelo.pojo.TipoDocumento;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;

/** 
* Project: JavaFXAppPracticasProfesionales 
* File: FXMLSubirDocumentoController.java 
* Author: Jose Luis Silva Gomez 
* Date: 2025-06-16 
* Description: Brief description of the file's purpose. 
*/
public class FXMLSubirDocumentoController implements Initializable {

    
    @FXML
    private TextField tfRutaArchivo;
    @FXML
    private Button btnSeleccionarArchivo;
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnCancelar;
    private int idEntrega;
    private int idExpediente;
    private File archivoSeleccionado;
    private static final String DIRECTORIO_PRINCIPAL_APP = "PracticasProfesionales_Documentos";
    private static final String SUBDIRECTORIO_DOCUMENTOS = "DocumentosIniciales";
    @FXML
    private TextField tfNombreArchivo;
    private INotificacion observador;
    @FXML
    private Label lbTipoDocumento;
    private String tipoDocumento;
    private String nombreUsuario = SesionUsuario.getInstancia().getUsuarioLogueado().getNombre();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void inicializarDatos(int idEntrega, int idExpediente, String tipoDocumento, INotificacion observador) {
        this.idEntrega = idEntrega;
        this.idExpediente = idExpediente;
        this.tipoDocumento = tipoDocumento;
        this.observador = observador;
        lbTipoDocumento.setText(tipoDocumento);
    }

    @FXML
    private void clicSeleccionarArchivo(ActionEvent event) {
        FileChooser dialogo = new FileChooser();
        Stage escenario = (Stage) btnSeleccionarArchivo.getScene().getWindow();
        archivoSeleccionado = dialogo.showOpenDialog(escenario);

        if (archivoSeleccionado != null) {
            tfRutaArchivo.setText(archivoSeleccionado.getAbsolutePath());
            tfNombreArchivo.setText(tipoDocumento + nombreUsuario);
        }
    }

     @FXML
    private void clicAceptar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }
        
        try {
            String nombreOriginal = archivoSeleccionado.getName();
            String extension = "";
            int i = nombreOriginal.lastIndexOf('.');
            if (i > 0) {
                extension = nombreOriginal.substring(i); 
            }
            String nombreFinalArchivo = tipoDocumento + "_" + nombreUsuario + extension;
            
            String userHome = System.getProperty("user.home");
            Path directorioPath = Paths.get(userHome, DIRECTORIO_PRINCIPAL_APP, SUBDIRECTORIO_DOCUMENTOS);
            Files.createDirectories(directorioPath);

            Path rutaDestino = directorioPath.resolve(nombreFinalArchivo);
            if (Files.exists(rutaDestino)) {
                AlertaUtilidad.mostrarAlertaSimple("Nombre duplicado", 
                        "Ya existe un archivo con el nombre '" + nombreFinalArchivo + "'. Por favor, elige otro nombre.", 
                        Alert.AlertType.WARNING);
                return; 
            }
            
            Files.copy(archivoSeleccionado.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            String nombreDocTipo = tipoDocumento;
            String rutaParaBD = rutaDestino.toAbsolutePath().toString();
            
            ResultadoOperacion resultado = DocumentoInicioDAO.guardarDocumentoInicio(nombreDocTipo, rutaParaBD, nombreFinalArchivo, idEntrega, idExpediente);

            if (!resultado.isError()) {
                AlertaUtilidad.mostrarAlertaSimple("Operación exitosa", "Operación realizada correctamente.", Alert.AlertType.INFORMATION);
                observador.operacionExitosa();
                cerrarVentana();
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error", resultado.getMensaje(), Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "Ocurrió un error con la base de datos: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Archivo", "No se pudo guardar el archivo en el directorio: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private boolean validarCampos() {
        if (archivoSeleccionado == null) {
            AlertaUtilidad.mostrarAlertaSimple("Archivo requerido", "Debes seleccionar un archivo para subir.", Alert.AlertType.WARNING);
            return false;
        }
        if (tfNombreArchivo.getText().trim().isEmpty()) {
            AlertaUtilidad.mostrarAlertaSimple("Campo requerido", "Debes ingresar un nombre para el archivo.", Alert.AlertType.WARNING);
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
        Utilidad.getEscenarioComponente(tfNombreArchivo).close();
    }
}
