package seedu.address.model;

import java.util.HashMap;

import seedu.address.model.person.Balance;
import seedu.address.model.person.Person;

/**
 * DebtsList of a Person, contains every person matched with the amount is owed or owes.
 */
public class DebtsList extends HashMap<Person,Balance> {
    public DebtsList() {
        super();
    }
    
    public void updateDept(Person person, Balance dept) {
        if (!this.containsKey(person)) {
            this.put(person, dept);
        }
        Balance oldDebts = this.get(person);
        this.replace(person, oldDebts.add(dept));
    }
}
