package javafxapppracticasprofesionales.utilidad;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
}