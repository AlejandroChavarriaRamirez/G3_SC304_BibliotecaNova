/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exceptions;

/**
 * Se lanza cuando el usuario deja campos vacios o digita datos que no sirven.
 *
 * @author Grupo 3
 */
public class DatosInvalidosException extends BibliotecaException {

    // Constructor de la excepcion
    public DatosInvalidosException(String message) {
        super(message);
    }

}
