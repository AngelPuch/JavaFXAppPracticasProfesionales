package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.modelo.dao.PeriodoDAO;
import javafxapppracticasprofesionales.modelo.dao.ProyectoDAO;
import javafxapppracticasprofesionales.modelo.pojo.Periodo;
import javafxapppracticasprofesionales.modelo.pojo.Proyecto;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLProyectosController implements Initializable {

    @FXML
    private ComboBox<Periodo> cbPeriodos;
    @FXML
    private TableView<Proyecto> tvProyectos;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn<Proyecto, String> colOrganizacion;
    @FXML
    private TableColumn<Proyecto, String> colResponsable;
    @FXML
    private TableColumn colCupos;
    @FXML
    private TableColumn<Proyecto, String> colEstado;
    
    private ObservableList<Periodo> listaPeriodos;
    private ObservableList<Proyecto> listaProyectos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarPeriodos();
        
        cbPeriodos.valueProperty().addListener(new ChangeListener<Periodo>() {
            @Override
            public void changed(ObservableValue<? extends Periodo> observable, Periodo oldValue, Periodo newValue) {
                if (newValue != null) {
                    cargarProyectos(newValue.getIdPeriodo());
                }
            }
        });
    }    
    
    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCupos.setCellValueFactory(new PropertyValueFactory<>("numeroCupos"));
        colOrganizacion.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getOrganizacion().getNombre()));
        colResponsable.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getResponsable().getNombre()));
        colEstado.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getEstado().getNombreEstado()));
    }
    
    private void cargarPeriodos() {
        listaPeriodos = FXCollections.observableArrayList();
        try {
            ArrayList<Periodo> periodosBD = PeriodoDAO.obtenerTodosLosPeriodos();
            listaPeriodos.addAll(periodosBD);
            cbPeriodos.setItems(listaPeriodos);
            
            if (!listaPeriodos.isEmpty()) {
                cbPeriodos.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple("Error de conexión", 
                    "No fue posible conectar con la base de datos. Intente de nuevo o contacte al administrador.", 
                    Alert.AlertType.ERROR);
        }
    }
    
    private void cargarProyectos(int idPeriodo) {
        listaProyectos = FXCollections.observableArrayList();
        try {
            ArrayList<Proyecto> proyectosBD = ProyectoDAO.obtenerProyectosPorPeriodo(idPeriodo);
            listaProyectos.addAll(proyectosBD);
            tvProyectos.setItems(listaProyectos);
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple("Error de consulta", "Hubo un error al cargar los proyectos.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnClicRegistrar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLSeleccionarOrganizacion.fxml"));
            Parent vista = loader.load();
            
            Stage escenario = new Stage();
            escenario.setTitle("Registrar Nuevo Proyecto - Paso 1");
            escenario.setScene(new Scene(vista));
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
            
            refrescarTabla();
            
        } catch (IOException e) {
            Utilidad.mostrarAlertaSimple("Error", "No se pudo abrir la ventana de registro.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    private void refrescarTabla() {
        Periodo periodoSeleccionado = cbPeriodos.getSelectionModel().getSelectedItem();
        if (periodoSeleccionado != null) {
            cargarProyectos(periodoSeleccionado.getIdPeriodo());
        }
    }
}