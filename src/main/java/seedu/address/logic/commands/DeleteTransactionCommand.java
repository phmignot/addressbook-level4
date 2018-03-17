package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.exceptions.TransactionNotFoundException;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Deletes a transaction identified using its displayed index from the list of transactions.
 */
public class DeleteTransactionCommand extends UndoableCommand {

        public static final String COMMAND_WORD = "deleteTransaction";

        public static final String MESSAGE_USAGE = COMMAND_WORD
                + ": Deletes the transaction identified by its index number used in the list of transactions.\n"
                + "Parameters: INDEX (must be a positive integer)\n"
                + "Example: " + COMMAND_WORD + " 1";

        public static final String MESSAGE_DELETE_TRANSACTION_SUCCESS = "Deleted Transaction: %1$s";

        private final Index targetIndex;

        private Transaction transactionToDelete;

        public DeleteTransactionCommand(Index targetIndex) {
            this.targetIndex = targetIndex;
        }


        @Override
        public CommandResult executeUndoableCommand() {
            requireNonNull(transactionToDelete);
            try {
                model.deleteTransaction(transactionToDelete);
            } catch (TransactionNotFoundException tnfe) {
                throw new AssertionError("The target transaction cannot be missing");
            }

            return new CommandResult(String.format(MESSAGE_DELETE_TRANSACTION_SUCCESS, transactionToDelete));
        }
        /*
        @Override
        protected void preprocessUndoableCommand() throws CommandException {
            List<Transaction> lastShownList = model.getFilteredTransactionList();

            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            transactionToDelete = lastShownList.get(targetIndex.getZeroBased());
        }
        */
        @Override
        public boolean equals(Object other) {
            return other == this // short circuit if same object
                    || (other instanceof seedu.address.logic.commands.DeleteTransactionCommand // instanceof handles nulls
                    && this.targetIndex.equals(((seedu.address.logic.commands.DeleteTransactionCommand) other).targetIndex) // state check
                    && Objects.equals(this.transactionToDelete, ((seedu.address.logic.commands.DeleteTransactionCommand) other).transactionToDelete));
        }
    }

