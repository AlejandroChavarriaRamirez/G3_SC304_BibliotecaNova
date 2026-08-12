package BibliotecaNova;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javax.swing.JOptionPane;

/**
 *
 * @author aleja
 */

public class ArbolLibros {

    private NodoArbol raiz;

    public ArbolLibros() {
        this.raiz = null;
    }

    public boolean vacio() {
        return raiz == null;
    }

    public void insertarLibro(Libro libro) {
        raiz = insertarRec(raiz, libro);
    }

    private NodoArbol insertarRec(NodoArbol nodo, Libro libro) {
        if (nodo == null) {
            NodoArbol nuevo = new NodoArbol();
            nuevo.setElemento(libro);
            return nuevo;
        }
        if (libro.getCodigo().compareTo(nodo.getElemento().getCodigo()) <= 0) {
            nodo.setIzquierda(insertarRec(nodo.getIzquierda(), libro));
        } else {
            nodo.setDerecha(insertarRec(nodo.getDerecha(), libro));
        }
        return nodo;
    }

    public Libro buscarLibro(String codigo) {
        return buscarRec(raiz, codigo);
    }

    private Libro buscarRec(NodoArbol nodo, String codigo) {
        if (nodo == null) {
            return null;
        }
        if (codigo.equals(nodo.getElemento().getCodigo())) {
            return nodo.getElemento();
        } else if (codigo.compareTo(nodo.getElemento().getCodigo()) < 0) {
            return buscarRec(nodo.getIzquierda(), codigo);
        } else {
            return buscarRec(nodo.getDerecha(), codigo);
        }
    }

    public void mostrarInorden() {
        String texto = mostrarRec(raiz);
        if (texto.equals("")) {
            JOptionPane.showMessageDialog(null, "No hay libros registrados");
        } else {
            JOptionPane.showMessageDialog(null, texto);
        }
    }

    private String mostrarRec(NodoArbol nodo) {
        String texto = "";
        if (nodo != null) {
            texto = texto + mostrarRec(nodo.getIzquierda());
            texto = texto + "Codigo: " + nodo.getElemento().getCodigo()
                    + " - Titulo: " + nodo.getElemento().getTitulo()
                    + " - Autor: " + nodo.getElemento().getAutor()
                    + " - Categoria: " + nodo.getElemento().getCategoria()
                    + " - Disponibles: " + nodo.getElemento().getCantidadDisponible() + "\n";
            texto = texto + mostrarRec(nodo.getDerecha());
        }
        return texto;
    }

}


