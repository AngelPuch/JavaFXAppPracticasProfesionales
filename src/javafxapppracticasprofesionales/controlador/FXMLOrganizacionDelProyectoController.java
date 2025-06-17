
package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLOrganizacionDelProyectoController implements Initializable {

    @FXML
    private TableView<OrganizacionVinculada> tvOrganizaciones;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colTelefono;
    private ObservableList<OrganizacionVinculada> listaOrganizaciones;
    INotificacion observador;
    boolean isRegistrarProyecto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarOrganizaciones();
    }    
    
    public void inicializarInformacion(INotificacion obervador, boolean isRegistrarProyecto) {
        this.observador = obervador;
        this.isRegistrarProyecto = isRegistrarProyecto;
    }

    @FXML
    private void clicBtnContinuar(ActionEvent event) {
        OrganizacionVinculada organizacionSeleccionada = tvOrganizaciones.getSelectionModel().getSelectedItem();
        if (organizacionSeleccionada != null) {
            if (isRegistrarProyecto) {
                irSiguientePantallaRegistrarProyecto(organizacionSeleccionada);
            } else {
                irSiguientePantallaRegistrarResponsable(organizacionSeleccionada);
            }
        } else {
            AlertaUtilidad.mostrarAlertaSimple("Selección requerida", 
                    "Debes seleccionar una organización para continuar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
    }
    
    private void cargarOrganizaciones() {
        listaOrganizaciones = FXCollections.observableArrayList();
        try {
            listaOrganizaciones.addAll(OrganizacionVinculadaDAO.obtenerOrganizacionesVinculadas());
            tvOrganizaciones.setItems(listaOrganizaciones);
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Sin Conexión", "Se perdió la conexión. Inténtalo de nuevo", Alert.AlertType.ERROR);
            cerrarVentana();
        }
    }
    
    private void cerrarVentana(){
        Utilidad.getEscenarioComponente(tvOrganizaciones).close();
    }
    
    private void irSiguientePantallaRegistrarProyecto(OrganizacionVinculada organizacionSeleccionada) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLResponsableDelProyecto.fxml"));
            Parent vista = loader.load();

            FXMLResponsableDelProyectoController controller = loader.getController();
            controller.inicializarInformacion(organizacionSeleccionada, observador);

            Stage escenario = new Stage();
            escenario.setTitle("Registrar Nuevo Proyecto - Paso 2");
            escenario.setScene(new Scene(vista));
            cerrarVentana();
            escenario.show();

        } catch (IOException ex) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo abrir la siguiente ventana." + ex.getMessage(), Alert.AlertType.ERROR);
        }
    } 
    
    private void irSiguientePantallaRegistrarResponsable(OrganizacionVinculada organizacionSeleccionada) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLRegistrarResponsable.fxml"));
            Parent vista = loader.load();
            
            FXMLRegistrarResponsableController controller = loader.getController();
            controller.inicializarInformacion(organizacionSeleccionada, observador, null , false);
            
            Stage escenario = new Stage();
            escenario.setTitle("Registrar Responsable del Proyecto");
            escenario.setScene(new Scene(vista));
            cerrarVentana();
            escenario.show();
        } catch (IOException ex) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo abrir la siguiente ventana.", Alert.AlertType.ERROR);
        }
    }   
    
    
}
