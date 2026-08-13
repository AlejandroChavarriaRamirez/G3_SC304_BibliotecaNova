/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exceptions;

/**
 * Se lanza cuando se intenta insertar en el arbol un codigo de libro que ya
 * esta registrado.
 *
 * @author Grupo 3
 */
public class LibroDuplicadoException extends BibliotecaException {

    // Constructor de la excepcion
    public LibroDuplicadoException(String message) {
        super(message);
    }

}
