package guitests.guihandles;

import javafx.scene.Node;
import javafx.scene.control.Label;
//import javafx.scene.layout.Region;

//import java.util.List;
//import java.util.stream.Collectors;


/**
 * Provides a handle to a transaction card in the transaction list panel.
 */
public class TransactionCardHandle extends NodeHandle<Node> {
    private static final String ID_FIELD_ID = "#id";
    private static final String PAYER_FIELD_ID = "#payer";
    private static final String AMOUNT_FIELD_ID = "#amount";
    private static final String DESCRIPTION_FIELD_ID = "#description";
    private static final String PAYEE_FIELD_ID = "#payee";
    //private static final String TAGS_FIELD_ID = "#tags";

    private final Label idLabel;
    private final Label payerLabel;
    private final Label amountLabel;
    private final Label descriptionLabel;
    private final Label payeeLabel;
    //private final List<Label> payeeLabels;

    public TransactionCardHandle(Node cardNode) {
        super(cardNode);

        this.idLabel = getChildNode(ID_FIELD_ID);
        this.payerLabel = getChildNode(PAYER_FIELD_ID);
        this.amountLabel = getChildNode(AMOUNT_FIELD_ID);
        this.descriptionLabel = getChildNode(DESCRIPTION_FIELD_ID);
        this.payeeLabel = getChildNode(PAYEE_FIELD_ID);

        /*
        Region tagsContainer = getChildNode(TAGS_FIELD_ID);
        this.tagLabels = tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
        */
    }

    public String getId() {
        return idLabel.getText();
    }

    public String getPayer() {
        return payerLabel.getText();
    }

    public String getAmount() {
        return amountLabel.getText();
    }

    public String getDescription() {
        return descriptionLabel.getText();
    }

    public String getPayee() {
        return payeeLabel.getText();
    }
    /*
    public List<String> getTags() {
        return tagLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }
    */
}
