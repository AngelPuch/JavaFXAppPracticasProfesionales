
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
    

    /**
     * Initializes the controller class.
     */
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
    }    
    
    public void inicializarInformacion(Estudiante estudiante) {
        this.estudiante = estudiante;
        lbNombreEstudiante.setText("Estudiante: " + this.estudiante.getNombre());
        cargarDatosDeExpedienteYProyecto();
        cargarInformacionTabla();
    }

    @FXML
    private void btnClisCancelar(ActionEvent event) {
        if (AlertaUtilidad.mostrarAlertaConfirmacion("Salir de la evaluacion", null ,"¿Estás seguro que quieres cancelar?")) {
                Utilidad.getEscenarioComponente(lbPromedioPuntaje).close();
        }
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
        if (tfPuntajeMetodosTecnicasIS.getText().isEmpty() || /*...otras validaciones...*/ tfPuntajeOrtografiaRedaccion.getText().isEmpty()) {
            AlertaUtilidad.mostrarAlertaSimple("Datos Inválidos", "Existen campos inválidos. Por favor corrige tu información.", Alert.AlertType.WARNING);
            return;
        }

        if (this.idExpediente <= 0 || this.proyecto == null) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Inicialización", "No se pueden guardar los datos porque falta información del estudiante o del proyecto.", Alert.AlertType.ERROR);
            return;
        }

        // 1. Crear el objeto de evaluación principal
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setCalificacionTotal(Float.parseFloat(lbPromedioPuntaje.getText()));
        evaluacion.setComentarios(taObservacionesYComentarios.getText());
        evaluacion.setMotivo("Evaluación de Presentación de Avances");
        evaluacion.setIdTipoEvaluacion(2); // ID para 'Evaluacion de Presentacion'
        evaluacion.setIdUsuario(SesionUsuario.getInstancia().getUsuarioLogueado().getIdUsuario());
        evaluacion.setIdExpediente(this.idExpediente);

        // 2. Crear y poblar la lista de detalles de la evaluación
        List<EvaluacionDetalle> detalles = new ArrayList<>();

        // Criterio 1: USO DE MÉTODOS Y TÉCNICAS DE LA IS (idCriterio = 1)
        EvaluacionDetalle detalleMetodos = new EvaluacionDetalle();
        detalleMetodos.setIdCriterio(1);
        detalleMetodos.setCalificacion(Float.parseFloat(tfPuntajeMetodosTecnicasIS.getText()));
        detalles.add(detalleMetodos);
        
        // Criterio 2: REQUISITOS (idCriterio = 2)
        EvaluacionDetalle detalleRequisitos = new EvaluacionDetalle();
        detalleRequisitos.setIdCriterio(2);
        detalleRequisitos.setCalificacion(Float.parseFloat(tfPuntajeRequisitos.getText()));
        detalles.add(detalleRequisitos);

        // Criterio 3: SEGURIDAD Y DOMINIO (idCriterio = 3)
        EvaluacionDetalle detalleDominio = new EvaluacionDetalle();
        detalleDominio.setIdCriterio(3);
        detalleDominio.setCalificacion(Float.parseFloat(tfPuntajeSeguridadDominio.getText()));
        detalles.add(detalleDominio);

        // Criterio 4: CONTENIDO (idCriterio = 4)
        EvaluacionDetalle detalleContenido = new EvaluacionDetalle();
        detalleContenido.setIdCriterio(4);
        detalleContenido.setCalificacion(Float.parseFloat(tfPuntajeContenido.getText()));
        detalles.add(detalleContenido);

        // Criterio 5: ORTOGRAFÍA Y REDACCIÓN (idCriterio = 5)
        EvaluacionDetalle detalleOrtografia = new EvaluacionDetalle();
        detalleOrtografia.setIdCriterio(5);
        detalleOrtografia.setCalificacion(Float.parseFloat(tfPuntajeOrtografiaRedaccion.getText()));
        detalles.add(detalleOrtografia);
        
        // 3. Añadir la lista de detalles al objeto de evaluación principal
        evaluacion.setDetalles(detalles);

        // 4. Pasar el objeto completo a la ventana de confirmación
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/javafxapppracticasprofesionales/vista/FXMLConfirmarEvaluacion.fxml"));
            Parent vista = cargador.load();

            FXMLConfirmarEvaluacionController controlador = cargador.getController();
            // El controlador de confirmación ahora recibe el objeto completo
            controlador.inicializarDatos(this.estudiante, this.proyecto, evaluacion);

            Stage escenario = new Stage();
            escenario.setTitle("Confirmar Evaluación");
            escenario.setScene(new Scene(vista));
            Utilidad.getEscenarioComponente(lbNombreEstudiante).getScene().getWindow().hide(); // Se oculta en lugar de cerrar
            escenario.showAndWait(); // Espera a que la ventana de confirmación se cierre

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
        try {
            // 1. Obtiene la lista de criterios base desde la BD (ej. id=1, nombre='USO DE MÉTODOS...')
            ArrayList<CriterioEvaluacion> criteriosBD = CriterioEvaluacionDAO.obtenerCriteriosRubrica();

            // Verificación para asegurar que la BD regresó los 5 criterios esperados
            if (criteriosBD.size() < 5) {
                AlertaUtilidad.mostrarAlertaSimple("Error de Datos", 
                    "La cantidad de criterios en la base de datos no coincide con los 5 esperados en la aplicación.", Alert.AlertType.ERROR);
                return;
            }

            // 2. Asigna las descripciones estáticas a cada criterio cargado

            // CRITERIO 1: USO DE MÉTODOS Y TÉCNICAS DE LA IS
            CriterioEvaluacion critMetodos = criteriosBD.get(0);
            critMetodos.setCompetente("Los métodos y técnicas de la IS optimizan el aseguramiento de calidad y se han aplicado de manera correcta."); // 
            critMetodos.setIndependiente("Los métodos y técnicas de la IS, son adecuados y se han aplicado de manera correcta."); // 
            critMetodos.setBasicoAvanzado("Los métodos y técnicas de la IS, son adecuados, aunque se presentan algunas deficiencias en su aplicación."); // 
            critMetodos.setBasicoMinimo("Los métodos y técnicas de la IS, no son adecuados, pero se han aplicado de manera correcta."); // 
            critMetodos.setNoCompetente("No se han aplicado métodos y técnicas de la IS."); // 

            // CRITERIO 2: REQUISITOS
            CriterioEvaluacion critRequisitos = criteriosBD.get(1);
            critRequisitos.setCompetente("Cumplió con todos los requisitos. Excedió las expectativas."); // 
            critRequisitos.setIndependiente("Todos los requisitos fueron cumplidos."); // 
            critRequisitos.setBasicoAvanzado("No cumple satisfactoriamente con un requisito."); // 
            critRequisitos.setBasicoMinimo("Más de un requisito no fue cumplido satisfactoriamente."); // 
            critRequisitos.setNoCompetente("Más de dos requisitos no fueron cumplidos satisfactoriamente."); // 

            // CRITERIO 3: SEGURIDAD Y DOMINIO
            CriterioEvaluacion critDominio = criteriosBD.get(2);
            critDominio.setCompetente("El dominio del tema es excelente, la exposición es dada con seguridad."); // 
            critDominio.setIndependiente("Se posee un dominio adecuado y la exposición fue fluida."); // 
            critDominio.setBasicoAvanzado("Aunque con algunos fallos en el dominio, la exposición fue fluida."); // 
            critDominio.setBasicoMinimo("Se demuestra falta de dominio y una exposición deficiente."); // 
            critDominio.setNoCompetente("No existe dominio sobre el tema y la exposición es deficiente."); // 

            // CRITERIO 4: CONTENIDO
            CriterioEvaluacion critContenido = criteriosBD.get(3);
            critContenido.setCompetente("Cubre los temas a profundidad con detalles y ejemplos. El conocimiento del tema es excelente."); // 
            critContenido.setIndependiente("Incluye conocimiento básico sobre el tema. El contenido parece ser bueno."); // 
            critContenido.setBasicoAvanzado("Incluye información esencial sobre el tema, pero tiene 1-2 errores en los hechos."); // 
            critContenido.setBasicoMinimo("El contenido es mínimo y tiene tres errores en los hechos."); // 
            critContenido.setNoCompetente("El contenido es mínimo y tiene varios errores en los hechos."); // 

            // CRITERIO 5: ORTOGRAFÍA Y REDACCIÓN
            CriterioEvaluacion critOrtografia = criteriosBD.get(4);
            critOrtografia.setCompetente("No hay errores de gramática, ortografía o puntuación."); // 
            critOrtografia.setIndependiente("Casi no hay errores de gramática, ortografía o puntuación."); // 
            critOrtografia.setBasicoAvanzado("Algunos errores de gramáticas, ortografía o puntuación."); // 
            critOrtografia.setBasicoMinimo("Varios errores de gramática, ortografía o puntuación."); // 
            critOrtografia.setNoCompetente("Demasiados errores de gramática, ortografía o puntuación."); // 

            // 3. Agrega todos los criterios ya "decorados" a la lista observable
            criterios.addAll(criteriosBD);

            // 4. Muestra la información en la tabla
            tvRubricaEvaluacion.setItems(criterios);

        } catch (SQLException e) {
            AlertaUtilidad.mostrarAlertaSimple("Error de Conexión", 
                "No se pudieron cargar los criterios de evaluación desde la base de datos: " + e.getMessage(), Alert.AlertType.ERROR);
        } 
    }

}
