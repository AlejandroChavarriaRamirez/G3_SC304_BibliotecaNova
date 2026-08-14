/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exceptions;

/**
 * Salta cuando dejan campos vacios o digitan algo que no sirve.
 *
 * @author Grupo 3
 */
public class DatosInvalidosException extends BibliotecaException {

    // Constructor de la excepcion
    public DatosInvalidosException(String message) {
        super(message);
    }

}
