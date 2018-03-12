package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.FXCollections;
import org.fxmisc.easybind.EasyBind;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Name;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Description;
import seedu.address.model.transaction.Transaction;


/**
 * Panel containing the list of transactions.
 */
public class TransactionListPanel extends UiPart<Region> {
    private static final String FXML = "TransactionListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(seedu.address.ui.TransactionListPanel.class);

    @javafx.fxml.FXML
    private ListView<TransactionCard> transactionListView;

    public TransactionListPanel(ObservableList<Transaction> transactionList) {
        super(FXML);
        setConnections(transactionList);
        registerAsAnEventHandler(this);
    }
    
    private void setConnections(ObservableList<Transaction> transactionList) { 
        //ObservableList<TransactionCard> mappedList = EasyBind.map( transactionList, (transaction) -> new TransactionCard(transaction, 
                        //transactionList.indexOf(transaction) + 1));
        ObservableList<TransactionCard> mappedList = FXCollections.observableArrayList();
        Amount amount = new Amount("55");
        Name namePayer = new Name("John Doe");
        Description description = new Description("First transaction");
        Name namePayee = new Name("Alice Doe");
        Transaction transaction = new Transaction(namePayer, amount, description, namePayee);
        System.out.println(" 00000000000000 " + transaction.toString());
        TransactionCard card = new TransactionCard(transaction, 1);
        mappedList.add(card);
        transactionListView.setItems(mappedList);
        transactionListView.setCellFactory(listView -> new TransactionListViewCell());
    }
    /**
     * Custom {@code ListCell} that displays the graphics of a {@code TransactionCard}.
     */
    class TransactionListViewCell extends ListCell<TransactionCard> {

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

