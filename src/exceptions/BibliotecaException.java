/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exceptions;

/**
 * Excepcion base del sistema. De ella salen las demas excepciones del
 * proyecto, asi con un solo catch (BibliotecaException ex) se atrapa cualquier
 * error del negocio y quedan aparte de los errores tecnicos de Java.
 *
 * @author Grupo 3
 */
public class BibliotecaException extends Exception {

    // Constructor de la excepcion
    public BibliotecaException(String message) {
        super(message);
    }

    /*
     * Este otro guarda el error que provoco la falla, para no perder el motivo
     * real cuando un error tecnico termina convertido en uno del negocio.
     */
    public BibliotecaException(String message, Throwable causa) {
        super(message, causa);
    }

}
