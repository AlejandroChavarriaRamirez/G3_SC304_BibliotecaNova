package BibliotecaNova;

/**
 *
 * @author Grupo 3
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
