# ongkc
###### /java/seedu/address/logic/commands/AddTransactionCommandTest.java
``` java
public class AddTransactionCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullTransaction_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddTransactionCommand(null);
    }

    @Test
    public void execute_transactionAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingTransactionAdded modelStub =
                new ModelStubAcceptingTransactionAdded();
        Transaction validTransaction = new TransactionBuilder().build();

        CommandResult commandResult = getAddTransactionCommand(validTransaction, modelStub).execute();
        assertEquals(String.format(AddTransactionCommand.MESSAGE_SUCCESS, validTransaction),
                commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validTransaction), modelStub.transactionsAdded);
    }

    @Test
    public void execute_payerOrPayeeIsPayerOrPayee_throwsCommandException() throws Exception {
        ModelStub modelStub =
                new ModelStubThrowingPayerIsPayeeException();
        Transaction validTransaction = new TransactionBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddTransactionCommand.MESSAGE_PAYEE_IS_PAYER);

        getAddTransactionCommand(validTransaction, modelStub).execute();
    }

    @Test
    public void equals() {
        Transaction one = new TransactionBuilder().withPayees(TypicalTransactions.getTypicalPayees().get(3)).build();
        Transaction two = new TransactionBuilder().withPayees(TypicalTransactions.getTypicalPayees().get(4)).build();
        AddTransactionCommand addOneCommand = new AddTransactionCommand(one);
        AddTransactionCommand addTwoCommand = new AddTransactionCommand(two);

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
        assertFalse(addOneCommand.equals(addTwoCommand));
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
        public void addPerson(Person person) throws DuplicatePersonException {
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
        public boolean findTransactionsWithPayer(Person person) throws TransactionNotFoundException {
            fail("This method should not be called.");
            return true;
        }

        @Override
        public boolean findTransactionsWithPayee(Person person) throws TransactionNotFoundException {
            fail("This method should not be called.");
            return true;
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
        public void addTransaction(Transaction transaction) throws CommandException {}

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

    private class ModelStubThrowingPayerIsPayeeException extends ModelStub {
        @Override
        public void addTransaction(Transaction transaction) throws CommandException {
            throw new CommandException(MESSAGE_PAYEE_IS_PAYER);
        }


        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

    }


}
```
###### /java/seedu/address/storage/XmlSerializableAddressBookTest.java
``` java
    @Test
    public void toModelType_typicalTransactionsFile_success() throws Exception {
        XmlSerializableAddressBook dataFromFile = XmlUtil.getDataFromFile(TYPICAL_TRANSACTIONS_FILE,
                XmlSerializableAddressBook.class);
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalTransactionsAddressBook = TypicalTransactions.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalTransactionsAddressBook);
    }
    @Test
    public void toModelType_invalidTransactionFile_throwsIllegalValueException() throws Exception {
        XmlSerializableAddressBook dataFromFile = XmlUtil.getDataFromFile(INVALID_TRANSACTION_FILE,
                XmlSerializableAddressBook.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }
}
```
###### /java/seedu/address/storage/XmlAddressBookStorageTest.java
``` java
        original.addTransaction(TypicalTransactions.getTypicalTransactions().get(3));
        original.removeTransaction(TypicalTransactions.getTypicalTransactions().get(3));
        xmlAddressBookStorage.saveAddressBook(original, filePath);
        readBack = xmlAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));
```
###### /java/seedu/address/model/transaction/AmountTest.java
``` java
public class AmountTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Amount(null));
    }

    @Test
    public void constructor_invalidAmount_throwsIllegalArgumentException() {
        String invalidAmount = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Amount(invalidAmount));
    }

    @Test
    public void isValidAmount() {
        // null amount
        Assert.assertThrows(NullPointerException.class, () -> Amount.isValidAmount(null));

        // invalid amount
        assertFalse(Amount.isValidAmount("")); // empty string
        assertFalse(Amount.isValidAmount(" ")); // spaces only
        assertFalse(Amount.isValidAmount("^")); // only non-alphanumeric characters
        assertFalse(Amount.isValidAmount("123*")); // contains non-alphanumeric characters
        assertFalse(Amount.isValidAmount("1.2.3")); // contains irregular numeric format
        assertFalse(Amount.isValidAmount("one dollars")); // contains alphabet characters
        assertFalse(Amount.isValidAmount("1 2 3")); // contains spacing between numbers
        assertFalse(Amount.isValidAmount("12345.123")); // numbers & more than 2 decimals
        assertFalse(Amount.isValidAmount("12345.")); // numbers & decimal point only

        // valid name
        assertTrue(Amount.isValidAmount("12345")); // numbers only
        assertTrue(Amount.isValidAmount("12345.1")); // numbers & 1 decimal only
        assertTrue(Amount.isValidAmount("12345.78")); // numbers & 2 decimals only
    }
}
```
###### /java/seedu/address/model/transaction/DescriptionTest.java
``` java
public class DescriptionTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Description(null));
    }

    @Test
    public void constructor_invalidDescription_throwsIllegalArgumentException() {
        String invalidDescription = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Description(invalidDescription));
    }

    @Test
    public void isValidDescription() {
        // null description
        Assert.assertThrows(NullPointerException.class, () -> Description.isValidDescription(null));

        // invalid description
        assertFalse(Description.isValidDescription("")); // empty string
        assertFalse(Description.isValidDescription(" ")); // spaces only

        // valid description
        assertTrue(Description.isValidDescription("peter jack")); // alphabets only
        assertTrue(Description.isValidDescription("12345")); // numbers only
        assertTrue(Description.isValidDescription("lunch meal")); // alphanumeric characters
        assertTrue(Description.isValidDescription("Dinner Meal")); // with capital letters
        assertTrue(Description.isValidDescription("expense for meal and travel")); // long description
        assertTrue(Description.isValidDescription("*food*")); // contain non-alphanumeric characters
    }
}
```
###### /java/seedu/address/model/TransactionListTest.java
``` java
public class TransactionListTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        TransactionList transactionList = new TransactionList();
        thrown.expect(UnsupportedOperationException.class);
        transactionList.asObservableList().remove(0);
    }
}
```
###### /java/seedu/address/testutil/TypicalTransactions.java
``` java
/**
 * A utility class containing a list of {@code Transaction} objects to be used in tests.
 */
public class TypicalTransactions {

    private static Transaction t1 = null;
    private static Transaction t2 = null;
    private static Transaction t3 = null;
    private static Transaction t4 = null;
    private static Transaction t5 = null;

    private static Person person1 = SampleDataUtil.getSamplePersons()[0];
    private static Person person2 = SampleDataUtil.getSamplePersons()[1];
    private static Person person3 = SampleDataUtil.getSamplePersons()[2];
    private static Person person4 = SampleDataUtil.getSamplePersons()[3];
    private static Person person5 = SampleDataUtil.getSamplePersons()[4];
    private static Person person6 = SampleDataUtil.getSamplePersons()[5];

    private static UniquePersonList payee2 = new UniquePersonList();
    private static UniquePersonList payee4 = new UniquePersonList();
    private static UniquePersonList payee6 = new UniquePersonList();
    private static UniquePersonList payeeGeorge = new UniquePersonList();
    private static UniquePersonList payeeFiona = new UniquePersonList();

    private static Date date = new Date();

    private static SplitMethod splitEvenly = new SplitMethod(SplitMethod.SPLIT_METHOD_EVENLY);
    private static SplitMethod splitByUnits = new SplitMethod(SplitMethod.SPLIT_METHOD_UNITS);
    private static SplitMethod splitByPercentage = new SplitMethod(SplitMethod.SPLIT_METHOD_PERCENTAGE);
    private static List<Integer> unitsList = new ArrayList<Integer>(Arrays.asList(1, 2, 3));
    private static List<Integer> percentagesList = new ArrayList<Integer>(Arrays.asList(25, 30, 45));

    static {
        try {
            payee2.add(person2);
            payee4.add(person4);
            payee6.add(person6);
            payeeGeorge.add(TypicalPersons.GEORGE);
            payeeFiona.add(TypicalPersons.FIONA);


            t1 = new TransactionBuilder().withPayer(person1).withAmount("0.00")
                    .withDescription("Boat trip").withPayees(payee2).withDate(date)
                    .withSplitMethod(splitEvenly).build();
            t2 = new TransactionBuilder().withPayer(person3).withAmount("0.00")
                    .withDescription("Food for barbecue").withPayees(payee4)
                    .withSplitMethod(splitByUnits).withUnits(unitsList).withDate(date).build();
            t3 = new TransactionBuilder().withPayer(person5).withAmount("0.00")
                    .withDescription("Open air concert").withPayees(payee6)
                    .withSplitMethod(splitByPercentage).withPercentages(percentagesList).withDate(date).build();
            t4 = new TransactionBuilder().withPayer(TypicalPersons.GEORGE).withAmount("0.00")
                    .withDescription("Transport")
                    .withPayees(payeeFiona)
                    .withSplitMethod(splitEvenly).withDate(date).build();
            t5 = new TransactionBuilder().withPayer(TypicalPersons.FIONA)
                    .withAmount("0.00").withDescription("Dinner")
                    .withPayees(payeeFiona).withDate(date)
                    .withSplitMethod(splitEvenly).withDate(date).build();
        } catch (DuplicatePersonException dpe) {
            dpe.printStackTrace();
        }
    }

    private TypicalTransactions() { } // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Transaction transaction : getTypicalTransactions()) {
            try {
                ab.addTransaction(transaction);
            } catch (CommandException ce) {
                System.out.println(ce.getMessage());
            }
        }
        return ab;
    }

    public static List<Transaction> getTypicalTransactions() {
        return new ArrayList<>(Arrays.asList(t1, t2, t3, t4, t5));
    }

    public static List<UniquePersonList> getTypicalPayees() {
        return new ArrayList<>(Arrays.asList(payee2, payee4, payee6, payeeGeorge, payeeFiona));
    }
}
```
