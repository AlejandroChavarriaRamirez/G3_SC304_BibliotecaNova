/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exceptions;

/**
 * Salta cuando el prestamo no se puede hacer, sea porque el usuario esta
 * suspendido o porque el libro no tiene ejemplares libres.
 *
 * @author Grupo 3
 */
public class PrestamoNoPermitidoException extends BibliotecaException {

    // Constructor de la excepcion
    public PrestamoNoPermitidoException(String message) {
        super(message);
    }

}
