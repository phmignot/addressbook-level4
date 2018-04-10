package seedu.address.model;

import static seedu.address.logic.util.CalculationUtil.calculateAmountToAddForPayee;

import java.util.HashMap;

import seedu.address.model.person.Balance;
import seedu.address.model.person.Person;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.TransactionType;

/**
 * Stores all the debts between the persons from the addressBook.
 */
//@@author phmignot
public class DebtsTable extends HashMap<Person, DebtsList> {

    private DebtsTable internalList;

    public DebtsTable() {
        super();
    }

    /**
     * Updates the DebtsTable due to a transaction
     * payeeDebt is a negative {@Code Balance} value, because the payee owes money.
     * payerDebt is a positive {@Code Balance} value, because the payer is owed.
     * @param transaction to register the table.
     */
    public boolean updateDebts(String typeOfTransaction, Transaction transaction) {
        Person payer = transaction.getPayer();
        if (!this.containsKey(payer)) {
            this.add(payer);
            System.out.println("Adding payer " + payer.getName().fullName);
        }
        DebtsList payerDebtsList = this.get(payer);
        if (typeOfTransaction.equals(TransactionType.TRANSACTION_TYPE_PAYDEBT)) {
            for (Person payee : transaction.getPayees()) {
                if (payerDebtsList.get(payee) == null || payerDebtsList.get(payee).getDoubleValue() >= 0) {
                    return false;
                }
            }
        }
        Balance payerDebtToAdd = calculateAmountToAddForPayee(typeOfTransaction, transaction.getAmount(),
                transaction.getPayees());
        Balance payeeDebtToAdd = payerDebtToAdd.getInverse();
        for (Person payee: transaction.getPayees()) {
            if (!this.containsKey(payee)) {
                this.add(payee);
                System.out.println("Adding payee " + payee.getName().fullName);
            }
            DebtsList payeeDebtsList = this.get(payee);
            payerDebtsList.updateDebt(payee, payeeDebtToAdd);
            payeeDebtsList.updateDebt(payer, payerDebtToAdd);
        }
        return true;
    }

    public void add(Person personToAdd) {
        this.putIfAbsent(personToAdd, new DebtsList());
    }

    public DebtsTable asObservableList() {
        return internalList;
    }

    /**
     * Displays the content of the Debts Table in the terminal.
     */
    public void display() {
        System.out.println("DEBTS TABLE : ");
        this.forEach(((person, debtsList) -> {
            System.out.println(person.getName().fullName + " : ");
            debtsList.display();
            System.out.println();
        }));
    }


}

