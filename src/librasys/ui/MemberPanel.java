package librasys.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import librasys.AppContext;
import librasys.model.Member;
import librasys.model.User;
import librasys.util.IdGenerator;

/**
 *
 * @author AmmarPasifiky
 */
public class MemberPanel extends JPanel {

    private AppContext appContext;
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton passwordVisibilityButton;
    private JCheckBox activeCheckBox;
    private char defaultPasswordEchoChar;
    private boolean passwordVisible;
    private String selectedMemberNumber;
    private String selectedEmail;

    public MemberPanel(AppContext appContext) {
        this.appContext = appContext;
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(245, 247, 250));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.EAST);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Member Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(31, 41, 55));

        JButton refreshButton = createButton("Refresh");
        refreshButton.addActionListener(event -> refreshTable());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);
        return headerPanel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(
                new Object[]{"Member No", "User ID", "Name", "Email", "Active"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        memberTable = new JTable(tableModel);
        memberTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        memberTable.setRowHeight(26);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        memberTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                loadSelectedMember();
            }
        });

        JScrollPane scrollPane = new JScrollPane(memberTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
        return scrollPane;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setPreferredSize(new Dimension(300, 0));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel formTitle = new JLabel("Member Form");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(new Color(31, 41, 55));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(18, 0, 18, 0));

        nameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        defaultPasswordEchoChar = passwordField.getEchoChar();
        passwordVisibilityButton = new JButton("Show");
        activeCheckBox = new JCheckBox("Active member");
        passwordVisibilityButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordVisibilityButton.setFocusPainted(false);
        activeCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        activeCheckBox.setOpaque(false);
        activeCheckBox.setSelected(true);

        addField(fieldsPanel, "Name", nameField, 0);
        addField(fieldsPanel, "Email", emailField, 1);
        addField(fieldsPanel, "Password", passwordField, 2);
        addField(fieldsPanel, "Confirm Password", confirmPasswordField, 3);

        GridBagConstraints activeConstraints = new GridBagConstraints();
        activeConstraints.gridx = 0;
        activeConstraints.gridy = 8;
        activeConstraints.weightx = 1;
        activeConstraints.fill = GridBagConstraints.HORIZONTAL;
        activeConstraints.insets = new Insets(0, 0, 12, 0);
        fieldsPanel.add(activeCheckBox, activeConstraints);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 8));
        buttonPanel.setOpaque(false);

        JButton addButton = createButton("Add");
        JButton updateButton = createButton("Update");
        JButton deleteButton = createButton("Delete");
        JButton clearButton = createButton("Clear");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        addButton.addActionListener(event -> handleAdd());
        updateButton.addActionListener(event -> handleUpdate());
        deleteButton.addActionListener(event -> handleDelete());
        clearButton.addActionListener(event -> clearForm());
        passwordVisibilityButton.addActionListener(
                event -> togglePasswordVisibility());

        formPanel.add(formTitle, BorderLayout.NORTH);
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        return formPanel;
    }

    private void addField(JPanel panel, String labelText, JTextField field, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(55, 65, 81));

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row * 2;
        labelConstraints.weightx = 1;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.insets = new Insets(0, 0, 4, 0);
        panel.add(label, labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 0;
        fieldConstraints.gridy = row * 2 + 1;
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
            field.setPreferredSize(new Dimension(240, 32));
            panel.add(field, fieldConstraints);
        }
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        return button;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Member member : appContext.getMemberService().getAllMembers()) {
            tableModel.addRow(new Object[]{
                member.getMemberNumber(),
                member.getUserId(),
                member.getName(),
                member.getEmail(),
                member.isActive() ? "Yes" : "No"
            });
        }
    }

    private void loadSelectedMember() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }

        selectedMemberNumber = tableModel.getValueAt(selectedRow, 0).toString();
        Member member = appContext.getMemberService()
                .findMemberByMemberNumber(selectedMemberNumber);
        if (member == null) {
            return;
        }

        selectedEmail = member.getEmail();
        nameField.setText(member.getName());
        emailField.setText(member.getEmail());
        passwordField.setText(member.getPassword());
        confirmPasswordField.setText(member.getPassword());
        activeCheckBox.setSelected(member.isActive());
    }

    private void handleAdd() {
        try {
            String email = getRequiredText(emailField, "Email");
            if (appContext.getAuthService().findUserByEmail(email) != null) {
                throw new IllegalArgumentException("Email already exists.");
            }

            Member member = new Member(
                    IdGenerator.generateUserId(),
                    getRequiredText(nameField, "Name"),
                    email,
                    getRequiredPassword(),
                    IdGenerator.generateMemberNumber(),
                    activeCheckBox.isSelected()
            );

            appContext.getMemberService().registerMember(member);
            appContext.getAuthService().addUser(member);
            refreshTable();
            clearForm();
            showInfo("Member added successfully.");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void handleUpdate() {
        if (selectedMemberNumber == null) {
            showError("Select a member first.");
            return;
        }

        try {
            Member member = appContext.getMemberService()
                    .findMemberByMemberNumber(selectedMemberNumber);
            if (member == null) {
                showError("Selected member was not found.");
                return;
            }

            String email = getRequiredText(emailField, "Email");
            User existingUser = appContext.getAuthService().findUserByEmail(email);
            if (existingUser != null && existingUser != member) {
                throw new IllegalArgumentException("Email already exists.");
            }

            member.setName(getRequiredText(nameField, "Name"));
            member.setEmail(email);
            member.setPassword(getRequiredPassword());
            member.setActive(activeCheckBox.isSelected());

            appContext.getMemberService().updateMember(member);
            refreshTable();
            clearForm();
            showInfo("Member updated successfully.");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void handleDelete() {
        if (selectedMemberNumber == null) {
            showError("Select a member first.");
            return;
        }

        try {
            appContext.getMemberService()
                    .removeMemberByMemberNumber(selectedMemberNumber);
            appContext.getAuthService().removeUserByEmail(selectedEmail);
            refreshTable();
            clearForm();
            showInfo("Member deleted successfully.");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void clearForm() {
        selectedMemberNumber = null;
        selectedEmail = null;
        memberTable.clearSelection();
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        passwordVisible = true;
        togglePasswordVisibility();
        activeCheckBox.setSelected(true);
    }

    private String getRequiredText(JTextField field, String fieldName) {
        String value = field.getText().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value;
    }

    private String getRequiredPassword() {
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        if (password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException(
                    "Password and confirmation password must match.");
        }
        return password;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        char echoChar = passwordVisible
                ? (char) 0
                : defaultPasswordEchoChar;
        passwordField.setEchoChar(echoChar);
        confirmPasswordField.setEchoChar(echoChar);
        passwordVisibilityButton.setText(passwordVisible ? "Hide" : "Show");
    }
}
