# ongkc
###### /java/seedu/address/logic/util/BalanceCalculationUtil.java
``` java
    /**
     * Calculate new payee(s) balance after a new transaction is added
     */
    public static Balance calculateAddTransactionPayeeDebt(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double debt = -Double.valueOf(amount.value) / numberOfInvolvedPersons;
        debt = round(debt, NUMBER_OF_DECIMAL_PLACES);
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(debt)));
    }
    /**
     * Calculate new payer balance a transaction is deleted
     */
    public static Balance calculateDeleteTransactionPayerDebt(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double debt = -Double.valueOf(amount.value) * (numberOfInvolvedPersons - 1)
                / numberOfInvolvedPersons;
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(debt)));
    }
    /**
     * Calculate new payer balance after a new transaction is added
     */
    public static Balance calculateAddTransactionPayerDebt(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double debt = Double.valueOf(amount.value) * (numberOfInvolvedPersons - 1)
                / numberOfInvolvedPersons;
        debt = round(debt, NUMBER_OF_DECIMAL_PLACES);
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(debt)));
    }
    /**
     * Calculate new payee(s) balance after a transaction is deleted
     */
    public static Balance calculateDeleteTransactionPayeeDebt(Amount amount, UniquePersonList payees) {
        int numberOfInvolvedPersons = calculateNumberOfInvolvedPersons(payees);
        Double debt = Double.valueOf(amount.value) / numberOfInvolvedPersons;
        debt = round(debt, NUMBER_OF_DECIMAL_PLACES);
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(debt)));
    }

```
###### /java/seedu/address/logic/parser/AddTransactionCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AddTransactionCommand object
 */
public class AddTransactionCommandParser implements Parser<AddTransactionCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddTransactionCommand
     * and returns an AddTransactionCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddTransactionCommand parse(String args, Model model) throws ParseException, CommandException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PAYER, PREFIX_AMOUNT, PREFIX_DESCRIPTION, PREFIX_PAYEE);

        if (!arePrefixesPresent(argMultimap, PREFIX_PAYER, PREFIX_AMOUNT, PREFIX_DESCRIPTION, PREFIX_PAYEE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddTransactionCommand.MESSAGE_USAGE));
        }

        try {
            Person payer = model.findPersonByName(ParserUtil.parseName(argMultimap.getValue(PREFIX_PAYER)).get());
            Amount amount = ParserUtil.parseAmount(argMultimap.getValue(PREFIX_AMOUNT)).get();
            Description description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION)).get();
            UniquePersonList payees = model.getPayeesList(argMultimap, model);
            Date dateTime = Date.from(Instant.now(Clock.system(ZoneId.of("Asia/Singapore"))));

            Transaction transaction = new Transaction(payer, amount, description, dateTime, payees);

            return new AddTransactionCommand(transaction);
        } catch (PersonNotFoundException pnfe) {
            throw new CommandException(MESSAGE_NONEXISTENT_PERSON);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

```
###### /java/seedu/address/logic/commands/AddTransactionCommand.java
``` java
/**
 * Adds a transaction to the address book.
 */
public class AddTransactionCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addTransaction";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a new transaction to the address book. "
            + "Parameters: "
            + PREFIX_PAYER + "PAYER NAME "
            + PREFIX_AMOUNT + "AMOUNT "
            + PREFIX_DESCRIPTION + "DESCRIPTION "
            + "[" + PREFIX_PAYEE + "PAYEE NAME]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_PAYER + "John Doe "
            + PREFIX_AMOUNT + "3456.78 "
            + PREFIX_DESCRIPTION + "Taxi ride to NUS "
            + PREFIX_PAYEE + "Alex Yeoh "
            + PREFIX_PAYEE + "Bernice Yu";

    public static final String MESSAGE_SUCCESS = "New transaction added";
    public static final String MESSAGE_NONEXISTENT_PERSON = "The specified payer or payee(s) do not exist";

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
            model.addTransaction(toAdd);

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
```
###### /java/seedu/address/logic/LogicManager.java
``` java
    @Override
    public void updateFilteredPersonList(Person person) {
        DebtsTable debtsTable = model.getAddressBook().getDebtsTable();
        DebtsList debtsList = debtsTable.get(person);
        resetDebt();
        updateDebt(debtsList);
        //        model.updateFilteredPersonList(PREDICATE_SHOW_NO_PERSON);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

    }
    @Override
    public void updateFilteredPersonList() {
        resetDebt();
        model.updateFilteredPersonList(PREDICATE_SHOW_NO_PERSON);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }
    /**
     * Update amount of debt owed to other people
     */
    private void updateDebt(DebtsList debtsList) {
        ObservableList<Person> persons = model.getAddressBook().getPersonList();
        for (Person person: persons) {
            if (debtsList.get(person) != null) {
                person.setDebt(debtsList.get(person));
            }
        }
    }
```
###### /java/seedu/address/storage/XmlAdaptedTransaction.java
``` java
/**
 * JAXB-friendly adapted version of the Transaction.
 */
public class XmlAdaptedTransaction {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Transaction's %s field is missing!";

    @XmlElement(required = true)
    private XmlAdaptedPerson payer;
    @XmlElement(required = true)
    private String amount;
    @XmlElement(required = true)
    private String description;
    @XmlElement(required = true)
    private Date dateTime;
    @XmlElement(required = true)
    private List<XmlAdaptedPerson> payees = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedTransaction.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedTransaction() {}

    /**
     * Constructs an {@code XmlAdaptedTransaction} with the given person details.
     */
    public XmlAdaptedTransaction(Person payer, String amount, String description, UniquePersonList payees) {
        this.payer = new XmlAdaptedPerson(payer);
        this.amount = amount;
        this.description = description;
        this.dateTime = Date.from(Instant.now(Clock.system(ZoneId.of("Asia/Singapore"))));


```
###### /java/seedu/address/storage/XmlAdaptedTransaction.java
``` java
        if (this.amount == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Amount.class.getSimpleName()));
        }
        if (!Amount.isValidAmount(this.amount)) {
            throw new IllegalValueException(Amount.MESSAGE_AMOUNT_CONSTRAINTS);
        }
        final Amount amount = new Amount(this.amount);

        if (this.description == null) {
            throw new IllegalValueException(String.format
                    (MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(this.description)) {
            throw new IllegalValueException(Description.MESSAGE_DESCRIPTION_CONSTRAINTS);
        }
        final Description description = new Description(this.description);

```
###### /java/seedu/address/storage/XmlAdaptedTransaction.java
``` java
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedTransaction)) {
            return false;
        }

        XmlAdaptedTransaction otherTransaction = (XmlAdaptedTransaction) other;
        return Objects.equals(payer, otherTransaction.payer)
                && Objects.equals(amount, otherTransaction.amount)
                && Objects.equals(description, otherTransaction.description)
                && Objects.equals(payees, otherTransaction.payees);
    }

}
```
###### /java/seedu/address/model/transaction/Amount.java
``` java
/**
 * Represents the amount that a Payer paid in a SmartSplit transaction.
 */
public class Amount {

    public static final String MESSAGE_AMOUNT_CONSTRAINTS =
            "Amount can only take in a numerical number up to 2 decimal places, "
                    + "and it should not be blank";
    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String AMOUNT_VALIDATION_REGEX = "^\\d+(\\.\\d{1,2})?$";

    public final String value;

    /**
     * Constructs an {@code Amount}.
     *
     * @param amount A valid amount.
     */
    public Amount(String amount) {
        requireNonNull(amount);
        checkArgument(isValidAmount(amount), MESSAGE_AMOUNT_CONSTRAINTS);
        this.value = amount;
    }
    /**
     * Returns true if a given string is a valid amount.
     */
    public static boolean isValidAmount(String test) {
        return test.matches(AMOUNT_VALIDATION_REGEX);
    }

    public double getDoubleValue() {
        return Double.valueOf(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Amount // instanceof handles nulls
                && this.value.equals(((Amount) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

```
###### /java/seedu/address/model/transaction/Description.java
``` java
/**
 * Represents a Transaction's description in the address book.
 */
public class Description {
    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
            "Transaction description can take any value and should not be blank";
    /*
     * Description must not be only space or "" (a blank string).
     */
    public static final String DESCRIPTION_VALIDATION_REGEX = "^(\\s|\\S)*(\\S)+(\\s|\\S)*$";

    public final String value;

    /**
     * Constructs an {@code Description}.
     *
     * @param description A valid description.
     */
    public Description(String description) {
        requireNonNull(description);
        checkArgument(isValidDescription(description), MESSAGE_DESCRIPTION_CONSTRAINTS);
        this.value = description;
    }

    /**
     * Returns true if a given string is a valid person email.
     */
    public static boolean isValidDescription(String test) {
        return test.matches(DESCRIPTION_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

```
###### /java/seedu/address/model/transaction/TransactionList.java
``` java
/**
 * Recard all transactions to the list.
 *
 */
public class TransactionList implements Iterable<Transaction> {

    private final ObservableList<Transaction> internalList = FXCollections.observableArrayList();


    /**
     * Adds a transaction to the list.
     *
     */
    public void add(Transaction toAdd) {
        requireNonNull(toAdd);
        internalList.add(toAdd);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Transaction> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Transaction> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePersonList // instanceof handles nulls
                && this.internalList.equals(((TransactionList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public void findPersonInTransaction(Name name) throws PersonNotFoundException {
        Set<Person> matchingPersons = addressBook.getPersonList()
                .stream()
                .filter(person -> person.getName().equals(name))
                .collect(Collectors.toSet());
        if (matchingPersons.isEmpty()) {
            throw new PersonNotFoundException();
        }
    }
    @Override
    public boolean findTransactionsWithPayer(Person person) throws PersonFoundException {
        Set<Transaction> matchingTransactions = addressBook.getTransactionList()
                .stream()
                .filter(transaction -> transaction.getPayer().equals(person))
                .collect(Collectors.toSet());

        if (matchingTransactions.isEmpty()) {
            return true;
        } else {
            throw new PersonFoundException();
        }
    }
    @Override
    public boolean findTransactionsWithPayee(Person person) throws PersonFoundException {
        Set<Transaction> matchingTransactions = addressBook.getTransactionList()
                .stream()
                .filter(transaction -> transaction.getPayees().contains(person))
                .collect(Collectors.toSet());

        if (matchingTransactions.isEmpty()) {
            return true;
        } else {
            throw new PersonFoundException();
        }
    }

    /**
     * Returns an unmodifiable view of the list of {@code Transaction}
     */
    @Override
    public ObservableList<Transaction> getFilteredTransactionList() {
        return FXCollections.unmodifiableObservableList(filteredTransactions);
    }

    @Override
    public void addTransaction(Transaction transaction) {
        String transactionType = AddTransactionCommand.COMMAND_WORD;
        addressBook.addTransaction(transaction);
        addressBook.updatePayerAndPayeesDebt(transactionType , transaction.getAmount(),
                transaction.getPayer(), transaction.getPayees());
        updateFilteredTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);
        updateFilteredPersonList(PREDICATE_SHOW_NO_PERSON);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public void updateFilteredTransactionList(Predicate<Transaction> predicate) {
        requireNonNull(predicate);
        filteredTransactions.setPredicate(predicate);
    }


    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && filteredPersons.equals(other.filteredPersons);
    }

}
```
