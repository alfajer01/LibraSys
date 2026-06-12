package librasys.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import librasys.AppContext;
import librasys.model.Book;
import librasys.util.IdGenerator;

/**
 *
 * @author AmmarPasifiky
 */
public class BookPanel extends JPanel {

    private AppContext appContext;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField yearField;
    private JTextField searchField;
    private String selectedBookId;

    public BookPanel(AppContext appContext) {
        this.appContext = appContext;
        initComponents();
        loadBooks(appContext.getBookService().getAllBooks());
    }

    private void initComponents() {
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(245, 247, 250));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.EAST);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(12, 0));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Book Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(31, 41, 55));

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(260, 32));
        JButton searchButton = createButton("Search");
        JButton refreshButton = createButton("Refresh");

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(searchField, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        actionPanel.setOpaque(false);
        actionPanel.add(searchButton);
        actionPanel.add(refreshButton);
        searchPanel.add(actionPanel, BorderLayout.EAST);

        searchButton.addActionListener(event -> handleSearch());
        refreshButton.addActionListener(event -> refreshTable());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        return headerPanel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Title", "Author", "Year", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bookTable.setRowHeight(26);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        bookTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                loadSelectedBook();
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
        return scrollPane;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setPreferredSize(new Dimension(280, 0));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel formTitle = new JLabel("Book Form");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(new Color(31, 41, 55));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(18, 0, 18, 0));

        titleField = new JTextField();
        authorField = new JTextField();
        yearField = new JTextField();

        addField(fieldsPanel, "Title", titleField, 0);
        addField(fieldsPanel, "Author", authorField, 1);
        addField(fieldsPanel, "Publication Year", yearField, 2);

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
        field.setPreferredSize(new Dimension(220, 32));
        panel.add(field, fieldConstraints);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        return button;
    }

    private void loadBooks(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationYear(),
                book.isAvailable() ? "Available" : "Borrowed"
            });
        }
    }

    private void loadSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }

        selectedBookId = tableModel.getValueAt(selectedRow, 0).toString();
        Book book = appContext.getBookService().findBookById(selectedBookId);
        if (book == null) {
            return;
        }

        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        yearField.setText(String.valueOf(book.getPublicationYear()));
    }

    private void handleAdd() {
        try {
            Book book = new Book(
                    IdGenerator.generateBookId(),
                    getRequiredText(titleField, "Title"),
                    getRequiredText(authorField, "Author"),
                    parseYear(),
                    true
            );
            appContext.getBookService().addBook(book);
            refreshTable();
            clearForm();
            showInfo("Book added successfully.");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void handleUpdate() {
        if (selectedBookId == null) {
            showError("Select a book first.");
            return;
        }

        try {
            Book book = appContext.getBookService().findBookById(selectedBookId);
            if (book == null) {
                showError("Selected book was not found.");
                return;
            }

            book.setTitle(getRequiredText(titleField, "Title"));
            book.setAuthor(getRequiredText(authorField, "Author"));
            book.setPublicationYear(parseYear());
            appContext.getBookService().updateBook(book);
            refreshTable();
            clearForm();
            showInfo("Book updated successfully.");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void handleDelete() {
        if (selectedBookId == null) {
            showError("Select a book first.");
            return;
        }

        try {
            appContext.getBookService().removeBook(selectedBookId);
            refreshTable();
            clearForm();
            showInfo("Book deleted successfully.");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void handleSearch() {
        loadBooks(appContext.getBookService().searchBook(searchField.getText()));
    }

    private void refreshTable() {
        loadBooks(appContext.getBookService().getAllBooks());
    }

    private void clearForm() {
        selectedBookId = null;
        bookTable.clearSelection();
        titleField.setText("");
        authorField.setText("");
        yearField.setText("");
        searchField.setText("");
    }

    private String getRequiredText(JTextField field, String fieldName) {
        String value = field.getText().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value;
    }

    private int parseYear() {
        try {
            return Integer.parseInt(getRequiredText(yearField, "Publication year"));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Publication year must be numeric.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
