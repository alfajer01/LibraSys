package librasys;

import librasys.service.AuthService;
import librasys.service.BookService;
import librasys.service.LoanService;
import librasys.service.MemberService;
import librasys.service.ReportService;
import librasys.service.StandardFineCalculator;
import librasys.util.DataSeeder;

/**
 *
 * @author AmmarPasifiky
 */
public class AppContext {

    private BookService bookService;
    private MemberService memberService;
    private AuthService authService;
    private LoanService loanService;
    private ReportService reportService;

    public AppContext() {
        this.bookService = new BookService();
        this.memberService = new MemberService();
        this.authService = new AuthService();
        this.loanService = new LoanService(new StandardFineCalculator());
        this.reportService = new ReportService(loanService);

        DataSeeder.seed(bookService, memberService, authService, loanService);
    }

    public BookService getBookService() {
        return bookService;
    }

    public MemberService getMemberService() {
        return memberService;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public LoanService getLoanService() {
        return loanService;
    }

    public ReportService getReportService() {
        return reportService;
    }
}
