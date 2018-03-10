package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Transaction}.
 */
public class TransactionCard extends UiPart<Region> {

  private static final String FXML = "TransactionListCard.fxml";
  
  public final Transaction transaction;

  @FXML
  private HBox cardPane;
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
    //id.setText(displayedIndex + ". ");
    payerName.setText(transaction.getPayer().getName().fullName);
    amount.setText(String.valueOf(transaction.getAmount()));
    description.setText(transaction.getDescription());
    transaction.getPayees().forEach(payee -> payees.getChildren().add(new Label(payee.getName().fullName)));
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
    //return id.getText().equals(card.id.getText())
    //    && person.equals(card.person);
    return transaction.equals(card.transaction);
  
  }
}
