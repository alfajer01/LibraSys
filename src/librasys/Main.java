package librasys;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import librasys.ui.LoginFrame;

/**
 *
 * @author AmmarPasifiky
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setLookAndFeel();
            AppContext appContext = new AppContext();
            LoginFrame loginFrame = new LoginFrame(appContext);
            loginFrame.setVisible(true);
        });
    }

    private static void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info
                    : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
        } catch (Exception exception) {
            System.out.println("Using default look and feel.");
        }
    }
}
