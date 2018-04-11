package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
/**
 * Represents no selection in the Creditor List Panel
 */
public class CreditorPanelNoSelectionEvent extends BaseEvent {

    public CreditorPanelNoSelectionEvent() {
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
