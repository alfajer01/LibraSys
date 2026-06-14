package librasys.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import librasys.AppContext;
import librasys.model.Book;

/**
 *
 * @author AmmarPasifiky
 */
public class BookCatalogPanel extends JPanel {

    private AppContext appContext;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public BookCatalogPanel(AppContext appContext) {
        this.appContext = appContext;
        initComponents();
        loadBooks(appContext.getBookService().getAllBooks());
    }

    private void initComponents() {
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(245, 247, 250));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(12, 0));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Book Catalog");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(31, 41, 55));

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(280, 32));
        JButton searchButton = createButton("Search");
        JButton refreshButton = createButton("Refresh");

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(searchField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton);
        searchPanel.add(buttonPanel, BorderLayout.EAST);

        searchButton.addActionListener(event -> handleSearch());
        refreshButton.addActionListener(event -> {
            searchField.setText("");
            loadBooks(appContext.getBookService().getAllBooks());
        });

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
        bookTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
        return scrollPane;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        return button;
    }

    private void handleSearch() {
        loadBooks(appContext.getBookService().searchBook(searchField.getText()));
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
}
