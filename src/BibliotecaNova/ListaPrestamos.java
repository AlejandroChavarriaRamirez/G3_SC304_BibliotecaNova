package BibliotecaNova;

/**
 *
 * @author Grupo 3
 */
public class ListaPrestamos {

    private NodoPrestamo inicio;

    public ListaPrestamos() {
        this.inicio = null;
    }

    public boolean vacia() {
        return inicio == null;
    }

    public NodoPrestamo getInicio() {
        return inicio;
    }

    public void agregarPrestamo(Prestamo prestamo) {
        NodoPrestamo nuevo = new NodoPrestamo();
        nuevo.setElemento(prestamo);
        nuevo.setSiguiente(null);
        if (vacia()) {
            inicio = nuevo;
        } else {
            NodoPrestamo aux = inicio;
            while (aux.getSiguiente() != null) {
                aux = aux.getSiguiente();
            }
            aux.setSiguiente(nuevo);
        }
    }

    public Prestamo buscarPrestamo(String codigoPrestamo) {
        NodoPrestamo aux = inicio;
        while (aux != null) {
            if (aux.getElemento().getCodigoPrestamo().equals(codigoPrestamo)) {
                return aux.getElemento();
            }
            aux = aux.getSiguiente();
        }
        return null;
    }

    public int contar() {
        int total = 0;
        NodoPrestamo aux = inicio;
        while (aux != null) {
            total = total + 1;
            aux = aux.getSiguiente();
        }
        return total;
    }

    public int contarActivos() {
        int total = 0;
        NodoPrestamo aux = inicio;
        while (aux != null) {
            if (!aux.getElemento().getEstado().equals("Devuelto")) {
                total = total + 1;
            }
            aux = aux.getSiguiente();
        }
        return total;
    }

    public int contarVencidos() {
        int total = 0;
        NodoPrestamo aux = inicio;
        while (aux != null) {
            if (aux.getElemento().getEstado().equals("Vencido")) {
                total = total + 1;
            }
            aux = aux.getSiguiente();
        }
        return total;
    }

    public String recorridoDeUsuario(String carne) {
        String texto = "";
        NodoPrestamo aux = inicio;
        while (aux != null) {
            if (aux.getElemento().getCarneUsuario().equals(carne)) {
                texto = texto + "   " + aux.getElemento().getCodigoPrestamo()
                        + "  libro " + aux.getElemento().getCodigoLibro()
                        + "  vence " + aux.getElemento().getFechaVencimiento()
                        + "  estado " + aux.getElemento().getEstado() + "\n";
            }
            aux = aux.getSiguiente();
        }
        return texto;
    }

    public String[][] obtenerMatriz() {
        String[][] datos = new String[contar()][7];
        NodoPrestamo aux = inicio;
        int fila = 0;
        while (aux != null) {
            Prestamo prestamo = aux.getElemento();
            datos[fila][0] = prestamo.getCodigoPrestamo();
            datos[fila][1] = prestamo.getCarneUsuario();
            datos[fila][2] = prestamo.getCodigoLibro();
            datos[fila][3] = prestamo.getCodigoEjemplar();
            datos[fila][4] = prestamo.getFechaPrestamo();
            datos[fila][5] = prestamo.getFechaVencimiento();
            datos[fila][6] = prestamo.getEstado();
            fila = fila + 1;
            aux = aux.getSiguiente();
        }
        return datos;
    }

}
