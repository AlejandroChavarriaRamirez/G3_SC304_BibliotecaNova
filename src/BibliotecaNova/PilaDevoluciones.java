package BibliotecaNova;

/**
 *
 * @author Grupo 3
 */
public class PilaDevoluciones {

    private NodoDevolucion tope;

    public PilaDevoluciones() {
        this.tope = null;
    }

    public boolean vacia() {
        return tope == null;
    }

    public void apilar(Devolucion devolucion) {
        NodoDevolucion nuevo = new NodoDevolucion();
        nuevo.setElemento(devolucion);
        nuevo.setSiguiente(tope);
        tope = nuevo;
    }

    public Devolucion desapilar() {
        if (vacia()) {
            return null;
        }
        Devolucion devolucion = tope.getElemento();
        tope = tope.getSiguiente();
        return devolucion;
    }

    public Devolucion verTope() {
        if (vacia()) {
            return null;
        }
        return tope.getElemento();
    }

    public int contar() {
        int total = 0;
        NodoDevolucion aux = tope;
        while (aux != null) {
            total = total + 1;
            aux = aux.getSiguiente();
        }
        return total;
    }

    public double totalMultas() {
        double total = 0;
        NodoDevolucion aux = tope;
        while (aux != null) {
            total = total + aux.getElemento().getMulta();
            aux = aux.getSiguiente();
        }
        return total;
    }

    public double multasDeUsuario(String carne) {
        double total = 0;
        NodoDevolucion aux = tope;
        while (aux != null) {
            if (aux.getElemento().getCarneUsuario().equals(carne)) {
                total = total + aux.getElemento().getMulta();
            }
            aux = aux.getSiguiente();
        }
        return total;
    }

    public String[][] obtenerMatriz() {
        String[][] datos = new String[contar()][6];
        NodoDevolucion aux = tope;
        int fila = 0;
        while (aux != null) {
            Devolucion devolucion = aux.getElemento();
            datos[fila][0] = devolucion.getCodigoPrestamo();
            datos[fila][1] = devolucion.getCarneUsuario();
            datos[fila][2] = devolucion.getCodigoLibro();
            datos[fila][3] = devolucion.getCodigoEjemplar();
            datos[fila][4] = devolucion.getFechaDevolucion();
            datos[fila][5] = devolucion.isAtrasado() ? "Si (" + String.format("%,.2f", devolucion.getMulta()) + ")" : "No";
            fila = fila + 1;
            aux = aux.getSiguiente();
        }
        return datos;
    }

}
