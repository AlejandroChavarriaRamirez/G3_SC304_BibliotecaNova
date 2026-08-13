/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BibliotecaNova;

/**
 * Pila LIFO con el historial de devoluciones. La ultima devolucion registrada
 * es la primera que aparece cuando se consulta el historial.
 *
 * @author aleja
 */
public class PilaDevoluciones {

    private NodoDevolucion tope;

    public PilaDevoluciones() {
        this.tope = null;
    }

    public boolean vacia() {
        return tope == null;
    }

    public NodoDevolucion getTope() {
        return tope;
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

    /**
     * Suma de todas las multas cobradas por atraso.
     */
    public double totalMultas() {
        double total = 0;
        NodoDevolucion aux = tope;
        while (aux != null) {
            total = total + aux.getElemento().getMulta();
            aux = aux.getSiguiente();
        }
        return total;
    }

    /**
     * Multas que acumula un usuario, recorriendo la pila sin desarmarla.
     */
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

    /**
     * Matriz con el historial, del movimiento mas reciente al mas antiguo.
     */
    public String[][] obtenerMatriz() {
        String[][] datos = new String[contar()][7];
        NodoDevolucion aux = tope;
        int fila = 0;
        while (aux != null) {
            Devolucion devolucion = aux.getElemento();
            datos[fila][0] = devolucion.getCodigoPrestamo();
            datos[fila][1] = devolucion.getCarneUsuario();
            datos[fila][2] = devolucion.getCodigoLibro();
            datos[fila][3] = devolucion.getCodigoEjemplar();
            datos[fila][4] = devolucion.getFechaDevolucion().toString();
            datos[fila][5] = String.valueOf(devolucion.getDiasAtraso());
            datos[fila][6] = String.format("%,.2f", devolucion.getMulta());
            fila = fila + 1;
            aux = aux.getSiguiente();
        }
        return datos;
    }

}
