package librasys.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import librasys.AppContext;
import librasys.model.User;

/**
 *
 * @author AmmarPasifiky
 */
public class DashboardFrame extends JFrame {

    private AppContext appContext;
    private User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel dateTimeLabel;
    private Timer clockTimer;

    public DashboardFrame(AppContext appContext, User currentUser) {
        this.appContext = appContext;
        this.currentUser = currentUser;
        initFrame();
        initComponents();
    }

    private void initFrame() {
        setTitle("LibraSys Desktop - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 640);
        setMinimumSize(new Dimension(900, 580));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(new Color(245, 247, 250));

        JPanel sidebarPanel = createSidebarPanel();
        JPanel headerPanel = createHeaderPanel();

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(245, 247, 250));

        contentPanel.add(new BookPanel(appContext), "Books");
        contentPanel.add(new MemberPanel(appContext), "Members");
        contentPanel.add(new LoanPanel(appContext), "Loans");
        contentPanel.add(new ReportPanel(appContext), "Reports");

        rootPanel.add(sidebarPanel, BorderLayout.WEST);
        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(contentPanel, BorderLayout.CENTER);
        add(rootPanel);
    }

    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel(new BorderLayout());
        sidebarPanel.setPreferredSize(new Dimension(190, 0));
        sidebarPanel.setBackground(new Color(31, 41, 55));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 16, 20, 16));

        JLabel appLabel = new JLabel("LibraSys");
        appLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        appLabel.setForeground(Color.WHITE);

        JPanel menuPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(28, 0, 0, 0));

        menuPanel.add(createMenuButton("Books"));
        menuPanel.add(createMenuButton("Members"));
        menuPanel.add(createMenuButton("Loans"));
        menuPanel.add(createMenuButton("Reports"));

        JButton logoutButton = createSidebarButton("Logout");
        logoutButton.addActionListener(event -> handleLogout());

        sidebarPanel.add(appLabel, BorderLayout.NORTH);
        sidebarPanel.add(menuPanel, BorderLayout.CENTER);
        sidebarPanel.add(logoutButton, BorderLayout.SOUTH);
        return sidebarPanel;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));

        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(31, 41, 55));

        JLabel userLabel = new JLabel(currentUser.getName() + " - "
                + currentUser.getRole());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(new Color(75, 85, 99));

        dateTimeLabel = new JLabel();
        dateTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateTimeLabel.setForeground(new Color(107, 114, 128));
        dateTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        updateDateTime();

        JPanel userPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        userPanel.setOpaque(false);
        userPanel.add(userLabel);
        userPanel.add(dateTimeLabel);

        clockTimer = new Timer(1000, event -> updateDateTime());
        clockTimer.start();

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
        return headerPanel;
    }

    private JButton createMenuButton(String panelName) {
        JButton button = createSidebarButton(panelName);
        button.addActionListener(event -> cardLayout.show(contentPanel, panelName));
        return button;
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(55, 65, 81));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        return button;
    }

    private JPanel createPlaceholderPanel(String title, String description) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(36, 36, 36, 36)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(31, 41, 55));

        JLabel descriptionLabel = new JLabel(description, SwingConstants.LEFT);
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionLabel.setForeground(new Color(75, 85, 99));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(descriptionLabel, BorderLayout.CENTER);
        return panel;
    }

    private void handleLogout() {
        if (clockTimer != null) {
            clockTimer.stop();
        }
        appContext.getAuthService().logout(currentUser);
        LoginFrame loginFrame = new LoginFrame(appContext);
        loginFrame.setVisible(true);
        dispose();
    }

    private void updateDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "EEEE, dd MMMM yyyy HH:mm:ss");
        dateTimeLabel.setText(LocalDateTime.now().format(formatter));
    }
}
