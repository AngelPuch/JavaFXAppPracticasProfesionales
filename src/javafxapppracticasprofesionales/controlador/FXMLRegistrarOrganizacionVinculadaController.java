package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.ValidacionUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: FXMLRegistrarOrganizacionVinculadaController.java 
    * Autor: Rodrigo Luna Vázquez 
    * Fecha: 12/06/2025
*/
public class FXMLRegistrarOrganizacionVinculadaController implements Initializable {
    @FXML
    private Label lbContadorCaracteresNombre;
    @FXML
    private Label lbContadorCaracteresCalle;
    @FXML
    private Label lbContadorCaracteresNumero;
    @FXML
    private Label lbContadorCaracteresColonia;
    @FXML
    private Label lbContadorCaracteresCodigoPostal;
    @FXML
    private Label lbContadorCaracteresMunicipio;
    @FXML
    private Label lbContadorCaracteresEstado;
    @FXML
    private Label lbTitulo;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfCalle;
    @FXML
    private TextField tfNumero;
    @FXML
    private TextField tfColonia;
    @FXML
    private TextField tfCodigoPostal;
    @FXML
    private Label lbCodigoPostalError;
    @FXML
    private TextField tfMunicipio;
    @FXML
    private TextField tfEstado;
    @FXML
    private TextField tfTelefono;
    @FXML
    private Label lbTelefonoError;
    private INotificacion observador;
    private OrganizacionVinculada organizacionVinculada;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarInformacion(OrganizacionVinculada organizacionVinculada, INotificacion observador) {
        this.organizacionVinculada = organizacionVinculada;
        this.observador = observador;
        Utilidad.configurarTextAreaConContador(tfNombre, lbContadorCaracteresNombre, 80);
        Utilidad.configurarTextAreaConContador(tfCalle, lbContadorCaracteresCalle, 80);
        Utilidad.configurarTextAreaConContador(tfNumero, lbContadorCaracteresNumero, 10);
        Utilidad.configurarTextAreaConContador(tfColonia, lbContadorCaracteresColonia, 80);
        Utilidad.configurarTextAreaConContador(tfMunicipio, lbContadorCaracteresMunicipio, 50);
        Utilidad.configurarTextAreaConContador(tfEstado, lbContadorCaracteresEstado, 50);
    }

    @FXML
    private void btnClicGuardar(ActionEvent event) {
        if (validarCampos()) {
            OrganizacionVinculada organizacion = obtenerOrganizacionNueva();
            boolean confirmacionAceptada = mostrarVentanaConfirmacion(organizacion);

            if (confirmacionAceptada) {
                registrarOrganizacion(organizacion);
            }
        }
    }
    
    @FXML
    private void btnClicCancelar(ActionEvent event) {
        boolean confirmado = AlertaUtilidad.mostrarAlertaConfirmacion("Cancelar", null,
                "¿Estás seguro de que quieres cancelar?");
        if (confirmado) {
            cerrarVentana();
        }
    }
    
    private boolean mostrarVentanaConfirmacion(OrganizacionVinculada organizacion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLConfirmacionRegistroOrganizacion.fxml"));
            Parent vista = loader.load();
            
            FXMLConfirmacionRegistroOrganizacionController controller = loader.getController();
            controller.inicializarDatos(organizacion);
            
            Stage escenario = new Stage();
            escenario.setTitle("Confirmación de Datos");
            escenario.setScene(new Scene(vista));
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait(); 
            
            return controller.isConfirmado();
            
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo abrir la ventana de confirmación.", Alert.AlertType.ERROR);
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean validarCampos() {
        lbTelefonoError.setText("");
        lbCodigoPostalError.setText("");
        boolean esValido = true;
        
        if (!ValidacionUtilidad.isCampoTextoValido(tfNombre, tfCalle, tfNumero, tfColonia, 
                tfCodigoPostal, tfEstado, tfMunicipio)) {
            return false;
        }

        String telefono = tfTelefono.getText().trim();
        if (!telefono.isEmpty() && !ValidacionUtilidad.validarTelefono(telefono)) {
            lbTelefonoError.setText("Teléfono inválido (10 dígitos)");
            esValido = false;
        }

        String codigoPostal = tfCodigoPostal.getText().trim();
        if (!ValidacionUtilidad.validarCodigoPostal(codigoPostal)) {
            lbCodigoPostalError.setText("CP inválido (5 dígitos)");
            esValido = false;
        }
        
        return esValido;
    }
    
    private OrganizacionVinculada obtenerOrganizacionNueva() {
        OrganizacionVinculada nuevaOrganizacion = new OrganizacionVinculada();
        nuevaOrganizacion.setNombre(tfNombre.getText().trim());
        nuevaOrganizacion.setTelefono(tfTelefono.getText().trim());
        nuevaOrganizacion.setCalle(tfCalle.getText().trim());
        nuevaOrganizacion.setNumero(tfNumero.getText().trim());
        nuevaOrganizacion.setColonia(tfColonia.getText().trim());
        nuevaOrganizacion.setCodigoPostal(tfCodigoPostal.getText().trim());
        nuevaOrganizacion.setMunicipio(tfMunicipio.getText().trim());
        nuevaOrganizacion.setEstado(tfEstado.getText().trim());
        
        return nuevaOrganizacion;
    }
    
    private void registrarOrganizacion(OrganizacionVinculada organizacionVinculada) {
        try {
            ResultadoOperacion resultado = OrganizacionVinculadaDAO.registrarOrganizacion(organizacionVinculada);
            if (!resultado.isError()) {
                AlertaUtilidad.mostrarAlertaSimple("Operación exitosa",
                        "Operación realizada correctamente.", Alert.AlertType.INFORMATION);
                if (observador != null) {
                    observador.operacionExitosa();
                }
                
                cerrarVentana();
                
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error en el registro",
                        resultado.getMensaje(), Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Sin Conexión",
                    "Se perdió la conexión. Inténtalo de nuevo. Causa: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    private void cerrarVentana() {
        Utilidad.getEscenarioComponente(tfNombre).close();
    }
}