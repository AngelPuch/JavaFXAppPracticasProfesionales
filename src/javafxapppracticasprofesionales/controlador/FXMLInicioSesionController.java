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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.IControladorPrincipal;
import javafxapppracticasprofesionales.modelo.dao.InicioSesionDAO;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;


public class FXMLInicioSesionController implements Initializable {

    @FXML
    private PasswordField pfPassword;
    @FXML
    private TextField tfUsuario;
    @FXML
    private Label lbErrorUsuario;
    @FXML
    private Label lbErrorPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnClicIniciarSesion(ActionEvent event) {
        String username = tfUsuario.getText();
        String password = pfPassword.getText();
        
        if (validarCampos(username, password)) {
            validarCredenciales(username, password);
        }
    }
    
    private boolean validarCampos(String username, String password) {
        lbErrorUsuario.setText("");
        lbErrorPassword.setText("");
        boolean camposValidos = true;
        if (username.isEmpty()) {
            lbErrorUsuario.setText("Usuario obligatorio.");
            camposValidos = false;
        }
        if (password.isEmpty()) {
            lbErrorPassword.setText("Contraseña obligatoria");
            camposValidos = false;
        }
        return camposValidos;
    }
    
    private void validarCredenciales(String username, String password) {
        try {
            Usuario usuarioSesion = InicioSesionDAO.verificarUsuario(username, password); 
            if (usuarioSesion != null) {
                SesionUsuario.getInstancia().setUsuarioLogueado(usuarioSesion);
                AlertaUtilidad.mostrarAlertaSimple("Credenciales correctas", 
                        "Bienvenido(a) " + usuarioSesion.getNombre() + " al sistema. Rol: " + usuarioSesion.getRolPrincipal(), 
                        Alert.AlertType.INFORMATION);
                irPantallaPrincipal(usuarioSesion);
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Credenciales incorrectas", 
                        "Usuario y/o contraseña incorrectos, por favor verifica la información", Alert.AlertType.WARNING);
            }
        } catch (SQLException ex) {
            AlertaUtilidad.mostrarAlertaSimple("Problemas de conexión", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void irPantallaPrincipal(Usuario usuario) {
        try {
            String rol = usuario.getRolPrincipal();
            String rutaFXML = obtenerRutaFXMLPorRol(rol);

            if (rutaFXML == null) {
                AlertaUtilidad.mostrarAlertaSimple("Rol no reconocido", "No hay una pantalla principal para el rol: " + rol, Alert.AlertType.ERROR);
                return;
            }

            Stage escenarioPrincipal = (Stage) tfUsuario.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent vista = loader.load();
            IControladorPrincipal controlador = loader.getController();
            controlador.inicializarInformacion(usuario);

            Scene nuevaEscena = new Scene(vista);
            escenarioPrincipal.setScene(nuevaEscena);
            escenarioPrincipal.setTitle("Pantalla Principal");
            escenarioPrincipal.centerOnScreen();
            escenarioPrincipal.show();

        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Carga", "No se puede mostrar la pantalla principal.", Alert.AlertType.ERROR);
            e.printStackTrace();
        } catch (ClassCastException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Implementación", "El controlador de la vista no sigue el formato esperado (IControladorPrincipal).", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private String obtenerRutaFXMLPorRol(String rol) {
        switch (rol.toLowerCase()) {
            case "coordinador":
                return "/javafxapppracticasprofesionales/vista/FXMLPrincipalCoordinador.fxml";
            case "profesor":
                return "/javafxapppracticasprofesionales/vista/FXMLPrincipalProfesor.fxml";
            case "estudiante":
                return "/javafxapppracticasprofesionales/vista/FXMLPrincipalEstudiante.fxml";
            case "evaluador":
                return "/javafxapppracticasprofesionales/vista/FXMLPrincipalEvaluador.fxml";
            default:
                return null;
        }
    }
}