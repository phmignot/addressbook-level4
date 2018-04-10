package seedu.address.logic.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import seedu.address.logic.commands.DeleteTransactionCommand;
import seedu.address.model.person.Balance;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.TransactionType;

/**
 * Contains utility methods used for calculating balances of Persons.
 */
public class BalanceCalculationUtil {

    private static final int NUMBER_OF_DECIMAL_PLACES = 2;

    //@@author steven-jia
    /**
     * Returns the debt that a {@code payee} owes to the payer.
     */
    public static Balance calculatePayerDebt(String transactionType, Amount amount,
                                             UniquePersonList payees) {
        switch (transactionType) {
        case TransactionType.TRANSACTION_TYPE_VALIDATION_REGEX_PAYMENT:
            return calculateAddTransactionPaymentTypePayerDebt(amount, payees);
        case TransactionType.TRANSACTION_TYPE_VALIDATION_REGEX_PAYDEBT:
            return calculateAddTransactionPayDebtTypePayerDebt(amount);
        case DeleteTransactionCommand.COMMAND_WORD:
            return calculateDeleteTransactionPayerDebt(amount, payees);
        default:
            assert false : transactionType;
        }
        return null;
    }


    /**
     * Returns an updated balance for {@code payee}
     */
    public static Balance calculatePayeeDebt(String transactionType, Amount amount,
                                             UniquePersonList payees) {
        switch (transactionType) {
        case TransactionType.TRANSACTION_TYPE_VALIDATION_REGEX_PAYMENT:
            return calculateAddTransactionPaymentTypePayeeDebt(amount, payees);
        case TransactionType.TRANSACTION_TYPE_VALIDATION_REGEX_PAYDEBT:
            return calculateAddTransactionPayDebtTypePayeeDebt(amount, payees);
        case DeleteTransactionCommand.COMMAND_WORD:
            return calculateDeleteTransactionPayeeDebt(amount, payees);
        default:
            return null;
        }
    }


    //@@author ongkc
    /**
     * Calculate payer new debt for paydebt transaction
     */
    private static Balance calculateAddTransactionPayDebtTypePayerDebt(Amount amount) {
        Double debt = Double.valueOf(amount.value);
        debt = round(debt, NUMBER_OF_DECIMAL_PLACES);
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(debt)));
    }
    /**
     * Calculate payee new debt for paydebt transaction
     */
    private static Balance calculateAddTransactionPayDebtTypePayeeDebt(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double debt = -Double.valueOf(amount.value) / (numberOfInvolvedPersons - 1);
        debt = round(debt, NUMBER_OF_DECIMAL_PLACES);
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(debt)));
    }
    /**
     * Calculate new payee(s) balance after a new transaction is added
     */
    public static Balance calculateAddTransactionPaymentTypePayeeDebt(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double debt = -Double.valueOf(amount.value) / numberOfInvolvedPersons;
        debt = round(debt, NUMBER_OF_DECIMAL_PLACES);
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(debt)));
    }
    /**
     * Calculate new payer balance after a new transaction is added
     */
    public static Balance calculateAddTransactionPaymentTypePayerDebt(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double debt = Double.valueOf(amount.value) * (numberOfInvolvedPersons - 1)
                / numberOfInvolvedPersons;
        debt = round(debt, NUMBER_OF_DECIMAL_PLACES);
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(debt)));
    }
    /**
     * Calculate new payer balance a transaction is deleted
     */
    public static Balance calculateDeleteTransactionPayerDebt(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double debt = -Double.valueOf(amount.value) * (numberOfInvolvedPersons - 1)
                / numberOfInvolvedPersons;
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(debt)));
    }
    /**
     * Calculate new payee(s) balance after a transaction is deleted
     */
    public static Balance calculateDeleteTransactionPayeeDebt(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double debt = Double.valueOf(amount.value) / numberOfInvolvedPersons;
        debt = round(debt, NUMBER_OF_DECIMAL_PLACES);
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(debt)));
    }

    //@@author steven-jia
    /**
     * Calculate the number of people involved in a transaction
     */
    public static int calculateNumberOfInvolvedPersons(UniquePersonList payees) {
        return payees.asObservableList().size() + 1;
    }
    private static double round(double value, int places) {
        BigDecimal amount = new BigDecimal(value);
        amount = amount.setScale(places, RoundingMode.HALF_UP);
        return amount.doubleValue();
    }

}


