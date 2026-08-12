/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bibliotecanova;

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
