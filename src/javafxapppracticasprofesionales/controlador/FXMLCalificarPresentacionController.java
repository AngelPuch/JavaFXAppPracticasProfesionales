
package javafxapppracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafxapppracticasprofesionales.JavaFXAppPracticasProfesionales;
import javafxapppracticasprofesionales.modelo.dao.EvaluacionDAO;
import javafxapppracticasprofesionales.modelo.dao.ExpedienteDAO;
import javafxapppracticasprofesionales.modelo.dao.ProyectoDAO;
import javafxapppracticasprofesionales.modelo.pojo.CriterioEvaluacion;
import javafxapppracticasprofesionales.modelo.pojo.Estudiante;
import javafxapppracticasprofesionales.modelo.pojo.Evaluacion;
import javafxapppracticasprofesionales.modelo.pojo.Proyecto;
import javafxapppracticasprofesionales.modelo.pojo.ResultadoOperacion;
import javafxapppracticasprofesionales.utilidad.AlertaUtilidad;
import javafxapppracticasprofesionales.utilidad.SesionUsuario;
import javafxapppracticasprofesionales.utilidad.Utilidad;

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
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacionTabla();

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
    }    
    
    public void inicializarInformacion(Estudiante estudiante) {
        this.estudiante = estudiante;
        lbNombreEstudiante.setText("Estudiante: " + this.estudiante.getNombre());
        cargarDatosDeExpedienteYProyecto();
    }

    @FXML
    private void btnClisCancelar(ActionEvent event) {
        try {
            if (AlertaUtilidad.mostrarAlertaConfirmacion("Salir de la evaluacion", null ,"¿Estás seguro que quieres cancelar?")) {
                Stage escenarioListaEstudiantes = (Stage) lbNombreEstudiante.getScene().getWindow();
                FXMLLoader cargador = new FXMLLoader(JavaFXAppPracticasProfesionales.class.getResource("view/evaluator/FXMLEvaluatorMainScreen.fxml"));
                Parent vistaInicioSesion = cargador.load();
                Scene escenaPrincipal = new Scene(vistaInicioSesion);
                escenarioListaEstudiantes.setScene(escenaPrincipal);
                escenarioListaEstudiantes.setTitle("Página Principal");
                escenarioListaEstudiantes.show();
            }
        } catch (IOException ex) {
            AlertaUtilidad.mostrarAlertaSimple("Error al cargar", "Lo sentimos por el momento no se pudo "
                    + "mostrar la ventana", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
        // Validar que se hayan calificado todos los rubros
        if (tfPuntajeMetodosTecnicasIS.getText().isEmpty() || tfPuntajeRequisitos.getText().isEmpty() ||
            tfPuntajeSeguridadDominio.getText().isEmpty() || tfPuntajeContenido.getText().isEmpty() ||
            tfPuntajeOrtografiaRedaccion.getText().isEmpty()) {

            AlertaUtilidad.mostrarAlertaSimple("Datos Inválidos", "Existen campos inválidos. "
                    + "Por favor corrige tu información.", Alert.AlertType.WARNING);
            return;
        }

        // Validar que se pudieron cargar los datos necesarios
        if (this.idExpediente <= 0 || this.proyecto == null) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Inicialización", "No se pueden guardar los datos "
                    + "porque falta información del estudiante o del proyecto.", Alert.AlertType.ERROR);
            return;
        }

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setCalificacionTotal(Float.parseFloat(lbPromedioPuntaje.getText()));
        evaluacion.setComentarios(taObservacionesYComentarios.getText());
        evaluacion.setMotivo("Evaluación de Presentación de Avances");
        evaluacion.setIdTipoEvaluacion(2);
        evaluacion.setIdUsuario(SesionUsuario.getInstancia().getUsuarioLogueado().getIdUsuario());
        evaluacion.setIdExpediente(this.idExpediente); // Se usa el ID real obtenido

        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLConfirmarEvaluacion.fxml"));
            Parent vista = cargador.load();

            FXMLConfirmarEvaluacionController controlador = cargador.getController();
            controlador.inicializarDatos(this.estudiante, this.proyecto, evaluacion);

            Stage escenario = new Stage();
            escenario.setTitle("Confirmar Evaluación");
            escenario.setScene(new Scene(vista));
            Utilidad.getEscenarioComponente(lbNombreEstudiante).close();
            escenario.show();

        } catch (IOException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de UI", "No se pudo cargar la ventana de confirmación.",
                    Alert.AlertType.ERROR);
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
            if (texto != null && !texto.isEmpty() && !"1".equals(texto)) {
                try {
                    puntajeTotal += Double.parseDouble(texto);
                    camposValidos++;
                } catch (NumberFormatException e) {
                }
            }
        }
        if (camposValidos > 0) {
            double promedio = puntajeTotal / camposValidos;
            lbPromedioPuntaje.setText(String.format("%.1f", promedio));
        } else {
            lbPromedioPuntaje.setText("0.0");
        }
    }

    private void agregarValidacionNumerica(TextField campoTexto) {
        UnaryOperator<TextFormatter.Change> filtro = change -> {
            String nuevoTexto = change.getControlNewText();
            if (nuevoTexto.matches("($|[5-9]|1|10)")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> formateadorTexto = new TextFormatter<>(filtro);
        campoTexto.setTextFormatter(formateadorTexto);

        campoTexto.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if ("1".equals(campoTexto.getText())) {
                    campoTexto.setText("");
                    AlertaUtilidad.mostrarAlertaSimple("Error de puntuación", "La puntuación mínima es 5.",
                            Alert.AlertType.ERROR);
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
        ArrayList<CriterioEvaluacion> criteriosFuente = obtenerCriteriosDeFuenteDatos();
        criterios.addAll(criteriosFuente);
        tvRubricaEvaluacion.setItems(criterios);
    }

    private ArrayList<CriterioEvaluacion> obtenerCriteriosDeFuenteDatos() {
        ArrayList<CriterioEvaluacion> listaCriterios = new ArrayList<>();

        listaCriterios.add(new CriterioEvaluacion(
                "USO DE MÉTODOS Y TÉCNICAS DE LA IS",
                "Los métodos y técnicas de la IS optimizan el aseguramiento de calidad y se han aplicado de manera correcta.",
                "Los métodos y técnicas de la IS, son adecuados y se han aplicado de manera correcta.",
                "Los métodos y técnicas de la IS, son adecuados, aunque se presentan algunas deficiencias en su aplicación.",
                "Los métodos y técnicas de la IS, no son adecuados, pero se han aplicado de manera correcta.",
                "No se han aplicado métodos y técnicas de la IS."
        ));

        listaCriterios.add(new CriterioEvaluacion(
                "REQUISITOS",
                "Cumplió con todos los requisitos. Excedió las expectativas.",
                "Todos los requisitos fueron cumplidos.",
                "No cumple satisfactoriamente con un requisito.",
                "Más de un requisito no fue cumplido satisfactoriamente.",
                "Más de dos requisitos no fueron cumplidos satisfactoriamente."
        ));

        listaCriterios.add(new CriterioEvaluacion(
                "SEGURIDAD Y DOMINIO",
                "El dominio del tema es excelente, la exposición es dada con seguridad.",
                "Se posee un dominio adecuado y la exposición fue fluida.",
                "Aunque con algunos fallos en el dominio, la exposición fue fluida.",
                "Se demuestra falta de dominio y una exposición deficiente.",
                "No existe dominio sobre el tema y la exposición es deficiente."
        ));

        listaCriterios.add(new CriterioEvaluacion(
                "CONTENIDO",
                "Cubre los temas a profundidad con detalles y ejemplos. El conocimiento del tema es excelente.",
                "Incluye conocimiento básico sobre el tema. El contenido parece ser bueno.",
                "Incluye información esencial sobre el tema, pero tiene 1-2 errores en los hechos.",
                "El contenido es mínimo y tiene tres errores en los hechos.",
                "El contenido es mínimo y tiene varios errores en los hechos."
        ));

        listaCriterios.add(new CriterioEvaluacion(
                "ORTOGRAFÍA Y REDACCIÓN",
                "No hay errores de gramática, ortografía o puntuación.",
                "Casi no hay errores de gramática, ortografía o puntuación.",
                "Algunos errores de gramática, ortografía o puntuación.",
                "Varios errores de gramática, ortografía o puntuación.",
                "Demasiados errores de gramática, ortografía o puntuación."
        ));
        return listaCriterios;
    }


}


    

