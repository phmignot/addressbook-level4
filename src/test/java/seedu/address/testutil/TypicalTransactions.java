package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.transaction.Transaction;
//@author ongkc
public class TypicalTransactions {

    public static final Transaction ONE = new TransactionBuilder().withPayeeName("Alice Pauline")
            .withAmount("123.45").withDescription("for lunch meal")
            .withPayeeName("Brandon Ingram").build();
    public static final Transaction TWO = new TransactionBuilder().withPayeeName("Alice Pauline")
            .withAmount("123.00").withDescription("for transport")
            .withPayeeName("Brandon Ingram").build();
    public static final Transaction THREE = new TransactionBuilder().withPayeeName("Alice Pauline")
            .withAmount("123").withDescription("for dinner")
            .withPayeeName("Brandon Ingram").build();

    // Manually added
    public static final Transaction FOUR = new TransactionBuilder().withPayeeName("Alice Pauline")
            .withAmount("9999").withDescription("for transport")
            .withPayeeName("Brandon Ingram").build();
    public static final Transaction FIVE = new TransactionBuilder().withPayeeName("Alice Pauline")
            .withAmount("9999").withDescription("for dinner")
            .withPayeeName("Brandon Ingram").build();

    private TypicalTransactions() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Transaction transaction : getTypicalTransactions()) {
                ab.addTransaction(transaction);
        }
        return ab;
    }

    public static List<Transaction> getTypicalTransactions() {
        return new ArrayList<>(Arrays.asList(ONE, TWO, THREE));
    }
}
