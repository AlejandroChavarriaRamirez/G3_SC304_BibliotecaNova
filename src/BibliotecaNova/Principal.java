package BibliotecaNova;

/**
 * Integrantes:
 * Alejandro Chavarria Ramirez
 * Andres Brenes Herrera
 * Saul Amir Alvarado Montero
 * Josel Pablo Vargas Calderon
 *
 * @author Grupo 3
 */
public class Principal {

    public static void main(String[] args) {

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

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ui.Principal().setVisible(true);
            }
        });
    }

}
