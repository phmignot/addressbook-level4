package seedu.address.ui;

import java.util.logging.Logger;

import org.fxmisc.easybind.EasyBind;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.Person;


/**
 * Panel containing the list of transactions.
 */
public class TransactionListPanel extends UiPart<Region> {
  
    private static final String FXML = "TransactionListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(seedu.address.ui.PersonListPanel.class);

    @javafx.fxml.FXML
    private ListView<PersonCard> personListView;

    public TransactionListPanel(ObservableList<Person> personList) {
      super(FXML);
      //setConnections(personList);
      //registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Person> personList) {
      ObservableList<PersonCard> mappedList = EasyBind.map(
          personList, (person) -> new PersonCard(person, personList.indexOf(person) + 1));
      personListView.setItems(mappedList);
      personListView.setCellFactory(listView -> new PersonListViewCell());
    }
    
    /**
     * Custom {@code ListCell} that displays the graphics of a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<TransactionCard> {

      @Override
      protected void updateItem(TransactionCard transaction, boolean empty) {
        super.updateItem(transaction, empty);

        if (empty || transaction == null) {
          setGraphic(null);
          setText(null);
        } else {
          setGraphic(transaction.getRoot());
        }
      }
    }

  }

