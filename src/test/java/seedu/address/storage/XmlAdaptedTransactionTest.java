package seedu.address.storage;

import static seedu.address.storage.XmlAdaptedTransaction.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.TypicalTransactions.ONE;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Name;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Description;
import seedu.address.testutil.Assert;

public class XmlAdaptedTransactionTest {

    private static final String INVALID_PAYER = "R@chel";
    private static final String INVALID_AMOUNT = " ";
    private static final String INVALID_DESCRIPTION = " ";
    private static final String INVALID_PAYEE = "R@chel";

    private static final String VALID_PAYER = ONE.getPayer().toString();
    private static final String VALID_AMOUNT = ONE.getAmount().toString();
    private static final String VALID_DESCRIPTION = ONE.getDescription().toString();
    private static final String VALID_PAYEE = ONE.getPayee().toString();

    //@Test
    //public void toModelType_validTransactionDetails_returnsTransaction() throws Exception {
    //       XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(ONE);
    //       assertEquals(ONE, transaction.toModelType());
    // }

    @Test
    public void toModelType_invalidPayer_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction =
                new XmlAdaptedTransaction(INVALID_PAYER, VALID_AMOUNT, VALID_DESCRIPTION, VALID_PAYEE);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_nullPayer_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(null, VALID_AMOUNT, VALID_DESCRIPTION,
                VALID_PAYEE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_invalidAmount_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction =
                new XmlAdaptedTransaction(VALID_PAYER, INVALID_AMOUNT, VALID_DESCRIPTION, VALID_PAYEE);
        String expectedMessage = Amount.MESSAGE_AMOUNT_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_nullAmount_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(VALID_PAYER, null, VALID_DESCRIPTION,
                VALID_PAYEE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Amount.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_invalidDescription_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction =
                new XmlAdaptedTransaction(VALID_PAYER, VALID_AMOUNT, INVALID_DESCRIPTION, VALID_PAYEE);
        String expectedMessage = Description.MESSAGE_DESCRIPTION_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(VALID_PAYER, VALID_AMOUNT, null, VALID_PAYEE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_invalidPayee_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction =
                new XmlAdaptedTransaction(VALID_PAYER, VALID_AMOUNT, VALID_DESCRIPTION, INVALID_PAYEE);
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
