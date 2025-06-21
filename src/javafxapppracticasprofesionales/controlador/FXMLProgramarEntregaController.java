package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafxapppracticasprofesionales.interfaz.INotificacion;
import javafxapppracticasprofesionales.modelo.dao.GrupoDAO;
import javafxapppracticasprofesionales.modelo.dao.PeriodoDAO;
import javafxapppracticasprofesionales.modelo.dao.ProgramarEntregaDAO;
import javafxapppracticasprofesionales.modelo.pojo.Entrega;
import javafxapppracticasprofesionales.modelo.pojo.Grupo;
import javafxapppracticasprofesionales.modelo.pojo.Periodo;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.modelo.pojo.TipoDocumento;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.Utilidad;

public class FXMLProgramarEntregaController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextArea taDescripcion;
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaFin;
    @FXML
    private ComboBox<Grupo> cbGrupo;
    @FXML
    private TextField tfHoraInicio;
    @FXML
    private TextField tfHoraFin;
    @FXML
    private Label lbContadorCaracteresDescripcion;
    @FXML
    private Label lbContadorCaracteresNombre;
    @FXML
    private Label lbErrorGrupo;
    @FXML
    private Label lbErrorHora;

    private String tipoEntrega;
    private INotificacion observador;
    private TipoDocumento tipoDocumento;
    private ObservableList<Grupo> grupos;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnAceptar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Utilidad.configurarTextAreaConContador(tfNombre, lbContadorCaracteresNombre, 50);
        Utilidad.configurarTextAreaConContador(taDescripcion, lbContadorCaracteresDescripcion, 100);
        cargarGrupos();
    }

    public void inicializarInformacion(String tipoEntrega, TipoDocumento tipoDocumento, INotificacion observador) {
        this.tipoEntrega = tipoEntrega;
        this.observador = observador;
        this.tipoDocumento = tipoDocumento;
        this.tfNombre.setText(tipoDocumento.getNombre());

        // Listener para evitar que se borre el prefijo del nombre
        tfNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || !newValue.startsWith(tipoDocumento.getNombre())) {
                javafx.application.Platform.runLater(() -> tfNombre.setText(oldValue));
            }
        });
    }

    private void cargarGrupos() {
        grupos = FXCollections.observableArrayList();
        try {
            Periodo periodoActual = PeriodoDAO.obtenerPeriodoActual();
            if (periodoActual != null) {
                grupos.addAll(GrupoDAO.obtenerGruposPorPeriodo(periodoActual.getIdPeriodo()));
                cbGrupo.setItems(grupos);
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error de Periodo",
                        "No se encontró un periodo escolar activo. No se pueden cargar los grupos.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión",
                    "No se pudo conectar a la base de datos para cargar los grupos.", Alert.AlertType.ERROR);
        }
    }

    private boolean validarCampos() {
        lbErrorGrupo.setText("");
        lbErrorHora.setText("");
        boolean esValido = true;

        if (tfNombre.getText().trim().isEmpty() || dpFechaInicio.getValue() == null || dpFechaFin.getValue() == null || cbGrupo.getValue() == null) {
            AlertaUtilidad.mostrarAlertaSimple("Campos vacíos", "Los campos marcados con un (*) no deben estar vacíos.", Alert.AlertType.WARNING);
            if(cbGrupo.getValue() == null) lbErrorGrupo.setText("Debe seleccionar un grupo.");
            return false;
        }
        
        LocalDate fechaActual = LocalDate.now();
        if (dpFechaInicio.getValue().isBefore(fechaActual)) {
            AlertaUtilidad.mostrarAlertaSimple("Fecha incorrecta", "La fecha de inicio no puede ser anterior a la fecha actual.", Alert.AlertType.WARNING);
            return false;
        }
        if (dpFechaFin.getValue().isBefore(dpFechaInicio.getValue())) {
            AlertaUtilidad.mostrarAlertaSimple("Fechas incorrectas", "La fecha de fin no puede ser anterior a la fecha de inicio.", Alert.AlertType.WARNING);
            return false;
        }

        String regexHora = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$";
        if (!tfHoraInicio.getText().isEmpty() && !Pattern.matches(regexHora, tfHoraInicio.getText())) {
            lbErrorHora.setText("Formato de hora de inicio inválido (HH:mm).");
            esValido = false;
        }
         if (!tfHoraFin.getText().isEmpty() && !Pattern.matches(regexHora, tfHoraFin.getText())) {
            lbErrorHora.setText("Formato de hora de fin inválido (HH:mm).");
            esValido = false;
        }

        return esValido;
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        boolean confirmado = AlertaUtilidad.mostrarAlertaConfirmacion("Cancelar Operación",
                "¿Estás seguro de que quieres cancelar?", "Cualquier dato no guardado se perderá.");
        if (confirmado) {
            cerrarVentana();
        }
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
        if (validarCampos()) {
            String tablaDestino = "";
            switch (tipoEntrega) {
                case "DOCUMENTOS INICIALES": tablaDestino = "entregadocumentoinicio"; break;
                case "REPORTES": tablaDestino = "entregareporte"; break;
                case "DOCUMENTOS FINALES": tablaDestino = "entregadocumentofinal"; break;
            }

            Entrega nuevaEntrega = new Entrega();
            nuevaEntrega.setNombre(tfNombre.getText().trim());
            nuevaEntrega.setDescripcion(taDescripcion.getText().trim());
            nuevaEntrega.setFechaInicio(dpFechaInicio.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            nuevaEntrega.setFechaFin(dpFechaFin.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            nuevaEntrega.setHoraInicio(tfHoraInicio.getText().trim());
            nuevaEntrega.setHoraFin(tfHoraFin.getText().trim());

            int idGrupoSeleccionado = cbGrupo.getSelectionModel().getSelectedItem().getIdGrupo();
            int idTipoDocumentoSeleccionado = this.tipoDocumento.getIdTipoDocumento(); // Se obtiene el ID

            try {
                // CAMBIO AQUÍ: Se pasa el ID del tipo de documento al DAO
                ResultadoOperacion resultado = ProgramarEntregaDAO.programarNuevaEntrega(
                        nuevaEntrega, tablaDestino, idGrupoSeleccionado, idTipoDocumentoSeleccionado);
                
                if (!resultado.isError()) {
                    AlertaUtilidad.mostrarAlertaSimple("Operación Exitosa", "Operación realizada correctamente.", Alert.AlertType.INFORMATION);
                    observador.operacionExitosa();
                    cerrarVentana();
                } else {
                    AlertaUtilidad.mostrarAlertaSimple("Error", resultado.getMensaje(), Alert.AlertType.ERROR);
                }
            } catch (SQLException e) {
                AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", "No fue posible conectar con la base de datos: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
    private void cerrarVentana() {
        Utilidad.getEscenarioComponente(tfNombre).close();
    }
}