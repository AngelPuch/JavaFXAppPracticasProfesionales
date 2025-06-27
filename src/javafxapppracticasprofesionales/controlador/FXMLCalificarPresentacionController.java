package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafxapppracticasprofesionales.modelo.dao.ExpedienteDAO;
import javafxapppracticasprofesionales.modelo.dao.ProyectoDAO;
import javafxapppracticasprofesionales.modelo.pojo.CriterioEvaluacion;
import javafxapppracticasprofesionales.modelo.pojo.Estudiante;
import javafxapppracticasprofesionales.modelo.pojo.Evaluacion;
import javafxapppracticasprofesionales.modelo.pojo.Proyecto;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;
import javafxapppracticasprofesionales.modelo.dao.CriterioEvaluacionDAO; 
import javafxapppracticasprofesionales.modelo.pojo.EvaluacionDetalle;    
import java.util.List;                                                  
import java.util.ArrayList;                                             


public class FXMLCalificarPresentacionController implements Initializable {
    @FXML
    private TableView<CriterioEvaluacion> tvRubricaEvaluacion;
    @FXML
    private TableColumn colCriterio;
    @FXML
    private TableColumn colCompetente;
    @FXML
    private TableColumn colIndependiente;
    @FXML
    private TableColumn colBasicoAvanzado;
    @FXML
    private TableColumn colBasicoUmbral;
    @FXML
    private TableColumn colNoCompetente;
    @FXML
    private Label lbPromedioPuntaje;
    @FXML
    private TextField tfPuntajeContenido;
    @FXML
    private TextField tfPuntajeMetodosTecnicasIS;
    @FXML
    private Label lbNombreEstudiante;
    @FXML
    private TextArea taObservacionesYComentarios;
    @FXML
    private TextField tfPuntajeSeguridadDominio;
    @FXML
    private TextField tfPuntajeRequisitos;
    @FXML
    private TextField tfPuntajeOrtografiaRedaccion;
    
    private ObservableList<CriterioEvaluacion> criterios;
    private Estudiante estudiante;
    private Proyecto proyecto;
    private int idExpediente;
    @FXML
    private Label lbContadorCaracteres;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();

        agregarValidacionNumerica(tfPuntajeSeguridadDominio);
        agregarValidacionNumerica(tfPuntajeRequisitos);
        agregarValidacionNumerica(tfPuntajeOrtografiaRedaccion);
        agregarValidacionNumerica(tfPuntajeContenido);
        agregarValidacionNumerica(tfPuntajeMetodosTecnicasIS);

        tfPuntajeSeguridadDominio.textProperty().addListener((obs, oldV, newV) -> calcularYEstablecerPromedio());
        tfPuntajeRequisitos.textProperty().addListener((obs, oldV, newV) -> calcularYEstablecerPromedio());
        tfPuntajeOrtografiaRedaccion.textProperty().addListener((obs, oldV, newV) -> calcularYEstablecerPromedio());
        tfPuntajeContenido.textProperty().addListener((obs, oldV, newV) -> calcularYEstablecerPromedio());
        tfPuntajeMetodosTecnicasIS.textProperty().addListener((obs, oldV, newV) -> calcularYEstablecerPromedio());

        calcularYEstablecerPromedio();
        Utilidad.configurarTextAreaConContador(taObservacionesYComentarios, lbContadorCaracteres, 150);
        
    }    
    
    public void inicializarInformacion(Estudiante estudiante) {
        this.estudiante = estudiante;
        lbNombreEstudiante.setText("Estudiante: " + this.estudiante.getNombre());
        cargarDatosDeExpedienteYProyecto();
        cargarInformacionTabla();
    }
    
    @FXML
    private void btnClicCancelar(ActionEvent event) {
        if (AlertaUtilidad.mostrarAlertaConfirmacion("Salir de la evaluacion", null ,"¿Estás seguro que quieres cancelar?")) {
                Utilidad.getEscenarioComponente(lbPromedioPuntaje).close();
        }
    }

   @FXML
    private void btnClicCalificar(ActionEvent event) {
        if (validarCampos()) {
            Evaluacion evaluacion = construirEvaluacionDesdeFormulario();
            if (evaluacion != null) {
                irPantallaConfirmacion(evaluacion);
            }
        }
    }
    private boolean validarCampos() {
        if (tfPuntajeMetodosTecnicasIS.getText().isEmpty() || tfPuntajeRequisitos.getText().isEmpty() || tfPuntajeSeguridadDominio.getText().isEmpty() || tfPuntajeContenido.getText().isEmpty() || tfPuntajeOrtografiaRedaccion.getText().isEmpty()) {
            AlertaUtilidad.mostrarAlertaSimple("Datos Inválidos", "La calificación de los criterios no puede estar vacía. Por favor corrige tu información.", Alert.AlertType.WARNING);
            return false;
        }

        if (this.idExpediente <= 0 || this.proyecto == null) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Inicialización", "No se pueden guardar los datos porque falta información del estudiante o del proyecto.", Alert.AlertType.ERROR);
            return false;
        }


        return true;
    }
    
    private Evaluacion construirEvaluacionDesdeFormulario() {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setCalificacionTotal(Float.parseFloat(lbPromedioPuntaje.getText()));
        evaluacion.setComentarios(taObservacionesYComentarios.getText());
        evaluacion.setMotivo("Evaluación de Presentación de Avances");
        evaluacion.setIdTipoEvaluacion(2); 
        evaluacion.setIdUsuario(SesionUsuario.getInstancia().getUsuarioLogueado().getIdUsuario());
        evaluacion.setIdExpediente(this.idExpediente);

        List<EvaluacionDetalle> detalles = new ArrayList<>();

        EvaluacionDetalle detalleMetodos = new EvaluacionDetalle();
        detalleMetodos.setIdCriterio(1);
        detalleMetodos.setCalificacion(Float.parseFloat(tfPuntajeMetodosTecnicasIS.getText()));
        detalles.add(detalleMetodos);
        
        EvaluacionDetalle detalleRequisitos = new EvaluacionDetalle();
        detalleRequisitos.setIdCriterio(2);
        detalleRequisitos.setCalificacion(Float.parseFloat(tfPuntajeRequisitos.getText()));
        detalles.add(detalleRequisitos);

        EvaluacionDetalle detalleDominio = new EvaluacionDetalle();
        detalleDominio.setIdCriterio(3);
        detalleDominio.setCalificacion(Float.parseFloat(tfPuntajeSeguridadDominio.getText()));
        detalles.add(detalleDominio);

        EvaluacionDetalle detalleContenido = new EvaluacionDetalle();
        detalleContenido.setIdCriterio(4);
        detalleContenido.setCalificacion(Float.parseFloat(tfPuntajeContenido.getText()));
        detalles.add(detalleContenido);

        EvaluacionDetalle detalleOrtografia = new EvaluacionDetalle();
        detalleOrtografia.setIdCriterio(5);
        detalleOrtografia.setCalificacion(Float.parseFloat(tfPuntajeOrtografiaRedaccion.getText()));
        detalles.add(detalleOrtografia);
        
        evaluacion.setDetalles(detalles);
        return evaluacion;
    }

    private void irPantallaConfirmacion(Evaluacion evaluacion) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLConfirmarEvaluacion.fxml"));
            Parent vista = cargador.load();

            FXMLConfirmarEvaluacionController controlador = cargador.getController();
            controlador.inicializarDatos(this.estudiante, this.proyecto, evaluacion);

            Stage escenario = new Stage();
            escenario.setTitle("Confirmar Evaluación");
            escenario.setScene(new Scene(vista));
            Utilidad.getEscenarioComponente(lbNombreEstudiante).getScene().getWindow().hide();
            escenario.showAndWait(); 

        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de UI", "No se pudo cargar la ventana de confirmación.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    private void cargarDatosDeExpedienteYProyecto() {
        try {
            this.idExpediente = ExpedienteDAO.obtenerIdExpedientePorEstudiante(this.estudiante.getIdEstudiante());
            if (this.idExpediente > 0) {
                this.proyecto = ProyectoDAO.obtenerProyectoPorIdExpediente(this.idExpediente);
                if (this.proyecto == null) {
                    AlertaUtilidad.mostrarAlertaSimple("Error de Datos", "No se encontró un proyecto asignado para el expediente del estudiante.", Alert.AlertType.ERROR);
                }
            } else {
                AlertaUtilidad.mostrarAlertaSimple("Error de Datos", "No se encontró un expediente para el estudiante seleccionado.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    
    private void calcularYEstablecerPromedio() {
        TextField[] camposPuntaje = {
                tfPuntajeMetodosTecnicasIS,
                tfPuntajeRequisitos,
                tfPuntajeSeguridadDominio,
                tfPuntajeContenido,
                tfPuntajeOrtografiaRedaccion
        };
        double puntajeTotal = 0;
        int camposValidos = 0;

        for (TextField campo : camposPuntaje) {
            String texto = campo.getText();
            if (texto != null && !texto.isEmpty()) {
                try {
                    puntajeTotal += Double.parseDouble(texto);
                    camposValidos++;
                } catch (NumberFormatException e) {
                }
            }
        }
        if (camposValidos > 0) {
            double promedio = puntajeTotal / camposValidos;
            lbPromedioPuntaje.setText(String.format("%.2f", promedio));
        } else {
            lbPromedioPuntaje.setText("0.00");
        }
    }

    private void agregarValidacionNumerica(TextField campoTexto) {

        final String regex = "^(1|([5-9](\\.\\d{0,2})?)|10(\\.0{0,2})?|)$";

        UnaryOperator<TextFormatter.Change> filtro = change -> {
            String nuevoTexto = change.getControlNewText();
            if (nuevoTexto.matches(regex)) {
                return change;
            }
            return null;
        };

        TextFormatter<String> formateadorTexto = new TextFormatter<>(filtro);
        campoTexto.setTextFormatter(formateadorTexto);
        campoTexto.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { 
                String texto = campoTexto.getText();
                if (!texto.isEmpty()) {
                    try {
                        double valor = Double.parseDouble(texto);
                        if (valor < 5.0 || valor > 10.0) {
                            AlertaUtilidad.mostrarAlertaSimple("Valor fuera de rango", "La calificación debe estar entre 5.0 y 10.0.",
                                    Alert.AlertType.ERROR);
                            campoTexto.setText(""); 
                        }
                    } catch (NumberFormatException e) {
                        AlertaUtilidad.mostrarAlertaSimple("Formato incorrecto", "El valor ingresado no es un número válido.",
                                Alert.AlertType.ERROR);
                        campoTexto.setText("");
                    }
                }
            }
        });
    }

    private void configurarTabla() {
        colCriterio.setCellValueFactory(new PropertyValueFactory<>("criterio"));
        colCompetente.setCellValueFactory(new PropertyValueFactory<>("competente"));
        colIndependiente.setCellValueFactory(new PropertyValueFactory<>("independiente"));
        colBasicoAvanzado.setCellValueFactory(new PropertyValueFactory<>("basicoAvanzado"));
        colBasicoUmbral.setCellValueFactory(new PropertyValueFactory<>("basicoMinimo"));
        colNoCompetente.setCellValueFactory(new PropertyValueFactory<>("noCompetente"));
    }

    private void cargarInformacionTabla() {
        criterios = FXCollections.observableArrayList();
        try {
            ArrayList<CriterioEvaluacion> criteriosBD = CriterioEvaluacionDAO.obtenerCriteriosRubrica();
            criterios.addAll(criteriosBD);
            tvRubricaEvaluacion.setItems(criterios);

        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", 
                "No se pudieron cargar los criterios de evaluación desde la base de datos: " + e.getMessage(), Alert.AlertType.ERROR);
        } 
    }
}