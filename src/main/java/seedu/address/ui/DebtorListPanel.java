package seedu.address.ui;

import java.util.logging.Logger;

import org.fxmisc.easybind.EasyBind;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.DebtorPanelNoSelectionEvent;
import seedu.address.commons.events.ui.DebtorPanelSelectionChangedEvent;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.model.person.Debtor;
/**
 * Panel containing the list of debtors.
 */
public class DebtorListPanel extends UiPart<Region> {

    private static final String FXML = "DebtorListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(DebtorListPanel.class);

    @javafx.fxml.FXML
    private ListView<DebtorCard> debtorListView;

    public DebtorListPanel(ObservableList<Debtor> debtorsList) {
        super(FXML);
        setConnections(debtorsList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Debtor> debtorsList) {
        ObservableList<DebtorCard> mappedList = EasyBind.map(
                debtorsList, (debtor) -> new DebtorCard(debtor, debtorsList.indexOf(debtor) + 1));
        debtorListView.setItems(mappedList);
        debtorListView.setCellFactory(listView -> new DebtorListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        debtorListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in person list panel changed to : '" + newValue + "'");
                        raise(new DebtorPanelSelectionChangedEvent(newValue));
                    } else {
                        logger.fine("No person selected in the person list");
                        raise(new DebtorPanelNoSelectionEvent());
                    }
                });
    }

    /**
     * Scrolls to the {@code PersonCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            debtorListView.scrollTo(index);
            debtorListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code PersonCard}.
     */
    class DebtorListViewCell extends ListCell<DebtorCard> {
        @Override
        protected void updateItem(DebtorCard debtor, boolean empty) {
            super.updateItem(debtor, empty);

            if (empty || debtor == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(debtor.getRoot());
            }
        }
    }

}
