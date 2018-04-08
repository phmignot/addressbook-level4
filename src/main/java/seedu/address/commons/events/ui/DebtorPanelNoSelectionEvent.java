package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
/**
 * Represents no selection in the Debtor List Panel
 */
public class DebtorPanelNoSelectionEvent extends BaseEvent {

    public DebtorPanelNoSelectionEvent() {
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
