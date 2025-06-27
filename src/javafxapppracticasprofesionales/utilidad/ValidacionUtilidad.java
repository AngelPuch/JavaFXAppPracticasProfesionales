package javafxapppracticasprofesionales.utilidad;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputControl;


/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: ValidacionUtilidad.java 
    * Autor: Rodrigo Luna Vázquez, Jose Luis Silva Gómez
    * Fecha: 15/06/2025
*/
public final class ValidacionUtilidad {

    private static final String REGEX_CORREO = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final String REGEX_TELEFONO = "\\d{10}";
    private static final String REGEX_CODIGO_POSTAL = "\\d{5}";

    public static boolean validarCorreo(String correo) {
        if (correo == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(REGEX_CORREO);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }


    public static boolean validarTelefono(String telefono) {
        if (telefono == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(REGEX_TELEFONO);
        Matcher matcher = pattern.matcher(telefono);
        return matcher.matches();
    }
    
    public static boolean validarCodigoPostal(String codigoPostal) {
        if (codigoPostal == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(REGEX_CODIGO_POSTAL);
        Matcher matcher = pattern.matcher(codigoPostal);
        return matcher.matches();
    }
    
    public static boolean isCampoTextoValido(TextInputControl... campos) {
        for (TextInputControl campo : campos) {
            if (campo.getText() == null || campo.getText().trim().isEmpty()) {
                AlertaUtilidad.mostrarAlertaSimple("Campos vacíos", 
                    "Los campos marcados con un (*) no deben de ser vacíos. Por favor, complétalos para continuar.", 
                    Alert.AlertType.WARNING);
                return false;
            }
        }
        return true;
    }
}