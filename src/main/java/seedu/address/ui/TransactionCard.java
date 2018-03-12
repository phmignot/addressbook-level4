package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.transaction.Transaction;

/**
 * An UI component that displays information of a {@code Transaction}.
 */
public class TransactionCard extends UiPart<Region> {

    private static final String FXML = "TransactionListCard.fxml";
    public final Transaction transaction;
    
    @FXML
    private HBox cardPane;
    @FXML
    private Label idDisplay;
    @FXML
    private Label payerName;
    @FXML
    private Label amount;
    @FXML
    private Label description;
    @FXML
    private FlowPane payees;
    
    public TransactionCard(Transaction transaction, int displayedIndex) {
        super(FXML);
        this.transaction = transaction;
        System.out.println(" Creating a new TCard from " + transaction.toString());
        idDisplay.setText(displayedIndex + ". ");
        //payerName.setText(transaction.getPayer().getName().fullName);
        payerName.setText(transaction.getPayer().fullName);
        amount.setText(transaction.getAmount().toString());
        description.setText(transaction.getDescription().toString());
        //transaction.getPayees().forEach(payee -> payees.getChildren().add(new Label(payee.getName().fullName)));
        payees.getChildren().add(new Label(transaction.getPayee().fullName));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof TransactionCard)) {
            return false;
        }
        // state check
        TransactionCard card = (TransactionCard) other;
        return transaction.equals(card.transaction);
    }
}
