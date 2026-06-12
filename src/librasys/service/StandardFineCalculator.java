package librasys.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import librasys.model.Loan;

/**
 *
 * @author AmmarPasifiky
 */
public class StandardFineCalculator implements FineCalculator {

    public static final double FINE_PER_DAY = 1000.0;

    @Override
    public double calculateFine(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("Loan cannot be null.");
        }

        LocalDate dueDate = loan.getDueDate();
        LocalDate returnDate = loan.getReturnDate() != null
                ? loan.getReturnDate()
                : LocalDate.now();

        long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
        if (overdueDays <= 0) {
            return 0;
        }

        return overdueDays * FINE_PER_DAY;
    }
}
