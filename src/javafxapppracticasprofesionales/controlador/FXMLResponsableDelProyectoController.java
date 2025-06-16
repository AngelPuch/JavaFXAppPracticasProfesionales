
package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import javafxapppracticasprofesionales.modelo.pojo.ResponsableProyecto;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLResponsableDelProyectoController implements Initializable {

    @FXML
    private TableView<ResponsableProyecto> tvResponsables;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colCargo;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private Label lbOrganizacion;
    
    private OrganizacionVinculada organizacion;
    private ObservableList<ResponsableProyecto> listaResponsables;
    INotificacion observador;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }
    
    public void inicializarInformacion(OrganizacionVinculada organizacion, INotificacion observador){
        this.organizacion = organizacion;
        this.observador = observador;
        lbOrganizacion.setText("Organización: " + this.organizacion.getNombre());
        
        cargarInformacionTabla();
    }

    @FXML
    private void clicBtnContinuar(ActionEvent event) {
        ResponsableProyecto responsableSeleccionado = tvResponsables.getSelectionModel().getSelectedItem();
        if(responsableSeleccionado != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLRegistrarProyecto.fxml"));
                Parent vista = loader.load();

                FXMLRegistrarProyectoController controller = loader.getController();
                controller.inicializarInformacion(organizacion, responsableSeleccionado, observador);

                Stage escenario = new Stage();
                escenario.setTitle("Registrar Nuevo Proyecto - Paso 3");
                escenario.setScene(new Scene(vista));
                escenario.initModality(Modality.APPLICATION_MODAL);
                
                cerrarVentana();
                escenario.showAndWait();
                
            } catch (IOException e) {
                AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo abrir la siguiente ventana.", Alert.AlertType.ERROR);
            }
        } else {
            AlertaUtilidad.mostrarAlertaSimple("Selección requerida", "Debes seleccionar un responsable para continuar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicBtnRegresar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLOrganizacionDelProyecto.fxml"));
            Parent vista = loader.load();
            
            FXMLOrganizacionDelProyectoController controller = loader.getController();
            controller.inicializarInformacion(observador, true);

            Stage escenario = new Stage();
            escenario.setTitle("Registrar Nuevo Proyecto - Paso 1");
            escenario.setScene(new Scene(vista));
            cerrarVentana();
            escenario.show();

        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo volver a la ventana anterior.", Alert.AlertType.ERROR);
        }
    }
    
    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
    }
    
    private void cargarInformacionTabla() {
        listaResponsables = FXCollections.observableArrayList();
        try {
            listaResponsables.addAll(ResponsableProyectoDAO.obtenerResponsablesPorOrganizacion(organizacion.getIdOrganizacion()));
            tvResponsables.setItems(listaResponsables);
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Sin Conexión", "Se perdió la conexión. Inténtalo de nuevo", Alert.AlertType.ERROR);
            cerrarVentana();
        }
    }
    
    private void cerrarVentana(){
        Utilidad.getEscenarioComponente(tvResponsables).close();
    }
}
