/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exceptions;

/**
 * Excepcion base de todo el sistema Biblioteca Nova.
 *
 * De ella heredan las demas excepciones propias del proyecto. Sirve para poder
 * atrapar de un solo golpe cualquier error del negocio:
 *
 *     catch (BibliotecaException ex) { ... }
 *
 * y tambien para distinguirlos de los errores tecnicos de Java.
 *
 * @author Grupo 3
 */
public class BibliotecaException extends Exception {

    // Constructor de la excepcion
    public BibliotecaException(String message) {
        super(message);
    }

    /*
     * Este constructor guarda el error original que provoco la falla, para no
     * perder el motivo real cuando un error tecnico se convierte en un error
     * del negocio.
     */
    public BibliotecaException(String message, Throwable causa) {
        super(message, causa);
    }

}
