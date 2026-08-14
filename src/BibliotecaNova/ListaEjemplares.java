/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BibliotecaNova;

import javax.swing.JOptionPane;

/**
 *
 * @author aleja
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

    public NodoEjemplar getInicio() {
        return inicio;
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

    /**
     * El primer ejemplar que este libre.
     */
    public Ejemplar buscarDisponible() {
        NodoEjemplar aux = inicio;
        while (aux != null) {
            if (aux.getElemento().getEstado().equalsIgnoreCase("Disponible")) {
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
            if (aux.getElemento().getEstado().equalsIgnoreCase("Disponible")) {
                total = total + 1;
            }
            aux = aux.getSiguiente();
        }
        return total;
    }

    /**
     * La lista en texto, como la muestra la interfaz.
     */
    public String recorrido() {
        String texto = "";
        NodoEjemplar aux = inicio;
        while (aux != null) {
            texto = texto + "Ejemplar: " + aux.getElemento().getCodigoEjemplar()
                    + " - Estado: " + aux.getElemento().getEstado() + "\n";
            aux = aux.getSiguiente();
        }
        return texto;
    }

    public void mostrarEjemplares() {
        String texto = "";
        NodoEjemplar aux = inicio;
        while (aux != null) {
            texto = texto + "Codigo: " + aux.getElemento().getCodigoEjemplar()
                    + " - Estado: " + aux.getElemento().getEstado() + "\n";
            aux = aux.getSiguiente();
        }
        if (texto.equals("")) {
            JOptionPane.showMessageDialog(null, "No hay ejemplares registrados");
        } else {
            JOptionPane.showMessageDialog(null, texto);
        }
    }

}
