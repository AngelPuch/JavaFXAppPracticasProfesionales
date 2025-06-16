/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.ProyectoDAO;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import javafxapppracticasprofesionales.modelo.pojo.Proyecto;
import javafxapppracticasprofesionales.modelo.pojo.ResponsableProyecto;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class FXMLRegistrarProyectoController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextArea taDescripcion;
    @FXML
    private TextArea taObjetivo;
    @FXML
    private TextField tfCupos; // Este es tu nuevo campo para los cupos

    private OrganizacionVinculada organizacion;
    private ResponsableProyecto responsable;
    private INotificacion observador;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // El método para configurar el Spinner ya no es necesario.
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
        // Validar que ningún campo de texto principal esté vacío
        if (tfNombre.getText().isEmpty() || taDescripcion.getText().isEmpty()
                || taObjetivo.getText().isEmpty() || tfCupos.getText().isEmpty()) {
            AlertaUtilidad.mostrarAlertaSimple("Campos vacíos",
                    "Existen campos vacíos. Por favor, complétalos para continuar.", Alert.AlertType.WARNING);
            return false;
        }

        // Validar que los cupos sean un número entero positivo
        try {
            int cupos = Integer.parseInt(tfCupos.getText());
            if (cupos <= 0) {
                AlertaUtilidad.mostrarAlertaSimple("Datos inválidos",
                        "El número de cupos debe ser un entero positivo mayor a cero.", Alert.AlertType.WARNING);
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
        // Se convierte el texto a entero. La validación previa asegura que esto no fallará.
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