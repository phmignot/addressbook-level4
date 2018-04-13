package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TRANSACTION_PAYEE_ONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TRANSACTION_PAYEE_TWO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Creditor;
import seedu.address.model.person.Debtor;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.exceptions.TransactionNotFoundException;
import seedu.address.testutil.TransactionBuilder;

//@@author ongkc
public class AddTransactionCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullTransaction_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddTransactionCommand(null);
    }

    @Test
    public void execute_paymentTransactionAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingTransactionAdded modelStub =
                new ModelStubAcceptingTransactionAdded();
        Transaction validTransaction = new TransactionBuilder().build();

        CommandResult commandResult = getAddTransactionCommand(validTransaction, modelStub).execute();
        assertEquals(String.format(AddTransactionCommand.MESSAGE_SUCCESS, validTransaction),
                commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validTransaction), modelStub.transactionsAdded);
    }
    @Test
    public void execute_paymentTransactionRoundedAmountAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingTransactionAdded modelStub =
                new ModelStubAcceptingTransactionAdded();
        Transaction validTransaction = new TransactionBuilder().withAmount("12345").build();

        CommandResult commandResult = getAddTransactionCommand(validTransaction, modelStub).execute();
        assertEquals(String.format(AddTransactionCommand.MESSAGE_SUCCESS, validTransaction),
                commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validTransaction), modelStub.transactionsAdded);
    }
    @Test
    public void execute_paymentTransactionUnitsAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingTransactionAdded modelStub =
                new ModelStubAcceptingTransactionAdded();
        Transaction validTransaction = new TransactionBuilder().withSplitMethod("units").withUnits("4, 2").build();

        CommandResult commandResult = getAddTransactionCommand(validTransaction, modelStub).execute();
        assertEquals(String.format(AddTransactionCommand.MESSAGE_SUCCESS, validTransaction),
                commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validTransaction), modelStub.transactionsAdded);
    }
    @Test
    public void execute_paymentTransactionPercentageAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingTransactionAdded modelStub =
                new ModelStubAcceptingTransactionAdded();
        Transaction validTransaction = new TransactionBuilder().withSplitMethod("percentage")
                .withUnits("50, 50").build();

        CommandResult commandResult = getAddTransactionCommand(validTransaction, modelStub).execute();
        assertEquals(String.format(AddTransactionCommand.MESSAGE_SUCCESS, validTransaction),
                commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validTransaction), modelStub.transactionsAdded);
    }
    @Test
    public void execute_paydebtTransactionAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingTransactionAdded modelStub =
                new ModelStubAcceptingTransactionAdded();
        Transaction validTransaction = new TransactionBuilder().withTransactionType("paydebt").build();

        CommandResult commandResult = getAddTransactionCommand(validTransaction, modelStub).execute();
        assertEquals(String.format(AddTransactionCommand.MESSAGE_SUCCESS, validTransaction),
                commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validTransaction), modelStub.transactionsAdded);
    }
    @Test
    public void execute_personNotFound_throwsCommandException() throws Exception {
        ModelStub modelStub =
                new ModelStubThrowingPersonNotFoundException();
        Transaction validTransaction = new TransactionBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddTransactionCommand.MESSAGE_NONEXISTENT_PERSON);

        getAddTransactionCommand(validTransaction, modelStub).execute();
    }

    @Test
    public void equals() throws DuplicatePersonException {
        Transaction one = new TransactionBuilder().build();
        Transaction two = new TransactionBuilder().withTransactionType("paydebt").build();
        Transaction three = new TransactionBuilder().withPayees(
                VALID_TRANSACTION_PAYEE_ONE, VALID_TRANSACTION_PAYEE_TWO).build();
        AddTransactionCommand addOneCommand = new AddTransactionCommand(one);
        AddTransactionCommand addThreeCommand = new AddTransactionCommand(three);

        // same object -> returns true
        assertTrue(addOneCommand.equals(addOneCommand));

        // same values -> returns true
        AddTransactionCommand addOneCommandCopy = new AddTransactionCommand(one);
        assertTrue(addOneCommand.equals(addOneCommandCopy));

        // different types -> returns false
        assertFalse(addOneCommand.equals(1));

        // null -> returns false
        assertFalse(addOneCommand.equals(null));

        // different Transaction -> returns false
        assertFalse(addOneCommand.equals(addThreeCommand));
    }

    /**
     * Generates a new AddCommand with the details of the given transaction.
     */
    private AddTransactionCommand getAddTransactionCommand(Transaction transaction, Model model) {
        AddTransactionCommand command = new AddTransactionCommand(transaction);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(Person person) {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deletePerson(Person target) throws PersonNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updatePerson(Person target, Person editedPerson)
                throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public Person findPersonByName(Name name) throws PersonNotFoundException {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public UniquePersonList getPayeesList(ArgumentMultimap argMultimap, Model model) throws PersonNotFoundException,
                IllegalValueException {
            return null;
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public boolean hasNoTransactionWithPayer(Person person) throws TransactionNotFoundException {
            fail("This method should not be called.");
            return true;
        }

        @Override
        public boolean hasNoTransactionWithPayee(Person person) throws TransactionNotFoundException {
            fail("This method should not be called.");
            return true;
        }

        @Override
        public List<Transaction> findTransactionsWithPayer(Person person) {
            return null;
        }

        @Override
        public List<Transaction> findTransactionsWithPayee(Person person) {
            return null;
        }

        @Override
        public ObservableList<Transaction> getFilteredTransactionList() {
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredTransactionList(Predicate<Transaction> predicate) {

        }

        @Override
        public void addTransaction(Transaction transaction) throws CommandException, PersonNotFoundException {}

        @Override
        public void deleteTransaction(Transaction transaction) throws TransactionNotFoundException {
        }

        @Override
        public ObservableList<Debtor> getFilteredDebtors() {
            return null;
        }

        @Override
        public ObservableList<Creditor> getFilteredCreditors() {
            return null;
        }

        @Override
        public void updateDebtorList(Predicate<Debtor> predicateShowNoDebtors) {

        }

        @Override
        public void updateCreditorList(Predicate<Creditor> predicateShowAllCreditors) {

        }
    }

    public class ModelStubImpl extends AddTransactionCommandTest.ModelStub {
    }


    /**
     * A Model stub that always accept the transaction being added.
     */
    private class ModelStubAcceptingTransactionAdded extends ModelStub {
        final ArrayList<Transaction> transactionsAdded = new ArrayList<>();

        @Override
        public void addTransaction(Transaction transaction) {
            requireNonNull(transaction);
            transactionsAdded.add(transaction);
        }
        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that throw PersonNotFound except the transaction being added.
     */
    private class ModelStubThrowingPersonNotFoundException extends ModelStub {
        @Override
        public void addTransaction(Transaction transaction) throws PersonNotFoundException {
            throw new PersonNotFoundException();

        }
        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

    }


}
