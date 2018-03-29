package seedu.address.model;

import java.util.HashMap;

import seedu.address.model.person.Balance;
import seedu.address.model.transaction.Amount;
import seedu.address.model.person.Person;

/**
 * DebtsList of a Person, contains every person matched with the amount is owed or owes.
 */
public class DebtsList extends HashMap<Person,Balance> {
    public DebtsList() {
        super();
    }
    
    public addDept(Person person, Amount amount) {
        if (!this.containsKey(person)) {
            this.put(person, Balance.amount);
        }
        Balance oldDebts = this.get(person);
        this.replace(person, oldDebts.add(amount));
    }
    
}
