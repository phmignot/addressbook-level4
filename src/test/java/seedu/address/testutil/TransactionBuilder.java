//@@author phmignot
package seedu.address.testutil;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Description;
import seedu.address.model.transaction.SplitMethod;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.TransactionType;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Transaction objects.
 */
public class TransactionBuilder {

    public static final String DEFAULT_AMOUNT = "123.45";
    public static final String DEFAULT_DESCRIPTION = "paying for Cookies";

    private Person payer;
    private Amount amount;
    private Description description;
    private Date dateTime;
    private UniquePersonList payees;
    private TransactionType transactionType;
    private SplitMethod splitMethod;
    private List<Integer> unitsList;
    private List<Integer> percentagesList;

    public TransactionBuilder() {
        transactionType = new TransactionType("payment");
        payer = SampleDataUtil.getSamplePersons()[0];
        amount = new Amount(DEFAULT_AMOUNT);
        description = new Description(DEFAULT_DESCRIPTION);

        UniquePersonList samplePayees = new UniquePersonList();
        try {
            samplePayees.add(SampleDataUtil.getSamplePersons()[1]);
            samplePayees.add(SampleDataUtil.getSamplePersons()[2]);
            samplePayees.add(SampleDataUtil.getSamplePersons()[3]);
        } catch (DuplicatePersonException dpe) {
            throw new AssertionError("This payee has already been added");
        }
        dateTime = Date.from(Instant.now(Clock.system(ZoneId.of("Asia/Singapore"))));
        payees = samplePayees;
        splitMethod = new SplitMethod(SplitMethod.SPLIT_METHOD_EVENLY);
        unitsList = Collections.emptyList();
        percentagesList = Collections.emptyList();
    }

    /**
     * Initializes the TransactionBuilder with the data of {@code transactionToCopy}.
     */
    public TransactionBuilder(Transaction transactionToCopy) {
        payer = transactionToCopy.getPayer();
        amount = transactionToCopy.getAmount();
        description = transactionToCopy.getDescription();
        dateTime = transactionToCopy.getDateTime();
        payees = transactionToCopy.getPayees();
        transactionType = transactionToCopy.getTransactionType();
        splitMethod = transactionToCopy.getSplitMethod();
        unitsList = transactionToCopy.getUnits();
        percentagesList = transactionToCopy.getPercentages();
    }

    /**
     * Sets the {@code payer} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withPayer(Person payer) {
        this.payer = payer;
        return this;
    }
    /**
     * Sets the {@code payer} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
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
     * Sets the {@code payees} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withPayees(UniquePersonList payees) {
        this.payees = payees;
        return this;
    }

    /**
     * Sets the {@code date & time} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withDate(Date dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    /**
     * Sets the {@code SplitMethod} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withSplitMethod(SplitMethod splitMethod) {
        this.splitMethod = splitMethod;
        return this;
    }

    /**
     * Sets the units {@code List<Integer>} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withUnits(List<Integer> unitsList) {
        this.unitsList = unitsList;
        return this;
    }

    /**
     * Sets the percentages {@code List<Integer>} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withPercentages(List<Integer> percentagesList) {
        this.percentagesList = percentagesList;
        return this;
    }

    /**
     * Builds a new {@code Transaction}.
     */
    public Transaction build() {
        return new Transaction(transactionType, payer, amount, description, dateTime,
                payees, splitMethod, unitsList, percentagesList);
    }
}
