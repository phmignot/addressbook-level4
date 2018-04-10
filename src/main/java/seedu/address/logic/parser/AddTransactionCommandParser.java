package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.AddTransactionCommand.MESSAGE_INVALID_NUMBER_OF_VALUES;
import static seedu.address.logic.commands.AddTransactionCommand.MESSAGE_INVALID_PERCENTAGE_VALUES;
import static seedu.address.logic.commands.AddTransactionCommand.MESSAGE_NONEXISTENT_PERSON;
import static seedu.address.logic.commands.AddTransactionCommand.MESSAGE_PAYEE_IS_PAYER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYEE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPLIT_BY_PERCENTAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPLIT_BY_UNITS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPLIT_METHOD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_TYPE;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddTransactionCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Description;
import seedu.address.model.transaction.SplitMethod;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.TransactionType;
//@@author ongkc
/**
 * Parses input arguments and creates a new AddTransactionCommand object
 */
public class AddTransactionCommandParser implements Parser<AddTransactionCommand> {
    private TransactionType transactionType;
    private SplitMethod splitMethod;
    private List<Integer> units = Collections.emptyList();
    private List<Integer> percentages = Collections.emptyList();

    /**
     * Parses the given {@code String} of arguments in the context of the AddTransactionCommand
     * and returns an AddTransactionCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddTransactionCommand parse(String args, Model model) throws ParseException, CommandException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TRANSACTION_TYPE, PREFIX_PAYER, PREFIX_AMOUNT,
                        PREFIX_DESCRIPTION, PREFIX_PAYEE, PREFIX_SPLIT_METHOD, PREFIX_SPLIT_BY_UNITS,
                        PREFIX_SPLIT_BY_PERCENTAGE);

        if (!arePrefixesPresent(argMultimap, PREFIX_TRANSACTION_TYPE, PREFIX_PAYER, PREFIX_AMOUNT,
                PREFIX_DESCRIPTION, PREFIX_PAYEE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddTransactionCommand.MESSAGE_USAGE));
        }

        //@@author steven-jia
        try {
            transactionType = ParserUtil.parseTransactionType(argMultimap.getValue(
                    PREFIX_TRANSACTION_TYPE).get());
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (transactionType.value.equals(TransactionType.TRANSACTION_TYPE_PAYMENT)) {
            try {
                splitMethod = ParserUtil.parseSplitMethod(argMultimap.getValue(PREFIX_SPLIT_METHOD));
                switch (splitMethod.method) {
                case EVENLY:
                    break;
                case UNITS:
                    units = ParserUtil.parseUnitsList(argMultimap.getValue(PREFIX_SPLIT_BY_UNITS));
                    break;
                case PERCENTAGE:
                    percentages = ParserUtil.parsePercentagesList(argMultimap.getValue(PREFIX_SPLIT_BY_PERCENTAGE));
                    break;
                default:
                }
            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }
        } else {
            splitMethod = new SplitMethod(SplitMethod.SPLIT_METHOD_NOT_APPLICABLE);
        }

        try {
            Person payer = model.findPersonByName(ParserUtil.parseName(argMultimap.getValue(PREFIX_PAYER)).get());
            Amount amount = ParserUtil.parseAmount(argMultimap.getValue(PREFIX_AMOUNT)).get();
            Description description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION)).get();
            UniquePersonList payees = model.getPayeesList(argMultimap, model);
            Date dateTime = Date.from(Instant.now(Clock.system(ZoneId.of("Asia/Singapore"))));

            validatePayees(payer, payees);
            validateSplitMethodValues(payees, splitMethod, units, percentages);

            Transaction transaction = new Transaction(transactionType, payer, amount, description, dateTime,
                    payees, splitMethod, units, percentages);
            return new AddTransactionCommand(transaction);
        } catch (PersonNotFoundException pnfe) {
            throw new CommandException(MESSAGE_NONEXISTENT_PERSON);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }
    /**
     * Checks list of units and list of percentages for validity
     */
    private void validateSplitMethodValues(UniquePersonList payees, SplitMethod splitMethod,
                                           List<Integer> units, List<Integer> percentages) throws CommandException {
        if (splitMethod.method.equals(SplitMethod.Method.UNITS)) {
            if (units.size() != payees.asObservableList().size() + 1) {
                throw new CommandException(String.format(MESSAGE_INVALID_NUMBER_OF_VALUES, splitMethod.toString()));
            }
        } else if (splitMethod.method.equals(SplitMethod.Method.PERCENTAGE)) {
            if (percentages.size() != payees.asObservableList().size() + 1) {
                throw new CommandException(String.format(MESSAGE_INVALID_NUMBER_OF_VALUES, splitMethod.toString()));
            }
            Integer total = 0;
            for (Integer percentage: percentages) {
                total += percentage;
            }
            if (total != 100) {
                throw new CommandException(MESSAGE_INVALID_PERCENTAGE_VALUES);
            }
        }
    }

    private void validatePayees(Person payer, UniquePersonList payees) throws CommandException {
        if (payees.contains(payer)) {
            throw new CommandException(MESSAGE_PAYEE_IS_PAYER);
        }
    }

    //@@author ongkc
    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}

