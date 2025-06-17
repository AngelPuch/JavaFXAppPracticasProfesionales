package javafxapppracticasprofesionales.utilidad;

import org.mindrot.jbcrypt.BCrypt;

public class UtilidadPassword {
    public static String hashearPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verificarPassword(String password, String hashGuardado) {
        return BCrypt.checkpw(password, hashGuardado);
    }
}
