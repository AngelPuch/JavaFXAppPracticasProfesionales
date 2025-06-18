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
import javafxapppracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import javafxapppracticasprofesionales.modelo.pojo.ResponsableProyecto;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLRegistrarOrganizacionVinculadaController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfDireccion;
    @FXML
    private Label lbTelefonoError;
    private INotificacion observador;
    private OrganizacionVinculada organizacionVinculada;
    @FXML
    private Label lbContadorCaracteresNombre;
    @FXML
    private Label lbContadorCaracteresDireccion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarInformacion(OrganizacionVinculada organizacionVinculada, INotificacion observador) {
        this.organizacionVinculada = organizacionVinculada;
        this.observador = observador;
        Utilidad.configurarTextAreaConContador(tfNombre, lbContadorCaracteresNombre, 45);
        Utilidad.configurarTextAreaConContador(tfDireccion, lbContadorCaracteresDireccion, 100);
    }

    @FXML
    private void btnAceptar(ActionEvent event) {
        if (validarCampos()) {
            OrganizacionVinculada organizacion = obtenerOrganizacionNueva();
            boolean confirmacionAceptada = mostrarVentanaConfirmacion(organizacion);

            if (confirmacionAceptada) {
                registrarOrganizacion(organizacion);
            }
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

    @FXML
    private void btnCancelar(ActionEvent event) {
        boolean confirmado = AlertaUtilidad.mostrarAlertaConfirmacion("Cancelar", null,
                "¿Estás seguro de que quieres cancelar?");
        if (confirmado) {
            cerrarVentana();
        }
    }
    
    private boolean validarCampos() {
        boolean esValido = true;
        if (tfNombre.getText().trim().isEmpty()) {
            AlertaUtilidad.mostrarAlertaSimple("Campos vacíos",
                    "Los campos marcados con un (*) no deben de ser vacíos. Por favor, complétalos para continuar.", Alert.AlertType.WARNING);
            return false;
        }

        String telefono = tfTelefono.getText().trim();
        if (!telefono.isEmpty()) {
            if (!telefono.matches("\\d{10}")) {
                lbTelefonoError.setText("*Teléfono inválido");
                esValido = false;
            } else {
                lbTelefonoError.setText("");
            }
        } else {
            lbTelefonoError.setText("");
        }

        return esValido;
    }
    
    private OrganizacionVinculada obtenerOrganizacionNueva() {
        OrganizacionVinculada nuevaOrganizacion = new OrganizacionVinculada();
        nuevaOrganizacion.setNombre(tfNombre.getText().trim());
        nuevaOrganizacion.setDireccion(tfDireccion.getText().trim());
        nuevaOrganizacion.setTelefono(tfTelefono.getText().trim());
        
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
