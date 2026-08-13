/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package BibliotecaNova;

/**Integrantes:
 * Alejandro Chavarria Ramirez
 * Andres Brenes Herrera
 * Saul Amir Alvarado Montero
 * Josel Pablo Vargas Caldero
 *
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }

        /*
         * Los gestores se crean una sola vez porque las estructuras de datos
         * viven en memoria mientras la aplicacion este abierta.
         */
        final service.GestorLibros gestorLibros = new service.GestorLibros();
        final service.GestorUsuarios gestorUsuarios = new service.GestorUsuarios();
        final service.GestorPrestamos gestorPrestamos =
                new service.GestorPrestamos(gestorLibros, gestorUsuarios);

        /*
         * Si ya se habia usado el sistema antes, la informacion se lee de los
         * archivos de la carpeta "datos". La primera vez no hay nada guardado,
         * entonces se cargan los datos de ejemplo.
         */
        if (!service.GestorArchivos.cargar(gestorLibros, gestorUsuarios, gestorPrestamos)) {
            service.GestorDatosPrueba.cargar(gestorLibros, gestorUsuarios, gestorPrestamos);
        }

        //Arranca la aplicacion en la ventana principal
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ui.Principal(gestorLibros, gestorUsuarios, gestorPrestamos).setVisible(true);
            }
        });
    }

}
