/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import BibliotecaNova.Prestamo;
import exceptions.BibliotecaException;
import java.time.LocalDate;

/**
 * Carga unos datos de ejemplo la primera vez que se abre el sistema, para que
 * las tablas no salgan vacias durante la demostracion.
 *
 * @author Grupo 3
 */
public class GestorDatosPrueba {

    public static void cargar(GestorLibros gestorLibros, GestorUsuarios gestorUsuarios,
            GestorPrestamos gestorPrestamos) {

        try {

            cargarLibros(gestorLibros);
            cargarUsuarios(gestorUsuarios);
            cargarMovimientos(gestorPrestamos);

        } catch (BibliotecaException ex) {
            //Si los datos de prueba fallan el sistema igual puede arrancar vacio
            System.out.println("No se pudieron cargar los datos de prueba: " + ex.getMessage());
        }
    }

    /*
     * Los libros NO entran en orden de codigo a proposito. Entran por la mitad
     * del rango (L-7 primero, despues L-3 y L-10, y asi) para que el arbol
     * quede repartido a los dos lados y no como una sola rama. Con 13 libros
     * la altura queda en 4, que es lo minimo posible, y ahi se ve por que la
     * busqueda en el arbol es O(log n).
     */
    private static void cargarLibros(GestorLibros gestorLibros) throws BibliotecaException {

        agregarLibro(gestorLibros, "L-7", "Calculo de una Variable", "James Stewart", "Matematica", 2);

        agregarLibro(gestorLibros, "L-3", "Estructura de Datos en Java", "Luis Joyanes", "Computacion", 2);
        agregarLibro(gestorLibros, "L-1", "Don Quijote de la Mancha", "Miguel de Cervantes", "Literatura", 1);
        agregarLibro(gestorLibros, "L-2", "Cien Anos de Soledad", "Gabriel Garcia Marquez", "Literatura", 1);
        agregarLibro(gestorLibros, "L-5", "Introduccion a los Algoritmos", "Thomas Cormen", "Computacion", 3);
        agregarLibro(gestorLibros, "L-4", "Bases de Datos", "Abraham Silberschatz", "Computacion", 1);
        agregarLibro(gestorLibros, "L-6", "El Principito", "Antoine de Saint-Exupery", "Literatura", 2);

        agregarLibro(gestorLibros, "L-10", "Redes de Computadoras", "Andrew Tanenbaum", "Computacion", 2);
        agregarLibro(gestorLibros, "L-8", "Fisica Universitaria", "Francis Sears", "Referencia", 1);
        agregarLibro(gestorLibros, "L-9", "Historia de Costa Rica", "Ivan Molina", "Historia", 1);
        agregarLibro(gestorLibros, "L-12", "Algebra Lineal", "Stanley Grossman", "Matematica", 2);
        agregarLibro(gestorLibros, "L-11", "La Casa de los Espiritus", "Isabel Allende", "Literatura", 1);
        agregarLibro(gestorLibros, "L-13", "Sistemas Operativos", "William Stallings", "Computacion", 1);
    }

    //Registra el libro y de una vez sus ejemplares: E-1-1, E-1-2, ...
    private static void agregarLibro(GestorLibros gestorLibros, String codigo, String titulo,
            String autor, String categoria, int cantidadEjemplares) throws BibliotecaException {

        gestorLibros.registrarLibro(codigo, titulo, autor, categoria);

        String numero = codigo.replace("L-", "");

        for (int i = 1; i <= cantidadEjemplares; i++) {
            gestorLibros.agregarEjemplar(codigo, "E-" + numero + "-" + i);
        }
    }

    private static void cargarUsuarios(GestorUsuarios gestorUsuarios) throws BibliotecaException {

        gestorUsuarios.registrarUsuario("2024001", "Alejandro Chavarria Ramirez", "Ingenieria de Software", "8888-1111");
        gestorUsuarios.registrarUsuario("2024002", "Andres Brenes Herrera", "Ingenieria de Software", "8888-2222");
        gestorUsuarios.registrarUsuario("2024003", "Saul Amir Alvarado Montero", "Ingenieria de Software", "8888-3333");
        gestorUsuarios.registrarUsuario("2024004", "Josel Pablo Vargas Calderon", "Ingenieria de Software", "8888-4444");
    }

    /*
     * Movimientos de ejemplo: quedan 3 prestamos sin devolver (uno de ellos ya
     * vencido) y 2 devoluciones en la pila, una entregada a tiempo y la otra
     * con atraso para que se vea el cobro de la multa.
     */
    private static void cargarMovimientos(GestorPrestamos gestorPrestamos) throws BibliotecaException {

        LocalDate hoy = LocalDate.now();

        //Devolucion 1: entregado a tiempo, sin multa
        Prestamo prestamo1 = gestorPrestamos.registrarPrestamo("2024001", "L-5");
        prestamo1.setFechaPrestamo(hoy.minusDays(5));
        prestamo1.setFechaVencimiento(hoy.plusDays(2));
        gestorPrestamos.registrarDevolucion(prestamo1.getCodigoPrestamo(), hoy);

        //Devolucion 2: entregado 8 dias tarde, se le cobra la multa al usuario
        Prestamo prestamo2 = gestorPrestamos.registrarPrestamo("2024002", "L-3");
        prestamo2.setFechaPrestamo(hoy.minusDays(15));
        prestamo2.setFechaVencimiento(hoy.minusDays(8));
        gestorPrestamos.registrarDevolucion(prestamo2.getCodigoPrestamo(), hoy);

        //Prestamo al dia
        Prestamo prestamo3 = gestorPrestamos.registrarPrestamo("2024003", "L-1");
        prestamo3.setFechaPrestamo(hoy.minusDays(2));
        prestamo3.setFechaVencimiento(hoy.plusDays(5));

        //Prestamo que ya paso la fecha maxima: sale como Vencido
        Prestamo prestamo4 = gestorPrestamos.registrarPrestamo("2024004", "L-10");
        prestamo4.setFechaPrestamo(hoy.minusDays(12));
        prestamo4.setFechaVencimiento(hoy.minusDays(5));

        //Prestamo de hoy
        gestorPrestamos.registrarPrestamo("2024001", "L-7");

        //Deja marcados los que ya estan vencidos
        gestorPrestamos.actualizarEstados();
    }

}
