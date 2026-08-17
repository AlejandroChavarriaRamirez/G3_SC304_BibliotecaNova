package BibliotecaNova;

/**
 *
 * @author Grupo 3
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
