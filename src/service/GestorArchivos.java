/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import BibliotecaNova.Devolucion;
import BibliotecaNova.Ejemplar;
import BibliotecaNova.Libro;
import BibliotecaNova.NodoArbol;
import BibliotecaNova.NodoDevolucion;
import BibliotecaNova.NodoEjemplar;
import BibliotecaNova.NodoPrestamo;
import BibliotecaNova.NodoReserva;
import BibliotecaNova.NodoUsuario;
import BibliotecaNova.PilaDevoluciones;
import BibliotecaNova.Prestamo;
import BibliotecaNova.Reserva;
import BibliotecaNova.Usuario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDate;

/**
 * Guarda y carga la informacion en archivos de texto, para que los datos no se
 * pierdan al cerrar el programa.
 *
 * Cada archivo guarda un registro por linea y los campos van separados por
 * punto y coma. Al abrir el sistema las lineas se vuelven a insertar en las
 * estructuras: los libros en el arbol, los usuarios en la lista doble, las
 * reservas en la cola y las devoluciones en la pila.
 *
 * @author Grupo 3
 */
public class GestorArchivos {

    // Carpeta donde quedan los archivos, al lado del proyecto
    public static final String CARPETA = "datos";

    private static final String SEPARADOR = ";";

    private static final String ARCHIVO_LIBROS = "libros.txt";
    private static final String ARCHIVO_EJEMPLARES = "ejemplares.txt";
    private static final String ARCHIVO_USUARIOS = "usuarios.txt";
    private static final String ARCHIVO_PRESTAMOS = "prestamos.txt";
    private static final String ARCHIVO_RESERVAS = "reservas.txt";
    private static final String ARCHIVO_DEVOLUCIONES = "devoluciones.txt";
    private static final String ARCHIVO_CONTADORES = "contadores.txt";

    /**
     * Revisa si ya hay informacion guardada de una sesion anterior.
     */
    public static boolean hayDatosGuardados() {
        return new File(CARPETA, ARCHIVO_LIBROS).exists()
                || new File(CARPETA, ARCHIVO_USUARIOS).exists();
    }

    // =====================================================================
    // GUARDAR
    // =====================================================================

    /**
     * Recorre todas las estructuras y las escribe en los archivos.
     */
    public static void guardar(GestorLibros gestorLibros, GestorUsuarios gestorUsuarios,
            GestorPrestamos gestorPrestamos) {

        try {

            File carpeta = new File(CARPETA);
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            guardarLibros(gestorLibros);
            guardarEjemplares(gestorLibros);
            guardarUsuarios(gestorUsuarios);
            guardarPrestamos(gestorPrestamos);
            guardarReservas(gestorPrestamos);
            guardarDevoluciones(gestorPrestamos);
            guardarContadores(gestorPrestamos);

        } catch (Exception ex) {
            System.out.println("No se pudieron guardar los datos: " + ex.getMessage());
        }
    }

    //Recorrido del arbol para escribir los libros
    private static void guardarLibros(GestorLibros gestorLibros) throws Exception {

        PrintWriter salida = abrir(ARCHIVO_LIBROS);
        escribirLibrosRec(gestorLibros.getArbol().getRaiz(), salida);
        salida.close();
    }

    private static void escribirLibrosRec(NodoArbol nodo, PrintWriter salida) {

        if (nodo == null) {
            return;
        }

        /*
         * Se escribe en preorden a proposito: al volver a leer el archivo los
         * libros entran al arbol en el mismo orden en que estaban y el arbol
         * queda armado igualito que antes.
         */
        Libro libro = nodo.getElemento();
        salida.println(libro.getCodigo() + SEPARADOR + libro.getTitulo() + SEPARADOR
                + libro.getAutor() + SEPARADOR + libro.getCategoria());

        escribirLibrosRec(nodo.getIzquierda(), salida);
        escribirLibrosRec(nodo.getDerecha(), salida);
    }

    //Los ejemplares de cada libro, con el estado en que quedaron
    private static void guardarEjemplares(GestorLibros gestorLibros) throws Exception {

        PrintWriter salida = abrir(ARCHIVO_EJEMPLARES);
        escribirEjemplaresRec(gestorLibros.getArbol().getRaiz(), salida);
        salida.close();
    }

    private static void escribirEjemplaresRec(NodoArbol nodo, PrintWriter salida) {

        if (nodo == null) {
            return;
        }

        Libro libro = nodo.getElemento();
        NodoEjemplar aux = libro.getListaEjemplares().getInicio();

        while (aux != null) {
            salida.println(libro.getCodigo() + SEPARADOR + aux.getElemento().getCodigoEjemplar()
                    + SEPARADOR + aux.getElemento().getEstado());
            aux = aux.getSiguiente();
        }

        escribirEjemplaresRec(nodo.getIzquierda(), salida);
        escribirEjemplaresRec(nodo.getDerecha(), salida);
    }

    //Recorrido de la lista doble
    private static void guardarUsuarios(GestorUsuarios gestorUsuarios) throws Exception {

        PrintWriter salida = abrir(ARCHIVO_USUARIOS);
        NodoUsuario aux = gestorUsuarios.getLista().getInicio();

        while (aux != null) {
            Usuario usuario = aux.getDato();
            salida.println(usuario.getCarne() + SEPARADOR + usuario.getNombre() + SEPARADOR
                    + usuario.getCarrera() + SEPARADOR + usuario.getTelefono() + SEPARADOR
                    + usuario.getEstado() + SEPARADOR + usuario.getAtrasos());
            aux = aux.getSiguiente();
        }

        salida.close();
    }

    //Recorrido de la lista de prestamos
    private static void guardarPrestamos(GestorPrestamos gestorPrestamos) throws Exception {

        PrintWriter salida = abrir(ARCHIVO_PRESTAMOS);
        NodoPrestamo aux = gestorPrestamos.getPrestamos().getInicio();

        while (aux != null) {
            Prestamo prestamo = aux.getElemento();
            salida.println(prestamo.getCodigoPrestamo() + SEPARADOR + prestamo.getCarneUsuario()
                    + SEPARADOR + prestamo.getCodigoLibro() + SEPARADOR + prestamo.getCodigoEjemplar()
                    + SEPARADOR + prestamo.getFechaPrestamo() + SEPARADOR
                    + prestamo.getFechaVencimiento() + SEPARADOR + prestamo.getEstado());
            aux = aux.getSiguiente();
        }

        salida.close();
    }

    //La cola se guarda desde el frente, que es el orden en que se atiende
    private static void guardarReservas(GestorPrestamos gestorPrestamos) throws Exception {

        PrintWriter salida = abrir(ARCHIVO_RESERVAS);
        NodoReserva aux = gestorPrestamos.getReservas().getFrente();

        while (aux != null) {
            Reserva reserva = aux.getElemento();
            salida.println(reserva.getCodigoReserva() + SEPARADOR + reserva.getCarneUsuario()
                    + SEPARADOR + reserva.getCodigoLibro() + SEPARADOR + reserva.getFechaReserva());
            aux = aux.getSiguiente();
        }

        salida.close();
    }

    //La pila se guarda desde el tope, o sea de lo mas reciente a lo mas viejo
    private static void guardarDevoluciones(GestorPrestamos gestorPrestamos) throws Exception {

        PrintWriter salida = abrir(ARCHIVO_DEVOLUCIONES);
        NodoDevolucion aux = gestorPrestamos.getDevoluciones().getTope();

        while (aux != null) {
            Devolucion devolucion = aux.getElemento();
            salida.println(devolucion.getCodigoPrestamo() + SEPARADOR + devolucion.getCarneUsuario()
                    + SEPARADOR + devolucion.getCodigoLibro() + SEPARADOR + devolucion.getCodigoEjemplar()
                    + SEPARADOR + devolucion.getFechaDevolucion() + SEPARADOR
                    + devolucion.getDiasAtraso() + SEPARADOR + devolucion.getMulta());
            aux = aux.getSiguiente();
        }

        salida.close();
    }

    //Ultimos numeros usados en los codigos de prestamo y de reserva
    private static void guardarContadores(GestorPrestamos gestorPrestamos) throws Exception {

        PrintWriter salida = abrir(ARCHIVO_CONTADORES);
        salida.println(gestorPrestamos.getConsecutivoPrestamo() + SEPARADOR
                + gestorPrestamos.getConsecutivoReserva());
        salida.close();
    }

    private static PrintWriter abrir(String nombre) throws Exception {
        return new PrintWriter(new File(CARPETA, nombre), "UTF-8");
    }

    // =====================================================================
    // CARGAR
    // =====================================================================

    /**
     * Lee los archivos y vuelve a armar las estructuras. Devuelve false cuando
     * no habia nada guardado.
     */
    public static boolean cargar(GestorLibros gestorLibros, GestorUsuarios gestorUsuarios,
            GestorPrestamos gestorPrestamos) {

        if (!hayDatosGuardados()) {
            return false;
        }

        try {

            cargarLibros(gestorLibros);
            cargarEjemplares(gestorLibros);
            cargarUsuarios(gestorUsuarios);
            cargarPrestamos(gestorPrestamos);
            cargarReservas(gestorPrestamos);
            cargarDevoluciones(gestorPrestamos);
            cargarContadores(gestorPrestamos);

            return true;

        } catch (Exception ex) {
            System.out.println("No se pudieron leer los datos guardados: " + ex.getMessage());
            return false;
        }
    }

    //Los libros vuelven a entrar al arbol en el mismo orden en que se guardaron
    private static void cargarLibros(GestorLibros gestorLibros) throws Exception {

        BufferedReader entrada = leer(ARCHIVO_LIBROS);
        if (entrada == null) {
            return;
        }

        String linea = entrada.readLine();

        while (linea != null) {
            String[] campos = linea.split(SEPARADOR, -1);
            if (campos.length >= 4) {
                Libro libro = new Libro();
                libro.setCodigo(campos[0]);
                libro.setTitulo(campos[1]);
                libro.setAutor(campos[2]);
                libro.setCategoria(campos[3]);
                libro.setCantidadDisponible(0);
                gestorLibros.getArbol().insertarLibro(libro);
            }
            linea = entrada.readLine();
        }

        entrada.close();
    }

    private static void cargarEjemplares(GestorLibros gestorLibros) throws Exception {

        BufferedReader entrada = leer(ARCHIVO_EJEMPLARES);
        if (entrada == null) {
            return;
        }

        String linea = entrada.readLine();

        while (linea != null) {
            String[] campos = linea.split(SEPARADOR, -1);
            if (campos.length >= 3) {
                Libro libro = gestorLibros.buscarLibro(campos[0]);
                if (libro != null) {
                    Ejemplar ejemplar = new Ejemplar();
                    ejemplar.setCodigoEjemplar(campos[1]);
                    ejemplar.setEstado(campos[2]);
                    libro.getListaEjemplares().agregarEjemplar(ejemplar);
                    //Se recalcula cuantos quedaron libres
                    libro.setCantidadDisponible(libro.getListaEjemplares().contarDisponibles());
                }
            }
            linea = entrada.readLine();
        }

        entrada.close();
    }

    private static void cargarUsuarios(GestorUsuarios gestorUsuarios) throws Exception {

        BufferedReader entrada = leer(ARCHIVO_USUARIOS);
        if (entrada == null) {
            return;
        }

        String linea = entrada.readLine();

        while (linea != null) {
            String[] campos = linea.split(SEPARADOR, -1);
            if (campos.length >= 6) {
                Usuario usuario = new Usuario();
                usuario.setCarne(campos[0]);
                usuario.setNombre(campos[1]);
                usuario.setCarrera(campos[2]);
                usuario.setTelefono(campos[3]);
                usuario.setEstado(campos[4]);
                usuario.setAtrasos(Integer.parseInt(campos[5]));
                gestorUsuarios.getLista().agregarUsuario(usuario);
            }
            linea = entrada.readLine();
        }

        entrada.close();
    }

    private static void cargarPrestamos(GestorPrestamos gestorPrestamos) throws Exception {

        BufferedReader entrada = leer(ARCHIVO_PRESTAMOS);
        if (entrada == null) {
            return;
        }

        String linea = entrada.readLine();

        while (linea != null) {
            String[] campos = linea.split(SEPARADOR, -1);
            if (campos.length >= 7) {
                Prestamo prestamo = new Prestamo();
                prestamo.setCodigoPrestamo(campos[0]);
                prestamo.setCarneUsuario(campos[1]);
                prestamo.setCodigoLibro(campos[2]);
                prestamo.setCodigoEjemplar(campos[3]);
                prestamo.setFechaPrestamo(LocalDate.parse(campos[4]));
                prestamo.setFechaVencimiento(LocalDate.parse(campos[5]));
                prestamo.setEstado(campos[6]);
                gestorPrestamos.getPrestamos().agregarPrestamo(prestamo);
            }
            linea = entrada.readLine();
        }

        entrada.close();
    }

    //Se encolan en el mismo orden en que estaban esperando
    private static void cargarReservas(GestorPrestamos gestorPrestamos) throws Exception {

        BufferedReader entrada = leer(ARCHIVO_RESERVAS);
        if (entrada == null) {
            return;
        }

        String linea = entrada.readLine();

        while (linea != null) {
            String[] campos = linea.split(SEPARADOR, -1);
            if (campos.length >= 4) {
                Reserva reserva = new Reserva();
                reserva.setCodigoReserva(campos[0]);
                reserva.setCarneUsuario(campos[1]);
                reserva.setCodigoLibro(campos[2]);
                reserva.setFechaReserva(LocalDate.parse(campos[3]));
                gestorPrestamos.getReservas().encolar(reserva);
            }
            linea = entrada.readLine();
        }

        entrada.close();
    }

    /*
     * El archivo trae las devoluciones desde el tope. Si se apilaran de una vez
     * quedarian al reves, entonces primero se pasan a una pila auxiliar y de
     * ahi a la pila del sistema, que las deja en el orden correcto.
     */
    private static void cargarDevoluciones(GestorPrestamos gestorPrestamos) throws Exception {

        BufferedReader entrada = leer(ARCHIVO_DEVOLUCIONES);
        if (entrada == null) {
            return;
        }

        PilaDevoluciones auxiliar = new PilaDevoluciones();
        String linea = entrada.readLine();

        while (linea != null) {
            String[] campos = linea.split(SEPARADOR, -1);
            if (campos.length >= 7) {
                Devolucion devolucion = new Devolucion();
                devolucion.setCodigoPrestamo(campos[0]);
                devolucion.setCarneUsuario(campos[1]);
                devolucion.setCodigoLibro(campos[2]);
                devolucion.setCodigoEjemplar(campos[3]);
                devolucion.setFechaDevolucion(LocalDate.parse(campos[4]));
                devolucion.setDiasAtraso(Integer.parseInt(campos[5]));
                devolucion.setMulta(Double.parseDouble(campos[6]));
                auxiliar.apilar(devolucion);
            }
            linea = entrada.readLine();
        }

        entrada.close();

        while (!auxiliar.vacia()) {
            gestorPrestamos.getDevoluciones().apilar(auxiliar.desapilar());
        }
    }

    private static void cargarContadores(GestorPrestamos gestorPrestamos) throws Exception {

        BufferedReader entrada = leer(ARCHIVO_CONTADORES);
        if (entrada == null) {
            return;
        }

        String linea = entrada.readLine();

        if (linea != null) {
            String[] campos = linea.split(SEPARADOR, -1);
            if (campos.length >= 2) {
                gestorPrestamos.restaurarConsecutivos(
                        Integer.parseInt(campos[0]), Integer.parseInt(campos[1]));
            }
        }

        entrada.close();
    }

    private static BufferedReader leer(String nombre) throws Exception {

        File archivo = new File(CARPETA, nombre);

        if (!archivo.exists()) {
            return null;
        }

        return new BufferedReader(new InputStreamReader(new FileInputStream(archivo), "UTF-8"));
    }

}
