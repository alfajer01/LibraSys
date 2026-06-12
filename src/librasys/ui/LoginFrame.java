package librasys.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import librasys.AppContext;
import librasys.model.User;

/**
 *
 * @author AmmarPasifiky
 */
public class LoginFrame extends JFrame {

    private AppContext appContext;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton passwordVisibilityButton;
    private JButton loginButton;
    private char defaultPasswordEchoChar;
    private boolean passwordVisible;

    public LoginFrame(AppContext appContext) {
        this.appContext = appContext;
        initFrame();
        initComponents();
    }

    private void initFrame() {
        setTitle("LibraSys Desktop - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 320);
        setMinimumSize(new Dimension(420, 320));
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(new Color(245, 247, 250));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));

        JLabel titleLabel = new JLabel("LibraSys Desktop", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(31, 41, 55));

        JLabel subtitleLabel = new JLabel("Library Management System",
                SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(75, 85, 99));

        JPanel headerPanel = new JPanel(new BorderLayout(0, 6));
        headerPanel.setOpaque(false);
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(26, 0, 0, 0));

        emailField = new JTextField();
        passwordField = new JPasswordField();
        defaultPasswordEchoChar = passwordField.getEchoChar();
        passwordVisibilityButton = new JButton("Show");
        loginButton = new JButton("Login");

        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordVisibilityButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordVisibilityButton.setFocusPainted(false);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginButton.setBackground(new Color(37, 99, 235));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        addFormRow(formPanel, "Email", emailField, 0);
        addFormRow(formPanel, "Password", passwordField, 1);

        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 1;
        buttonConstraints.gridy = 2;
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraints.insets = new Insets(16, 8, 0, 0);
        formPanel.add(loginButton, buttonConstraints);

        loginButton.addActionListener(event -> handleLogin());
        passwordField.addActionListener(event -> handleLogin());
        passwordVisibilityButton.addActionListener(
                event -> togglePasswordVisibility());

        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(formPanel, BorderLayout.CENTER);
        add(rootPanel);
    }

    private void addFormRow(JPanel panel, String labelText, JTextField field,
            int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(55, 65, 81));

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(0, 0, 12, 12);
        panel.add(label, labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(0, 0, 12, 0);
        if (field == passwordField) {
            JPanel passwordPanel = new JPanel(new BorderLayout(6, 0));
            passwordPanel.setOpaque(false);
            field.setPreferredSize(new Dimension(170, 32));
            passwordVisibilityButton.setPreferredSize(new Dimension(64, 32));
            passwordPanel.add(field, BorderLayout.CENTER);
            passwordPanel.add(passwordVisibilityButton, BorderLayout.EAST);
            panel.add(passwordPanel, fieldConstraints);
        } else {
            field.setPreferredSize(new Dimension(230, 32));
            panel.add(field, fieldConstraints);
        }
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        try {
            User user = appContext.getAuthService().login(email, password);
            if (user == null) {
                JOptionPane.showMessageDialog(this,
                        "Email atau password tidak sesuai.",
                        "Login Gagal",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            DashboardFrame dashboardFrame = new DashboardFrame(appContext, user);
            dashboardFrame.setVisible(true);
            dispose();
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this,
                    exception.getMessage(),
                    "Login Gagal",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        passwordField.setEchoChar(passwordVisible
                ? (char) 0
                : defaultPasswordEchoChar);
        passwordVisibilityButton.setText(passwordVisible ? "Hide" : "Show");
    }
}
