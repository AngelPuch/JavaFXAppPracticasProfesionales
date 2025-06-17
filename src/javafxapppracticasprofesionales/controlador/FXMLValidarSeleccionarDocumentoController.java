package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
            // Paso 1: Obtener el id del usuario en sesión.
            int idUsuario = SesionUsuario.getInstancia().getUsuarioLogueado().getIdUsuario();
            // Paso 2: Obtener el objeto Academico completo.
            Academico profesor = AcademicoDAO.obtenerAcademicoPorIdUsuario(idUsuario);
            
            if (profesor != null) {
                // Paso 3: Llamar al DAO con el ID del tipo y el ID del académico.
                ArrayList<Entrega> entregasBD = EntregaDAO.obtenerEntregasPorTipo(tipoDocumento.getIdTipoDocumento(), profesor.getIdAcademico());
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
            // Aquí abrimos la ventana que muestra los estudiantes que han entregado este documento.
            // Es la misma lógica que te di en respuestas anteriores, ahora conectada al nuevo flujo.
            
            String[] tablaInfo = new String[3];
             switch (tipoDocumento.getIdTipoDocumento()) {
                case 1: // Documentos Iniciales
                    tablaInfo[0] = "documentoinicio";
                    tablaInfo[1] = "idDocumentoInicio";
                    tablaInfo[2] = "EntregaDocumentoInicio_idEntregaDocumentoInicio";
                    break;
                case 2: // Reportes
                    tablaInfo[0] = "reporte";
                    tablaInfo[1] = "idReporte";
                    tablaInfo[2] = "EntregaReporte_idEntregaReporte";
                    break;
                case 3: // Documentos Finales
                    tablaInfo[0] = "documentofinal";
                    tablaInfo[1] = "idDocumentoFinal";
                    tablaInfo[2] = "EntregaDocumentoFinal_idEntregaDocumentoFinal";
                    break;
            }
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLSeleccionarEntregaParaValidar.fxml"));
                Parent vista = loader.load();
                FXMLSeleccionarEntregaParaValidarController controlador = loader.getController();
                controlador.inicializar(entregaSeleccionada, tablaInfo);

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