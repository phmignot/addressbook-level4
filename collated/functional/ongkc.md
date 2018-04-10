# ongkc
###### /java/seedu/address/logic/util/CalculationUtil.java
``` java
    /**
     * Calculates amount to add to the payer's balance after a new paydebt transaction is added.
     * Returned amount will be positive.
     */
    private static Balance calculateAmountToAddForPayerForPaydebtTransaction(Transaction transaction) {
        Double amountToAdd = Double.valueOf(transaction.getAmount().value);
        return getRoundedFormattedBalance(amountToAdd);
    }

    /**
     * Calculates amount to add to the payee's balance after a new paydebt transaction is added.
     * Returned amount will be negative.
     */
    private static Balance calculateAmountToAddForPayeeForPaydebtTransaction(Transaction transaction) {
        Double amountToAdd = -Double.valueOf(transaction.getAmount().value);
        return getRoundedFormattedBalance(amountToAdd);
    }

```
###### /java/seedu/address/logic/parser/AddTransactionCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AddTransactionCommand object
 */
public class AddTransactionCommandParser implements Parser<AddTransactionCommand> {
    private TransactionType transactionType;
    private SplitMethod splitMethod;
    private List<Integer> units = Collections.emptyList();
    private List<Integer> percentages = Collections.emptyList();

    /**
     * Parses the given {@code String} of arguments in the context of the AddTransactionCommand
     * and returns an AddTransactionCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddTransactionCommand parse(String args, Model model) throws ParseException, CommandException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TRANSACTION_TYPE, PREFIX_PAYER, PREFIX_AMOUNT,
                        PREFIX_DESCRIPTION, PREFIX_PAYEE, PREFIX_SPLIT_METHOD, PREFIX_SPLIT_BY_UNITS,
                        PREFIX_SPLIT_BY_PERCENTAGE);

        if (!arePrefixesPresent(argMultimap, PREFIX_TRANSACTION_TYPE, PREFIX_PAYER, PREFIX_AMOUNT,
                PREFIX_DESCRIPTION, PREFIX_PAYEE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddTransactionCommand.MESSAGE_USAGE));
        }

```
###### /java/seedu/address/logic/parser/AddTransactionCommandParser.java
``` java
    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}

```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses {@code Collection<String> TransactionType} into a {@code Set<TransactionType>}.
     */
    public static TransactionType parseTransactionType(String type) throws IllegalValueException {
        requireNonNull(type);
        String trimmedType = type.trim();
        if (!TransactionType.isValidTransactionType(trimmedType)) {
            throw new IllegalValueException(TransactionType.MESSAGE_TRANSACTION_TYPE_CONSTRAINTS);
        }
        return new TransactionType(trimmedType);
    }

```
###### /java/seedu/address/logic/commands/AddTransactionCommand.java
``` java
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
            + PREFIX_SPLIT_METHOD + "SPLIT METHOD "
            + PREFIX_SPLIT_BY_UNITS + "LIST OF UNITS "
            + PREFIX_SPLIT_BY_PERCENTAGE + "LIST OF PERCENTAGES...\n"
```
###### /java/seedu/address/logic/commands/AddTransactionCommand.java
``` java
    public static final String MESSAGE_SUCCESS = "New transaction added";


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
    public void updateDebtorsList(Person person) {
        model.updateDebtorList(PREDICATE_SHOW_ALL_DEBTORS);
        DebtsTable debtsTable = model.getAddressBook().getDebtsTable();
        DebtsList debtsList = debtsTable.get(person);
        model.getAddressBook().setDebtors(debtsList);

    }
    @Override
    public void updateCreditorsList() {
        model.updateCreditorList(PREDICATE_SHOW_NO_CREDITORS);

    }

    /**
     * Update the people in the creditor list
     */
    public void updateCreditorsList(Person person) {
        model.updateCreditorList(PREDICATE_SHOW_ALL_CREDITORS);
        DebtsTable debtsTable = model.getAddressBook().getDebtsTable();
        DebtsList debtsList = debtsTable.get(person);
        model.getAddressBook().setCreditors(debtsList);

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
    private String transactionType;
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
    @XmlElement(required = true)
    private String splitMethod;

    @XmlElement
    private String unitsList;
    @XmlElement
    private String percentagesList;

    /**
     * Constructs an XmlAdaptedTransaction.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedTransaction() {}

    /**
     * Constructs an {@code XmlAdaptedTransaction} with the given person details.
     */
    public XmlAdaptedTransaction(String transactionType, Person payer, String amount, String description,
                                 UniquePersonList payees, String splitMethod, List<Integer> unitsList,
                                 List<Integer> percentagesList) {
        this.payer = new XmlAdaptedPerson(payer);
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.dateTime = Date.from(Instant.now(Clock.system(ZoneId.of("Asia/Singapore"))));

```
###### /java/seedu/address/storage/XmlAdaptedTransaction.java
``` java
        if (this.amount == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Amount.class.getSimpleName()));
        }
        if (!this.amount.contains(".")) {
            this.amount += ".00";
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

        if (this.transactionType == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    TransactionType.class.getSimpleName()));
        }
        if (!TransactionType.isValidTransactionType(this.transactionType)) {
            throw new IllegalValueException(TransactionType.MESSAGE_TRANSACTION_TYPE_CONSTRAINTS);
        }
        final TransactionType transactionType = new TransactionType(this.transactionType);

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
                && Objects.equals(payees, otherTransaction.payees)
                && Objects.equals(transactionType, otherTransaction.transactionType)
                && Objects.equals(splitMethod, otherTransaction.splitMethod);
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

    public ObservableList<Debtor> getFilteredDebtors() {
        return FXCollections.unmodifiableObservableList(filteredDebtors);
    }

    public ObservableList<Creditor> getFilteredCreditors() {
        return FXCollections.unmodifiableObservableList(filteredCreditors);
    }
    @Override
    public void addTransaction(Transaction transaction) throws CommandException, PersonNotFoundException {
        addressBook.addTransaction(transaction);
        addressBook.updatePayerAndPayeesBalance(true, transaction, findPersonByName(
                transaction.getPayer().getName()), getPayeesList(transaction.getPayees()));
        updateFilteredTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);
        updateDebtorList(PREDICATE_SHOW_NO_DEBTORS);
        updateCreditorList(PREDICATE_SHOW_NO_CREDITORS);
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
