package librasys.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import librasys.AppContext;
import librasys.model.Loan;
import librasys.model.Member;

/**
 *
 * @author AmmarPasifiky
 */
public class MemberLoanHistoryPanel extends JPanel {

    private AppContext appContext;
    private Member member;
    private JTable loanTable;
    private DefaultTableModel tableModel;

    public MemberLoanHistoryPanel(AppContext appContext, Member member) {
        this.appContext = appContext;
        this.member = member;
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(245, 247, 250));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("My Loan History");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(31, 41, 55));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(event -> refreshTable());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);
        return headerPanel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(
                new Object[]{
                    "Loan ID", "Book", "Loan Date", "Due Date",
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
        loanTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(loanTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
        return scrollPane;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Loan> loans = appContext.getLoanService().getLoansByMember(member);
        for (Loan loan : loans) {
            tableModel.addRow(new Object[]{
                loan.getLoanId(),
                loan.getBook().getTitle(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate() == null ? "-" : loan.getReturnDate(),
                loan.getFine(),
                loan.getStatus()
            });
        }
    }
}
