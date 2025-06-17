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
            Usuario usuarioSesion = InicioSesionDAO.verificarUsuario(username, password); // Cambio aquí
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
        Stage escenarioPrincipal = (Stage) tfUsuario.getScene().getWindow();
        escenarioPrincipal.setTitle("Pantalla Principal");

        // Se asume que el primer rol es el principal para la redirección
        String primerRol = usuario.getRolPrincipal();

        try {
            FXMLLoader loader;

            switch (primerRol.toLowerCase()) {
                case "coordinador":
                    loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLPrincipalCoordinador.fxml"));
                    Parent vistaCoordinador = loader.load();
                    FXMLPrincipalCoordinadorController controllerCoordinador = loader.getController();
                    controllerCoordinador.inicializarInformacion(usuario);
                    Scene escenaCoordinador = new Scene(vistaCoordinador);
                    escenarioPrincipal.setScene(escenaCoordinador);
                    break;

                case "profesor":
                    loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLPrincipalProfesor.fxml"));
                    Parent vistaProfesor = loader.load();
                    FXMLPrincipalProfesorController controllerProfesor = loader.getController();
                    controllerProfesor.inicializarInformacion(usuario);
                    Scene escenaProfesor = new Scene(vistaProfesor);
                    escenarioPrincipal.setScene(escenaProfesor);
                    break;

                case "estudiante":
                    loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLPrincipalEstudiante.fxml"));
                    Parent vistaEstudiante = loader.load();
                    FXMLPrincipalEstudianteController controllerEstudiante = loader.getController();
                    controllerEstudiante.inicializarInformacion(usuario);
                    Scene escenaEstudiante = new Scene(vistaEstudiante);
                    escenarioPrincipal.setScene(escenaEstudiante);
                    break;

                // CASO AÑADIDO PARA EL EVALUADOR
                case "evaluador":
                    loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLPrincipalEvaluador.fxml"));
                    Parent vistaEvaluador = loader.load();
                    FXMLPrincipalEvaluadorController controllerEvaluador = loader.getController();
                    controllerEvaluador.inicializarInformacion(usuario);
                    Scene escenaEvaluador = new Scene(vistaEvaluador);
                    escenarioPrincipal.setScene(escenaEvaluador);
                    break;

                default:
                    AlertaUtilidad.mostrarAlertaSimple("Rol no reconocido", "No hay una pantalla principal para el rol: " + primerRol, Alert.AlertType.ERROR);
                    return;
            }

            escenarioPrincipal.show();

        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se puede mostrar la pantalla principal.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

}