package librasys.util;

/**
 *
 * @author AmmarPasifiky
 */
public final class IdGenerator {

    private static int bookCounter = 1;
    private static int userCounter = 1;
    private static int memberCounter = 1;
    private static int loanCounter = 1;

    private IdGenerator() {
    }

    public static String generateBookId() {
        return String.format("B%03d", bookCounter++);
    }

    public static String generateUserId() {
        return String.format("USR%03d", userCounter++);
    }

    public static String generateMemberNumber() {
        return String.format("MBR%03d", memberCounter++);
    }

    public static String generateLoanId() {
        return String.format("L%03d", loanCounter++);
    }
}
