/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bibliotecanova;

/**
 *
 * @author aleja
 */
public class NodoEjemplar {

    private Ejemplar elemento;
    private NodoEjemplar siguiente;

    public NodoEjemplar() {
        this.elemento = null;
        this.siguiente = null;
    }

    public Ejemplar getElemento() {
        return elemento;
    }

    public void setElemento(Ejemplar elemento) {
        this.elemento = elemento;
    }

    public NodoEjemplar getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoEjemplar siguiente) {
        this.siguiente = siguiente;
    }

}
