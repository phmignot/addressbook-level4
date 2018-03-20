package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.transaction.Transaction;
//@author ongkc
/**
 * A utility class containing a list of {@code Transaction} objects to be used in tests.
 */
public class TypicalTransactions {

    public static final Transaction T1 = new TransactionBuilder().withPayerName("Alice Dupree").withAmount("5.62")
            .withDescription("Boat trip").withPayeeName("John Remi").build();
    public static final Transaction T2 = new TransactionBuilder().withPayerName("Joseph Marie").withAmount("123.45")
            .withDescription("Food for barbecue").withPayerName("Marie Joe").build();
    public static final Transaction T3 = new TransactionBuilder().withPayerName("Damien Francois").withAmount("10.00")
            .withDescription("Open air concert").withPayeeName("Patrick Bruel").build();
    // Manually added
    public static final Transaction T4 = new TransactionBuilder().withPayeeName("Alice Pauline")
            .withAmount("9999").withDescription("for transport")
            .withPayeeName("Brandon Ingram").build();
    public static final Transaction T5 = new TransactionBuilder().withPayeeName("Alice Pauline")
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
        return new ArrayList<>(Arrays.asList(T1, T2, T3));
    }
}
