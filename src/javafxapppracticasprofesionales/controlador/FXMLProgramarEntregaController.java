package javafxapppracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
    private Label lbContadorCaracteresDescripcion;
    @FXML
    private Label lbContadorCaracteresNombre;
    @FXML
    private Label lbErrorGrupo;
    @FXML
    private ComboBox<String> cmbHorasInicio;
    @FXML
    private ComboBox<String> cmbMinutosInicio;
    @FXML
    private ComboBox<String> cmbHorasFin;
    @FXML
    private ComboBox<String> cmbMinutosFin;

    private String tipoEntrega;
    private INotificacion observador;
    private TipoDocumento tipoDocumento;
    private ObservableList<Grupo> grupos;
    private Periodo periodoActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Utilidad.configurarTextAreaConContador(tfNombre, lbContadorCaracteresNombre, 50);
        Utilidad.configurarTextAreaConContador(taDescripcion, lbContadorCaracteresDescripcion, 100);
        cargarGrupos();
        configurarComboBoxes();
    }

    public void inicializarInformacion(String tipoEntrega, TipoDocumento tipoDocumento, INotificacion observador) {
        this.tipoEntrega = tipoEntrega;
        this.observador = observador;
        this.tipoDocumento = tipoDocumento;
        this.tfNombre.setText(tipoDocumento.getNombre());

        tfNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || !newValue.startsWith(tipoDocumento.getNombre())) {
                javafx.application.Platform.runLater(() -> tfNombre.setText(oldValue));
            }
        });
    }

    private void cargarGrupos() {
        grupos = FXCollections.observableArrayList();
        try {
            this.periodoActual = PeriodoDAO.obtenerPeriodoActual();
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
    
    private void configurarComboBoxes() {
        ObservableList<String> horas = FXCollections.observableArrayList();
        for (int i = 0; i <= 23; i++) {
            horas.add(String.format("%02d", i));
        }
        ObservableList<String> minutos = FXCollections.observableArrayList();
        for (int i = 0; i <= 59; i++) {
            minutos.add(String.format("%02d", i));
        }
        cmbHorasInicio.setItems(horas);
        cmbMinutosInicio.setItems(minutos);
        cmbHorasFin.setItems(horas);
        cmbMinutosFin.setItems(minutos);
        cmbHorasInicio.setValue("08");
        cmbMinutosInicio.setValue("00");
        cmbHorasFin.setValue("18");
        cmbMinutosFin.setValue("00");
    }

    private boolean validarCampos() {
        lbErrorGrupo.setText("");

        if (tfNombre.getText().trim().isEmpty() || dpFechaInicio.getValue() == null || dpFechaFin.getValue() == null ||
                cbGrupo.getValue() == null || cmbHorasInicio.getValue() == null || cmbMinutosInicio.getValue() == null ||
                cmbHorasFin.getValue() == null || cmbMinutosFin.getValue() == null) {
            AlertaUtilidad.mostrarAlertaSimple("Campos vacíos",
                    "Los campos marcados con un (*) no deben de ser vacíos. Por favor, complétalos para continuar.",
                    Alert.AlertType.WARNING);
            return false;
        }
        LocalDate fechaActual = LocalDate.now();
        if (dpFechaInicio.getValue().isBefore(fechaActual)) {
            DateTimeFormatter formatoAmigable = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            AlertaUtilidad.mostrarAlertaSimple("Fecha de inicio inválida",
                    "La fecha de inicio no puede ser anterior a la fecha actual (" + fechaActual.format(formatoAmigable) + ").",
                    Alert.AlertType.WARNING);
            return false;
        }
        if (dpFechaFin.getValue().isBefore(dpFechaInicio.getValue())) {
            AlertaUtilidad.mostrarAlertaSimple("Fechas incorrectas", "La fecha de fin no puede ser anterior a la fecha de inicio.", Alert.AlertType.WARNING);
            return false;
        }
        if (this.periodoActual != null) {
            LocalDate fechaFinEntrega = dpFechaFin.getValue();
            LocalDate fechaFinPeriodo = LocalDate.parse(this.periodoActual.getFechaFin());
            if (fechaFinEntrega.isAfter(fechaFinPeriodo)) {
                DateTimeFormatter formatoAmigable = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                AlertaUtilidad.mostrarAlertaSimple("Fecha fuera de rango",
                        "La fecha de fin (" + fechaFinEntrega.format(formatoAmigable) +
                        ") no puede exceder el fin del periodo (" +
                        fechaFinPeriodo.format(formatoAmigable) + ").",
                        Alert.AlertType.WARNING);
                return false;
            }
        }

        return true;
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
                default:
                    AlertaUtilidad.mostrarAlertaSimple("Error Interno", "Tipo de entrega no válido: " + tipoEntrega, Alert.AlertType.ERROR);
                    return;
            }

            Entrega nuevaEntrega = obtenerNuevaEntrega();
            int idGrupoSeleccionado = cbGrupo.getSelectionModel().getSelectedItem().getIdGrupo();
            int idTipoDocumentoSeleccionado = this.tipoDocumento.getIdTipoDocumento();

            try {
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
    
    private Entrega obtenerNuevaEntrega() {
        Entrega entrega = new Entrega();
        entrega.setNombre(tfNombre.getText().trim());
        entrega.setDescripcion(taDescripcion.getText().trim());
        entrega.setFechaInicio(dpFechaInicio.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        entrega.setFechaFin(dpFechaFin.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        String horaInicio = cmbHorasInicio.getValue() + ":" + cmbMinutosInicio.getValue();
        String horaFin = cmbHorasFin.getValue() + ":" + cmbMinutosFin.getValue();
        entrega.setHoraInicio(horaInicio);
        entrega.setHoraFin(horaFin);

        return entrega;
    }
    
    private void cerrarVentana() {
        Utilidad.getEscenarioComponente(tfNombre).close();
    }

}