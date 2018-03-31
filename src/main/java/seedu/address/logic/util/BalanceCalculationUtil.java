package seedu.address.logic.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import seedu.address.model.person.Balance;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.transaction.Amount;

/**
 * Contains utility methods used for calculating balances of Persons.
 */
public class BalanceCalculationUtil {

    private static final int NUMBER_OF_DECIMAL_PLACES = 2;

    /**
     * Returns the dept that a {@code payee} owes to the payer.
     */
    public static Balance calculatePayeeDept(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = numberOfInvolvedPersons(payees);
        Double dept = -Double.valueOf(amount.value) / numberOfInvolvedPersons;
        dept = round(dept, NUMBER_OF_DECIMAL_PLACES);
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(dept)));
    }

    /**
     * Returns the dept that a {@code payer} is owed from the payees.
     */
    public static Balance calculatePayerDept(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = numberOfInvolvedPersons(payees);
        Double dept = Double.valueOf(amount.value) * (numberOfInvolvedPersons - 1)
                / numberOfInvolvedPersons;
        dept = round(dept, NUMBER_OF_DECIMAL_PLACES);
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(dept)));
    }

    private static double round(double value, int places) {
        BigDecimal amount = new BigDecimal(value);
        amount = amount.setScale(places, RoundingMode.HALF_UP);
        return amount.doubleValue();
    }

    public static int numberOfInvolvedPersons(UniquePersonList payees) {
        return payees.asObservableList().size() + 1;
    }

}
