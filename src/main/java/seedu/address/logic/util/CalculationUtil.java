package seedu.address.logic.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import seedu.address.model.person.Balance;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Transaction;
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
    public static Balance calculateAmountToAddForPayer(Boolean isAddingTransaction,
                                                       Transaction transaction) {
        // This is a deleteTransaction command
        if (!isAddingTransaction) {
            return calculateAmountToAddForPayerForDeleteTransaction(transaction);
        }

        switch (transaction.getTransactionType().value) {
        case TransactionType.TRANSACTION_TYPE_PAYMENT:
            return calculateAmountToAddForPayerForPaymentTransaction(transaction);
        case TransactionType.TRANSACTION_TYPE_PAYDEBT:
            return calculateAmountToAddForPayerForPaydebtTransaction(transaction);
        default:
            assert false : transaction.getTransactionType().value;
            ;
        }
        return null;
    }

    /**
     * Returns the amount to add to the balance or debt of a payee.
     */
    public static Balance calculateAmountToAddForPayee(Boolean isAddingTransaction,
                                                       Integer splitMethodValuesListIndex,
                                                       Transaction transaction) {
        // This is a deleteTransaction command
        if (!isAddingTransaction) {
            return calculateAmountToAddForPayeeForDeleteTransaction(splitMethodValuesListIndex, transaction);
        }

        switch (transaction.getTransactionType().value) {
        case TransactionType.TRANSACTION_TYPE_PAYMENT:
            return calculateAmountToAddForPayeeForPaymentTransaction(splitMethodValuesListIndex, transaction);
        case TransactionType.TRANSACTION_TYPE_PAYDEBT:
            return calculateAmountToAddForPayeeForPaydebtTransaction(transaction);
        default:
            assert false : transaction.getTransactionType().value;
        }
        return null;
    }

    //@@author ongkc
    /**
     * Calculates amount to add to the payer's balance after a new paydebt transaction is added.
     * Returned amount will be positive.
     */
    private static Balance calculateAmountToAddForPayerForPaydebtTransaction(Transaction transaction) {
        Double amountToAdd = Double.valueOf(transaction.getAmount().value);
        return getRoundedFormattedBalance(amountToAdd);
    }

    /**
     * Calculates amount to add to the payee's balance after a new paydebt transaction is added.
     * Returned amount will be negative.
     */
    private static Balance calculateAmountToAddForPayeeForPaydebtTransaction(Transaction transaction) {
        Integer numberOfPayee = calculateNumberOfInvolvedPersons(transaction.getPayees());
        Double amountToAdd = -Double.valueOf(transaction.getAmount().value) / (numberOfPayee - 1);
        return getRoundedFormattedBalance(amountToAdd);
    }

    //@@author steven-jia
    /**
     * Calculates amount to add to the payer's balance after a new payment transaction is added.
     * Returned amount will be positive.
     */
    public static Balance calculateAmountToAddForPayerForPaymentTransaction(Transaction transaction) {
        Double amountToAdd;
        switch (transaction.getSplitMethod().method) {
        case UNITS:
            Integer numberOfUnitsForPayer = transaction.getUnits().get(0);
            int totalNumberOfUnits = calculateTotalNumberOfUnits(transaction.getUnits());
            amountToAdd = transaction.getAmount().getDoubleValue()
                    * (totalNumberOfUnits - numberOfUnitsForPayer) / totalNumberOfUnits;
            break;
        case PERCENTAGE:
            Integer percentageForPayer = transaction.getPercentages().get(0);
            amountToAdd = transaction.getAmount().getDoubleValue()
                    * (100 - percentageForPayer) / 100;
            break;
        case EVENLY:
        default:
            amountToAdd = calculateAmountForPayerSplitEvenly(transaction.getAmount(),
                    transaction.getPayees());
            break;
        }
        return getRoundedFormattedBalance(amountToAdd);
    }

    /**
     * Calculates amount to add to the payee's balance after a new payment transaction is added.
     * Returned amount will be negative.
     */
    public static Balance calculateAmountToAddForPayeeForPaymentTransaction(Integer splitMethodValuesListIndex,
                                                                            Transaction transaction) {
        Double amountToAdd;
        switch (transaction.getSplitMethod().method) {
        case UNITS:
            Integer numberOfUnitsForPayee = transaction.getUnits().get(splitMethodValuesListIndex);
            int totalNumberOfUnits = calculateTotalNumberOfUnits(transaction.getUnits());
            amountToAdd = -Double.valueOf(transaction.getAmount().value) * numberOfUnitsForPayee
                    / totalNumberOfUnits;
            break;
        case PERCENTAGE:
            Integer percentageForPayee = transaction.getPercentages().get(splitMethodValuesListIndex);
            amountToAdd = -Double.valueOf(transaction.getAmount().value) * percentageForPayee / 100;
            break;
        case EVENLY:
        default:
            amountToAdd = -calculateAmountForPayeeSplitEvenly(transaction.getAmount(),
                    transaction.getPayees());
            break;
        }
        return getRoundedFormattedBalance(amountToAdd);
    }
    /**
     * Calculates amount to add to the payer's balance after a transaction is deleted.
     * Returned amount will be negative.
     */
    public static Balance calculateAmountToAddForPayerForDeleteTransaction(Transaction transaction) {
        if (transaction.getTransactionType().value.equals(TransactionType.TRANSACTION_TYPE_PAYDEBT)) {
            return getRoundedFormattedBalance(-transaction.getAmount().getDoubleValue());
        }

        Double amountToAdd;
        switch (transaction.getSplitMethod().method) {
        case UNITS:
            Integer numberOfUnitsForPayer = transaction.getUnits().get(0);
            int totalNumberOfUnits = calculateTotalNumberOfUnits(transaction.getUnits());
            amountToAdd = -transaction.getAmount().getDoubleValue()
                    * (totalNumberOfUnits - numberOfUnitsForPayer) / totalNumberOfUnits;
            break;
        case PERCENTAGE:
            Integer percentageForPayer = transaction.getPercentages().get(0);
            amountToAdd = -transaction.getAmount().getDoubleValue()
                    * (100 - percentageForPayer) / 100;
            break;
        case EVENLY:
        default:
            amountToAdd = -calculateAmountForPayerSplitEvenly(transaction.getAmount(),
                    transaction.getPayees());
            break;
        }
        return getRoundedFormattedBalance(amountToAdd);
    }
    /**
     * Calculates amount to add to the payee's balance after a transaction is deleted.
     * Returned amount will be positive.
     */
    public static Balance calculateAmountToAddForPayeeForDeleteTransaction(Integer splitMethodValuesListIndex,
                                                                           Transaction transaction) {
        if (transaction.getTransactionType().value.equals(TransactionType.TRANSACTION_TYPE_PAYDEBT)) {
            return getRoundedFormattedBalance(transaction.getAmount().getDoubleValue());
        }

        Double amountToAdd;
        switch (transaction.getSplitMethod().method) {
        case UNITS:
            Integer numberOfUnitsForPayee = transaction.getUnits().get(splitMethodValuesListIndex);
            int totalNumberOfUnits = calculateTotalNumberOfUnits(transaction.getUnits());
            amountToAdd = transaction.getAmount().getDoubleValue() * numberOfUnitsForPayee
                    / totalNumberOfUnits;
            break;
        case PERCENTAGE:
            Integer percentageForPayee = transaction.getPercentages().get(splitMethodValuesListIndex);
            amountToAdd = transaction.getAmount().getDoubleValue() * percentageForPayee / 100;
            break;
        case EVENLY:
        default:
            amountToAdd = calculateAmountForPayeeSplitEvenly(transaction.getAmount(),
                    transaction.getPayees());
            break;
        }
        return getRoundedFormattedBalance(amountToAdd);
    }

    /**
     * Calculates amount to add to a payer's balance for a transaction that is split evenly.
     * Returned amount will be positive.
     */
    private static Double calculateAmountForPayerSplitEvenly(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        return Double.valueOf(amount.value) * (numberOfInvolvedPersons - 1) / numberOfInvolvedPersons;
    }

    /**
     * Calculates amount to add to a payee's balance for a transaction that is split evenly.
     * Returned amount will be positive.
     */
    private static Double calculateAmountForPayeeSplitEvenly(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        return Double.valueOf(amount.value) / numberOfInvolvedPersons;
    }

    private static Balance getRoundedFormattedBalance(Double amountToAdd) {
        amountToAdd = round(amountToAdd, NUMBER_OF_DECIMAL_PLACES);
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(amountToAdd)));
    }

    /**
     * Calculates the total number of people involved in a transaction
     */
    public static int calculateNumberOfInvolvedPersons(UniquePersonList payees) {
        return payees.asObservableList().size() + 1;
    }

    /**
     * Calculates the total number of units given a list of units
     */
    private static int calculateTotalNumberOfUnits(List<Integer> units) {
        int totalNumberOfUnits = 0;
        for (Integer unit: units) {
            totalNumberOfUnits += unit;
        }
        return totalNumberOfUnits;
    }

    private static double round(double value, int places) {
        BigDecimal amount = new BigDecimal(value);
        amount = amount.setScale(places, RoundingMode.HALF_UP);
        return amount.doubleValue();
    }

}


