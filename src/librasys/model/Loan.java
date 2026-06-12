package librasys.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author AmmarPasifiky
 */
public class Loan {

    private String loanId;
    private Member member;
    private Book book;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;
    private LoanStatus status;

    public Loan(String loanId, Member member, Book book, LocalDate loanDate,
            LocalDate dueDate) {
        this.loanId = loanId;
        this.member = member;
        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.status = LoanStatus.ACTIVE;
    }

    public int calculateDuration() {
        LocalDate endDate = returnDate != null ? returnDate : LocalDate.now();
        return (int) ChronoUnit.DAYS.between(loanDate, endDate);
    }

    public void markReturned() {
        this.status = LoanStatus.RETURNED;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }
}
