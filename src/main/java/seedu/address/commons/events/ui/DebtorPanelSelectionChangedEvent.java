package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.DebtorCard;
/**
 * Represents a selection change in the Debtor List Panel
 */
public class DebtorPanelSelectionChangedEvent extends BaseEvent {



    private final DebtorCard newSelection;

    public DebtorPanelSelectionChangedEvent(DebtorCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public DebtorCard getNewSelection() {
        return newSelection;
    }
}
