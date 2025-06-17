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
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.modelo.dao.DocumentoInicioDAO;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.modelo.pojo.TipoDocumento;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
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
    private ComboBox<TipoDocumento> cbTipoDocumento;
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

    // --- INICIO DE LA LÓGICA RECOMENDADA ---
    // Nombres de los directorios para organizar los archivos fuera del proyecto.
    private static final String DIRECTORIO_PRINCIPAL_APP = "PracticasProfesionales_Documentos";
    private static final String SUBDIRECTORIO_DOCUMENTOS = "DocumentosIniciales";
    // --- FIN DE LA LÓGICA RECOMENDADA ---

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTiposDocumento();
    }    
    
    public void inicializarDatos(int idEntrega, int idExpediente) {
        this.idEntrega = idEntrega;
        this.idExpediente = idExpediente;
    }

    private void cargarTiposDocumento() {
        cbTipoDocumento.setItems(FXCollections.observableArrayList(TipoDocumento.obtenerTiposDocumentoInicial()));
    }

    @FXML
    private void clicSeleccionarArchivo(ActionEvent event) {
        FileChooser dialogo = new FileChooser();
        dialogo.setTitle("Selecciona tu documento");
        Stage escenario = (Stage) btnSeleccionarArchivo.getScene().getWindow();
        archivoSeleccionado = dialogo.showOpenDialog(escenario);

        if (archivoSeleccionado != null) {
            tfRutaArchivo.setText(archivoSeleccionado.getAbsolutePath());
        }
    }

    @FXML
    private void clicAceptar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }
        
        try {
            // 1. Obtener la ruta de la carpeta personal del usuario (ej: "C:\Users\TuUsuario")
            String userHome = System.getProperty("user.home");

            // 2. Definir y crear la ruta completa del directorio de almacenamiento.
            // Esto crea una ruta como: "C:\Users\TuUsuario\PracticasProfesionales_Documentos\DocumentosIniciales"
            Path directorioPath = Paths.get(userHome, DIRECTORIO_PRINCIPAL_APP, SUBDIRECTORIO_DOCUMENTOS);
            Files.createDirectories(directorioPath); // Crea todos los directorios en la ruta si no existen.

            // 3. Crear un nombre de archivo único para evitar sobreescrituras.
            String nombreOriginal = archivoSeleccionado.getName();
            String extension = "";
            int i = nombreOriginal.lastIndexOf('.');
            if (i > 0) {
                extension = nombreOriginal.substring(i);
            }
            String nombreUnico = UUID.randomUUID().toString() + extension;
            
            // 4. Copiar el archivo al nuevo directorio.
            Path rutaDestino = directorioPath.resolve(nombreUnico);
            Files.copy(archivoSeleccionado.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            // 5. Preparar los datos para guardar en la BD. La ruta es ABSOLUTA.
            String nombreDocTipo = cbTipoDocumento.getValue().getNombre();
            String rutaParaBD = rutaDestino.toAbsolutePath().toString();
            
            // 6. Llamar al DAO con la nueva ruta absoluta y el nuevo nombre de archivo.
            ResultadoOperacion resultado = DocumentoInicioDAO.guardarDocumentoInicio(nombreDocTipo, rutaParaBD, nombreUnico, idEntrega, idExpediente);

            if (!resultado.isError()) {
                AlertaUtilidad.mostrarAlertaSimple("Operación exitosa", "El documento se ha guardado correctamente.", Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo guardar la información en la base de datos.", Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "Ocurrió un error con la base de datos: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Archivo", "No se pudo guardar el archivo en el directorio: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private boolean validarCampos() {
        if (cbTipoDocumento.getValue() == null) {
            AlertaUtilidad.mostrarAlertaSimple("Campo requerido", "Debes seleccionar un tipo de documento.", Alert.AlertType.WARNING);
            return false;
        }
        if (archivoSeleccionado == null) {
            AlertaUtilidad.mostrarAlertaSimple("Archivo requerido", "Debes seleccionar un archivo para subir.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Utilidad.getEscenarioComponente(cbTipoDocumento).close();
    }
}
