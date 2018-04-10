package seedu.address.logic.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import seedu.address.logic.commands.DeleteTransactionCommand;
import seedu.address.model.person.Balance;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.SplitMethod;
import seedu.address.model.transaction.TransactionType;

/**
 * Contains utility methods used for calculating balances of Persons.
 */
public class CalculationUtil {

    private static final int NUMBER_OF_DECIMAL_PLACES = 2;

    //@@author steven-jia
    /**
     * Returns the amount to add to the balance of a payer.
     */
    public static Balance calculateAmountToAddForPayer(String transactionType, Amount amount,
                                                       UniquePersonList payees, SplitMethod splitMethod,
                                                       List<Integer> units, List<Integer> percentages) {
        switch (transactionType) {
        case TransactionType.TRANSACTION_TYPE_PAYMENT:
            return calculateAmountToAddForPayerForPaymentTransaction(amount, payees, splitMethod, units, percentages);
        case TransactionType.TRANSACTION_TYPE_PAYDEBT:
            return calculateAmountToAddForPayerForPaydebtTransaction(amount);
        case DeleteTransactionCommand.COMMAND_WORD:
            return calculateAmountToAddForPayerForDeleteTransaction(amount, payees);
        default:
            return null;
        }
    }

    /**
     * Returns the amount to add to the balance or debt of a payee.
     */
    public static Balance calculateAmountToAddForPayee(String transactionType, Amount amount,
                                                       UniquePersonList payees, SplitMethod splitMethod,
                                                       List<Integer> units, List<Integer> percentages) {
        switch (transactionType) {
        case TransactionType.TRANSACTION_TYPE_PAYMENT:
            return calculateAmountToAddForPayeeForPaymentTransaction(amount, payees, splitMethod, units, percentages);
        case TransactionType.TRANSACTION_TYPE_PAYDEBT:
            return calculateAmountToAddForPayeeForPaydebtTransaction(amount, payees);
        case DeleteTransactionCommand.COMMAND_WORD:
            return calculateAmountToAddForPayeeForDeleteTransaction(amount, payees);
        default:
            return null;
        }
    }

    //@@author ongkc
    /**
     * Calculate amount to add to the payer's balance after a new paydebt transaction is added.
     * Returned amount will be positive.
     */
    private static Balance calculateAmountToAddForPayerForPaydebtTransaction(Amount amount) {
        Double debt = Double.valueOf(amount.value);
        return getRoundedFormattedBalance(debt);
    }

    /**
     * Calculate amount to add to the payee's balance after a new paydebt transaction is added.
     * Returned amount will be negative.
     */
    private static Balance calculateAmountToAddForPayeeForPaydebtTransaction(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double debt = -Double.valueOf(amount.value) / (numberOfInvolvedPersons - 1);
        return getRoundedFormattedBalance(debt);
    }
    /**
     * Calculate amount to add to the payer's balance after a new payment transaction is added.
     * Returned amount will be positive.
     */
    public static Balance calculateAmountToAddForPayerForPaymentTransaction(Amount amount, UniquePersonList payees,
                                                                            SplitMethod splitMethod,
                                                                            List<Integer> units,
                                                                            List<Integer> percentages) {
        switch (splitMethod.method) {
            case UNITS:
                Integer numberOfUnitsForPayer = units.get(0);
                int totalNumberOfUnits = 0;
                for (Integer unit: units) {
                    totalNumberOfUnits += unit;
                }
                Double amountToAdd = Double.valueOf(amount.value) * (totalNumberOfUnits - numberOfUnitsForPayer)
                        / totalNumberOfUnits;
                return getRoundedFormattedBalance(amountToAdd);

                break;
            case PERCENTAGE:



                break;
            case EVENLY:
            default:
                int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
                Double amountToAdd = Double.valueOf(amount.value) * (numberOfInvolvedPersons - 1)
                    / numberOfInvolvedPersons;
                return getRoundedFormattedBalance(amountToAdd);
                break;
        }
    }

    /**
     * Calculate amount to add to the payee's balance after a new payment transaction is added.
     * Returned amount will be negative.
     */
    public static Balance calculateAmountToAddForPayeeForPaymentTransaction(Amount amount, UniquePersonList payees,
                                                                            SplitMethod splitMethod,
                                                                            List<Integer> units,
                                                                            List<Integer> percentages) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double amountToAdd = -Double.valueOf(amount.value) / numberOfInvolvedPersons;
        return getRoundedFormattedBalance(amountToAdd);
    }
    /**
     * Calculate amount to add to the payer's balance after a transaction is deleted.
     * Returned amount will be negative.
     */
    public static Balance calculateAmountToAddForPayerForDeleteTransaction(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double amountToAdd = -Double.valueOf(amount.value) * (numberOfInvolvedPersons - 1)
                / numberOfInvolvedPersons;
        return getFormattedBalance(amountToAdd);
    }
    /**
     * Calculate amount to add to the payee's balance after a transaction is deleted.
     * Returned amount will be positive.
     */
    public static Balance calculateAmountToAddForPayeeForDeleteTransaction(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double amountToAdd = Double.valueOf(amount.value) / numberOfInvolvedPersons;
        return getRoundedFormattedBalance(amountToAdd);
    }

    //@@author steven-jia
    private static Balance getFormattedBalance(Double debt) {
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(debt)));
    }

    private static Balance getRoundedFormattedBalance(Double amountToAdd) {
        amountToAdd = round(amountToAdd, NUMBER_OF_DECIMAL_PLACES);
        return getFormattedBalance(amountToAdd);
    }

    /**
     * Calculates the total number of people involved in a transaction
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


