package seedu.address.model.transaction;

//@@author steven-jia

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
