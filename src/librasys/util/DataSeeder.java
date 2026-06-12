package librasys.util;

import librasys.model.Book;
import librasys.model.Librarian;
import librasys.model.Member;
import librasys.service.AuthService;
import librasys.service.BookService;
import librasys.service.MemberService;

/**
 *
 * @author AmmarPasifiky
 */
public final class DataSeeder {

    private DataSeeder() {
    }

    public static void seed(BookService bookService, MemberService memberService,
            AuthService authService) {
        if (bookService == null || memberService == null || authService == null) {
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

        addMember(memberService, authService, "Ammar", "ammar@gmail.com",
                "Member123", true);
        addMember(memberService, authService, "Siti", "siti@gmail.com",
                "Member123", true);
        addMember(memberService, authService, "Budi", "budi@gmail.com",
                "Member123", false);

        bookService.addBook(new Book(
                IdGenerator.generateBookId(),
                "Clean Code",
                "Robert C. Martin",
                2008,
                true
        ));
        bookService.addBook(new Book(
                IdGenerator.generateBookId(),
                "Effective Java",
                "Joshua Bloch",
                2018,
                true
        ));
        bookService.addBook(new Book(
                IdGenerator.generateBookId(),
                "Design Patterns",
                "Erich Gamma",
                1994,
                true
        ));
        bookService.addBook(new Book(
                IdGenerator.generateBookId(),
                "Head First Java",
                "Kathy Sierra",
                2022,
                true
        ));
    }

    private static void addMember(MemberService memberService,
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
    }
}
