package seedu.address.model;

import seedu.address.model.person.Balance;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.transaction.Amount;

import java.util.HashMap;

/**
 * Stores all the debts between the persons from the addressBook.
 */
public class DebtsTable extends HashMap<Person, DebtsList> {
    
    public DebtsTable() {
        super();
    }
    
    public void updateDebts(Balance payeeDebt, Person payer, UniquePersonList payees) {
        DebtsList payerDebtsLit = this.get(payer);
        Balance payerDept = payeeDebt.getInverse();
        for (Person payee: payees) {
            DebtsList payeeDebtsLit = this.get(payee);
            payerDebtsLit.updateDept(payee, payeeDebt);
            payeeDebtsLit.updateDept(payer, payerDept);
        }
    }

    public void add(Person personToAdd) {
        this.putIfAbsent(personToAdd, new DebtsList());
    }
}

