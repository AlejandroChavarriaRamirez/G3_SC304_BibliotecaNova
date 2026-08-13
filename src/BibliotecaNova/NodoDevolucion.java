/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BibliotecaNova;

/**
 *
 * @author aleja
 */
public class NodoDevolucion {

    private Devolucion elemento;
    private NodoDevolucion siguiente;

    public NodoDevolucion() {
        this.elemento = null;
        this.siguiente = null;
    }

    public Devolucion getElemento() {
        return elemento;
    }

    public void setElemento(Devolucion elemento) {
        this.elemento = elemento;
    }

    public NodoDevolucion getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoDevolucion siguiente) {
        this.siguiente = siguiente;
    }

}
