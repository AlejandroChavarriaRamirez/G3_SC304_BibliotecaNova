/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exceptions;

/**
 * Se lanza cuando el prestamo no se puede hacer: el usuario esta moroso, el
 * libro no tiene ejemplares libres o el ejemplar ya esta prestado.
 *
 * @author Grupo 3
 */
public class PrestamoNoPermitidoException extends BibliotecaException {

    // Constructor de la excepcion
    public PrestamoNoPermitidoException(String message) {
        super(message);
    }

}
