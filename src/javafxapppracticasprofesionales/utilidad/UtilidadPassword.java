package javafxapppracticasprofesionales.utilidad;

import org.mindrot.jbcrypt.BCrypt;

/** 
    * Proyecto: JavaFXAppPracticasProfesionales 
    * Archivo: UtilidadPassword.java 
    * Autor: Angel Jonathan Puch Hernández
    * Fecha: 12/06/2025
*/
public class UtilidadPassword {
    public static String hashearPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verificarPassword(String password, String hashGuardado) {
        return BCrypt.checkpw(password, hashGuardado);
    }
}
