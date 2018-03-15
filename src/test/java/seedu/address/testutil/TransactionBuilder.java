//@phmignot
package seedu.address.testutil;

import seedu.address.model.person.Name;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Description;
import seedu.address.model.transaction.Transaction;

/**
 * A utility class to help with building Transaction objects.
 */
public class TransactionBuilder {

    public static final String DEFAULT_PAYER_NAME = "Alice Pauline";
    public static final String DEFAULT_AMOUNT = "123.45";
    public static final String DEFAULT_DESCRIPTION = "paying for Cookies";
    public static final String DEFAULT_PAYEE_NAME = "Bob Jacques";

    private Name payerName;
    private Amount amount;
    private Description description;
    private Name payeeName;

    public TransactionBuilder() {
        payerName = new Name(DEFAULT_PAYER_NAME);
        amount = new Amount(DEFAULT_AMOUNT);
        description = new Description(DEFAULT_DESCRIPTION);
        payeeName = new Name(DEFAULT_PAYEE_NAME);
    }

    /**
     * Initializes the TransactionBuilder with the data of {@code transactionToCopy}.
     */
    public TransactionBuilder(Transaction transactionToCopy) {
        payerName = transactionToCopy.getPayer();
        amount = transactionToCopy.getAmount();
        description = transactionToCopy.getDescription();
        payeeName = transactionToCopy.getPayee();
    }


    /**
     * Sets the {@code payerName} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withPayerName(String payerName) {
        this.payerName = new Name(payerName);
        return this;
    }

    /**
     * Sets the {@code Amount} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withAmount(String amount) {
        this.amount = new Amount(amount);
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withDescription(String description) {
        this.description = new Description(description);
        return this;
    }

    /**
     * Sets the {@code payeeName} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withPayeeName(String payeeName) {
        this.payeeName = new Name(payeeName);
        return this;
    }

    public Transaction build() {
        return new Transaction(payerName, amount, description, payeeName);
    }
}
