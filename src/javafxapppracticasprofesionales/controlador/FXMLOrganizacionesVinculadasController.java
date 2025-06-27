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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import javafxapppracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import javafxapppracticasprofesionales.modelo.pojo.ResponsableProyecto;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLOrganizacionesVinculadasController implements Initializable, INotificacion{

    @FXML
    private TableView<OrganizacionVinculada> tvOrganizacionesVinculadas;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colTelefono;
    
    private ObservableList<OrganizacionVinculada> listaOrganizaciones;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacionTabla();
    }    
    
    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccionCompleta"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
    }
    
    private void cargarInformacionTabla() {
        listaOrganizaciones = FXCollections.observableArrayList();
        try {
            listaOrganizaciones.addAll(OrganizacionVinculadaDAO.obtenerOrganizacionesVinculadas());
            tvOrganizacionesVinculadas.setItems(listaOrganizaciones);
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Sin Conexión", "Se perdió la conexión. Inténtalo de nuevo", Alert.AlertType.ERROR);
            cerrarVentana();
        }
    }
    
     private void cerrarVentana(){
        Utilidad.getEscenarioComponente(tvOrganizacionesVinculadas).close();
    }

    @FXML
    private void btnClicRegistrar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLRegistrarOrganizacionVinculada.fxml"));
            Parent vista = loader.load();
            
            FXMLRegistrarOrganizacionVinculadaController controller = loader.getController();
            controller.inicializarInformacion(null, this);
            
            Stage escenario = new Stage();
            escenario.setTitle("Organizacion del Responsable");
            escenario.setScene(new Scene(vista));
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo abrir la ventana de registro.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @Override
    public void operacionExitosa() {
        cargarInformacionTabla();
    }
    
}
