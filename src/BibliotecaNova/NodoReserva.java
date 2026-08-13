/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BibliotecaNova;

/**
 *
 * @author aleja
 */
public class NodoReserva {

    private Reserva elemento;
    private NodoReserva siguiente;

    public NodoReserva() {
        this.elemento = null;
        this.siguiente = null;
    }

    public Reserva getElemento() {
        return elemento;
    }

    public void setElemento(Reserva elemento) {
        this.elemento = elemento;
    }

    public NodoReserva getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoReserva siguiente) {
        this.siguiente = siguiente;
    }

}
