package librasys.service;

import java.util.ArrayList;
import java.util.List;
import librasys.model.Loan;
import librasys.model.LoanStatus;

/**
 *
 * @author AmmarPasifiky
 */
public class ReportService {

    private LoanService loanService;

    public ReportService(LoanService loanService) {
        if (loanService == null) {
            throw new IllegalArgumentException("Loan service cannot be null.");
        }
        this.loanService = loanService;
    }

    public List<Loan> getLoanHistory() {
        return loanService.getAllLoans();
    }

    public List<Loan> getOverdueLoans() {
        List<Loan> overdueLoans = new ArrayList<>();
        for (Loan loan : loanService.getAllLoans()) {
            if (loan.getStatus() == LoanStatus.OVERDUE) {
                overdueLoans.add(loan);
            }
        }
        return overdueLoans;
    }

    public String generateReport() {
        List<Loan> loanHistory = getLoanHistory();
        List<Loan> overdueLoans = getOverdueLoans();
        int returnedCount = 0;
        int activeCount = 0;
        double totalFine = 0;

        for (Loan loan : loanHistory) {
            if (loan.getStatus() == LoanStatus.RETURNED) {
                returnedCount++;
            } else if (loan.getStatus() == LoanStatus.ACTIVE) {
                activeCount++;
            }
            totalFine += loan.getFine();
        }

        return "LibraSys Loan Report\n"
                + "Total Loans: " + loanHistory.size() + "\n"
                + "Active Loans: " + activeCount + "\n"
                + "Returned Loans: " + returnedCount + "\n"
                + "Overdue Loans: " + overdueLoans.size() + "\n"
                + "Total Fine: " + totalFine;
    }
}
