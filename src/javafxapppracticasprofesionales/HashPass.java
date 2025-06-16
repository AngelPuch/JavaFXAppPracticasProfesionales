/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapppracticasprofesionales;

import javafxapppracticasprofesionales.utilidad.UtilidadPassword;

/**
 *
 * @author Dell
 */
public class HashPass {
    public static void main(String[] args) {
        // Elige la contraseña que quieras usar para iniciar sesión
        String miPasswordFacil = "coordinador123"; 
        
        String hashGenerado = UtilidadPassword.hashearPassword("coordinador123");
        
        System.out.println("La contraseña para iniciar sesión es: " + miPasswordFacil);
        System.out.println("El hash para la base de datos es: " + hashGenerado);
    }
}
