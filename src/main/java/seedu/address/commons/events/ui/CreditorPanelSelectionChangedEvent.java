package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.CreditorCard;
/**
 * Represents a selection change in the Creditor List Panel
 */
public class CreditorPanelSelectionChangedEvent extends BaseEvent {


    private final CreditorCard newSelection;

    public CreditorPanelSelectionChangedEvent(CreditorCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public CreditorCard getNewSelection() {
        return newSelection;
    }
}
