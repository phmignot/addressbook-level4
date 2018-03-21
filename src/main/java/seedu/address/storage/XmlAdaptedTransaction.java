package seedu.address.storage;

import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.tag.Tag;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Description;
import seedu.address.model.transaction.Transaction;
//@@author ongkc
/**
 * JAXB-friendly adapted version of the Transaction.
 */
public class XmlAdaptedTransaction {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Transaction's %s field is missing!";

    @XmlElement(required = true)
    private Person payer;
    @XmlElement(required = true)
    private String amount;
    @XmlElement(required = true)
    private String description;
    @XmlElement(required = true)
    private UniquePersonList payees;

    /**
     * Constructs an XmlAdaptedTransaction.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedTransaction() {}

    /**
     * Constructs an {@code XmlAdaptedTransaction} with the given person details.
     */
    public XmlAdaptedTransaction(Person payer, String amount, String description, UniquePersonList payees) {
        this.payer = payer;
        this.amount = amount;
        this.description = description;
        this.payees = payees;
    }

    /**
     * Converts a given Transaction into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedTransaction(Transaction source) {
        payer = source.getPayer();
        amount = source.getAmount().value;
        description = source.getDescription().value;
        payees = source.getPayees();
    }

    /**
     * Converts this jaxb-friendly adapted transaction's object into the model's Transaction object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted Transaction
     */
    public Transaction toModelType() throws IllegalValueException {

        //@@author steven-jia
        if (this.payer == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Payer"));
        }
        validatePersonFields(this.payer);
        final Person payer = this.payer;

        //@@author ongkc
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

        if (this.payees == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Payees"));
        }

        //@@author steven-jia
        for (Person payee: this.payees) {
            validatePersonFields(payee);
        }
        final UniquePersonList payees = this.payees;

        return new Transaction(payer, amount, description, payees);
    }

    /**
     * Checks each field of the {@code person} for validity
     */
    private void validatePersonFields(Person person) throws IllegalValueException {
        if (!Name.isValidName(person.getName().fullName)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        if (!Phone.isValidPhone(person.getPhone().value)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        if (!Address.isValidAddress(person.getAddress().value)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
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

    //@@author ongkc
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
