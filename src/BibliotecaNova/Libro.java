/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BibliotecaNova;

/**
 *
 * @author aleja
 */
public class Libro {

    private String codigo;
    private String titulo;
    private String autor;
    private String categoria;
    private int cantidadDisponible;
    private ListaEjemplares listaEjemplares;

    public Libro() {
        this.codigo = "";
        this.titulo = "";
        this.autor = "";
        this.categoria = "";
        this.cantidadDisponible = 0;
        this.listaEjemplares = new ListaEjemplares();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public ListaEjemplares getListaEjemplares() {
        return listaEjemplares;
    }

    public void setListaEjemplares(ListaEjemplares listaEjemplares) {
        this.listaEjemplares = listaEjemplares;
    }

}
