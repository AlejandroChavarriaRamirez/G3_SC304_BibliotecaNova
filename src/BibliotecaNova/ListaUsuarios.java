package BibliotecaNova;

/**
 *
 * @author Grupo 3
 */
public class ListaUsuarios {

    private NodoUsuario inicio;
    private NodoUsuario fin;

    public ListaUsuarios() {
        this.inicio = null;
        this.fin = null;
    }

    public boolean vacia() {
        return inicio == null;
    }

    public NodoUsuario getInicio() {
        return inicio;
    }

    public void agregarUsuario(Usuario usuario) {
        NodoUsuario nuevo = new NodoUsuario();
        nuevo.setDato(usuario);
        if (vacia()) {
            inicio = nuevo;
            fin = nuevo;
        } else {
            nuevo.setAnterior(fin);
            fin.setSiguiente(nuevo);
            fin = nuevo;
        }
    }

    public Usuario buscarUsuario(String carne) {
        NodoUsuario aux = inicio;
        while (aux != null) {
            if (aux.getDato().getCarne().equals(carne)) {
                return aux.getDato();
            }
            aux = aux.getSiguiente();
        }
        return null;
    }

    public boolean eliminarUsuario(String carne) {
        NodoUsuario aux = inicio;
        while (aux != null) {
            if (aux.getDato().getCarne().equals(carne)) {
                if (aux.getAnterior() == null) {
                    inicio = aux.getSiguiente();
                } else {
                    aux.getAnterior().setSiguiente(aux.getSiguiente());
                }
                if (aux.getSiguiente() == null) {
                    fin = aux.getAnterior();
                } else {
                    aux.getSiguiente().setAnterior(aux.getAnterior());
                }
                return true;
            }
            aux = aux.getSiguiente();
        }
        return false;
    }

    public int contar() {
        int total = 0;
        NodoUsuario aux = inicio;
        while (aux != null) {
            total = total + 1;
            aux = aux.getSiguiente();
        }
        return total;
    }

    public int contarSuspendidos() {
        int total = 0;
        NodoUsuario aux = inicio;
        while (aux != null) {
            if (aux.getDato().getEstado().equals("Suspendido")) {
                total = total + 1;
            }
            aux = aux.getSiguiente();
        }
        return total;
    }

    public String recorridoInverso() {
        String texto = "";
        NodoUsuario aux = fin;
        while (aux != null) {
            texto = texto + aux.getDato().getCarne() + " - " + aux.getDato().getNombre() + "\n";
            aux = aux.getAnterior();
        }
        return texto;
    }

    public String[][] obtenerMatriz() {
        String[][] datos = new String[contar()][6];
        NodoUsuario aux = inicio;
        int fila = 0;
        while (aux != null) {
            Usuario usuario = aux.getDato();
            datos[fila][0] = usuario.getCarne();
            datos[fila][1] = usuario.getNombre();
            datos[fila][2] = usuario.getCarrera();
            datos[fila][3] = usuario.getTelefono();
            datos[fila][4] = usuario.getEstado();
            datos[fila][5] = String.valueOf(usuario.getAtrasos());
            fila = fila + 1;
            aux = aux.getSiguiente();
        }
        return datos;
    }

}
