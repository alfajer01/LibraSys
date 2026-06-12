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
import javax.swing.JComboBox;
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
import librasys.model.Loan;
import librasys.model.Member;

/**
 *
 * @author AmmarPasifiky
 */
public class LoanPanel extends JPanel {

    private AppContext appContext;
    private JTable loanTable;
    private DefaultTableModel tableModel;
    private JComboBox<MemberItem> memberComboBox;
    private JComboBox<BookItem> bookComboBox;
    private JTextField memberSearchField;
    private JTextField bookSearchField;
    private String selectedLoanId;

    public LoanPanel(AppContext appContext) {
        this.appContext = appContext;
        initComponents();
        refreshAll();
    }

    private void initComponents() {
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(245, 247, 250));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createActionPanel(), BorderLayout.EAST);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Loan Transactions");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(31, 41, 55));

        JButton refreshButton = createButton("Refresh");
        refreshButton.addActionListener(event -> refreshAll());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);
        return headerPanel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(
                new Object[]{
                    "Loan ID", "Member", "Book", "Loan Date", "Due Date",
                    "Return Date", "Fine", "Status"
                }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        loanTable = new JTable(tableModel);
        loanTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loanTable.setRowHeight(26);
        loanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loanTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        loanTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                loadSelectedLoan();
            }
        });

        JScrollPane scrollPane = new JScrollPane(loanTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
        return scrollPane;
    }

    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setPreferredSize(new Dimension(310, 0));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel formTitle = new JLabel("Borrow Book");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(new Color(31, 41, 55));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(18, 0, 18, 0));

        memberComboBox = new JComboBox<>();
        bookComboBox = new JComboBox<>();
        memberSearchField = new JTextField();
        bookSearchField = new JTextField();
        memberSearchField.setPreferredSize(new Dimension(250, 32));
        bookSearchField.setPreferredSize(new Dimension(250, 32));
        memberComboBox.setPreferredSize(new Dimension(250, 32));
        bookComboBox.setPreferredSize(new Dimension(250, 32));

        addTextField(fieldsPanel, "Search Member", memberSearchField, 0);
        addComboField(fieldsPanel, "Member", memberComboBox, 1);
        addTextField(fieldsPanel, "Search Book", bookSearchField, 2);
        addComboField(fieldsPanel, "Available Book", bookComboBox, 3);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        buttonPanel.setOpaque(false);

        JButton borrowButton = createButton("Borrow");
        JButton returnButton = createButton("Return Selected Loan");
        JButton refreshButton = createButton("Refresh");

        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(refreshButton);

        borrowButton.addActionListener(event -> handleBorrow());
        returnButton.addActionListener(event -> handleReturn());
        refreshButton.addActionListener(event -> refreshAll());
        memberSearchField.addActionListener(event -> refreshMemberComboBox());
        bookSearchField.addActionListener(event -> refreshBookComboBox());

        actionPanel.add(formTitle, BorderLayout.NORTH);
        actionPanel.add(fieldsPanel, BorderLayout.CENTER);
        actionPanel.add(buttonPanel, BorderLayout.SOUTH);
        return actionPanel;
    }

    private void addComboField(JPanel panel, String labelText,
            JComboBox<?> comboBox, int row) {
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
        panel.add(comboBox, fieldConstraints);
    }

    private void addTextField(JPanel panel, String labelText, JTextField field,
            int row) {
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
        panel.add(field, fieldConstraints);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        return button;
    }

    private void refreshAll() {
        refreshMemberComboBox();
        refreshBookComboBox();
        refreshTable();
    }

    private void refreshMemberComboBox() {
        memberComboBox.removeAllItems();
        String keyword = memberSearchField.getText().trim().toLowerCase();
        for (Member member : appContext.getMemberService().getAllMembers()) {
            if (member.isActive() && matchesMember(member, keyword)) {
                memberComboBox.addItem(new MemberItem(member));
            }
        }
    }

    private void refreshBookComboBox() {
        bookComboBox.removeAllItems();
        String keyword = bookSearchField.getText().trim().toLowerCase();
        for (Book book : appContext.getBookService().getAllBooks()) {
            if (book.isAvailable() && matchesBook(book, keyword)) {
                bookComboBox.addItem(new BookItem(book));
            }
        }
    }

    private boolean matchesMember(Member member, String keyword) {
        return keyword.isEmpty()
                || member.getMemberNumber().toLowerCase().contains(keyword)
                || member.getName().toLowerCase().contains(keyword)
                || member.getEmail().toLowerCase().contains(keyword);
    }

    private boolean matchesBook(Book book, String keyword) {
        return keyword.isEmpty()
                || book.getBookId().toLowerCase().contains(keyword)
                || book.getTitle().toLowerCase().contains(keyword)
                || book.getAuthor().toLowerCase().contains(keyword);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Loan> loans = appContext.getLoanService().getAllLoans();
        for (Loan loan : loans) {
            tableModel.addRow(new Object[]{
                loan.getLoanId(),
                loan.getMember().getMemberNumber() + " - "
                        + loan.getMember().getName(),
                loan.getBook().getTitle(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate() == null ? "-" : loan.getReturnDate(),
                loan.getFine(),
                loan.getStatus()
            });
        }
    }

    private void loadSelectedLoan() {
        int selectedRow = loanTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        selectedLoanId = tableModel.getValueAt(selectedRow, 0).toString();
    }

    private void handleBorrow() {
        MemberItem memberItem = (MemberItem) memberComboBox.getSelectedItem();
        BookItem bookItem = (BookItem) bookComboBox.getSelectedItem();

        if (memberItem == null || bookItem == null) {
            showError("Select member and book first.");
            return;
        }

        try {
            appContext.getLoanService().borrowBook(
                    memberItem.getMember(),
                    bookItem.getBook()
            );
            selectedLoanId = null;
            refreshAll();
            showInfo("Book borrowed successfully.");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void handleReturn() {
        if (selectedLoanId == null) {
            showError("Select a loan first.");
            return;
        }

        try {
            Loan loan = appContext.getLoanService().findLoanById(selectedLoanId);
            if (loan == null) {
                showError("Selected loan was not found.");
                return;
            }

            appContext.getLoanService().returnBook(loan);
            selectedLoanId = null;
            loanTable.clearSelection();
            refreshAll();
            showInfo("Book returned successfully. Fine: " + loan.getFine());
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Loan Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static class MemberItem {

        private Member member;

        MemberItem(Member member) {
            this.member = member;
        }

        Member getMember() {
            return member;
        }

        @Override
        public String toString() {
            return member.getMemberNumber() + " - " + member.getName();
        }
    }

    private static class BookItem {

        private Book book;

        BookItem(Book book) {
            this.book = book;
        }

        Book getBook() {
            return book;
        }

        @Override
        public String toString() {
            return book.getBookId() + " - " + book.getTitle();
        }
    }
}
