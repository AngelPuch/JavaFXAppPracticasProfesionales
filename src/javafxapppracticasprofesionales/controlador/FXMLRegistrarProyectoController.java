package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.ProyectoDAO;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import javafxapppracticasprofesionales.modelo.pojo.Proyecto;
import javafxapppracticasprofesionales.modelo.pojo.ResponsableProyecto;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLRegistrarProyectoController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextArea taDescripcion;
    @FXML
    private TextArea taObjetivo;
    @FXML
    private TextField tfCupos; 

    private OrganizacionVinculada organizacion;
    private ResponsableProyecto responsable;
    private INotificacion observador;
    @FXML
    private Label lbContadorCaracteresDescripcion;
    @FXML
    private Label lbContadorCaracteresObjetivo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTextAreaConContador(taDescripcion, lbContadorCaracteresDescripcion, 100);
        configurarTextAreaConContador(taObjetivo, lbContadorCaracteresObjetivo, 100);
    }
    
    private void configurarTextAreaConContador(TextArea textArea, Label contadorLabel, int maxLength) {
        if (textArea == null || contadorLabel == null) {
            return;
        }

        UnaryOperator<TextFormatter.Change> filtro = change -> {
            String newText = change.getControlNewText();
            return newText.length() > maxLength ? null : change;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filtro);
        textArea.setTextFormatter(textFormatter);

        contadorLabel.setText("0/" + maxLength + " Max.");
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            contadorLabel.setText(newValue.length() + "/" + maxLength + " Max.");
        });
    }

    public void inicializarInformacion(OrganizacionVinculada organizacion, ResponsableProyecto responsable,
            INotificacion observador) {
        this.organizacion = organizacion;
        this.responsable = responsable;
        this.observador = observador;
        
    }

    @FXML
    private void clicBtnAceptar(ActionEvent event) {
        if (validarCampos()) {
            registrarProyecto(obtenerProyectoNuevo());
        }
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        boolean confirmado = AlertaUtilidad.mostrarAlertaConfirmacion("Cancelar", null,
                "¿Estás seguro de que quieres cancelar?");
        if (confirmado) {
            cerrarVentana();
        }
    }

    private boolean validarCampos() {
        if (tfNombre.getText().isEmpty() || tfCupos.getText().isEmpty()) {
            AlertaUtilidad.mostrarAlertaSimple("Campos vacíos",
                    "Los campos marcados con un (*) no deben de ser vacíos. Por favor, complétalos para continuar.", Alert.AlertType.WARNING);
            return false;
        }

        try {
            int cupos = Integer.parseInt(tfCupos.getText());
            if (cupos <= 0) {
                AlertaUtilidad.mostrarAlertaSimple("Datos inválidos",
                        "El número de cupos debe ser un entero positivo mayor a cero.", Alert.AlertType.WARNING);
                return false;
            }
            if (cupos > 5) {
                AlertaUtilidad.mostrarAlertaSimple("Datos inválidos",
                        "El número límite de cupos es 5 por proyecto.", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            AlertaUtilidad.mostrarAlertaSimple("Datos inválidos",
                    "El valor en 'Cupos Disponibles' no es un número válido. Por favor, corrígelo.", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private Proyecto obtenerProyectoNuevo() {
        Proyecto nuevoProyecto = new Proyecto();
        nuevoProyecto.setNombre(tfNombre.getText());
        nuevoProyecto.setDescripcion(taDescripcion.getText());
        nuevoProyecto.setObjetivo(taObjetivo.getText());
        nuevoProyecto.setNumeroCupos(Integer.parseInt(tfCupos.getText()));
        nuevoProyecto.setOrganizacion(organizacion);
        nuevoProyecto.setResponsable(responsable);

        return nuevoProyecto;
    }

    private void registrarProyecto(Proyecto proyecto) {
        try {
            ResultadoOperacion resultado = ProyectoDAO.registrarProyecto(proyecto);
            if (!resultado.isError()) {
                AlertaUtilidad.mostrarAlertaSimple("Operación exitosa",
                        "Operación realizada correctamente.", Alert.AlertType.INFORMATION);
                observador.operacionExitosa();
                cerrarVentana();
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error en el registro",
                        resultado.getMensaje(), Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Sin Conexión",
                    "Se perdió la conexión. Inténtalo de nuevo.", Alert.AlertType.ERROR);
        }
    }

    private void cerrarVentana() {
        Utilidad.getEscenarioComponente(tfNombre).close();
    }

}