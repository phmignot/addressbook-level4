package seedu.address.model.transaction;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.model.person.Address;
//@@author ongkc
/**
 * Represents the amount that a Payer paid in a SmartSplit transaction.
 */
public class Amount {

    public static final String MESSAGE_AMOUNT_CONSTRAINTS =
            "Payer amount can only take in numerical number with any decimal number precision, "
                    + "and it should not be blank";
    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String AMOUNT_VALIDATION_REGEX = "\\d+(\\.\\d*)?|\\.\\d+\n";

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

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Address // instanceof handles nulls
                && this.value.equals(((Address) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

