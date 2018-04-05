# phmignot
###### /java/seedu/address/ui/TransactionListPanel.java
``` java
package seedu.address.ui;

import java.util.logging.Logger;

import org.fxmisc.easybind.EasyBind;

//import com.google.common.eventbus.Subscribe;

import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
//import seedu.address.commons.events.ui.JumpToListRequestEvent;
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
        ObservableList<TransactionCard> mappedList = EasyBind.map(transactionList, (transaction) -> new TransactionCard(
                transaction, transactionList.indexOf(transaction) + 1));
        transactionListView.setItems(mappedList);
        transactionListView.setCellFactory(listView -> new TransactionListViewCell());
    }


    private void setEventHandlerForSelectionChangeEvent() {
    }

    /**
     * Scrolls to the {@code PersonCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
    }

    /* Method could be used for selection in the transactionList
    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        //scrollTo(event.targetIndex);
    }*/

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

```
###### /java/seedu/address/ui/TransactionCard.java
``` java
package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
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
    @FXML
    private Label date;

    public TransactionCard(Transaction transaction, int displayedIndex) {
       super(FXML);
        this.transaction = transaction;
        idDisplay.setText(displayedIndex + ". ");
        payerName.setText(transaction.getPayer().getName().fullName);
        amount.setText(transaction.getAmount().toString());
        description.setText(transaction.getDescription().toString());
        for (Person payee : transaction.getPayees()) {
            payees.getChildren().add(new Label(payee.getName().fullName));
            payees.getChildren().add(new Label(", "));
        }
        date.setText(String.valueOf(transaction.getDateTime()));
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
```
###### /java/seedu/address/commons/events/ui/PersonPanelNoSelectionEvent.java
``` java
public class PersonPanelNoSelectionEvent extends BaseEvent {

    public PersonPanelNoSelectionEvent() {
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### /java/seedu/address/model/transaction/TransactionContainsPersonPredicate.java
``` java
package seedu.address.model.transaction;

import java.util.function.Predicate;

import seedu.address.model.person.Person;

/**
 * Tests that the {@code Person} involved in a transaction matches the {@code Person} given.
 */
public class TransactionContainsPersonPredicate implements Predicate<Transaction> {

    private final Person person;

    public TransactionContainsPersonPredicate(Person person) {
        this.person = person;
    }

    @Override
    public boolean test(Transaction transaction) {
        return transaction.isImplied(person);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TransactionContainsPersonPredicate // instanceof handles nulls
                && this.person.equals(((TransactionContainsPersonPredicate) other).person)); // state check
    }

}
```
###### /java/seedu/address/model/transaction/exceptions/TransactionNotFoundException.java
``` java
package seedu.address.model.transaction.exceptions;

/**
 * Signals that the operation is unable to find the specified transaction.
 */
public class TransactionNotFoundException extends Exception {}
```
###### /java/seedu/address/model/transaction/TransactionList.java
``` java
    /**
     * Removes the equivalent transaction from the list of transactions.
     *
     * @throws TransactionNotFoundException if no such transaction could be found in the list.
     */
    public boolean remove(Transaction toRemove) throws TransactionNotFoundException {
        requireNonNull(toRemove);
        final boolean transactionFoundAndDeleted = internalList.remove(toRemove);
        if (!transactionFoundAndDeleted) {
            throw new TransactionNotFoundException();
        }
        return transactionFoundAndDeleted;
    }
    /**
     * Replaces the list of transactions by a input list of transaction.
     * @param transactions that will be the new transactions' list.
     */
    public void setTransactions(List<Transaction> transactions) {
        requireAllNonNull(transactions);
        internalList.setAll(transactions);
    }

}
```
###### /java/seedu/address/model/DebtsList.java
``` java
public class DebtsList extends HashMap<Person, Balance> {
    public DebtsList() {
        super();
    }

    /**
     * Updates the debt with a person. If the person has no previous debt, then the person
     * is added to the HashMap.
     * @param person that owes or is owed money.
     * @param debt to add to the old debt.
     */
    public void updateDebt(Person person, Balance debt) {
        if (!this.containsKey(person)) {
            this.put(person, new Balance("0.00"));
        }
        Balance oldDebts = this.get(person);
        this.replace(person, oldDebts.add(debt));
    }

    /**
     * Displays the content of DebtsList in the terminal.
     */
    public void display() {
        System.out.print("dl =");
        this.forEach(((person, balance) -> System.out.print(person.getName().fullName
            + " : " + balance.toString())));
    }
}
```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public void deleteTransaction(Transaction target) throws TransactionNotFoundException, CommandException {
        String transactionType = DeleteTransactionCommand.COMMAND_WORD;
        try {
            addressBook.updatePayerAndPayeesDebt(transactionType , target.getAmount(),
                    findPersonByName(target.getPayer().getName()), getPayeesList(target.getPayees()));
        }   catch (PersonNotFoundException e) {
            throw new CommandException(MESSAGE_NONEXISTENT_PAYER_PAYEES);
        }
        addressBook.removeTransaction(target);
        updateFilteredPersonList(PREDICATE_SHOW_NO_PERSON);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }
    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }


    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

```
###### /java/seedu/address/model/DebtsTable.java
``` java
public class DebtsTable extends HashMap<Person, DebtsList> {

    private final ObservableList internalList = FXCollections.observableArrayList();

    public DebtsTable() {
        super();
    }

    /**
     * Updates the DebtsTable due to a transaction
     * payeeDebt is a negative {@Code Balance} value, because the payee owes money.
     * payerDebt is a positive {@Code Balance} value, because the payer is owed.
     * @param transaction to register the table.
     */
    public void updateDebts(String typeOfTransaction, Transaction transaction) {
        Person payer = transaction.getPayer();
        if (!this.containsKey(payer)) {
            this.add(payer);
            System.out.println("Adding payer " + payer.getName().fullName);
        }
        DebtsList payerDebtsLit = this.get(payer);
        Balance payerDebt = calculatePayeeDebt(typeOfTransaction, transaction.getAmount(), transaction.getPayees());
        Balance payeeDebt = payerDebt.getInverse();
        for (Person payee: transaction.getPayees()) {
            if (!this.containsKey(payee)) {
                this.add(payee);
                System.out.println("Adding payee " + payee.getName().fullName);
            }
            DebtsList payeeDebtsLit = this.get(payee);
            payerDebtsLit.updateDebt(payee, payeeDebt);
            payeeDebtsLit.updateDebt(payer, payerDebt);
        }
    }

    public void add(Person personToAdd) {
        this.putIfAbsent(personToAdd, new DebtsList());
    }

    public ObservableList asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    public void setDebtsTable(DebtsTable debtsTable) {
        requireAllNonNull(debtsTable);
        internalList.setAll(debtsTable);
    }
    /**
     * Displays the content of the Debts Table in the terminal.
     */
    public void display() {
        System.out.println("DEBTS TABLE : ");
        this.forEach(((person, debtsList) -> {
            System.out.println(person.getName().fullName + " : ");
            debtsList.display();
            System.out.println();
        }));
    }


}

```
###### /java/seedu/address/model/Model.java
``` java
    /** Deletes the given person. */
    void deleteTransaction(Transaction target) throws TransactionNotFoundException, CommandException;
}
```
