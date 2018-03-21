package seedu.address.storage;

import static seedu.address.storage.XmlAdaptedTransaction.MISSING_FIELD_MESSAGE_FORMAT;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Description;
import seedu.address.testutil.Assert;
import seedu.address.testutil.TypicalTransactions;

public class XmlAdaptedTransactionTest {

    private static final Person INVALID_PAYER = new Person(new Name("R@chel"), new Phone(""),
                    new Email(""), new Address(""), new UniqueTagList().toSet());
    private static final String INVALID_AMOUNT = " ";
    private static final String INVALID_DESCRIPTION = " ";
    private static final Person INVALID_PAYEE = new Person(new Name("R@chel"), new Phone(""),
            new Email(""), new Address(""), new UniqueTagList().toSet());
    private static UniquePersonList invalidPayees = new UniquePersonList();

    private static final Person VALID_PAYER = TypicalTransactions.getTypicalTransactions().get(0).getPayer();
    private static final String VALID_AMOUNT = TypicalTransactions.getTypicalTransactions().get(0)
            .getAmount().toString();
    private static final String VALID_DESCRIPTION = TypicalTransactions.getTypicalTransactions().get(0)
            .getDescription().toString();
    private static final Person VALID_PAYEE = TypicalTransactions.getTypicalTransactions().get(0)
            .getPayees().iterator().next();
    private static UniquePersonList validPayees = new UniquePersonList();

    @Before
    public void setUp() {
        try {
            invalidPayees.add(INVALID_PAYEE);
            validPayees.add(VALID_PAYEE);
        } catch (DuplicatePersonException dpe) {
            dpe.printStackTrace();
        }
    }

    @Test
    public void toModelType_invalidPayer_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction =
                new XmlAdaptedTransaction(INVALID_PAYER, VALID_AMOUNT, VALID_DESCRIPTION, validPayees);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_nullPayer_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(null, VALID_AMOUNT, VALID_DESCRIPTION,
                validPayees);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_invalidAmount_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction =
                new XmlAdaptedTransaction(VALID_PAYER, INVALID_AMOUNT, VALID_DESCRIPTION, validPayees);
        String expectedMessage = Amount.MESSAGE_AMOUNT_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_nullAmount_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(VALID_PAYER, null, VALID_DESCRIPTION,
                validPayees);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Amount.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_invalidDescription_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction =
                new XmlAdaptedTransaction(VALID_PAYER, VALID_AMOUNT, INVALID_DESCRIPTION, validPayees);
        String expectedMessage = Description.MESSAGE_DESCRIPTION_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(VALID_PAYER, VALID_AMOUNT, null, validPayees);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_invalidPayee_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction =
                new XmlAdaptedTransaction(VALID_PAYER, VALID_AMOUNT, VALID_DESCRIPTION, invalidPayees);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_nullPayee_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(VALID_PAYER, VALID_AMOUNT, VALID_DESCRIPTION,
                null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

}
