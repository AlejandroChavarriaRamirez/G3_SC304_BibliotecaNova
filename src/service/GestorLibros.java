/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import BibliotecaNova.ArbolLibros;
import BibliotecaNova.Ejemplar;
import BibliotecaNova.Libro;
import exceptions.DatosInvalidosException;
import exceptions.LibroDuplicadoException;

/**
 * Logica del catalogo. Los libros van en un arbol binario de busqueda
 * ordenado por codigo, de ahi que buscar cueste O(log n).
 *
 * @author Grupo 3
 */
public class GestorLibros {

    private ArbolLibros arbol;

    //Constructor
    public GestorLibros() {
        arbol = new ArbolLibros();
    }

    public ArbolLibros getArbol() {
        return arbol;
    }

    //Registra un libro nuevo en el arbol
    public Libro registrarLibro(String codigo, String titulo, String autor, String categoria)
            throws DatosInvalidosException, LibroDuplicadoException {

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DatosInvalidosException("El codigo del libro es obligatorio.");
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new DatosInvalidosException("El titulo del libro es obligatorio.");
        }
        if (autor == null || autor.trim().isEmpty()) {
            throw new DatosInvalidosException("El autor del libro es obligatorio.");
        }

        //Si ese codigo ya esta en el arbol no se puede repetir
        if (arbol.existeLibro(codigo.trim())) {
            throw new LibroDuplicadoException("Ya existe un libro con el codigo " + codigo.trim() + ".");
        }

        Libro libro = new Libro();
        libro.setCodigo(codigo.trim());
        libro.setTitulo(titulo.trim());
        libro.setAutor(autor.trim());
        libro.setCategoria(categoria);
        libro.setCantidadDisponible(0);

        arbol.insertarLibro(libro);
        return libro;
    }

    //Agrega un ejemplar fisico a un libro que ya existe
    public void agregarEjemplar(String codigoLibro, String codigoEjemplar)
            throws DatosInvalidosException {

        if (codigoEjemplar == null || codigoEjemplar.trim().isEmpty()) {
            throw new DatosInvalidosException("El codigo del ejemplar es obligatorio.");
        }

        Libro libro = arbol.buscarLibro(codigoLibro);

        if (libro == null) {
            throw new DatosInvalidosException("No existe un libro con el codigo " + codigoLibro + ".");
        }

        if (libro.getListaEjemplares().buscarEjemplar(codigoEjemplar.trim()) != null) {
            throw new DatosInvalidosException("Ese ejemplar ya esta registrado en el libro.");
        }

        Ejemplar ejemplar = new Ejemplar();
        ejemplar.setCodigoEjemplar(codigoEjemplar.trim());
        ejemplar.setEstado("Disponible");

        libro.getListaEjemplares().agregarEjemplar(ejemplar);
        libro.setCantidadDisponible(libro.getListaEjemplares().contarDisponibles());
    }

    /**
     * El codigo que le toca al proximo libro: L-1, L-2, L-3 y asi.
     */
    public String siguienteCodigo() {
        return "L-" + (arbol.numeroMaximo() + 1);
    }

    public Libro buscarLibro(String codigo) {
        if (codigo == null) {
            return null;
        }
        return arbol.buscarLibro(codigo.trim());
    }

    public String[][] obtenerMatrizLibros() {
        return arbol.obtenerMatriz();
    }

    public int contarLibros() {
        return arbol.contarLibros();
    }

    public int contarEjemplares() {
        return arbol.contarEjemplares();
    }

    public int alturaArbol() {
        return arbol.altura();
    }

    public String recorridoInorden() {
        return arbol.recorridoInorden();
    }

    public String recorridoPreorden() {
        return arbol.recorridoPreorden();
    }

    public String recorridoPostorden() {
        return arbol.recorridoPostorden();
    }

}
