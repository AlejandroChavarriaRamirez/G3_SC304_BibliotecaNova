package BibliotecaNova;

/**
 *
 * @author Grupo 3
 */
public class ArbolLibros {

    private NodoArbol raiz;
    private int totalLibros;

    public ArbolLibros() {
        this.raiz = null;
        this.totalLibros = 0;
    }

    public boolean vacio() {
        return raiz == null;
    }

    public void insertarLibro(Libro libro) {
        raiz = insertarRec(raiz, libro);
        totalLibros = totalLibros + 1;
    }

    private NodoArbol insertarRec(NodoArbol nodo, Libro libro) {
        if (nodo == null) {
            NodoArbol nuevo = new NodoArbol();
            nuevo.setElemento(libro);
            return nuevo;
        }
        if (libro.getCodigo().compareTo(nodo.getElemento().getCodigo()) <= 0) {
            nodo.setIzquierda(insertarRec(nodo.getIzquierda(), libro));
        } else {
            nodo.setDerecha(insertarRec(nodo.getDerecha(), libro));
        }
        return nodo;
    }

    public Libro buscarLibro(String codigo) {
        return buscarRec(raiz, codigo);
    }

    private Libro buscarRec(NodoArbol nodo, String codigo) {
        if (nodo == null) {
            return null;
        }
        if (codigo.equals(nodo.getElemento().getCodigo())) {
            return nodo.getElemento();
        } else if (codigo.compareTo(nodo.getElemento().getCodigo()) < 0) {
            return buscarRec(nodo.getIzquierda(), codigo);
        } else {
            return buscarRec(nodo.getDerecha(), codigo);
        }
    }

    public boolean existeLibro(String codigo) {
        return buscarLibro(codigo) != null;
    }

    public String siguienteCodigo() {
        return "L-" + (totalLibros + 1);
    }

    public int contarLibros() {
        return contarRec(raiz);
    }

    private int contarRec(NodoArbol nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contarRec(nodo.getIzquierda()) + contarRec(nodo.getDerecha());
    }

    public int contarEjemplares() {
        return contarEjemplaresRec(raiz);
    }

    private int contarEjemplaresRec(NodoArbol nodo) {
        if (nodo == null) {
            return 0;
        }
        return nodo.getElemento().getListaEjemplares().contar()
                + contarEjemplaresRec(nodo.getIzquierda())
                + contarEjemplaresRec(nodo.getDerecha());
    }

    public String recorridoInorden() {
        return inordenRec(raiz);
    }

    private String inordenRec(NodoArbol nodo) {
        String texto = "";
        if (nodo != null) {
            texto = texto + inordenRec(nodo.getIzquierda());
            texto = texto + linea(nodo);
            texto = texto + inordenRec(nodo.getDerecha());
        }
        return texto;
    }

    public String recorridoPreorden() {
        return preordenRec(raiz);
    }

    private String preordenRec(NodoArbol nodo) {
        String texto = "";
        if (nodo != null) {
            texto = texto + linea(nodo);
            texto = texto + preordenRec(nodo.getIzquierda());
            texto = texto + preordenRec(nodo.getDerecha());
        }
        return texto;
    }

    public String recorridoPostorden() {
        return postordenRec(raiz);
    }

    private String postordenRec(NodoArbol nodo) {
        String texto = "";
        if (nodo != null) {
            texto = texto + postordenRec(nodo.getIzquierda());
            texto = texto + postordenRec(nodo.getDerecha());
            texto = texto + linea(nodo);
        }
        return texto;
    }

    private String linea(NodoArbol nodo) {
        Libro libro = nodo.getElemento();
        return "Codigo: " + libro.getCodigo()
                + " - Titulo: " + libro.getTitulo()
                + " - Autor: " + libro.getAutor()
                + " - Categoria: " + libro.getCategoria()
                + " - Disponibles: " + libro.getCantidadDisponible() + "\n";
    }

    public String[][] obtenerMatriz() {
        String[][] datos = new String[contarLibros()][6];
        int[] fila = {0};
        llenarMatrizRec(raiz, datos, fila);
        return datos;
    }

    private void llenarMatrizRec(NodoArbol nodo, String[][] datos, int[] fila) {
        if (nodo != null) {
            llenarMatrizRec(nodo.getIzquierda(), datos, fila);
            Libro libro = nodo.getElemento();
            datos[fila[0]][0] = libro.getCodigo();
            datos[fila[0]][1] = libro.getTitulo();
            datos[fila[0]][2] = libro.getAutor();
            datos[fila[0]][3] = libro.getCategoria();
            datos[fila[0]][4] = String.valueOf(libro.getListaEjemplares().contar());
            datos[fila[0]][5] = String.valueOf(libro.getCantidadDisponible());
            fila[0] = fila[0] + 1;
            llenarMatrizRec(nodo.getDerecha(), datos, fila);
        }
    }

}
