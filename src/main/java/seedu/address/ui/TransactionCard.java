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
  
 // public final Transaction transaction;

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

  public TransactionCard(Person person, int displayedIndex) {
    super(FXML);
    /*this.person = person;
    id.setText(displayedIndex + ". ");
    name.setText(person.getName().fullName);
    phone.setText(person.getPhone().value);
    address.setText(person.getAddress().value);
    email.setText(person.getEmail().value);
    person.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
  */
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
  
    return false;
  }
}
