/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BibliotecaNova;

/**
 *
 * @author aleja
 */
public class NodoPrestamo {

    private Prestamo elemento;
    private NodoPrestamo siguiente;

    public NodoPrestamo() {
        this.elemento = null;
        this.siguiente = null;
    }

    public Prestamo getElemento() {
        return elemento;
    }

    public void setElemento(Prestamo elemento) {
        this.elemento = elemento;
    }

    public NodoPrestamo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoPrestamo siguiente) {
        this.siguiente = siguiente;
    }

}
