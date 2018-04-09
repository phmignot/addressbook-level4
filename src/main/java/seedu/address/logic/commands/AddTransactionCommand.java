package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYEE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPLIT_METHOD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_TYPE;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.transaction.Transaction;

//@@author ongkc
/**
 * Adds a transaction to the address book.
 */
public class AddTransactionCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addTransaction";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a new transaction to the address book. \n"
            + "Parameters: "
            + PREFIX_TRANSACTION_TYPE + "TRANSACTION TYPE "
            + PREFIX_PAYER + "PAYER NAME "
            + PREFIX_AMOUNT + "AMOUNT "
            + PREFIX_DESCRIPTION + "DESCRIPTION "
            + "[" + PREFIX_PAYEE + "PAYEE NAME] "
            + PREFIX_SPLIT_METHOD + "SPLIT METHOD...\n"
            + "The transaction type options are: \"payment\" or \"paydebt\". \n"
            + "Only add a split method if you are recording a payment. \n"
            + "Your split method options are: \"evenly\", \"units\", or \"percentage\". \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TRANSACTION_TYPE + "payment "
            + PREFIX_PAYER + "John Doe "
            + PREFIX_AMOUNT + "100.00 "
            + PREFIX_DESCRIPTION + "Taxi ride to NUS "
            + PREFIX_PAYEE + "Alex Yeoh "
            + PREFIX_PAYEE + "Bernice Yu "
            + PREFIX_SPLIT_METHOD + "evenly";

    public static final String MESSAGE_SUCCESS = "New transaction added";
    public static final String MESSAGE_NONEXISTENT_PERSON = "The specified payer or payee(s) do not exist";
    public static final String MESSAGE_PAYEE_IS_PAYER = "A payee cannot be the payer";

    private final Transaction toAdd;

    /**
     * Creates an AddTransactionCommand to add the specified {@code Transaction}
     */
    public AddTransactionCommand(Transaction transaction) {
        requireNonNull(transaction);
        toAdd = transaction;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {

            if (!model.addTransaction(toAdd)) {
                throw new CommandException("Payee(s) has no debt");
            }
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (PersonNotFoundException pnfe) {
            throw new CommandException(MESSAGE_NONEXISTENT_PERSON);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddTransactionCommand// instanceof handles nulls
                && toAdd.equals(((AddTransactionCommand) other).toAdd));
    }
}
