package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.transaction.Transaction;

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

    public static List<Transaction> getTypicalTransactions() {
        return new ArrayList<>(Arrays.asList(T1, T2, T3));
    }
}
