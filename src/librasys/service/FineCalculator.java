package librasys.service;

import librasys.model.Loan;

/**
 *
 * @author AmmarPasifiky
 */
public interface FineCalculator {

    double calculateFine(Loan loan);
}
