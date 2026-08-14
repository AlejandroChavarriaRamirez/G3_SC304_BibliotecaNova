package BibliotecaNova;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javax.swing.JOptionPane;

/**
 *
 * @author aleja
 */

public class ArbolLibros {

    private NodoArbol raiz;

    public ArbolLibros() {
        this.raiz = null;
    }

    public boolean vacio() {
        return raiz == null;
    }

    /**
     * Devuelve la raiz para poder recorrer el arbol desde afuera, que es lo
     * que hace falta a la hora de guardar los libros.
     */
    public NodoArbol getRaiz() {
        return raiz;
    }

    public void insertarLibro(Libro libro) {
        raiz = insertarRec(raiz, libro);
    }

    private NodoArbol insertarRec(NodoArbol nodo, Libro libro) {
        if (nodo == null) {
            NodoArbol nuevo = new NodoArbol();
            nuevo.setElemento(libro);
            return nuevo;
        }
        if (comparar(libro.getCodigo(), nodo.getElemento().getCodigo()) <= 0) {
            nodo.setIzquierda(insertarRec(nodo.getIzquierda(), libro));
        } else {
            nodo.setDerecha(insertarRec(nodo.getDerecha(), libro));
        }
        return nodo;
    }

    /**
     * Compara dos codigos de libro. Si los dos llevan el mismo prefijo y
     * terminan en numero (L-1, L-2, L-10) se comparan los numeros. Comparados
     * como texto, "L-10" caeria antes que "L-2" y el inorden saldria
     * desordenado. Cualquier otro formato se compara como texto normal.
     */
    private int comparar(String codigo1, String codigo2) {

        int numero1 = numeroDe(codigo1);
        int numero2 = numeroDe(codigo2);

        if (numero1 >= 0 && numero2 >= 0 && prefijoDe(codigo1).equals(prefijoDe(codigo2))) {
            if (numero1 < numero2) {
                return -1;
            }
            if (numero1 > numero2) {
                return 1;
            }
            return 0;
        }

        return codigo1.compareTo(codigo2);
    }

    //Lo que va antes del guion, por ejemplo la "L"
    private String prefijoDe(String codigo) {
        int guion = codigo.indexOf('-');
        if (guion < 0) {
            return codigo;
        }
        return codigo.substring(0, guion);
    }

    //El numero de despues del guion; -1 si el codigo no viene en ese formato
    private int numeroDe(String codigo) {

        int guion = codigo.indexOf('-');

        if (guion < 0 || guion == codigo.length() - 1) {
            return -1;
        }

        String numero = codigo.substring(guion + 1).trim();

        for (int i = 0; i < numero.length(); i++) {
            if (!Character.isDigit(numero.charAt(i))) {
                return -1;
            }
        }

        return Integer.parseInt(numero);
    }

    /**
     * Busca el numero de codigo mas alto que hay en el arbol. Con ese sale el
     * consecutivo que se le propone al usuario al registrar un libro.
     */
    public int numeroMaximo() {
        return maximoRec(raiz);
    }

    private int maximoRec(NodoArbol nodo) {

        if (nodo == null) {
            return 0;
        }

        int actual = numeroDe(nodo.getElemento().getCodigo());
        int izquierda = maximoRec(nodo.getIzquierda());
        int derecha = maximoRec(nodo.getDerecha());

        if (izquierda > actual) {
            actual = izquierda;
        }
        if (derecha > actual) {
            actual = derecha;
        }
        return actual;
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
        } else if (comparar(codigo, nodo.getElemento().getCodigo()) < 0) {
            return buscarRec(nodo.getIzquierda(), codigo);
        } else {
            return buscarRec(nodo.getDerecha(), codigo);
        }
    }

    public void mostrarInorden() {
        String texto = mostrarRec(raiz);
        if (texto.equals("")) {
            JOptionPane.showMessageDialog(null, "No hay libros registrados");
        } else {
            JOptionPane.showMessageDialog(null, texto);
        }
    }

    public boolean existeLibro(String codigo) {
        return buscarLibro(codigo) != null;
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

    /**
     * Cuenta los ejemplares de todo el catalogo.
     */
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

    /**
     * El inorden en forma de texto, que es como lo ocupa la interfaz.
     */
    public String recorridoInorden() {
        return mostrarRec(raiz);
    }

    /**
     * Preorden: la raiz, luego el subarbol izquierdo y de ultimo el derecho.
     * Con este se ve como quedo armado el arbol.
     */
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

    /**
     * Postorden: los dos subarboles primero y la raiz de ultima.
     */
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

    //Arma la linea de texto de un libro
    private String linea(NodoArbol nodo) {
        return "Codigo: " + nodo.getElemento().getCodigo()
                + " - Titulo: " + nodo.getElemento().getTitulo()
                + " - Autor: " + nodo.getElemento().getAutor()
                + " - Categoria: " + nodo.getElemento().getCategoria()
                + " - Disponibles: " + nodo.getElemento().getCantidadDisponible() + "\n";
    }

    /**
     * El inorden pasado a matriz para llenar la tabla del catalogo. Como en un
     * arbol de busqueda el inorden ya sale ordenado, la tabla queda ordenada
     * por codigo sin tener que ordenar nada.
     */
    public String[][] obtenerMatriz() {
        String[][] datos = new String[contarLibros()][6];
        int[] fila = new int[1];
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

    /**
     * Altura del arbol. Es el dato con el que se explica por que la busqueda
     * anda en O(log n) y no en O(n).
     */
    public int altura() {
        return alturaRec(raiz);
    }

    private int alturaRec(NodoArbol nodo) {
        if (nodo == null) {
            return 0;
        }
        int izquierda = alturaRec(nodo.getIzquierda());
        int derecha = alturaRec(nodo.getDerecha());
        if (izquierda > derecha) {
            return izquierda + 1;
        }
        return derecha + 1;
    }

    private String mostrarRec(NodoArbol nodo) {
        String texto = "";
        if (nodo != null) {
            texto = texto + mostrarRec(nodo.getIzquierda());
            texto = texto + "Codigo: " + nodo.getElemento().getCodigo()
                    + " - Titulo: " + nodo.getElemento().getTitulo()
                    + " - Autor: " + nodo.getElemento().getAutor()
                    + " - Categoria: " + nodo.getElemento().getCategoria()
                    + " - Disponibles: " + nodo.getElemento().getCantidadDisponible() + "\n";
            texto = texto + mostrarRec(nodo.getDerecha());
        }
        return texto;
    }

}


