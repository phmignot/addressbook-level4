package seedu.address.ui;

import guitests.guihandles.TransactionCardHandle;
import org.junit.Test;
import seedu.address.model.transaction.Transaction;

import static org.junit.Assert.assertEquals;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysTransaction;

public class TransactionCardTest extends GuiUnitTest {
    @Test
    public void display() {

    }

    @Test
    public void equals() {

    }

    /**
     * Asserts that {@code transactionCard} displays the details of {@code expectedTransaction} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplayTransaction(TransactionCard transactionCard, Transaction expectedTransaction, int expectedId) {
        guiRobot.pauseForHuman();

        TransactionCardHandle transactionCardHandle = new TransactionCardHandle(transactionCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", transactionCardHandle.getId());

        // verify transaction details are displayed correctly
        assertCardDisplaysTransaction(expectedTransaction, transactionCardHandle);
    }
}