package seedu.address.model.transaction;

import java.util.Objects;
import java.util.Set;

import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Represent a transaction
 */
public class Transaction {

    private final UniquePersonList payer;
    private final Amount amount;
    private final Description description;
    private final UniquePersonList payees;

    public Transaction(UniquePersonList payer, Amount amount, Description description, UniquePersonList payees) {
        this.payer = payer;
        this.amount = amount;
        this.description = description;
        this.payees = payees;
    }

    public Transaction(UniquePersonList payer, Amount amount, Description description, Set<Person> payeesToAdd) {
        UniquePersonList payees = new UniquePersonList();
        for (Person p: payeesToAdd) {
            try {
                payees.add(p);
            } catch (DuplicatePersonException e) {
                System.out.println("Duplicate person" + p.getName() + " not added to list of payees");
            }
        }

        this.payer = payer;
        this.amount = amount;
        this.description = description;
        this.payees = payees;
    }

    public UniquePersonList getPayer() {
        return payer;
    }

    public Amount getAmount() {
        return amount;
    }

    public Description getDescription() {
        return description;
    }

    public UniquePersonList getPayees() {
        return payees;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Transaction)) {
            return false;
        }

        Transaction otherTransaction = (Transaction) other;
        return otherTransaction.getPayer().equals(this.getPayer())
                && otherTransaction.getAmount().equals(this.getAmount())
                && otherTransaction.getDescription().equals(this.getDescription())
                && otherTransaction.getPayees().equals(this.getPayees());
    }

    @Override
    public int hashCode() {
        return Objects.hash(payer, amount, description, payees);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Transaction paid by: ")
                .append(getPayer())
                .append("\n Amount: ")
                .append(getAmount())
                .append("\n Description: ")
                .append(getDescription())
                .append("\n Payees: ")
                .append(getPayees().asObservableList().toString());
        return builder.toString();
    }

}
