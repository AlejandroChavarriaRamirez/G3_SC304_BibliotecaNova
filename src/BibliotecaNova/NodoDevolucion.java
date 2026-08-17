package BibliotecaNova;

/**
 *
 * @author Grupo 3
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
