/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BibliotecaNova;

/**
 * Cola FIFO de reservas: el que reserva primero es el que recibe el libro
 * cuando el ejemplar vuelve a la biblioteca.
 *
 * @author aleja
 */
public class ColaReservas {

    private NodoReserva frente;
    private NodoReserva ultimo;

    public ColaReservas() {
        this.frente = null;
        this.ultimo = null;
    }

    public boolean vacia() {
        return frente == null;
    }

    public NodoReserva getFrente() {
        return frente;
    }

    /**
     * Mete la reserva al final de la fila.
     */
    public void encolar(Reserva reserva) {
        NodoReserva nuevo = new NodoReserva();
        nuevo.setElemento(reserva);
        nuevo.setSiguiente(null);
        if (vacia()) {
            frente = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }
    }

    /**
     * Saca la que lleva mas tiempo esperando.
     */
    public Reserva desencolar() {
        if (vacia()) {
            return null;
        }
        Reserva reserva = frente.getElemento();
        frente = frente.getSiguiente();
        if (frente == null) {
            ultimo = null;
        }
        return reserva;
    }

    public Reserva verFrente() {
        if (vacia()) {
            return null;
        }
        return frente.getElemento();
    }

    /**
     * Saca de la fila la reserva mas vieja de ese libro y deja las demas en su
     * orden. Se llama al devolver un ejemplar, cuando hay que entregarselo al
     * usuario que lo pidio primero.
     */
    public Reserva desencolarPorLibro(String codigoLibro) {
        if (vacia()) {
            return null;
        }
        ColaReservas auxiliar = new ColaReservas();
        Reserva encontrada = null;
        while (!vacia()) {
            Reserva reserva = desencolar();
            if (encontrada == null && reserva.getCodigoLibro().equals(codigoLibro)) {
                encontrada = reserva;
            } else {
                auxiliar.encolar(reserva);
            }
        }
        while (!auxiliar.vacia()) {
            encolar(auxiliar.desencolar());
        }
        return encontrada;
    }

    /**
     * Consulta la primera reserva de ese libro, sin sacarla.
     */
    public Reserva buscarPorLibro(String codigoLibro) {
        NodoReserva aux = frente;
        while (aux != null) {
            if (aux.getElemento().getCodigoLibro().equals(codigoLibro)) {
                return aux.getElemento();
            }
            aux = aux.getSiguiente();
        }
        return null;
    }

    public int contar() {
        int total = 0;
        NodoReserva aux = frente;
        while (aux != null) {
            total = total + 1;
            aux = aux.getSiguiente();
        }
        return total;
    }

    /**
     * Las reservas en matriz, en el orden en que se van a atender.
     */
    public String[][] obtenerMatriz() {
        String[][] datos = new String[contar()][5];
        NodoReserva aux = frente;
        int fila = 0;
        while (aux != null) {
            Reserva reserva = aux.getElemento();
            datos[fila][0] = String.valueOf(fila + 1);
            datos[fila][1] = reserva.getCodigoReserva();
            datos[fila][2] = reserva.getCarneUsuario();
            datos[fila][3] = reserva.getCodigoLibro();
            datos[fila][4] = reserva.getFechaReserva().toString();
            fila = fila + 1;
            aux = aux.getSiguiente();
        }
        return datos;
    }

}
