/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

import BibliotecaNova.ArbolLibros;
import BibliotecaNova.ColaReservas;
import BibliotecaNova.Devolucion;
import BibliotecaNova.Ejemplar;
import BibliotecaNova.Libro;
import BibliotecaNova.ListaPrestamos;
import BibliotecaNova.ListaUsuarios;
import BibliotecaNova.NodoPrestamo;
import BibliotecaNova.NodoUsuario;
import BibliotecaNova.PilaDevoluciones;
import BibliotecaNova.Prestamo;
import BibliotecaNova.Reserva;
import BibliotecaNova.Usuario;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Grupo 3
 */
public class Principal extends javax.swing.JFrame {

    public static final int DIAS_PRESTAMO = 7;
    public static final double MULTA_POR_ATRASO = 3500;
    private static final int MAX_ATRASOS = 3;

    private ArbolLibros arbol;
    private ListaUsuarios usuarios;
    private ListaPrestamos prestamos;
    private ColaReservas reservas;
    private PilaDevoluciones devoluciones;

    private int consecutivoPrestamo;
    private int consecutivoReserva;

    private DefaultTableModel modeloLibros;
    private DefaultTableModel modeloUsuarios;
    private DefaultTableModel modeloPrestamos;
    private DefaultTableModel modeloReservas;
    private DefaultTableModel modeloDevoluciones;

    public Principal() {

        initComponents();

        arbol = new ArbolLibros();
        usuarios = new ListaUsuarios();
        prestamos = new ListaPrestamos();
        reservas = new ColaReservas();
        devoluciones = new PilaDevoluciones();
        consecutivoPrestamo = 0;
        consecutivoReserva = 0;

        setTitle("Biblioteca Nova - Sistema de Prestamos");
        setLocationRelativeTo(null);

        setIconImage(new javax.swing.ImageIcon(
                getClass().getResource("/imagenes/LogoBibliotecaNova.png")).getImage());

        fijarBarras();

        cargarTablas();
        actualizarTablas();
    }

    /*
     * Esto va aparte del codigo del disenador: el editor visual vuelve a
     * generar initComponents cada vez que se mueve algo en la pestana Design,
     * y ahi se pierde la propiedad floatable.
     */
    private void fijarBarras() {
        tbCatalogo.setFloatable(false);
        tbUsuarios.setFloatable(false);
        tbPrestamos.setFloatable(false);
        tbReservas.setFloatable(false);
        tbDevoluciones.setFloatable(false);
        tbGeneral.setFloatable(false);
    }

    //Arma las columnas de cada tabla
    private void cargarTablas() {

        String[] columnasLibros = {"Código", "Título", "Autor", "Categoría", "Ejemplares", "Disponibles"};
        modeloLibros = new DefaultTableModel(columnasLibros, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tblLibros.setModel(modeloLibros);

        String[] columnasUsuarios = {"Carné", "Nombre", "Carrera", "Teléfono", "Estado", "Atrasos"};
        modeloUsuarios = new DefaultTableModel(columnasUsuarios, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tblUsuarios.setModel(modeloUsuarios);

        String[] columnasPrestamos = {"Préstamo", "Carné", "Libro", "Ejemplar", "Fecha préstamo", "Vencimiento", "Estado"};
        modeloPrestamos = new DefaultTableModel(columnasPrestamos, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tblPrestamos.setModel(modeloPrestamos);

        String[] columnasReservas = {"Turno", "Reserva", "Carné", "Libro", "Fecha"};
        modeloReservas = new DefaultTableModel(columnasReservas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tblReservas.setModel(modeloReservas);

        String[] columnasDevoluciones = {"Préstamo", "Carné", "Libro", "Ejemplar", "Fecha devolución", "Días atraso", "Multa"};
        modeloDevoluciones = new DefaultTableModel(columnasDevoluciones, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tblDevoluciones.setModel(modeloDevoluciones);
    }

    /**
     * Vuelve a recorrer las estructuras y refresca las tablas y los
     * indicadores. Se llama cada vez que un diálogo guarda algo.
     */
    public void actualizarTablas() {

        actualizarEstadosPrestamos();

        llenarTabla(modeloLibros, arbol.obtenerMatriz());
        llenarTabla(modeloUsuarios, usuarios.obtenerMatriz());
        llenarTabla(modeloPrestamos, prestamos.obtenerMatriz());
        llenarTabla(modeloReservas, reservas.obtenerMatriz());
        llenarTabla(modeloDevoluciones, devoluciones.obtenerMatriz());

        lblValLibros.setText(String.valueOf(arbol.contarLibros()));
        lblValEjemplares.setText(String.valueOf(arbol.contarEjemplares()));
        lblValUsuarios.setText(String.valueOf(usuarios.contar()));
        lblValPrestamos.setText(String.valueOf(prestamos.contarActivos()));
        lblValVencidos.setText(String.valueOf(prestamos.contarVencidos()));
        lblValReservas.setText(String.valueOf(reservas.contar()));
        lblValMultas.setText(String.format("%,.0f", devoluciones.totalMultas()));

        lblEstado.setText("  Libros: " + arbol.contarLibros()
                + "   |   Usuarios suspendidos: " + usuarios.contarSuspendidos()
                + "   |   Devoluciones en la pila: " + devoluciones.contar());
    }

    //Marca como vencidos los prestamos que pasaron la fecha maxima
    private void actualizarEstadosPrestamos() {
        String hoy = fechaDeHoy();
        NodoPrestamo aux = prestamos.getInicio();
        while (aux != null) {
            Prestamo prestamo = aux.getElemento();
            if (!prestamo.getEstado().equals("Devuelto")) {
                if (hoy.compareTo(prestamo.getFechaVencimiento()) > 0) {
                    prestamo.setEstado("Vencido");
                } else {
                    prestamo.setEstado("Activo");
                }
            }
            aux = aux.getSiguiente();
        }
    }

    //Fecha de hoy en formato aaaa-mm-dd, para sugerirla en los formularios
    public String fechaDeHoy() {
        return java.time.LocalDate.now().toString();
    }

    //Fecha limite de devolucion: hoy mas los dias de plazo del prestamo
    public String fechaVencimiento(int diasPlazo) {
        return java.time.LocalDate.now().plusDays(diasPlazo).toString();
    }

    //Pasa una matriz a la tabla
    private void llenarTabla(DefaultTableModel modelo, String[][] datos) {
        modelo.setRowCount(0);
        for (int fila = 0; fila < datos.length; fila++) {
            modelo.addRow(datos[fila]);
        }
    }

    //Muestra textos largos, como los recorridos, dentro de un area con scroll
    private void mostrarTexto(String titulo, String texto) {

        if (texto == null || texto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay información para mostrar.", titulo,
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        javax.swing.JTextArea area = new javax.swing.JTextArea(texto, 14, 55);
        area.setEditable(false);
        area.setFont(new java.awt.Font("Consolas", 0, 12));
        area.setCaretPosition(0);

        JOptionPane.showMessageDialog(this, new javax.swing.JScrollPane(area), titulo,
                JOptionPane.INFORMATION_MESSAGE);
    }

    //Devuelve el valor de la primera columna de la fila seleccionada
    private String codigoSeleccionado(javax.swing.JTable tabla) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return null;
        }
        return String.valueOf(tabla.getValueAt(fila, 0));
    }

    //===================== Catalogo de libros (arbol) =====================

    public Libro registrarLibro(String codigo, String titulo, String autor, String categoria) {

        if (codigo == null || codigo.trim().isEmpty()
                || titulo == null || titulo.trim().isEmpty()
                || autor == null || autor.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El codigo, el titulo y el autor son obligatorios.",
                    "Registrar libro", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (arbol.existeLibro(codigo.trim())) {
            JOptionPane.showMessageDialog(this, "Ya existe un libro con el codigo " + codigo.trim() + ".",
                    "Registrar libro", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Libro libro = new Libro();
        libro.setCodigo(codigo.trim());
        libro.setTitulo(titulo.trim());
        libro.setAutor(autor.trim());
        libro.setCategoria(categoria);
        libro.setCantidadDisponible(0);

        arbol.insertarLibro(libro);
        return libro;
    }

    public String siguienteCodigoLibro() {
        return arbol.siguienteCodigo();
    }

    public boolean agregarEjemplar(String codigoLibro, String codigoEjemplar) {

        if (codigoEjemplar == null || codigoEjemplar.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El codigo del ejemplar es obligatorio.",
                    "Agregar ejemplar", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Libro libro = arbol.buscarLibro(codigoLibro);

        if (libro == null) {
            JOptionPane.showMessageDialog(this, "No existe un libro con el codigo " + codigoLibro + ".",
                    "Agregar ejemplar", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (libro.getListaEjemplares().buscarEjemplar(codigoEjemplar.trim()) != null) {
            JOptionPane.showMessageDialog(this, "Ese ejemplar ya esta registrado en el libro.",
                    "Agregar ejemplar", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Ejemplar ejemplar = new Ejemplar();
        ejemplar.setCodigoEjemplar(codigoEjemplar.trim());
        ejemplar.setEstado("Disponible");

        libro.getListaEjemplares().agregarEjemplar(ejemplar);
        libro.setCantidadDisponible(libro.getListaEjemplares().contarDisponibles());
        return true;
    }

    public Libro buscarLibro(String codigo) {
        if (codigo == null) {
            return null;
        }
        return arbol.buscarLibro(codigo.trim());
    }

    //===================== Usuarios (lista doble) =====================

    public Usuario registrarUsuario(String carne, String nombre, String carrera, String telefono) {

        if (carne == null || carne.trim().isEmpty() || nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carne y el nombre son obligatorios.",
                    "Registrar usuario", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (usuarios.buscarUsuario(carne.trim()) != null) {
            JOptionPane.showMessageDialog(this, "Ya existe un usuario con el carne " + carne.trim() + ".",
                    "Registrar usuario", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setCarne(carne.trim());
        usuario.setNombre(nombre.trim());
        usuario.setCarrera(carrera);
        usuario.setTelefono(telefono);
        usuario.setEstado("Activo");
        usuario.setAtrasos(0);

        usuarios.agregarUsuario(usuario);
        return usuario;
    }

    public Usuario buscarUsuario(String carne) {
        if (carne == null) {
            return null;
        }
        return usuarios.buscarUsuario(carne.trim());
    }

    public boolean eliminarUsuario(String carne) {
        if (carne == null) {
            return false;
        }
        return usuarios.eliminarUsuario(carne.trim());
    }

    public Usuario reactivarUsuario(String carne) {

        Usuario usuario = buscarUsuario(carne);

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "No existe un usuario con el carne " + carne + ".",
                    "Reactivar usuario", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (usuario.getEstado().equals("Activo")) {
            JOptionPane.showMessageDialog(this, "El usuario " + usuario.getNombre() + " ya esta activo.",
                    "Reactivar usuario", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        usuario.setEstado("Activo");
        usuario.setAtrasos(0);
        return usuario;
    }

    //===================== Prestamos, reservas y devoluciones =====================

    public Prestamo registrarPrestamo(String carne, String codigoLibro, String fechaPrestamo, String fechaVencimiento) {

        Usuario usuario = buscarUsuario(carne);

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "No existe un usuario con el carne " + carne + ".",
                    "Prestamo", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (usuario.getEstado().equals("Suspendido")) {
            JOptionPane.showMessageDialog(this,
                    "El usuario " + usuario.getNombre() + " esta suspendido y no puede llevar libros.",
                    "Prestamo", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Libro libro = buscarLibro(codigoLibro);

        if (libro == null) {
            JOptionPane.showMessageDialog(this, "No existe un libro con el codigo " + codigoLibro + ".",
                    "Prestamo", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Ejemplar ejemplar = libro.getListaEjemplares().buscarDisponible();

        if (ejemplar == null) {
            JOptionPane.showMessageDialog(this,
                    "El libro \"" + libro.getTitulo() + "\" no tiene ejemplares disponibles.\n"
                    + "Puede dejar una reserva en la cola.",
                    "Prestamo", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        ejemplar.setEstado("Prestado");
        libro.setCantidadDisponible(libro.getListaEjemplares().contarDisponibles());

        consecutivoPrestamo = consecutivoPrestamo + 1;

        Prestamo prestamo = new Prestamo();
        prestamo.setCodigoPrestamo("P-" + consecutivoPrestamo);
        prestamo.setCarneUsuario(usuario.getCarne());
        prestamo.setCodigoLibro(libro.getCodigo());
        prestamo.setCodigoEjemplar(ejemplar.getCodigoEjemplar());
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setFechaVencimiento(fechaVencimiento);
        prestamo.setEstado("Activo");

        prestamos.agregarPrestamo(prestamo);
        return prestamo;
    }

    public Devolucion registrarDevolucion(String codigoPrestamo, String fechaDevolucion) {

        Prestamo prestamo = prestamos.buscarPrestamo(codigoPrestamo);

        if (prestamo == null) {
            JOptionPane.showMessageDialog(this, "No existe el prestamo " + codigoPrestamo + ".",
                    "Devolucion", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (prestamo.getEstado().equals("Devuelto")) {
            JOptionPane.showMessageDialog(this, "El prestamo " + codigoPrestamo + " ya fue devuelto.",
                    "Devolucion", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        boolean atrasado = fechaDevolucion.compareTo(prestamo.getFechaVencimiento()) > 0;
        double multa = atrasado ? MULTA_POR_ATRASO : 0;

        Libro libro = buscarLibro(prestamo.getCodigoLibro());
        if (libro != null) {
            Ejemplar ejemplar = libro.getListaEjemplares().buscarEjemplar(prestamo.getCodigoEjemplar());
            if (ejemplar != null) {
                ejemplar.setEstado("Disponible");
            }
            libro.setCantidadDisponible(libro.getListaEjemplares().contarDisponibles());
        }

        Usuario usuario = buscarUsuario(prestamo.getCarneUsuario());
        if (usuario != null && atrasado) {
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
        devolucion.setAtrasado(atrasado);
        devolucion.setMulta(multa);

        devoluciones.apilar(devolucion);
        return devolucion;
    }

    public Reserva registrarReserva(String carne, String codigoLibro, String fechaReserva) {

        Usuario usuario = buscarUsuario(carne);

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "No existe un usuario con el carne " + carne + ".",
                    "Reserva", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (usuario.getEstado().equals("Suspendido")) {
            JOptionPane.showMessageDialog(this,
                    "El usuario " + usuario.getNombre() + " esta suspendido y no puede reservar.",
                    "Reserva", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Libro libro = buscarLibro(codigoLibro);

        if (libro == null) {
            JOptionPane.showMessageDialog(this, "No existe un libro con el codigo " + codigoLibro + ".",
                    "Reserva", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        consecutivoReserva = consecutivoReserva + 1;

        Reserva reserva = new Reserva();
        reserva.setCodigoReserva("R-" + consecutivoReserva);
        reserva.setCarneUsuario(usuario.getCarne());
        reserva.setCodigoLibro(libro.getCodigo());
        reserva.setFechaReserva(fechaReserva);

        reservas.encolar(reserva);
        return reserva;
    }

    public Prestamo atenderPrimeraReserva() {

        Reserva reserva = reservas.verFrente();

        if (reserva == null) {
            JOptionPane.showMessageDialog(this, "La cola de reservas esta vacia.",
                    "Cola de reservas", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String hoy = fechaDeHoy();
        Prestamo prestamo = registrarPrestamo(reserva.getCarneUsuario(), reserva.getCodigoLibro(), hoy, fechaVencimiento(DIAS_PRESTAMO));

        if (prestamo != null) {
            reservas.desencolar();
        }
        return prestamo;
    }

    public Prestamo atenderReservaDeLibro(String codigoLibro) {

        Reserva reserva = reservas.buscarPorLibro(codigoLibro);

        if (reserva == null) {
            return null;
        }

        String hoy = fechaDeHoy();
        Prestamo prestamo = registrarPrestamo(reserva.getCarneUsuario(), reserva.getCodigoLibro(), hoy, fechaVencimiento(DIAS_PRESTAMO));

        if (prestamo != null) {
            reservas.desencolarPorLibro(codigoLibro);
        }
        return prestamo;
    }

    public Reserva reservaPendienteDe(String codigoLibro) {
        return reservas.buscarPorLibro(codigoLibro);
    }

    public String calcularSanciones() {

        String texto = "";
        double granTotal = 0;
        NodoUsuario aux = usuarios.getInicio();

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

            if (usuario.getEstado().equals("Suspendido")) {
                texto = texto + "   >> SUSPENDIDO por acumular " + MAX_ATRASOS + " atrasos\n";
            }

            texto = texto + "\n";
            aux = aux.getSiguiente();
        }

        texto = texto + "-----------------------------------------------\n"
                + "Total de multas cobradas: " + String.format("%,.2f", granTotal) + " colones\n"
                + "Usuarios suspendidos: " + usuarios.contarSuspendidos();

        return texto;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlEncabezado = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        pnlIndicadores = new javax.swing.JPanel();
        pnlIndLibros = new javax.swing.JPanel();
        lblValLibros = new javax.swing.JLabel();
        pnlIndEjemplares = new javax.swing.JPanel();
        lblValEjemplares = new javax.swing.JLabel();
        pnlIndUsuarios = new javax.swing.JPanel();
        lblValUsuarios = new javax.swing.JLabel();
        pnlIndPrestamos = new javax.swing.JPanel();
        lblValPrestamos = new javax.swing.JLabel();
        pnlIndVencidos = new javax.swing.JPanel();
        lblValVencidos = new javax.swing.JLabel();
        pnlIndReservas = new javax.swing.JPanel();
        lblValReservas = new javax.swing.JLabel();
        pnlIndMultas = new javax.swing.JPanel();
        lblValMultas = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlCatalogo = new javax.swing.JPanel();
        tbCatalogo = new javax.swing.JToolBar();
        btnRegistrarLibro = new javax.swing.JButton();
        btnAgregarEjemplar = new javax.swing.JButton();
        btnRecorridoInorden = new javax.swing.JButton();
        btnRecorridoPreorden = new javax.swing.JButton();
        btnRecorridoPostorden = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLibros = new javax.swing.JTable();
        pnlUsuarios = new javax.swing.JPanel();
        tbUsuarios = new javax.swing.JToolBar();
        btnRegistrarUsuario = new javax.swing.JButton();
        btnReactivarUsuario = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        pnlPrestamos = new javax.swing.JPanel();
        tbPrestamos = new javax.swing.JToolBar();
        btnRegistrarPrestamo = new javax.swing.JButton();
        btnRegistrarDevolucion = new javax.swing.JButton();
        btnCalcularSanciones = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPrestamos = new javax.swing.JTable();
        pnlReservas = new javax.swing.JPanel();
        tbReservas = new javax.swing.JToolBar();
        btnRegistrarReserva = new javax.swing.JButton();
        btnAtenderReserva = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblReservas = new javax.swing.JTable();
        pnlDevoluciones = new javax.swing.JPanel();
        tbDevoluciones = new javax.swing.JToolBar();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblDevoluciones = new javax.swing.JTable();
        pnlPie = new javax.swing.JPanel();
        lblEstado = new javax.swing.JLabel();
        tbGeneral = new javax.swing.JToolBar();
        btnActualizar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        miRegistrarLibro = new javax.swing.JMenuItem();
        miRegistrarUsuario = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miSalir = new javax.swing.JMenuItem();
        menuCirculacion = new javax.swing.JMenu();
        miPrestarLibro = new javax.swing.JMenuItem();
        miDevolverLibro = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        miEncolarReserva = new javax.swing.JMenuItem();
        miAtenderReserva = new javax.swing.JMenuItem();
        menuConsultas = new javax.swing.JMenu();
        miVerPrestamos = new javax.swing.JMenuItem();
        miVerArbol = new javax.swing.JMenuItem();
        miVerDevoluciones = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        miCalcularSanciones = new javax.swing.JMenuItem();
        menuAyuda = new javax.swing.JMenu();
        miAcercaDe = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Biblioteca Nova");
        setMinimumSize(new java.awt.Dimension(1080, 640));

        pnlEncabezado.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 10, 4, 10));
        pnlEncabezado.setLayout(new java.awt.BorderLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/LogoBibliotecaNova.png"))); // NOI18N
        lblTitulo.setText("Biblioteca Nova - Gestión de Préstamos");
        lblTitulo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 8, 0));
        lblTitulo.setIconTextGap(12);
        pnlEncabezado.add(lblTitulo, java.awt.BorderLayout.NORTH);

        pnlIndicadores.setLayout(new java.awt.GridLayout(1, 7));

        pnlIndLibros.setBorder(javax.swing.BorderFactory.createTitledBorder("Libros"));
        pnlIndLibros.setLayout(new java.awt.BorderLayout());

        lblValLibros.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblValLibros.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValLibros.setText("0");
        pnlIndLibros.add(lblValLibros, java.awt.BorderLayout.CENTER);

        pnlIndicadores.add(pnlIndLibros);

        pnlIndEjemplares.setBorder(javax.swing.BorderFactory.createTitledBorder("Ejemplares"));
        pnlIndEjemplares.setLayout(new java.awt.BorderLayout());

        lblValEjemplares.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblValEjemplares.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValEjemplares.setText("0");
        pnlIndEjemplares.add(lblValEjemplares, java.awt.BorderLayout.CENTER);

        pnlIndicadores.add(pnlIndEjemplares);

        pnlIndUsuarios.setBorder(javax.swing.BorderFactory.createTitledBorder("Usuarios"));
        pnlIndUsuarios.setLayout(new java.awt.BorderLayout());

        lblValUsuarios.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblValUsuarios.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValUsuarios.setText("0");
        pnlIndUsuarios.add(lblValUsuarios, java.awt.BorderLayout.CENTER);

        pnlIndicadores.add(pnlIndUsuarios);

        pnlIndPrestamos.setBorder(javax.swing.BorderFactory.createTitledBorder("Préstamos activos"));
        pnlIndPrestamos.setLayout(new java.awt.BorderLayout());

        lblValPrestamos.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblValPrestamos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValPrestamos.setText("0");
        pnlIndPrestamos.add(lblValPrestamos, java.awt.BorderLayout.CENTER);

        pnlIndicadores.add(pnlIndPrestamos);

        pnlIndVencidos.setBorder(javax.swing.BorderFactory.createTitledBorder("Vencidos"));
        pnlIndVencidos.setLayout(new java.awt.BorderLayout());

        lblValVencidos.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblValVencidos.setForeground(new java.awt.Color(153, 0, 0));
        lblValVencidos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValVencidos.setText("0");
        pnlIndVencidos.add(lblValVencidos, java.awt.BorderLayout.CENTER);

        pnlIndicadores.add(pnlIndVencidos);

        pnlIndReservas.setBorder(javax.swing.BorderFactory.createTitledBorder("Reservas en cola"));
        pnlIndReservas.setLayout(new java.awt.BorderLayout());

        lblValReservas.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblValReservas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValReservas.setText("0");
        pnlIndReservas.add(lblValReservas, java.awt.BorderLayout.CENTER);

        pnlIndicadores.add(pnlIndReservas);

        pnlIndMultas.setBorder(javax.swing.BorderFactory.createTitledBorder("Multas cobradas"));
        pnlIndMultas.setLayout(new java.awt.BorderLayout());

        lblValMultas.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblValMultas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValMultas.setText("0");
        pnlIndMultas.add(lblValMultas, java.awt.BorderLayout.CENTER);

        pnlIndicadores.add(pnlIndMultas);

        pnlEncabezado.add(pnlIndicadores, java.awt.BorderLayout.CENTER);

        getContentPane().add(pnlEncabezado, java.awt.BorderLayout.NORTH);

        pnlCatalogo.setLayout(new java.awt.BorderLayout());

        tbCatalogo.setRollover(true);

        btnRegistrarLibro.setText("Registrar libro");
        btnRegistrarLibro.setFocusable(false);
        btnRegistrarLibro.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRegistrarLibro.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRegistrarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarLibroActionPerformed(evt);
            }
        });
        tbCatalogo.add(btnRegistrarLibro);

        btnAgregarEjemplar.setText("Agregar ejemplar");
        btnAgregarEjemplar.setFocusable(false);
        btnAgregarEjemplar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAgregarEjemplar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAgregarEjemplar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarEjemplarActionPerformed(evt);
            }
        });
        tbCatalogo.add(btnAgregarEjemplar);

        btnRecorridoInorden.setText("Inorden");
        btnRecorridoInorden.setFocusable(false);
        btnRecorridoInorden.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRecorridoInorden.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRecorridoInorden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecorridoInordenActionPerformed(evt);
            }
        });
        tbCatalogo.add(btnRecorridoInorden);

        btnRecorridoPreorden.setText("Preorden");
        btnRecorridoPreorden.setFocusable(false);
        btnRecorridoPreorden.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRecorridoPreorden.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRecorridoPreorden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecorridoPreordenActionPerformed(evt);
            }
        });
        tbCatalogo.add(btnRecorridoPreorden);

        btnRecorridoPostorden.setText("Postorden");
        btnRecorridoPostorden.setFocusable(false);
        btnRecorridoPostorden.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRecorridoPostorden.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRecorridoPostorden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecorridoPostordenActionPerformed(evt);
            }
        });
        tbCatalogo.add(btnRecorridoPostorden);

        pnlCatalogo.add(tbCatalogo, java.awt.BorderLayout.NORTH);

        jScrollPane1.setViewportView(tblLibros);

        pnlCatalogo.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Catálogo (árbol)", pnlCatalogo);

        pnlUsuarios.setLayout(new java.awt.BorderLayout());

        tbUsuarios.setRollover(true);

        btnRegistrarUsuario.setText("Registrar usuario");
        btnRegistrarUsuario.setFocusable(false);
        btnRegistrarUsuario.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRegistrarUsuario.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRegistrarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarUsuarioActionPerformed(evt);
            }
        });
        tbUsuarios.add(btnRegistrarUsuario);

        btnReactivarUsuario.setText("Reactivar usuario");
        btnReactivarUsuario.setFocusable(false);
        btnReactivarUsuario.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReactivarUsuario.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReactivarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReactivarUsuarioActionPerformed(evt);
            }
        });
        tbUsuarios.add(btnReactivarUsuario);

        pnlUsuarios.add(tbUsuarios, java.awt.BorderLayout.NORTH);

        jScrollPane2.setViewportView(tblUsuarios);

        pnlUsuarios.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Usuarios (lista doble)", pnlUsuarios);

        pnlPrestamos.setLayout(new java.awt.BorderLayout());

        tbPrestamos.setRollover(true);

        btnRegistrarPrestamo.setText("Registrar préstamo");
        btnRegistrarPrestamo.setFocusable(false);
        btnRegistrarPrestamo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRegistrarPrestamo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRegistrarPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarPrestamoActionPerformed(evt);
            }
        });
        tbPrestamos.add(btnRegistrarPrestamo);

        btnRegistrarDevolucion.setText("Registrar devolución");
        btnRegistrarDevolucion.setFocusable(false);
        btnRegistrarDevolucion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRegistrarDevolucion.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRegistrarDevolucion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarDevolucionActionPerformed(evt);
            }
        });
        tbPrestamos.add(btnRegistrarDevolucion);

        btnCalcularSanciones.setText("Calcular sanciones");
        btnCalcularSanciones.setFocusable(false);
        btnCalcularSanciones.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCalcularSanciones.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCalcularSanciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularSancionesActionPerformed(evt);
            }
        });
        tbPrestamos.add(btnCalcularSanciones);

        pnlPrestamos.add(tbPrestamos, java.awt.BorderLayout.NORTH);

        jScrollPane3.setViewportView(tblPrestamos);

        pnlPrestamos.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Préstamos", pnlPrestamos);

        pnlReservas.setLayout(new java.awt.BorderLayout());

        tbReservas.setRollover(true);

        btnRegistrarReserva.setText("Registrar reserva");
        btnRegistrarReserva.setFocusable(false);
        btnRegistrarReserva.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRegistrarReserva.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRegistrarReserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarReservaActionPerformed(evt);
            }
        });
        tbReservas.add(btnRegistrarReserva);

        btnAtenderReserva.setText("Atender siguiente");
        btnAtenderReserva.setFocusable(false);
        btnAtenderReserva.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAtenderReserva.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAtenderReserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtenderReservaActionPerformed(evt);
            }
        });
        tbReservas.add(btnAtenderReserva);

        pnlReservas.add(tbReservas, java.awt.BorderLayout.NORTH);

        jScrollPane4.setViewportView(tblReservas);

        pnlReservas.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Reservas (cola FIFO)", pnlReservas);

        pnlDevoluciones.setLayout(new java.awt.BorderLayout());

        tbDevoluciones.setRollover(true);
        pnlDevoluciones.add(tbDevoluciones, java.awt.BorderLayout.NORTH);

        jScrollPane5.setViewportView(tblDevoluciones);

        pnlDevoluciones.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Devoluciones (pila LIFO)", pnlDevoluciones);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pnlPie.setLayout(new java.awt.BorderLayout());

        lblEstado.setText("  ");
        pnlPie.add(lblEstado, java.awt.BorderLayout.CENTER);

        tbGeneral.setRollover(true);

        btnActualizar.setText("Actualizar");
        btnActualizar.setFocusable(false);
        btnActualizar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnActualizar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        tbGeneral.add(btnActualizar);

        btnSalir.setText("Salir");
        btnSalir.setFocusable(false);
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        tbGeneral.add(btnSalir);

        pnlPie.add(tbGeneral, java.awt.BorderLayout.EAST);

        getContentPane().add(pnlPie, java.awt.BorderLayout.SOUTH);

        menuArchivo.setText("Archivo");

        miRegistrarLibro.setText("a) Registrar libro");
        miRegistrarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarLibroActionPerformed(evt);
            }
        });
        menuArchivo.add(miRegistrarLibro);

        miRegistrarUsuario.setText("b) Registrar usuario");
        miRegistrarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarUsuarioActionPerformed(evt);
            }
        });
        menuArchivo.add(miRegistrarUsuario);
        menuArchivo.add(jSeparator1);

        miSalir.setText("k) Salir");
        miSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSalirActionPerformed(evt);
            }
        });
        menuArchivo.add(miSalir);

        jMenuBar1.add(menuArchivo);

        menuCirculacion.setText("Circulación");

        miPrestarLibro.setText("c) Prestar libro");
        miPrestarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarPrestamoActionPerformed(evt);
            }
        });
        menuCirculacion.add(miPrestarLibro);

        miDevolverLibro.setText("d) Devolver libro");
        miDevolverLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarDevolucionActionPerformed(evt);
            }
        });
        menuCirculacion.add(miDevolverLibro);
        menuCirculacion.add(jSeparator2);

        miEncolarReserva.setText("g) Agregar usuario a cola de reserva");
        miEncolarReserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarReservaActionPerformed(evt);
            }
        });
        menuCirculacion.add(miEncolarReserva);

        miAtenderReserva.setText("h) Atender reserva");
        miAtenderReserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtenderReservaActionPerformed(evt);
            }
        });
        menuCirculacion.add(miAtenderReserva);

        jMenuBar1.add(menuCirculacion);

        menuConsultas.setText("Consultas");

        miVerPrestamos.setText("e) Ver préstamos activos");
        miVerPrestamos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miVerPrestamosActionPerformed(evt);
            }
        });
        menuConsultas.add(miVerPrestamos);

        miVerArbol.setText("f) Ver árbol de libros");
        miVerArbol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miVerArbolActionPerformed(evt);
            }
        });
        menuConsultas.add(miVerArbol);

        miVerDevoluciones.setText("i) Ver devoluciones recientes");
        miVerDevoluciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miVerDevolucionesActionPerformed(evt);
            }
        });
        menuConsultas.add(miVerDevoluciones);
        menuConsultas.add(jSeparator3);

        miCalcularSanciones.setText("j) Calcular sanciones");
        miCalcularSanciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularSancionesActionPerformed(evt);
            }
        });
        menuConsultas.add(miCalcularSanciones);

        jMenuBar1.add(menuConsultas);

        menuAyuda.setText("Ayuda");

        miAcercaDe.setText("Acerca de");
        miAcercaDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miAcercaDeActionPerformed(evt);
            }
        });
        menuAyuda.add(miAcercaDe);

        jMenuBar1.add(menuAyuda);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Abre el formulario para registrar un libro en el arbol
    private void btnRegistrarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarLibroActionPerformed
        LibroDialog dialogo = new LibroDialog(this, true, this);
        dialogo.setVisible(true);
        actualizarTablas();
    }//GEN-LAST:event_btnRegistrarLibroActionPerformed

    //Agrega un ejemplar al libro seleccionado en la tabla
    private void btnAgregarEjemplarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEjemplarActionPerformed

        String codigo = codigoSeleccionado(tblLibros);

        if (codigo == null) {
            JOptionPane.showMessageDialog(this, "Seleccione primero un libro de la tabla.");
            return;
        }

        EjemplarDialog dialogo = new EjemplarDialog(this, true, this, codigo);
        dialogo.setVisible(true);
        actualizarTablas();
    }//GEN-LAST:event_btnAgregarEjemplarActionPerformed

    //Muestra el recorrido inorden del arbol, que sale ordenado por codigo
    private void btnRecorridoInordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecorridoInordenActionPerformed
        mostrarTexto("Recorrido inorden del árbol", arbol.recorridoInorden());
    }//GEN-LAST:event_btnRecorridoInordenActionPerformed

    //Abre el formulario para registrar un usuario en la lista doble
    private void btnRegistrarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarUsuarioActionPerformed
        UsuarioDialog dialogo = new UsuarioDialog(this, true, this);
        dialogo.setVisible(true);
        actualizarTablas();
    }//GEN-LAST:event_btnRegistrarUsuarioActionPerformed

    //Reactiva manualmente a un usuario que quedo suspendido por atrasos
    private void btnReactivarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReactivarUsuarioActionPerformed

        String carne = codigoSeleccionado(tblUsuarios);

        if (carne == null) {
            JOptionPane.showMessageDialog(this, "Seleccione primero un usuario de la tabla.");
            return;
        }

        Usuario usuario = reactivarUsuario(carne);

        if (usuario != null) {
            JOptionPane.showMessageDialog(this,
                    "El usuario " + usuario.getNombre() + " quedó activo otra vez.\n"
                    + "Se le borraron los atrasos acumulados.",
                    "Reactivar usuario", JOptionPane.INFORMATION_MESSAGE);
            actualizarTablas();
        }
    }//GEN-LAST:event_btnReactivarUsuarioActionPerformed

    //Reporte de sanciones: atrasos, multas y estado de cada usuario
    private void btnCalcularSancionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularSancionesActionPerformed
        mostrarTexto("Cálculo de sanciones", calcularSanciones());
    }//GEN-LAST:event_btnCalcularSancionesActionPerformed

    //Opciones del menu que llevan a la pestaña correspondiente
    private void miVerPrestamosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miVerPrestamosActionPerformed
        actualizarTablas();
        jTabbedPane1.setSelectedComponent(pnlPrestamos);
    }//GEN-LAST:event_miVerPrestamosActionPerformed

    private void miVerArbolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miVerArbolActionPerformed
        jTabbedPane1.setSelectedComponent(pnlCatalogo);
        btnRecorridoInordenActionPerformed(evt);
    }//GEN-LAST:event_miVerArbolActionPerformed

    private void miVerDevolucionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miVerDevolucionesActionPerformed
        actualizarTablas();
        jTabbedPane1.setSelectedComponent(pnlDevoluciones);
    }//GEN-LAST:event_miVerDevolucionesActionPerformed

    //Abre el formulario del prestamo
    private void btnRegistrarPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarPrestamoActionPerformed
        PrestamoDialog dialogo = new PrestamoDialog(this, true, this);
        dialogo.setVisible(true);
        actualizarTablas();
    }//GEN-LAST:event_btnRegistrarPrestamoActionPerformed

    //Abre el formulario de la devolucion, con el prestamo seleccionado
    private void btnRegistrarDevolucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarDevolucionActionPerformed

        String codigo = codigoSeleccionado(tblPrestamos);

        DevolucionDialog dialogo = new DevolucionDialog(this, true, this, codigo);
        dialogo.setVisible(true);
        actualizarTablas();

        /*
         * Cuando el ejemplar regresa hay que atender al primer usuario que lo
         * tenia reservado, que es lo que pide el enunciado.
         */
        if (dialogo.getLibroDevuelto() != null) {
            revisarReservaPendiente(dialogo.getLibroDevuelto());
        }
    }//GEN-LAST:event_btnRegistrarDevolucionActionPerformed

    //Pregunta si se le asigna el libro devuelto al primero de la cola
    private void revisarReservaPendiente(String codigoLibro) {

        Reserva reserva = reservaPendienteDe(codigoLibro);

        if (reserva == null) {
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this,
                "El libro " + codigoLibro + " tiene una reserva pendiente.\n\n"
                + "Reserva: " + reserva.getCodigoReserva() + "\n"
                + "Carné: " + reserva.getCarneUsuario() + "\n"
                + "En cola desde: " + reserva.getFechaReserva() + "\n\n"
                + "¿Desea asignarle el ejemplar ahora?",
                "Cola de reservas", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        Prestamo prestamo = atenderReservaDeLibro(codigoLibro);

        if (prestamo != null) {
            JOptionPane.showMessageDialog(this,
                    "Reserva atendida.\n\n"
                    + "Préstamo: " + prestamo.getCodigoPrestamo() + "\n"
                    + "Carné: " + prestamo.getCarneUsuario() + "\n"
                    + "Ejemplar: " + prestamo.getCodigoEjemplar() + "\n"
                    + "Vence: " + prestamo.getFechaVencimiento(),
                    "Cola de reservas", JOptionPane.INFORMATION_MESSAGE);
            actualizarTablas();
        }
    }

    //Encola una reserva
    private void btnRegistrarReservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarReservaActionPerformed
        ReservaDialog dialogo = new ReservaDialog(this, true, this);
        dialogo.setVisible(true);
        actualizarTablas();
    }//GEN-LAST:event_btnRegistrarReservaActionPerformed

    //Desencola la reserva que lleva mas tiempo esperando y le hace el prestamo
    private void btnAtenderReservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtenderReservaActionPerformed

        Prestamo prestamo = atenderPrimeraReserva();

        if (prestamo != null) {
            JOptionPane.showMessageDialog(this,
                    "Reserva atendida.\n\n"
                    + "Préstamo: " + prestamo.getCodigoPrestamo() + "\n"
                    + "Carné: " + prestamo.getCarneUsuario() + "\n"
                    + "Libro: " + prestamo.getCodigoLibro() + "\n"
                    + "Ejemplar: " + prestamo.getCodigoEjemplar() + "\n"
                    + "Vence: " + prestamo.getFechaVencimiento(),
                    "Cola de reservas", JOptionPane.INFORMATION_MESSAGE);
            actualizarTablas();
        }
    }//GEN-LAST:event_btnAtenderReservaActionPerformed

    //Vuelve a recorrer las estructuras
    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        actualizarTablas();
    }//GEN-LAST:event_btnActualizarActionPerformed

    //Cierra la aplicacion desde la barra de abajo
    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        miSalirActionPerformed(evt);
    }//GEN-LAST:event_btnSalirActionPerformed

    //Cierra la aplicacion
    private void miSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSalirActionPerformed

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Desea salir del sistema?",
                "Salir", JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_miSalirActionPerformed

    //Datos del proyecto
    private void miAcercaDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miAcercaDeActionPerformed
        AcercaDialog dialogo = new AcercaDialog(this, true);
        dialogo.setVisible(true);
    }//GEN-LAST:event_miAcercaDeActionPerformed

    private void btnRecorridoPreordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecorridoPreordenActionPerformed
        // TODO add your handling code here:
        mostrarTexto("Recorrido preorden del árbol", arbol.recorridoPreorden());
    }//GEN-LAST:event_btnRecorridoPreordenActionPerformed

    private void btnRecorridoPostordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecorridoPostordenActionPerformed
        // TODO add your handling code here:
        mostrarTexto("Recorrido postorden del árbol", arbol.recorridoPostorden());
    }//GEN-LAST:event_btnRecorridoPostordenActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregarEjemplar;
    private javax.swing.JButton btnAtenderReserva;
    private javax.swing.JButton btnCalcularSanciones;
    private javax.swing.JButton btnReactivarUsuario;
    private javax.swing.JButton btnRecorridoInorden;
    private javax.swing.JButton btnRecorridoPostorden;
    private javax.swing.JButton btnRecorridoPreorden;
    private javax.swing.JButton btnRegistrarDevolucion;
    private javax.swing.JButton btnRegistrarLibro;
    private javax.swing.JButton btnRegistrarPrestamo;
    private javax.swing.JButton btnRegistrarReserva;
    private javax.swing.JButton btnRegistrarUsuario;
    private javax.swing.JButton btnSalir;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblValEjemplares;
    private javax.swing.JLabel lblValLibros;
    private javax.swing.JLabel lblValMultas;
    private javax.swing.JLabel lblValPrestamos;
    private javax.swing.JLabel lblValReservas;
    private javax.swing.JLabel lblValUsuarios;
    private javax.swing.JLabel lblValVencidos;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenu menuAyuda;
    private javax.swing.JMenu menuCirculacion;
    private javax.swing.JMenu menuConsultas;
    private javax.swing.JMenuItem miAcercaDe;
    private javax.swing.JMenuItem miAtenderReserva;
    private javax.swing.JMenuItem miCalcularSanciones;
    private javax.swing.JMenuItem miDevolverLibro;
    private javax.swing.JMenuItem miEncolarReserva;
    private javax.swing.JMenuItem miPrestarLibro;
    private javax.swing.JMenuItem miRegistrarLibro;
    private javax.swing.JMenuItem miRegistrarUsuario;
    private javax.swing.JMenuItem miSalir;
    private javax.swing.JMenuItem miVerArbol;
    private javax.swing.JMenuItem miVerDevoluciones;
    private javax.swing.JMenuItem miVerPrestamos;
    private javax.swing.JPanel pnlCatalogo;
    private javax.swing.JPanel pnlDevoluciones;
    private javax.swing.JPanel pnlEncabezado;
    private javax.swing.JPanel pnlIndEjemplares;
    private javax.swing.JPanel pnlIndLibros;
    private javax.swing.JPanel pnlIndMultas;
    private javax.swing.JPanel pnlIndPrestamos;
    private javax.swing.JPanel pnlIndReservas;
    private javax.swing.JPanel pnlIndUsuarios;
    private javax.swing.JPanel pnlIndVencidos;
    private javax.swing.JPanel pnlIndicadores;
    private javax.swing.JPanel pnlPie;
    private javax.swing.JPanel pnlPrestamos;
    private javax.swing.JPanel pnlReservas;
    private javax.swing.JPanel pnlUsuarios;
    private javax.swing.JToolBar tbCatalogo;
    private javax.swing.JToolBar tbDevoluciones;
    private javax.swing.JToolBar tbGeneral;
    private javax.swing.JToolBar tbPrestamos;
    private javax.swing.JToolBar tbReservas;
    private javax.swing.JToolBar tbUsuarios;
    private javax.swing.JTable tblDevoluciones;
    private javax.swing.JTable tblLibros;
    private javax.swing.JTable tblPrestamos;
    private javax.swing.JTable tblReservas;
    private javax.swing.JTable tblUsuarios;
    // End of variables declaration//GEN-END:variables
}
