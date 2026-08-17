package BibliotecaNova;

/**
 *
 * @author Grupo 3
 */
public class NodoArbol {

    private Libro elemento;
    private NodoArbol izquierda;
    private NodoArbol derecha;

    public NodoArbol() {
        this.elemento = null;
        this.izquierda = null;
        this.derecha = null;
    }

    public Libro getElemento() {
        return elemento;
    }

    public void setElemento(Libro elemento) {
        this.elemento = elemento;
    }

    public NodoArbol getIzquierda() {
        return izquierda;
    }

    public void setIzquierda(NodoArbol izquierda) {
        this.izquierda = izquierda;
    }

    public NodoArbol getDerecha() {
        return derecha;
    }

    public void setDerecha(NodoArbol derecha) {
        this.derecha = derecha;
    }

}
