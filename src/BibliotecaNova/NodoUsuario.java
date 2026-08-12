/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BibliotecaNova;

/**
 *
 * @author aleja
 */
public class NodoUsuario {

    private Usuario dato;
    private NodoUsuario siguiente;
    private NodoUsuario anterior;

    public NodoUsuario() {
        this.dato = null;
        this.siguiente = null;
        this.anterior = null;
    }

    public Usuario getDato() {
        return dato;
    }

    public void setDato(Usuario dato) {
        this.dato = dato;
    }

    public NodoUsuario getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoUsuario siguiente) {
        this.siguiente = siguiente;
    }

    public NodoUsuario getAnterior() {
        return anterior;
    }

    public void setAnterior(NodoUsuario anterior) {
        this.anterior = anterior;
    }

}
