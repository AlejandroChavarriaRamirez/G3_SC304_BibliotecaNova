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
     * Raiz del arbol. Se ocupa para recorrerlo desde afuera, por ejemplo
     * cuando se guardan los libros en el archivo.
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
     * Compara dos codigos de libro.
     *
     * Cuando los dos traen el mismo prefijo y un numero (L-1, L-2, L-10) la
     * comparacion se hace por el numero. Si se compararan como texto, "L-10"
     * quedaria antes que "L-2" y el recorrido inorden saldria desordenado.
     * Si los codigos no tienen ese formato se comparan como texto normal.
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

    //Parte del codigo que va antes del guion, por ejemplo "L"
    private String prefijoDe(String codigo) {
        int guion = codigo.indexOf('-');
        if (guion < 0) {
            return codigo;
        }
        return codigo.substring(0, guion);
    }

    //Numero que va despues del guion, o -1 si el codigo no tiene ese formato
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
     * Numero de codigo mas alto que hay en el arbol. Sirve para proponer el
     * consecutivo cuando se registra un libro nuevo.
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
     * Suma los ejemplares de todos los libros del catalogo.
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
     * Devuelve el recorrido inorden como texto, para mostrarlo en la interfaz
     * sin abrir la ventana emergente.
     */
    public String recorridoInorden() {
        return mostrarRec(raiz);
    }

    /**
     * Recorrido preorden: primero la raiz, despues el subarbol izquierdo y de
     * ultimo el derecho. Sirve para ver como quedo armado el arbol.
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
     * Recorrido postorden: los dos subarboles y de ultimo la raiz.
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

    //Arma la linea de un libro para los recorridos
    private String linea(NodoArbol nodo) {
        return "Codigo: " + nodo.getElemento().getCodigo()
                + " - Titulo: " + nodo.getElemento().getTitulo()
                + " - Autor: " + nodo.getElemento().getAutor()
                + " - Categoria: " + nodo.getElemento().getCategoria()
                + " - Disponibles: " + nodo.getElemento().getCantidadDisponible() + "\n";
    }

    /**
     * Recorrido inorden pasado a una matriz, para llenar la tabla del catalogo.
     * Como el inorden de un arbol binario de busqueda sale ordenado, la tabla
     * queda ordenada por codigo sin necesidad de ordenarla despues.
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
     * Altura del arbol, sirve para explicar en la defensa por que la busqueda
     * es O(log n) y no O(n).
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


