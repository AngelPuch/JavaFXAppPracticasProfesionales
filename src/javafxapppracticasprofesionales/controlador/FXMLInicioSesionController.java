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
import javafxapppracticasprofesionales.JavaFXAppPracticasProfesionales;
import javafxapppracticasprofesionales.modelo.dao.InicioSesionDAO;
import javafxapppracticasprofesionales.modelo.pojo.Usuario;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;


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
            Usuario usuarioSesion = InicioSesionDAO.verificarUsuario(username, password); // Cambio aquí
            if (usuarioSesion != null) {
                SesionUsuario.getInstancia().setUsuarioLogueado(usuarioSesion);
                AlertaUtilidad.mostrarAlertaSimple("Credenciales correctas", 
                        "Bienvenido(a) " + usuarioSesion.getUsername() + " al sistema. Roles: " + usuarioSesion.getRoles(), Alert.AlertType.INFORMATION);
                irPantallaPrincipal(usuarioSesion);
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Credenciales incorrectas", 
                        "Usuario y/o contraseña incorrectos, por favor verifica la información", Alert.AlertType.WARNING);
            }
        } catch (SQLException ex) {
            AlertaUtilidad.mostrarAlertaSimple("Problemas de conexión", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void irPantallaPrincipal(Usuario usuarioSesion) {
        try {
            Stage escenarioBase = (Stage) pfPassword.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(JavaFXAppPracticasProfesionales.class.getResource("vista/FXMLPrincipalCoordinador.fxml"));
            Parent vista = cargador.load();
            
            FXMLPrincipalCoordinadorController controlador = cargador.getController();
            controlador.inicializarInformacion(usuarioSesion);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Home");
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}