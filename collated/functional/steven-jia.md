# steven-jia
###### /java/seedu/address/logic/util/BalanceCalculationUtil.java
``` java
    /**
     * Returns the debt that a {@code payee} owes to the payer.
     */
    public static Balance calculatePayerDebt(String transactionType, Amount amount,
                                             UniquePersonList payees) {
        switch (transactionType) {
        case AddTransactionCommand.COMMAND_WORD:
            return calculateAddTransactionPayerDebt(amount, payees);
        case DeleteTransactionCommand.COMMAND_WORD:
            return calculateDeleteTransactionPayerDebt(amount, payees);
        default:
            return null;
        }
    }
    /**
     * Returns an updated balance for {@code payee}
     */
    public static Balance calculatePayeeDebt(String transactionType, Amount amount,
                                             UniquePersonList payees) {
        switch (transactionType) {
        case AddTransactionCommand.COMMAND_WORD:
            return calculateAddTransactionPayeeDebt(amount, payees);
        case DeleteTransactionCommand.COMMAND_WORD:
            return calculateDeleteTransactionPayeeDebt(amount, payees);
        default:
            return null;
        }
    }

```
###### /java/seedu/address/logic/util/BalanceCalculationUtil.java
``` java
    /**
     * Calculate the number of people involved in a transaction
     */
    public static int calculateNumberOfInvolvedPersons(UniquePersonList payees) {
        return payees.asObservableList().size() + 1;
    }
    private static double round(double value, int places) {
        BigDecimal amount = new BigDecimal(value);
        amount = amount.setScale(places, RoundingMode.HALF_UP);
        return amount.doubleValue();
    }

}


```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses a {@code String splitMethod} into a {@code SplitMethod}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code splitMethod} is invalid.
     */
    public static SplitMethod parseSplitMethod(String splitMethod) throws IllegalValueException {
        requireNonNull(splitMethod);
        String trimmedSplitMethod = splitMethod.trim();
        if (!SplitMethod.isValidSplitMethod(trimmedSplitMethod)) {
            throw new IllegalValueException(SplitMethod.MESSAGE_SPLIT_METHOD_CONSTRAINTS);
        }
        return new SplitMethod(splitMethod);
    }

    /**
     * Parses {@code Collection<String> SplitMethod} into a {@code Set<SplitMethod>}.
     */
    public static Optional<SplitMethod> parseSplitMethod(Optional<String> splitMethod) throws IllegalValueException {
        requireNonNull(splitMethod);
        return splitMethod.isPresent() ? Optional.of(parseSplitMethod(splitMethod.get())) : Optional.empty();
    }

}
```
###### /java/seedu/address/storage/XmlAdaptedPerson.java
``` java
        if (this.balance == null) {
            // Retroactively apply a balance of 0.00 to saved Persons without a balance
            this.balance = "0.00";
        }
        if (!Balance.isValidBalance(this.balance)) {
            throw new IllegalValueException(Balance.MESSAGE_BALANCE_CONSTRAINTS);
        }
        final Balance balance = new Balance(this.balance);
```
###### /java/seedu/address/storage/XmlAdaptedTransaction.java
``` java
        List<XmlAdaptedPerson> payeesToStore = new ArrayList<>();
        payees.asObservableList().forEach(payee -> payeesToStore.add(new XmlAdaptedPerson(payee)));
        this.payees = payeesToStore;
```
###### /java/seedu/address/storage/XmlAdaptedTransaction.java
``` java
        List<XmlAdaptedPerson> payeesToStore = new ArrayList<>();
        source.getPayees().asObservableList().forEach(payee -> payeesToStore.add(new XmlAdaptedPerson(payee)));
        payees = payeesToStore;
```
###### /java/seedu/address/storage/XmlAdaptedTransaction.java
``` java
        if (this.payer == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Payer"));
        }
        validatePersonFields(this.payer.toModelType());
        final Person payer = this.payer.toModelType();

```
###### /java/seedu/address/storage/XmlAdaptedTransaction.java
``` java
        if (this.payees == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Payees"));
        }
        for (XmlAdaptedPerson payee: this.payees) {
            validatePersonFields(payee.toModelType());
        }

        UniquePersonList convertedPayees = new UniquePersonList();
        for (XmlAdaptedPerson payee: this.payees) {
            convertedPayees.add(payee.toModelType());
        }
        final UniquePersonList payees = convertedPayees;

        return new Transaction(payer, amount, description, dateTime, payees);
    }

```
###### /java/seedu/address/storage/XmlAdaptedTransaction.java
``` java
    /**
     * Checks each field of the {@code person} for validity
     */
    private void validatePersonFields(Person person) throws IllegalValueException {
        if (person.getName().fullName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(person.getName().fullName)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }

        if (person.getPhone().value == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(person.getPhone().value)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }

        if (person.getEmail().value == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(person.getEmail().value)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }

        for (Tag tag: person.getTags()) {
            if (!Tag.isValidTagName(tag.tagName)) {
                throw new IllegalValueException(Tag.MESSAGE_TAG_CONSTRAINTS);
            }
        }
    }

```
###### /java/seedu/address/model/transaction/SplitMethod.java
``` java

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Transaction's split method in SmartSplit.
 */
public class SplitMethod {

    /**
     * Split methods that can be used
     */
    public enum Method {
        EVENLY,
        BY_UNITS
    }

    public static final String MESSAGE_SPLIT_METHOD_CONSTRAINTS =
            "Transaction split method can only be one of the numbers shown in the list of options";

    public static final String SPLIT_METHOD_VALIDATION_REGEX = "^[1-9]$";

    public final Method method;

    /**
     * Constructs a {@code splitMethod}.
     *
     * @param splitMethod A valid split method.
     */
    public SplitMethod(String splitMethod) {
        requireNonNull(splitMethod);
        checkArgument(isValidSplitMethod(splitMethod), MESSAGE_SPLIT_METHOD_CONSTRAINTS);
        switch (Integer.valueOf(splitMethod)) {
        case 1:
            this.method = Method.EVENLY;
            break;
        case 2:
            this.method = Method.BY_UNITS;
            break;
        default:
            this.method = Method.EVENLY;
        }
    }

    /**
     * Returns true if a given string is a valid split method.
     */
    public static boolean isValidSplitMethod(String test) {
        boolean found = false;
        for (Method method: Method.values()) {
            if (method.ordinal() == Integer.parseInt(test)) {
                found = true;
            }
        }
        return test.matches(SPLIT_METHOD_VALIDATION_REGEX) && found;
    }

    @Override
    public String toString() {
        return method.toString();
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }

}
```
###### /java/seedu/address/model/transaction/Transaction.java
``` java
package seedu.address.model.transaction;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import seedu.address.logic.util.BalanceCalculationUtil;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Represents a Transaction in SmartSplit.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Transaction extends BalanceCalculationUtil {
    private static Integer lastTransactionId = 0;
    private final Integer id;
    private final Date dateTime;
    private final Person payer;
    private final Amount amount;
    private final Description description;
    private final UniquePersonList payees;

    public Transaction(Person payer, Amount amount, Description description, Date dateTime, UniquePersonList payees) {
        this.dateTime = dateTime;
        this.id = lastTransactionId++;
        this.payer = payer;
        this.amount = amount;
        this.description = description;
        this.payees = payees;

    }

    public Transaction(Person payer, Amount amount, Description description, Date dateTime, Set<Person> payeesToAdd) {
        UniquePersonList payees = new UniquePersonList();
        for (Person p: payeesToAdd) {
            try {
                payees.add(p);
            } catch (DuplicatePersonException e) {
                System.out.println("Duplicate person" + p.getName() + " not added to list of payees");
            }
        }

        this.dateTime = dateTime;
        this.id = lastTransactionId++;
        this.payer = payer;
        this.amount = amount;
        this.description = description;
        this.payees = payees;
    }

    public Integer getId() {
        return id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public Person getPayer() {
        return payer;
    }

    public Amount getAmount() {
        return amount;
    }

    public Description getDescription() {
        return description;
    }

    public UniquePersonList getPayees() {
        return payees;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Transaction)) {
            return false;
        }

        Transaction otherTransaction = (Transaction) other;
        return otherTransaction.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, payer, amount, description, payees);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Transaction id: ")
                .append(getId())
                .append("\n Created on: ")
                .append(getDateTime())
                .append("\n Transaction paid by: ")
                .append(getPayer().getName())
                .append("\n Amount: ")
                .append(getAmount().toString())
                .append("\r\n Description: ")
                .append(getDescription().toString())
                .append("\n Payees: ")
                .append(getPayees().asObservableList().toString());
        return builder.toString();
    }

    /**
     * Tests if a person is implied in this transaction.
     * @param person to check his implication.
     * @return true if the person is the payer or one of the payee;
     * false otherwise.
     */
    public boolean isImplied(Person person) {
        return (payer.equals(person) || payees.contains(person));
    }
}
```
###### /java/seedu/address/model/person/Balance.java
``` java
/**
 * Represents a Person's balance in SmartSplit.
 * Guarantees: immutable; is valid as declared in {@link #isValidBalance(String)}
 */
public class Balance {

    public static final String MESSAGE_BALANCE_CONSTRAINTS =
            "Balance should only contain numeric characters up to 2 digits, and it should not be blank,";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String BALANCE_VALIDATION_REGEX = "^-?\\d*\\.\\d{2}$";

    public final String value;

    /**
     * Constructs a {@code Balance}.
     *
     * @param balance A valid balance.
     */
    public Balance(String balance) {
        requireNonNull(balance);
        checkArgument(isValidBalance(balance), MESSAGE_BALANCE_CONSTRAINTS);
        this.value = balance;
    }

    /**
     * Returns true if a given string is a valid person balance.
     */
    public static boolean isValidBalance(String test) {
        return test.matches(BALANCE_VALIDATION_REGEX);
    }


    public double getDoubleValue() {
        return Double.valueOf(value);
    }

    public Balance getInverse() {
        if (this.value.contains("-")) {
            return new Balance(this.value.substring(1));
        }
        return new Balance("-" + this.value);
    }

    /**
     * Adds the value of two balance
     * @param balance to add.
     * @return Balance with the value of the sum.
     */
    public Balance add(Balance balance) {
        double addition = this.getDoubleValue() + balance.getDoubleValue();
        DecimalFormat formatter = new DecimalFormat("#.00");
        return new Balance(String.valueOf(formatter.format(addition)));
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Balance // instanceof handles nulls
                && this.value.equals(((Balance) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public Person findPersonByName(Name name) throws PersonNotFoundException {
        Set<Person> matchingPersons = addressBook.getPersonList()
                .stream()
                .filter(person -> person.getName().equals(name))
                .collect(Collectors.toSet());

        if (!matchingPersons.isEmpty()) {
            return matchingPersons.iterator().next();
        } else {
            throw new PersonNotFoundException();
        }
    }

```
###### /java/seedu/address/model/ModelManager.java
``` java
    public UniquePersonList getPayeesList(ArgumentMultimap argMultimap, Model model)
            throws PersonNotFoundException, IllegalValueException {
        UniquePersonList payees = new UniquePersonList();
        List<String> payeeNamesToAdd = argMultimap.getAllValues(PREFIX_PAYEE);

        if (!payeeNamesToAdd.isEmpty()) {
            for (String payeeName: payeeNamesToAdd) {
                payees.add(model.findPersonByName(ParserUtil.parseName(payeeName)));
            }
        }
        return payees;
    }
    public UniquePersonList getPayeesList(UniquePersonList transactionPayees)
            throws PersonNotFoundException {
        UniquePersonList payees = new UniquePersonList();

        for (Person payee: transactionPayees) {
            try {
                payees.add(findPersonByName(payee.getName()));
            } catch (PersonNotFoundException pnfe) {
                throw new PersonNotFoundException();
            } catch (DuplicatePersonException e) {
                e.printStackTrace();
            }
        }

        return payees;
    }
```
###### /java/seedu/address/model/Model.java
``` java
    /** Finds a person by name */
    Person findPersonByName(Name name) throws PersonNotFoundException;

    UniquePersonList getPayeesList(ArgumentMultimap argMultimap, Model model) throws PersonNotFoundException,
            IllegalValueException;

    /** Finds a person by in transaction */
    void findPersonInTransaction(Name name) throws PersonNotFoundException;

```
###### /java/seedu/address/model/Model.java
``` java
    /** Returns a set of transactions that have {@code person} as the payer */
    boolean findTransactionsWithPayer(Person person) throws TransactionNotFoundException, PersonFoundException;

    /** Returns a set of transactions that have {@code person} as a payee */
    boolean findTransactionsWithPayee(Person person) throws TransactionNotFoundException, PersonFoundException;

```
