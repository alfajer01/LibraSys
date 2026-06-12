package librasys.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import librasys.AppContext;
import librasys.model.Loan;

/**
 *
 * @author AmmarPasifiky
 */
public class ReportPanel extends JPanel {

    private AppContext appContext;
    private JTextArea reportTextArea;
    private JTable historyTable;
    private DefaultTableModel tableModel;

    public ReportPanel(AppContext appContext) {
        this.appContext = appContext;
        initComponents();
        refreshReport();
    }

    private void initComponents() {
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(245, 247, 250));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Reports");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(31, 41, 55));

        JButton refreshButton = createButton("Refresh");
        refreshButton.addActionListener(event -> refreshReport());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 0, 16));
        contentPanel.setOpaque(false);

        reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);
        reportTextArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        reportTextArea.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JScrollPane reportScrollPane = new JScrollPane(reportTextArea);
        reportScrollPane.setBorder(BorderFactory.createLineBorder(
                new Color(229, 231, 235)));

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

        historyTable = new JTable(tableModel);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        historyTable.setRowHeight(26);
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane tableScrollPane = new JScrollPane(historyTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(
                new Color(229, 231, 235)));

        contentPanel.add(reportScrollPane);
        contentPanel.add(tableScrollPane);
        return contentPanel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        return button;
    }

    private void refreshReport() {
        reportTextArea.setText(appContext.getReportService().generateReport());
        loadHistory(appContext.getReportService().getLoanHistory());
    }

    private void loadHistory(List<Loan> loans) {
        tableModel.setRowCount(0);
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
}
