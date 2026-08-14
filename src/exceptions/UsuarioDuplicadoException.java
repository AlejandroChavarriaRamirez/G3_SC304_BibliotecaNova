/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exceptions;

/**
 * Salta al intentar registrar un carne que ya esta en la lista.
 *
 * @author Grupo 3
 */
public class UsuarioDuplicadoException extends BibliotecaException {

    // Constructor de la excepcion
    public UsuarioDuplicadoException(String message) {
        super(message);
    }

}
