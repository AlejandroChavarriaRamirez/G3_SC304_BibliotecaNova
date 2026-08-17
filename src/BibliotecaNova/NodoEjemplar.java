package BibliotecaNova;

/**
 *
 * @author Grupo 3
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
