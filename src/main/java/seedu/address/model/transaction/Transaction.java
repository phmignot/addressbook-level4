package seedu.address.model.transaction;

import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;

import java.util.Objects;
import java.util.Set;

public class Transaction {

    private final Person payer;
    private final Double amount;
    private final String description;
    private final UniquePersonList payees;

    public Transaction(Person payer, Double amount, String description, UniquePersonList payees) {
        this.payer = payer;
        this.amount = amount;
        this.description = description;
        this.payees = payees;
    }

    public Transaction(Person payer, Double amount, String description, Set<Person> payeesToAdd) {
        UniquePersonList payees = new UniquePersonList();
        for(Person p: payeesToAdd) {
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

    public Person getPayer() {
        return payer;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
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
