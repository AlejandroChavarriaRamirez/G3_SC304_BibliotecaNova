/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import BibliotecaNova.ColaReservas;
import BibliotecaNova.Devolucion;
import BibliotecaNova.Ejemplar;
import BibliotecaNova.Libro;
import BibliotecaNova.ListaPrestamos;
import BibliotecaNova.NodoPrestamo;
import BibliotecaNova.NodoUsuario;
import BibliotecaNova.PilaDevoluciones;
import BibliotecaNova.Prestamo;
import BibliotecaNova.Reserva;
import BibliotecaNova.Usuario;
import exceptions.DatosInvalidosException;
import exceptions.PrestamoNoPermitidoException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Toda la circulacion: prestamos, devoluciones, reservas y sanciones. Aqui se
 * juntan tres estructuras, la lista de prestamos, la cola de reservas y la pila
 * con el historial de devoluciones.
 *
 * @author Grupo 3
 */
public class GestorPrestamos {

    // Cuanto dura un prestamo
    public static final int DIAS_PRESTAMO = 7;

    // Lo que se cobra por dia de atraso
    public static final double MULTA_POR_DIA = 500;

    // Atrasos que se aguantan antes de suspender al usuario
    public static final int MAX_ATRASOS = 3;

    private ListaPrestamos prestamos;
    private ColaReservas reservas;
    private PilaDevoluciones devoluciones;

    private GestorLibros gestorLibros;
    private GestorUsuarios gestorUsuarios;

    // De aqui salen los codigos P- y R-
    private int consecutivoPrestamo;
    private int consecutivoReserva;

    //Constructor
    public GestorPrestamos(GestorLibros gestorLibros, GestorUsuarios gestorUsuarios) {
        this.gestorLibros = gestorLibros;
        this.gestorUsuarios = gestorUsuarios;
        this.prestamos = new ListaPrestamos();
        this.reservas = new ColaReservas();
        this.devoluciones = new PilaDevoluciones();
        this.consecutivoPrestamo = 0;
        this.consecutivoReserva = 0;
    }

    public ListaPrestamos getPrestamos() {
        return prestamos;
    }

    public int getConsecutivoPrestamo() {
        return consecutivoPrestamo;
    }

    public int getConsecutivoReserva() {
        return consecutivoReserva;
    }

    /**
     * Deja los consecutivos donde quedaron la ultima vez, para no repetir
     * codigos al volver a abrir.
     */
    public void restaurarConsecutivos(int ultimoPrestamo, int ultimaReserva) {
        this.consecutivoPrestamo = ultimoPrestamo;
        this.consecutivoReserva = ultimaReserva;
    }

    public ColaReservas getReservas() {
        return reservas;
    }

    public PilaDevoluciones getDevoluciones() {
        return devoluciones;
    }

    /*
     * Busca el libro en el arbol, revisa que el usuario no este suspendido y le
     * entrega el primer ejemplar que encuentre libre.
     */
    public Prestamo registrarPrestamo(String carne, String codigoLibro)
            throws DatosInvalidosException, PrestamoNoPermitidoException {

        Usuario usuario = gestorUsuarios.buscarUsuario(carne);

        if (usuario == null) {
            throw new DatosInvalidosException("No existe un usuario con el carne " + carne + ".");
        }

        if (usuario.getEstado().equalsIgnoreCase("suspendido")) {
            throw new PrestamoNoPermitidoException(
                    "El usuario " + usuario.getNombre() + " esta suspendido y no puede llevar libros.\n"
                    + "Debe reactivarlo desde la pestaña de usuarios.");
        }

        Libro libro = gestorLibros.buscarLibro(codigoLibro);

        if (libro == null) {
            throw new DatosInvalidosException("No existe un libro con el codigo " + codigoLibro + ".");
        }

        Ejemplar ejemplar = libro.getListaEjemplares().buscarDisponible();

        if (ejemplar == null) {
            throw new PrestamoNoPermitidoException(
                    "El libro \"" + libro.getTitulo() + "\" no tiene ejemplares disponibles.\n"
                    + "Puede dejar una reserva en la cola.");
        }

        ejemplar.setEstado("Prestado");
        libro.setCantidadDisponible(libro.getListaEjemplares().contarDisponibles());

        consecutivoPrestamo = consecutivoPrestamo + 1;

        Prestamo prestamo = new Prestamo();
        prestamo.setCodigoPrestamo("P-" + consecutivoPrestamo);
        prestamo.setCarneUsuario(usuario.getCarne());
        prestamo.setCodigoLibro(libro.getCodigo());
        prestamo.setCodigoEjemplar(ejemplar.getCodigoEjemplar());
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaVencimiento(LocalDate.now().plusDays(DIAS_PRESTAMO));
        prestamo.setEstado("Activo");

        prestamos.agregarPrestamo(prestamo);
        return prestamo;
    }

    /*
     * Cuenta los dias de atraso contra la fecha de vencimiento, cobra la multa,
     * deja libre el ejemplar y apila el movimiento en el historial.
     */
    public Devolucion registrarDevolucion(String codigoPrestamo, LocalDate fechaDevolucion)
            throws DatosInvalidosException {

        Prestamo prestamo = prestamos.buscarPrestamo(codigoPrestamo);

        if (prestamo == null) {
            throw new DatosInvalidosException("No existe el prestamo " + codigoPrestamo + ".");
        }

        if (prestamo.getEstado().equals("Devuelto")) {
            throw new DatosInvalidosException("El prestamo " + codigoPrestamo + " ya fue devuelto.");
        }

        if (fechaDevolucion == null) {
            fechaDevolucion = LocalDate.now();
        }

        //Los dias que se paso de la fecha
        int diasAtraso = 0;
        if (fechaDevolucion.isAfter(prestamo.getFechaVencimiento())) {
            diasAtraso = (int) ChronoUnit.DAYS.between(prestamo.getFechaVencimiento(), fechaDevolucion);
        }

        double multa = diasAtraso * MULTA_POR_DIA;

        //El ejemplar vuelve a quedar libre
        Libro libro = gestorLibros.buscarLibro(prestamo.getCodigoLibro());
        if (libro != null) {
            Ejemplar ejemplar = libro.getListaEjemplares().buscarEjemplar(prestamo.getCodigoEjemplar());
            if (ejemplar != null) {
                ejemplar.setEstado("Disponible");
            }
            libro.setCantidadDisponible(libro.getListaEjemplares().contarDisponibles());
        }

        //Se le anota el atraso; con tres el usuario queda suspendido
        Usuario usuario = gestorUsuarios.buscarUsuario(prestamo.getCarneUsuario());
        if (usuario != null && diasAtraso > 0) {
            usuario.setAtrasos(usuario.getAtrasos() + 1);
            if (usuario.getAtrasos() >= MAX_ATRASOS) {
                usuario.setEstado("Suspendido");
            }
        }

        prestamo.setEstado("Devuelto");

        Devolucion devolucion = new Devolucion();
        devolucion.setCodigoPrestamo(prestamo.getCodigoPrestamo());
        devolucion.setCarneUsuario(prestamo.getCarneUsuario());
        devolucion.setCodigoLibro(prestamo.getCodigoLibro());
        devolucion.setCodigoEjemplar(prestamo.getCodigoEjemplar());
        devolucion.setFechaDevolucion(fechaDevolucion);
        devolucion.setDiasAtraso(diasAtraso);
        devolucion.setMulta(multa);

        //El historial es una pila: lo ultimo que entra queda arriba
        devoluciones.apilar(devolucion);

        return devolucion;
    }

    /*
     * Encola una reserva. Tiene sentido cuando el libro no tiene ejemplares
     * libres en ese momento.
     */
    public Reserva registrarReserva(String carne, String codigoLibro)
            throws DatosInvalidosException, PrestamoNoPermitidoException {

        Usuario usuario = gestorUsuarios.buscarUsuario(carne);

        if (usuario == null) {
            throw new DatosInvalidosException("No existe un usuario con el carne " + carne + ".");
        }

        if (usuario.getEstado().equalsIgnoreCase("suspendido")) {
            throw new PrestamoNoPermitidoException(
                    "El usuario " + usuario.getNombre() + " esta suspendido y no puede reservar.");
        }

        Libro libro = gestorLibros.buscarLibro(codigoLibro);

        if (libro == null) {
            throw new DatosInvalidosException("No existe un libro con el codigo " + codigoLibro + ".");
        }

        consecutivoReserva = consecutivoReserva + 1;

        Reserva reserva = new Reserva();
        reserva.setCodigoReserva("R-" + consecutivoReserva);
        reserva.setCarneUsuario(usuario.getCarne());
        reserva.setCodigoLibro(libro.getCodigo());
        reserva.setFechaReserva(LocalDate.now());

        reservas.encolar(reserva);
        return reserva;
    }

    /**
     * Le hace el prestamo al que lleva mas tiempo esperando, siempre que haya
     * un ejemplar libre.
     */
    public Prestamo atenderPrimeraReserva()
            throws DatosInvalidosException, PrestamoNoPermitidoException {

        Reserva reserva = reservas.verFrente();

        if (reserva == null) {
            throw new DatosInvalidosException("La cola de reservas esta vacia.");
        }

        //Si el prestamo falla, la reserva no se saca de la cola
        Prestamo prestamo = registrarPrestamo(reserva.getCarneUsuario(), reserva.getCodigoLibro());

        reservas.desencolar();
        return prestamo;
    }

    /**
     * Atiende al primero que estaba esperando ese libro. Se llama despues de
     * una devolucion, que es cuando queda un ejemplar libre. Si el prestamo no
     * se puede hacer, la reserva se queda donde estaba.
     */
    public Prestamo atenderReservaDeLibro(String codigoLibro)
            throws DatosInvalidosException, PrestamoNoPermitidoException {

        Reserva reserva = reservas.buscarPorLibro(codigoLibro);

        if (reserva == null) {
            throw new DatosInvalidosException("No hay reservas para el libro " + codigoLibro + ".");
        }

        Prestamo prestamo = registrarPrestamo(reserva.getCarneUsuario(), reserva.getCodigoLibro());

        reservas.desencolarPorLibro(codigoLibro);
        return prestamo;
    }

    /**
     * Mira si alguien esta esperando ese libro en la cola.
     */
    public Reserva reservaPendienteDe(String codigoLibro) {
        return reservas.buscarPorLibro(codigoLibro);
    }

    /*
     * Marca como vencidos los que ya pasaron la fecha maxima. Se llama cada vez
     * que se refresca la pantalla.
     */
    public void actualizarEstados() {

        LocalDate hoy = LocalDate.now();
        NodoPrestamo aux = prestamos.getInicio();

        while (aux != null) {
            Prestamo prestamo = aux.getElemento();
            if (!prestamo.getEstado().equals("Devuelto")) {
                if (hoy.isAfter(prestamo.getFechaVencimiento())) {
                    prestamo.setEstado("Vencido");
                } else {
                    prestamo.setEstado("Activo");
                }
            }
            aux = aux.getSiguiente();
        }
    }

    /**
     * Reporte de sanciones. Recorre la lista doble y arma el detalle de
     * atrasos, multas y estado de cada usuario.
     */
    public String calcularSanciones() {

        String texto = "";
        double granTotal = 0;
        NodoUsuario aux = gestorUsuarios.getLista().getInicio();

        while (aux != null) {

            Usuario usuario = aux.getDato();
            double multa = devoluciones.multasDeUsuario(usuario.getCarne());
            granTotal = granTotal + multa;

            texto = texto + usuario.getCarne() + " - " + usuario.getNombre() + "\n"
                    + "   Estado: " + usuario.getEstado()
                    + "   Atrasos: " + usuario.getAtrasos() + " de " + MAX_ATRASOS
                    + "   Multa acumulada: " + String.format("%,.2f", multa) + "\n";

            String detalle = prestamos.recorridoDeUsuario(usuario.getCarne());
            if (detalle.isEmpty()) {
                texto = texto + "   (sin prestamos registrados)\n";
            } else {
                texto = texto + detalle;
            }

            if (usuario.getEstado().equalsIgnoreCase("suspendido")) {
                texto = texto + "   >> SUSPENDIDO por acumular " + MAX_ATRASOS + " atrasos\n";
            }

            texto = texto + "\n";
            aux = aux.getSiguiente();
        }

        texto = texto + "-----------------------------------------------\n"
                + "Total de multas cobradas: " + String.format("%,.2f", granTotal) + " colones\n"
                + "Usuarios suspendidos: " + gestorUsuarios.contarSuspendidos();

        return texto;
    }

    public double totalMultas() {
        return devoluciones.totalMultas();
    }

    public int contarPrestamosVencidos() {
        return prestamos.contarVencidos();
    }

    public int contarPrestamosActivos() {
        return prestamos.contarActivos();
    }

    public String[][] obtenerMatrizPrestamos() {
        return prestamos.obtenerMatriz();
    }

    public String[][] obtenerMatrizReservas() {
        return reservas.obtenerMatriz();
    }

    public String[][] obtenerMatrizDevoluciones() {
        return devoluciones.obtenerMatriz();
    }

}
