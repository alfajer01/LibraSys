package librasys.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import librasys.exception.BookNotAvailableException;
import librasys.exception.LoanAlreadyReturnedException;
import librasys.exception.MemberInactiveException;
import librasys.model.Book;
import librasys.model.Loan;
import librasys.model.LoanStatus;
import librasys.model.Member;
import librasys.util.IdGenerator;

/**
 *
 * @author AmmarPasifiky
 */
public class LoanService {

    public static final int DEFAULT_LOAN_DAYS = 7;

    private List<Loan> loans;
    private FineCalculator fineCalculator;

    public LoanService(FineCalculator fineCalculator) {
        if (fineCalculator == null) {
            throw new IllegalArgumentException("Fine calculator cannot be null.");
        }
        this.loans = new ArrayList<>();
        this.fineCalculator = fineCalculator;
    }

    public Loan borrowBook(Member member, Book book) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        if (!member.isActive()) {
            throw new MemberInactiveException(
                    "Inactive member cannot borrow books.");
        }
        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book is not available.");
        }

        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanDate.plusDays(DEFAULT_LOAN_DAYS);
        Loan loan = new Loan(IdGenerator.generateLoanId(), member, book, loanDate,
                dueDate);

        book.setAvailable(false);
        loans.add(loan);
        return loan;
    }

    public void returnBook(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("Loan cannot be null.");
        }
        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new LoanAlreadyReturnedException(
                    "Loan has already been returned.");
        }

        if (loan.getReturnDate() == null) {
            loan.setReturnDate(LocalDate.now());
        }

        double fine = calculateFine(loan);
        loan.setFine(fine);
        loan.markReturned();
        loan.getBook().setAvailable(true);
    }

    public double calculateFine(Loan loan) {
        return fineCalculator.calculateFine(loan);
    }

    public List<Loan> getActiveLoans() {
        refreshOverdueStatus();
        List<Loan> activeLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getStatus() == LoanStatus.ACTIVE) {
                activeLoans.add(loan);
            }
        }
        return activeLoans;
    }

    public List<Loan> getAllLoans() {
        refreshOverdueStatus();
        return new ArrayList<>(loans);
    }

    private void refreshOverdueStatus() {
        LocalDate today = LocalDate.now();
        for (Loan loan : loans) {
            if (loan.getStatus() == LoanStatus.ACTIVE
                    && today.isAfter(loan.getDueDate())) {
                loan.setStatus(LoanStatus.OVERDUE);
            }
        }
    }
}
