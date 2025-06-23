package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import javafx.stage.Stage;
import javafxapppracticasprofesionales.modelo.dao.AcademicoDAO;
import javafxapppracticasprofesionales.modelo.dao.EntregaDAO;
import javafxapppracticasprofesionales.modelo.pojo.Academico;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;
import javafxapppracticasprofesionales.modelo.pojo.TipoDocumento;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;

public class FXMLValidarSeleccionarDocumentoController implements Initializable {

    @FXML
    private Label lblTitulo;
    @FXML
    private TableView<Entrega> tvEntregasProgramadas;
    @FXML
    private TableColumn colNombreEntrega;
    @FXML
    private TableColumn colFechaInicio;
    @FXML
    private TableColumn colFechaFin;

    private TipoDocumento tipoDocumento;
    private ObservableList<Entrega> listaEntregas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNombreEntrega.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
    }    
    
    public void inicializar(TipoDocumento tipoDoc) {
        this.tipoDocumento = tipoDoc;
        lblTitulo.setText("Paso 2: Seleccionar Entrega de " + tipoDoc.getNombre());
        cargarEntregas();
    }
    
    private void cargarEntregas() {
        try {
            int idUsuario = SesionUsuario.getInstancia().getUsuarioLogueado().getIdUsuario();
            Academico profesor = AcademicoDAO.obtenerAcademicoPorIdUsuario(idUsuario);
            
            if (profesor != null) {
                List<Entrega> entregasBD = EntregaDAO.obtenerEntregasPorTipo(tipoDocumento.getNombre(), profesor.getIdAcademico());
                listaEntregas = FXCollections.observableArrayList(entregasBD);
                tvEntregasProgramadas.setItems(listaEntregas);
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error","No se encontró el perfil de académico para este usuario.", Alert.AlertType.ERROR);
            }
            
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error",e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
private void btnClicContinuar(ActionEvent event) {
    Entrega entregaSeleccionada = tvEntregasProgramadas.getSelectionModel().getSelectedItem();
    if (entregaSeleccionada != null) {
        // ¡El bloque switch que creaba el arreglo "tablaInfo" se elimina por completo!
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLSeleccionarEntregaParaValidar.fxml"));
            Parent vista = loader.load();
            FXMLSeleccionarEntregaParaValidarController controlador = loader.getController();
            
            // Pasamos la información de forma limpia y directa.
            controlador.inicializar(entregaSeleccionada, tipoDocumento.getNombre());

            Stage escenario = (Stage) lblTitulo.getScene().getWindow();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Validar Entregas - Paso 3");
        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error","No se pudo cargar la ventana: " + e.getMessage(), Alert.AlertType.ERROR);
        }

    } else {
        AlertaUtilidad.mostrarAlertaSimple("Sin selección", "Debe seleccionar una entrega para continuar.", Alert.AlertType.WARNING);
    }
}
    @FXML
private void btnClicRegresar(ActionEvent event) {
    try {
        Stage escenarioActual = (Stage) lblTitulo.getScene().getWindow();
        Parent vistaAnterior = FXMLLoader.load(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLValidarSeleccionarTipoEntrega.fxml"));

        Scene escenaAnterior = new Scene(vistaAnterior);
        escenarioActual.setTitle("Validar Entregas - Paso 1");

        escenarioActual.setScene(escenaAnterior);

    } catch (IOException e) {
        AlertaUtilidad.mostrarAlertaSimple("Error", "No se pudo cargar la ventana anterior: " + e.getMessage(), Alert.AlertType.ERROR);
    }
}
}