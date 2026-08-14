/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

import BibliotecaNova.Devolucion;
import BibliotecaNova.Libro;
import BibliotecaNova.Prestamo;
import BibliotecaNova.Reserva;
import BibliotecaNova.Usuario;
import exceptions.BibliotecaException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import service.GestorArchivos;
import service.GestorLibros;
import service.GestorPrestamos;
import service.GestorUsuarios;

/**
 * Ventana principal. Cada pestaña corresponde a una estructura: el catálogo
 * es el árbol de búsqueda, los usuarios van en la lista doble, los préstamos
 * en una lista simple, las reservas en la cola y las devoluciones en la pila.
 *
 * @author Grupo 3
 */
public class Principal extends javax.swing.JFrame {

    private GestorLibros gestorLibros;
    private GestorUsuarios gestorUsuarios;
    private GestorPrestamos gestorPrestamos;

    private DefaultTableModel modeloLibros;
    private DefaultTableModel modeloUsuarios;
    private DefaultTableModel modeloPrestamos;
    private DefaultTableModel modeloReservas;
    private DefaultTableModel modeloDevoluciones;

    /**
     * Creates new form Principal
     */

    //Constructor
    public Principal(GestorLibros gestorLibros, GestorUsuarios gestorUsuarios,
            GestorPrestamos gestorPrestamos) {

        initComponents();

        this.gestorLibros = gestorLibros;
        this.gestorUsuarios = gestorUsuarios;
        this.gestorPrestamos = gestorPrestamos;

        setTitle("Biblioteca Nova - Sistema de Préstamos");
        setLocationRelativeTo(null);

        //Logo de la biblioteca como icono de la ventana
        setIconImage(new javax.swing.ImageIcon(
                getClass().getResource("/imagenes/LogoBibliotecaNova.png")).getImage());

        //Para que las barras no se puedan arrastrar
        fijarBarras();

        //Cerrando con la X tambien se guarda
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                guardarDatos();
            }
        });

        //Arma las columnas y llena las tablas
        cargarTablas();
        actualizarTablas();
    }

    /*
     * Va aparte del codigo del disenador. Cada vez que uno mueve algo en la
     * pestana Design el editor regenera initComponents y ahi se pierde el
     * floatable.
     */
    private void fijarBarras() {
        tbCatalogo.setFloatable(false);
        tbUsuarios.setFloatable(false);
        tbPrestamos.setFloatable(false);
        tbReservas.setFloatable(false);
        tbDevoluciones.setFloatable(false);
        tbGeneral.setFloatable(false);
    }

    //Los titulos de columna de cada tabla
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
     * Recorre otra vez las estructuras y refresca tablas e indicadores. Se
     * llama cada vez que un diálogo guarda algo.
     */
    public void actualizarTablas() {

        //De paso marca los prestamos que ya se pasaron de la fecha
        gestorPrestamos.actualizarEstados();

        llenarTabla(modeloLibros, gestorLibros.obtenerMatrizLibros());
        llenarTabla(modeloUsuarios, gestorUsuarios.obtenerMatrizUsuarios());
        llenarTabla(modeloPrestamos, gestorPrestamos.obtenerMatrizPrestamos());
        llenarTabla(modeloReservas, gestorPrestamos.obtenerMatrizReservas());
        llenarTabla(modeloDevoluciones, gestorPrestamos.obtenerMatrizDevoluciones());

        lblValLibros.setText(String.valueOf(gestorLibros.contarLibros()));
        lblValEjemplares.setText(String.valueOf(gestorLibros.contarEjemplares()));
        lblValUsuarios.setText(String.valueOf(gestorUsuarios.contarUsuarios()));
        lblValPrestamos.setText(String.valueOf(gestorPrestamos.contarPrestamosActivos()));
        lblValVencidos.setText(String.valueOf(gestorPrestamos.contarPrestamosVencidos()));
        lblValReservas.setText(String.valueOf(gestorPrestamos.getReservas().contar()));
        lblValMultas.setText(String.format("%,.0f", gestorPrestamos.totalMultas()));

        lblEstado.setText("  Árbol: " + gestorLibros.contarLibros() + " nodos, altura "
                + gestorLibros.alturaArbol()
                + "   |   Usuarios suspendidos: " + gestorUsuarios.contarSuspendidos()
                + "   |   Devoluciones en la pila: " + gestorPrestamos.getDevoluciones().contar());

        //Cada cambio queda guardado en los archivos
        guardarDatos();
    }

    /**
     * Escribe las estructuras en la carpeta "datos", asi la informacion sigue
     * ahi la proxima vez que se abra el sistema.
     */
    private void guardarDatos() {
        GestorArchivos.guardar(gestorLibros, gestorUsuarios, gestorPrestamos);
    }

    //Vuelca una matriz en la tabla
    private void llenarTabla(DefaultTableModel modelo, String[][] datos) {
        modelo.setRowCount(0);
        for (int fila = 0; fila < datos.length; fila++) {
            modelo.addRow(datos[fila]);
        }
    }

    //Para los textos largos, como los recorridos, con su scroll
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

    //El valor de la primera columna de la fila que este seleccionada
    private String codigoSeleccionado(javax.swing.JTable tabla) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return null;
        }
        return String.valueOf(tabla.getValueAt(fila, 0));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        btnBuscarLibro = new javax.swing.JButton();
        btnVerEjemplares = new javax.swing.JButton();
        btnRecorridoInorden = new javax.swing.JButton();
        btnRecorridoPreorden = new javax.swing.JButton();
        btnRecorridoPostorden = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLibros = new javax.swing.JTable();
        pnlUsuarios = new javax.swing.JPanel();
        tbUsuarios = new javax.swing.JToolBar();
        btnRegistrarUsuario = new javax.swing.JButton();
        btnBuscarUsuario = new javax.swing.JButton();
        btnEliminarUsuario = new javax.swing.JButton();
        btnReactivarUsuario = new javax.swing.JButton();
        btnRecorridoInverso = new javax.swing.JButton();
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
        btnVerFrente = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblReservas = new javax.swing.JTable();
        pnlDevoluciones = new javax.swing.JPanel();
        tbDevoluciones = new javax.swing.JToolBar();
        btnVerTope = new javax.swing.JButton();
        btnDesapilar = new javax.swing.JButton();
        btnTotalMultas = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblDevoluciones = new javax.swing.JTable();
        pnlPie = new javax.swing.JPanel();
        lblEstado = new javax.swing.JLabel();
        tbGeneral = new javax.swing.JToolBar();
        btnActualizar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

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

        btnBuscarLibro.setText("Buscar en el árbol");
        btnBuscarLibro.setFocusable(false);
        btnBuscarLibro.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBuscarLibro.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBuscarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarLibroActionPerformed(evt);
            }
        });
        tbCatalogo.add(btnBuscarLibro);

        btnVerEjemplares.setText("Ver ejemplares");
        btnVerEjemplares.setFocusable(false);
        btnVerEjemplares.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnVerEjemplares.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnVerEjemplares.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerEjemplaresActionPerformed(evt);
            }
        });
        tbCatalogo.add(btnVerEjemplares);

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

        btnBuscarUsuario.setText("Buscar usuario");
        btnBuscarUsuario.setFocusable(false);
        btnBuscarUsuario.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBuscarUsuario.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBuscarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarUsuarioActionPerformed(evt);
            }
        });
        tbUsuarios.add(btnBuscarUsuario);

        btnEliminarUsuario.setText("Eliminar usuario");
        btnEliminarUsuario.setFocusable(false);
        btnEliminarUsuario.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEliminarUsuario.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEliminarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarUsuarioActionPerformed(evt);
            }
        });
        tbUsuarios.add(btnEliminarUsuario);

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

        btnRecorridoInverso.setText("Recorrido inverso");
        btnRecorridoInverso.setFocusable(false);
        btnRecorridoInverso.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRecorridoInverso.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRecorridoInverso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecorridoInversoActionPerformed(evt);
            }
        });
        tbUsuarios.add(btnRecorridoInverso);

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

        btnVerFrente.setText("Ver frente de la cola");
        btnVerFrente.setFocusable(false);
        btnVerFrente.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnVerFrente.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnVerFrente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerFrenteActionPerformed(evt);
            }
        });
        tbReservas.add(btnVerFrente);

        pnlReservas.add(tbReservas, java.awt.BorderLayout.NORTH);

        jScrollPane4.setViewportView(tblReservas);

        pnlReservas.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Reservas (cola FIFO)", pnlReservas);

        pnlDevoluciones.setLayout(new java.awt.BorderLayout());

        tbDevoluciones.setRollover(true);

        btnVerTope.setText("Ver tope de la pila");
        btnVerTope.setFocusable(false);
        btnVerTope.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnVerTope.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnVerTope.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerTopeActionPerformed(evt);
            }
        });
        tbDevoluciones.add(btnVerTope);

        btnDesapilar.setText("Desapilar");
        btnDesapilar.setFocusable(false);
        btnDesapilar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDesapilar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDesapilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesapilarActionPerformed(evt);
            }
        });
        tbDevoluciones.add(btnDesapilar);

        btnTotalMultas.setText("Total de multas");
        btnTotalMultas.setFocusable(false);
        btnTotalMultas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTotalMultas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTotalMultas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTotalMultasActionPerformed(evt);
            }
        });
        tbDevoluciones.add(btnTotalMultas);

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

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Formulario para meter un libro al arbol
    private void btnRegistrarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarLibroActionPerformed
        LibroDialog dialogo = new LibroDialog(this, true, gestorLibros);
        dialogo.setVisible(true);
        actualizarTablas();
    }//GEN-LAST:event_btnRegistrarLibroActionPerformed

    //Le agrega un ejemplar al libro que este seleccionado
    private void btnAgregarEjemplarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEjemplarActionPerformed

        String codigo = codigoSeleccionado(tblLibros);

        if (codigo == null) {
            JOptionPane.showMessageDialog(this, "Seleccione primero un libro de la tabla.");
            return;
        }

        EjemplarDialog dialogo = new EjemplarDialog(this, true, gestorLibros, codigo);
        dialogo.setVisible(true);
        actualizarTablas();
    }//GEN-LAST:event_btnAgregarEjemplarActionPerformed

    //Busca un libro bajando por el arbol
    private void btnBuscarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarLibroActionPerformed

        String codigo = JOptionPane.showInputDialog(this, "Digite el código del libro:", "Buscar en el árbol",
                JOptionPane.QUESTION_MESSAGE);

        if (codigo == null || codigo.trim().isEmpty()) {
            return;
        }

        Libro libro = gestorLibros.buscarLibro(codigo);

        if (libro == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el libro " + codigo + " en el catálogo.",
                    "Buscar", JOptionPane.WARNING_MESSAGE);
            return;
        }

        mostrarTexto("Libro encontrado",
                "Código: " + libro.getCodigo() + "\n"
                + "Título: " + libro.getTitulo() + "\n"
                + "Autor: " + libro.getAutor() + "\n"
                + "Categoría: " + libro.getCategoria() + "\n"
                + "Ejemplares: " + libro.getListaEjemplares().contar() + "\n"
                + "Disponibles: " + libro.getCantidadDisponible() + "\n\n"
                + libro.getListaEjemplares().recorrido());
    }//GEN-LAST:event_btnBuscarLibroActionPerformed

    //Los ejemplares del libro seleccionado
    private void btnVerEjemplaresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerEjemplaresActionPerformed

        String codigo = codigoSeleccionado(tblLibros);

        if (codigo == null) {
            JOptionPane.showMessageDialog(this, "Seleccione primero un libro de la tabla.");
            return;
        }

        Libro libro = gestorLibros.buscarLibro(codigo);
        mostrarTexto("Ejemplares de " + libro.getTitulo(), libro.getListaEjemplares().recorrido());
    }//GEN-LAST:event_btnVerEjemplaresActionPerformed

    //El inorden, que sale ordenado por codigo
    private void btnRecorridoInordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecorridoInordenActionPerformed
        mostrarTexto("Recorrido inorden del árbol  (altura: " + gestorLibros.alturaArbol() + ")",
                gestorLibros.recorridoInorden());
    }//GEN-LAST:event_btnRecorridoInordenActionPerformed

    //Preorden: raiz, izquierda y derecha
    private void btnRecorridoPreordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecorridoPreordenActionPerformed
        mostrarTexto("Recorrido preorden del árbol", gestorLibros.recorridoPreorden());
    }//GEN-LAST:event_btnRecorridoPreordenActionPerformed

    //Postorden: izquierda, derecha y raiz
    private void btnRecorridoPostordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecorridoPostordenActionPerformed
        mostrarTexto("Recorrido postorden del árbol", gestorLibros.recorridoPostorden());
    }//GEN-LAST:event_btnRecorridoPostordenActionPerformed

    //Formulario para meter un usuario a la lista doble
    private void btnRegistrarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarUsuarioActionPerformed
        UsuarioDialog dialogo = new UsuarioDialog(this, true, gestorUsuarios);
        dialogo.setVisible(true);
        actualizarTablas();
    }//GEN-LAST:event_btnRegistrarUsuarioActionPerformed

    //Busca por carne
    private void btnBuscarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarUsuarioActionPerformed

        String carne = JOptionPane.showInputDialog(this, "Digite el carné del usuario:", "Buscar usuario",
                JOptionPane.QUESTION_MESSAGE);

        if (carne == null || carne.trim().isEmpty()) {
            return;
        }

        Usuario usuario = gestorUsuarios.buscarUsuario(carne);

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "No existe un usuario con el carné " + carne + ".",
                    "Buscar", JOptionPane.WARNING_MESSAGE);
            return;
        }

        mostrarTexto("Usuario encontrado",
                "Carné: " + usuario.getCarne() + "\n"
                + "Nombre: " + usuario.getNombre() + "\n"
                + "Carrera: " + usuario.getCarrera() + "\n"
                + "Teléfono: " + usuario.getTelefono() + "\n"
                + "Estado: " + usuario.getEstado() + "\n"
                + "Atrasos acumulados: " + usuario.getAtrasos());
    }//GEN-LAST:event_btnBuscarUsuarioActionPerformed

    //Saca de la lista al usuario seleccionado
    private void btnEliminarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarUsuarioActionPerformed

        String carne = codigoSeleccionado(tblUsuarios);

        if (carne == null) {
            JOptionPane.showMessageDialog(this, "Seleccione primero un usuario de la tabla.");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Seguro que desea eliminar al usuario " + carne + "?",
                "Eliminar usuario", JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            if (gestorUsuarios.eliminarUsuario(carne)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado de la lista.");
                actualizarTablas();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el usuario.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEliminarUsuarioActionPerformed

    //Le levanta la suspension a un usuario
    private void btnReactivarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReactivarUsuarioActionPerformed

        String carne = codigoSeleccionado(tblUsuarios);

        if (carne == null) {
            JOptionPane.showMessageDialog(this, "Seleccione primero un usuario de la tabla.");
            return;
        }

        try {

            Usuario usuario = gestorUsuarios.reactivarUsuario(carne);

            JOptionPane.showMessageDialog(this,
                    "El usuario " + usuario.getNombre() + " quedó activo otra vez.\n"
                    + "Se le borraron los atrasos acumulados.",
                    "Reactivar usuario", JOptionPane.INFORMATION_MESSAGE);

            actualizarTablas();

        } catch (BibliotecaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Reactivar usuario",
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnReactivarUsuarioActionPerformed

    //El reporte con atrasos, multas y estado de cada quien
    private void btnCalcularSancionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularSancionesActionPerformed
        mostrarTexto("Cálculo de sanciones", gestorPrestamos.calcularSanciones());
    }//GEN-LAST:event_btnCalcularSancionesActionPerformed

    //Saca del historial la devolucion mas reciente
    private void btnDesapilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesapilarActionPerformed

        Devolucion devolucion = gestorPrestamos.getDevoluciones().verTope();

        if (devolucion == null) {
            JOptionPane.showMessageDialog(this, "La pila de devoluciones está vacía.");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Sacar del historial la devolución del préstamo "
                + devolucion.getCodigoPrestamo() + "?\n"
                + "Es la operación desapilar de la pila.",
                "Desapilar", JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            gestorPrestamos.getDevoluciones().desapilar();
            actualizarTablas();
        }
    }//GEN-LAST:event_btnDesapilarActionPerformed

    //Estas opciones del menu solo saltan a la pestaña que toca
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

    //Va del final al inicio por la lista doble
    private void btnRecorridoInversoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecorridoInversoActionPerformed
        mostrarTexto("Recorrido inverso de la lista de usuarios", gestorUsuarios.recorridoInverso());
    }//GEN-LAST:event_btnRecorridoInversoActionPerformed

    //Formulario del prestamo
    private void btnRegistrarPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarPrestamoActionPerformed
        PrestamoDialog dialogo = new PrestamoDialog(this, true, gestorPrestamos);
        dialogo.setVisible(true);
        actualizarTablas();
    }//GEN-LAST:event_btnRegistrarPrestamoActionPerformed

    //Formulario de la devolucion, ya con el prestamo seleccionado
    private void btnRegistrarDevolucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarDevolucionActionPerformed

        String codigo = codigoSeleccionado(tblPrestamos);

        DevolucionDialog dialogo = new DevolucionDialog(this, true, gestorPrestamos, codigo);
        dialogo.setVisible(true);
        actualizarTablas();

        /*
         * Cuando el ejemplar regresa hay que atender al primero que lo tenia
         * reservado, tal como lo pide el enunciado.
         */
        if (dialogo.getLibroDevuelto() != null) {
            revisarReservaPendiente(dialogo.getLibroDevuelto());
        }
    }//GEN-LAST:event_btnRegistrarDevolucionActionPerformed

    //Le consulta al bibliotecario si se lo asigna al primero de la cola
    private void revisarReservaPendiente(String codigoLibro) {

        Reserva reserva = gestorPrestamos.reservaPendienteDe(codigoLibro);

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

        try {

            Prestamo prestamo = gestorPrestamos.atenderReservaDeLibro(codigoLibro);

            JOptionPane.showMessageDialog(this,
                    "Reserva atendida.\n\n"
                    + "Préstamo: " + prestamo.getCodigoPrestamo() + "\n"
                    + "Carné: " + prestamo.getCarneUsuario() + "\n"
                    + "Ejemplar: " + prestamo.getCodigoEjemplar() + "\n"
                    + "Vence: " + prestamo.getFechaVencimiento(),
                    "Cola de reservas", JOptionPane.INFORMATION_MESSAGE);

            actualizarTablas();

        } catch (BibliotecaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cola de reservas",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    //Mete una reserva en la cola
    private void btnRegistrarReservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarReservaActionPerformed
        ReservaDialog dialogo = new ReservaDialog(this, true, gestorPrestamos);
        dialogo.setVisible(true);
        actualizarTablas();
    }//GEN-LAST:event_btnRegistrarReservaActionPerformed

    //Le hace el prestamo al que lleva mas tiempo esperando
    private void btnAtenderReservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtenderReservaActionPerformed

        try {

            Prestamo prestamo = gestorPrestamos.atenderPrimeraReserva();

            JOptionPane.showMessageDialog(this,
                    "Reserva atendida.\n\n"
                    + "Préstamo: " + prestamo.getCodigoPrestamo() + "\n"
                    + "Carné: " + prestamo.getCarneUsuario() + "\n"
                    + "Libro: " + prestamo.getCodigoLibro() + "\n"
                    + "Ejemplar: " + prestamo.getCodigoEjemplar() + "\n"
                    + "Vence: " + prestamo.getFechaVencimiento(),
                    "Cola de reservas", JOptionPane.INFORMATION_MESSAGE);

            actualizarTablas();

        } catch (BibliotecaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cola de reservas",
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnAtenderReservaActionPerformed

    //Muestra quien va de primero, sin sacarlo de la cola
    private void btnVerFrenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerFrenteActionPerformed

        Reserva reserva = gestorPrestamos.getReservas().verFrente();

        if (reserva == null) {
            JOptionPane.showMessageDialog(this, "La cola de reservas está vacía.");
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Frente de la cola:\n\n"
                + "Reserva: " + reserva.getCodigoReserva() + "\n"
                + "Carné: " + reserva.getCarneUsuario() + "\n"
                + "Libro: " + reserva.getCodigoLibro() + "\n"
                + "Fecha: " + reserva.getFechaReserva(),
                "Cola de reservas", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnVerFrenteActionPerformed

    //La ultima devolucion registrada, o sea el tope de la pila
    private void btnVerTopeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerTopeActionPerformed

        Devolucion devolucion = gestorPrestamos.getDevoluciones().verTope();

        if (devolucion == null) {
            JOptionPane.showMessageDialog(this, "La pila de devoluciones está vacía.");
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Última devolución registrada:\n\n"
                + "Préstamo: " + devolucion.getCodigoPrestamo() + "\n"
                + "Carné: " + devolucion.getCarneUsuario() + "\n"
                + "Libro: " + devolucion.getCodigoLibro() + "\n"
                + "Ejemplar: " + devolucion.getCodigoEjemplar() + "\n"
                + "Fecha: " + devolucion.getFechaDevolucion() + "\n"
                + "Días de atraso: " + devolucion.getDiasAtraso() + "\n"
                + "Multa: " + String.format("%,.2f", devolucion.getMulta()) + " colones",
                "Pila de devoluciones", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnVerTopeActionPerformed

    //El total de multas de toda la pila
    private void btnTotalMultasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTotalMultasActionPerformed
        JOptionPane.showMessageDialog(this,
                "Devoluciones registradas: " + gestorPrestamos.getDevoluciones().contar() + "\n"
                + "Total de multas cobradas: " + String.format("%,.2f", gestorPrestamos.totalMultas()) + " colones",
                "Sanciones por atraso", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnTotalMultasActionPerformed

    //Refresca todo
    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        actualizarTablas();
    }//GEN-LAST:event_btnActualizarActionPerformed

    //El boton de abajo hace lo mismo que la opcion del menu
    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        miSalirActionPerformed(evt);
    }//GEN-LAST:event_btnSalirActionPerformed

    //Cierra el programa
    private void miSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSalirActionPerformed

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Desea salir del sistema?\nLa información queda guardada en la carpeta \""
                + GestorArchivos.CARPETA + "\".",
                "Salir", JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            guardarDatos();
            System.exit(0);
        }
    }//GEN-LAST:event_miSalirActionPerformed

    //La ventana con los datos del proyecto
    private void miAcercaDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miAcercaDeActionPerformed
        AcercaDialog dialogo = new AcercaDialog(this, true);
        dialogo.setVisible(true);
    }//GEN-LAST:event_miAcercaDeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregarEjemplar;
    private javax.swing.JButton btnAtenderReserva;
    private javax.swing.JButton btnBuscarLibro;
    private javax.swing.JButton btnBuscarUsuario;
    private javax.swing.JButton btnCalcularSanciones;
    private javax.swing.JButton btnDesapilar;
    private javax.swing.JButton btnEliminarUsuario;
    private javax.swing.JButton btnReactivarUsuario;
    private javax.swing.JButton btnRecorridoInorden;
    private javax.swing.JButton btnRecorridoInverso;
    private javax.swing.JButton btnRecorridoPostorden;
    private javax.swing.JButton btnRecorridoPreorden;
    private javax.swing.JButton btnRegistrarDevolucion;
    private javax.swing.JButton btnRegistrarLibro;
    private javax.swing.JButton btnRegistrarPrestamo;
    private javax.swing.JButton btnRegistrarReserva;
    private javax.swing.JButton btnRegistrarUsuario;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnTotalMultas;
    private javax.swing.JButton btnVerEjemplares;
    private javax.swing.JButton btnVerFrente;
    private javax.swing.JButton btnVerTope;
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
