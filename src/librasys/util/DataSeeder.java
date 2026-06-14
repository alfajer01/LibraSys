package librasys.util;

import java.time.LocalDate;
import librasys.model.Book;
import librasys.model.Librarian;
import librasys.model.Loan;
import librasys.model.Member;
import librasys.service.AuthService;
import librasys.service.BookService;
import librasys.service.LoanService;
import librasys.service.MemberService;

/**
 *
 * @author AmmarPasifiky
 */
public final class DataSeeder {

    private DataSeeder() {
    }

    public static void seed(BookService bookService, MemberService memberService,
            AuthService authService, LoanService loanService) {
        if (bookService == null || memberService == null || authService == null
                || loanService == null) {
            throw new IllegalArgumentException("Services cannot be null.");
        }

        Librarian admin = new Librarian(
                IdGenerator.generateUserId(),
                "Admin LibraSys",
                "admin@gmail.com",
                "Admin123",
                "EMP001"
        );
        authService.addUser(admin);

        Member ammar = addMember(memberService, authService, "Ammar", "ammar@gmail.com",
                "Member123", true);
        Member siti = addMember(memberService, authService, "Siti", "siti@gmail.com",
                "Member123", true);
        addMember(memberService, authService, "Budi", "budi@gmail.com",
                "Member123", false);

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
        Book designPatterns = new Book(
                IdGenerator.generateBookId(),
                "Design Patterns",
                "Erich Gamma",
                1994,
                true
        );
        bookService.addBook(cleanCode);
        bookService.addBook(effectiveJava);
        bookService.addBook(designPatterns);
        bookService.addBook(new Book(
                IdGenerator.generateBookId(),
                "Head First Java",
                "Kathy Sierra",
                2022,
                true
        ));

        seedLoans(loanService, ammar, siti, cleanCode, effectiveJava,
                designPatterns);
    }

    private static Member addMember(MemberService memberService,
            AuthService authService, String name, String email, String password,
            boolean active) {
        Member member = new Member(
                IdGenerator.generateUserId(),
                name,
                email,
                password,
                IdGenerator.generateMemberNumber(),
                active
        );
        memberService.registerMember(member);
        authService.addUser(member);
        return member;
    }

    private static void seedLoans(LoanService loanService, Member ammar,
            Member siti, Book cleanCode, Book effectiveJava,
            Book designPatterns) {
        loanService.borrowBook(ammar, cleanCode);

        Loan overdueLoan = loanService.borrowBook(siti, effectiveJava);
        overdueLoan.setDueDate(LocalDate.now().minusDays(2));

        Loan returnedLateLoan = loanService.borrowBook(ammar, designPatterns);
        returnedLateLoan.setDueDate(LocalDate.now().minusDays(3));
        returnedLateLoan.setReturnDate(LocalDate.now());
        loanService.returnBook(returnedLateLoan);
    }
}
