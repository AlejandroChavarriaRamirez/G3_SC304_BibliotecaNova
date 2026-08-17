package BibliotecaNova;

/**
 *
 * @author Grupo 3
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

    public int contar() {
        int total = 0;
        NodoReserva aux = frente;
        while (aux != null) {
            total = total + 1;
            aux = aux.getSiguiente();
        }
        return total;
    }

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
            datos[fila][4] = reserva.getFechaReserva();
            fila = fila + 1;
            aux = aux.getSiguiente();
        }
        return datos;
    }

}
