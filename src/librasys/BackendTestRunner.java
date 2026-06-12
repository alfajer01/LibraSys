package librasys;

import java.time.LocalDate;
import java.util.Arrays;
import librasys.model.Book;
import librasys.model.Librarian;
import librasys.model.Loan;
import librasys.model.Member;
import librasys.model.User;
import librasys.service.AuthService;
import librasys.service.BookService;
import librasys.service.LoanService;
import librasys.service.MemberService;
import librasys.service.ReportService;
import librasys.service.StandardFineCalculator;
import librasys.util.IdGenerator;

/**
 *
 * @author AmmarPasifiky
 */
public class BackendTestRunner {

    public static void main(String[] args) {
        BookService bookService = new BookService();
        MemberService memberService = new MemberService();
        LoanService loanService = new LoanService(new StandardFineCalculator());
        ReportService reportService = new ReportService(loanService);

        Member member = new Member(
                IdGenerator.generateUserId(),
                "Ammar",
                "ammar@mail.com",
                "12345",
                IdGenerator.generateMemberNumber(),
                true
        );
        Member inactiveMember = new Member(
                IdGenerator.generateUserId(),
                "Budi",
                "budi@mail.com",
                "12345",
                IdGenerator.generateMemberNumber(),
                false
        );

        Librarian librarian = new Librarian(
                IdGenerator.generateUserId(),
                "Siti",
                "siti@mail.com",
                "admin123",
                "EMP001"
        );
        Book cleanCode = new Book(
                IdGenerator.generateBookId(),
                "Clean Code",
                "Robert C. Martin",
                2008,
                true
        );
        Book effectiveJava = new Book(
                IdGenerator.generateBookId(),
                "Effective Java",
                "Joshua Bloch",
                2018,
                true
        );

        memberService.registerMember(member);
        memberService.registerMember(inactiveMember);
        bookService.addBook(cleanCode);
        bookService.addBook(effectiveJava);

        AuthService authService = new AuthService(Arrays.asList(member, librarian));

        System.out.println("=== LIBRASYS BACKEND CONSOLE TEST ===");
        printResult("Generate book ID", cleanCode.getBookId().startsWith("B"));
        printResult("Generate user ID", member.getUserId().startsWith("USR"));
        printResult("Generate member number",
                member.getMemberNumber().startsWith("MBR"));
        printResult("Register member", memberService.getAllMembers().size() == 2);
        printResult("Add book", bookService.getAllBooks().size() == 2);

        User loggedInUser = authService.login("ammar@mail.com", "12345");
        User failedLoginUser = authService.login("ammar@mail.com", "wrong");
        printResult("Login success", loggedInUser != null);
        printResult("Login failed", failedLoginUser == null);

        Loan activeLoan = loanService.borrowBook(member, cleanCode);
        printResult("Borrow available book", activeLoan != null);
        printResult("Book unavailable after borrow", !cleanCode.isAvailable());
        printResult("Generate loan ID", activeLoan.getLoanId().startsWith("L"));

        printResult("Reject unavailable book", expectError(() -> {
            loanService.borrowBook(member, cleanCode);
        }));

        printResult("Reject inactive member", expectError(() -> {
            loanService.borrowBook(inactiveMember, effectiveJava);
        }));

        loanService.returnBook(activeLoan);
        printResult("Return book", cleanCode.isAvailable());
        printResult("No fine if not late", activeLoan.getFine() == 0);

        Loan overdueLoan = loanService.borrowBook(member, effectiveJava);
        overdueLoan.setDueDate(LocalDate.now().minusDays(3));
        overdueLoan.setReturnDate(LocalDate.now());
        loanService.returnBook(overdueLoan);
        printResult("Fine if late", overdueLoan.getFine() == 3000.0);

        Book overdueBook = new Book(
                IdGenerator.generateBookId(),
                "Design Patterns",
                "Erich Gamma",
                1994,
                true
        );
        bookService.addBook(overdueBook);
        Loan activeOverdueLoan = loanService.borrowBook(member, overdueBook);
        activeOverdueLoan.setDueDate(LocalDate.now().minusDays(1));
        printResult("Active loan list", loanService.getActiveLoans().isEmpty());
        printResult("Overdue loan report",
                reportService.getOverdueLoans().size() == 1);

        System.out.println();
        System.out.println(reportService.generateReport());
    }

    private static void printResult(String testName, boolean passed) {
        System.out.println((passed ? "[PASS] " : "[FAIL] ") + testName);
    }

    private static boolean expectError(Runnable action) {
        try {
            action.run();
            return false;
        } catch (IllegalArgumentException exception) {
            return true;
        }
    }
}
