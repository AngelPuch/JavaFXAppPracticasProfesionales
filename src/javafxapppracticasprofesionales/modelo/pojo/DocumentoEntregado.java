package javafxapppracticasprofesionales.modelo.pojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DocumentoEntregado {

    private int idDocumento;
    private String nombreEstudiante;
    private String matricula;
    private LocalDateTime fechaEntregado;
    private String rutaArchivo;
    private String tipoDocumento; // "documentoinicio", "reporte" o "documentofinal"

    public DocumentoEntregado() {
    }

    // Getters y Setters para todas las propiedades
    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public LocalDateTime getFechaEntregado() {
        return fechaEntregado;
    }
    
    public String getFechaEntregadoFormateada() {
        if (this.fechaEntregado != null) {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm");
            return this.fechaEntregado.format(formato);
        }
        return "Fecha no disponible";
    }

    public void setFechaEntregado(LocalDateTime fechaEntregado) {
        this.fechaEntregado = fechaEntregado;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}