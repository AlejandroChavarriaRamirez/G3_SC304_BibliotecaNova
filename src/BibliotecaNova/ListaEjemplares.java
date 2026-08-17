package BibliotecaNova;

/**
 *
 * @author Grupo 3
 */
public class ListaEjemplares {

    private NodoEjemplar inicio;

    public ListaEjemplares() {
        this.inicio = null;
    }

    public boolean vacia() {
        return inicio == null;
    }

    public void agregarEjemplar(Ejemplar ejemplar) {
        NodoEjemplar nuevo = new NodoEjemplar();
        nuevo.setElemento(ejemplar);
        nuevo.setSiguiente(null);
        if (vacia()) {
            inicio = nuevo;
        } else {
            NodoEjemplar aux = inicio;
            while (aux.getSiguiente() != null) {
                aux = aux.getSiguiente();
            }
            aux.setSiguiente(nuevo);
        }
    }

    public Ejemplar buscarEjemplar(String codigoEjemplar) {
        NodoEjemplar aux = inicio;
        while (aux != null) {
            if (aux.getElemento().getCodigoEjemplar().equals(codigoEjemplar)) {
                return aux.getElemento();
            }
            aux = aux.getSiguiente();
        }
        return null;
    }

    public Ejemplar buscarDisponible() {
        NodoEjemplar aux = inicio;
        while (aux != null) {
            if (aux.getElemento().getEstado().equals("Disponible")) {
                return aux.getElemento();
            }
            aux = aux.getSiguiente();
        }
        return null;
    }

    public int contar() {
        int total = 0;
        NodoEjemplar aux = inicio;
        while (aux != null) {
            total = total + 1;
            aux = aux.getSiguiente();
        }
        return total;
    }

    public int contarDisponibles() {
        int total = 0;
        NodoEjemplar aux = inicio;
        while (aux != null) {
            if (aux.getElemento().getEstado().equals("Disponible")) {
                total = total + 1;
            }
            aux = aux.getSiguiente();
        }
        return total;
    }

    public String listar() {
        String texto = "";
        NodoEjemplar aux = inicio;
        while (aux != null) {
            texto = texto + "Ejemplar: " + aux.getElemento().getCodigoEjemplar()
                    + " - Estado: " + aux.getElemento().getEstado() + "\n";
            aux = aux.getSiguiente();
        }
        return texto;
    }

}
