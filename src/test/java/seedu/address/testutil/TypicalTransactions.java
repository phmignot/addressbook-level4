package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.transaction.SplitMethod;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.util.SampleDataUtil;
//@@author ongkc
/**
 * A utility class containing a list of {@code Transaction} objects to be used in tests.
 */
public class TypicalTransactions {

    private static Transaction t1 = null;
    private static Transaction t2 = null;
    private static Transaction t3 = null;
    private static Transaction t4 = null;
    private static Transaction t5 = null;

    private static Person person1 = SampleDataUtil.getSamplePersons()[0];
    private static Person person2 = SampleDataUtil.getSamplePersons()[1];
    private static Person person3 = SampleDataUtil.getSamplePersons()[2];
    private static Person person4 = SampleDataUtil.getSamplePersons()[3];
    private static Person person5 = SampleDataUtil.getSamplePersons()[4];
    private static Person person6 = SampleDataUtil.getSamplePersons()[5];

    private static UniquePersonList payee2 = new UniquePersonList();
    private static UniquePersonList payee4 = new UniquePersonList();
    private static UniquePersonList payee6 = new UniquePersonList();
    private static UniquePersonList payeeGeorge = new UniquePersonList();
    private static UniquePersonList payeeFiona = new UniquePersonList();

    private static Date date = new Date();

    private static SplitMethod splitEvenly = new SplitMethod(SplitMethod.SPLIT_METHOD_EVENLY);
    private static SplitMethod splitByUnits = new SplitMethod(SplitMethod.SPLIT_METHOD_UNITS);
    private static SplitMethod splitByPercentage = new SplitMethod(SplitMethod.SPLIT_METHOD_PERCENTAGE);
    private static List<Integer> unitsList = new ArrayList<Integer>(Arrays.asList(1, 2, 3));
    private static List<Integer> percentagesList = new ArrayList<Integer>(Arrays.asList(25, 30, 45));

    static {
        try {
            payee2.add(person2);
            payee4.add(person4);
            payee6.add(person6);
            payeeGeorge.add(TypicalPersons.GEORGE);
            payeeFiona.add(TypicalPersons.FIONA);


            t1 = new TransactionBuilder().withPayer(person1).withAmount("0.00")
                    .withDescription("Boat trip").withPayees(payee2).withDate(date)
                    .withSplitMethod(splitEvenly).build();
            t2 = new TransactionBuilder().withPayer(person3).withAmount("0.00")
                    .withDescription("Food for barbecue").withPayees(payee4)
                    .withSplitMethod(splitByUnits).withUnits(unitsList).withDate(date).build();
            t3 = new TransactionBuilder().withPayer(person5).withAmount("0.00")
                    .withDescription("Open air concert").withPayees(payee6)
                    .withSplitMethod(splitByPercentage).withPercentages(percentagesList).withDate(date).build();
            t4 = new TransactionBuilder().withPayer(TypicalPersons.GEORGE).withAmount("0.00")
                    .withDescription("Transport")
                    .withPayees(payeeFiona)
                    .withSplitMethod(splitEvenly).withDate(date).build();
            t5 = new TransactionBuilder().withPayer(TypicalPersons.FIONA)
                    .withAmount("0.00").withDescription("Dinner")
                    .withPayees(payeeFiona).withDate(date)
                    .withSplitMethod(splitEvenly).withDate(date).build();
        } catch (DuplicatePersonException dpe) {
            dpe.printStackTrace();
        }
    }

    private TypicalTransactions() { } // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Transaction transaction : getTypicalTransactions()) {
            try {
                ab.addTransaction(transaction);
            } catch (CommandException ce) {
                System.out.println(ce.getMessage());
            }
        }
        return ab;
    }

    public static List<Transaction> getTypicalTransactions() {
        return new ArrayList<>(Arrays.asList(t1, t2, t3, t4, t5));
    }

    public static List<UniquePersonList> getTypicalPayees() {
        return new ArrayList<>(Arrays.asList(payee2, payee4, payee6, payeeGeorge, payeeFiona));
    }
}
