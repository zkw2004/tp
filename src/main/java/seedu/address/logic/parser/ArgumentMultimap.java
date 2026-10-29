package seedu.address.logic.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Stores mapping of prefixes to their respective arguments.
 * Each key may be associated with multiple argument values.
 * Values for a given key are stored in a list, and the insertion ordering is maintained.
 * Keys are unique, but the list of argument values may contain duplicate argument values, i.e. the same argument value
 * can be inserted multiple times for the same prefix.
 */
public class ArgumentMultimap {

    /** Prefixes mapped to their respective arguments */
    private final Map<Prefix, List<String>> argMultimap = new HashMap<>();
    /** Prefixes mapped to their arguments before whitespace is trimmed. */
    private final Map<Prefix, List<String>> rawArgMultimap = new HashMap<>();

    /**
     * Associates the specified argument value, and the original value before trimming, with {@code prefix}.
     * If the map previously contained a mapping for the key, the new values are appended to the existing values.
     * The original value is available to parsers that must validate otherwise-trimmed characters.
     *
     * @param prefix      Prefix key with which the specified argument value is to be associated
     * @param argValue    Trimmed argument value to be associated with the specified prefix key
     * @param rawArgValue The same argument value before trimming
     */
    public void put(Prefix prefix, String argValue, String rawArgValue) {
        List<String> argValues = getAllValues(prefix);
        argValues.add(argValue);
        argMultimap.put(prefix, argValues);

        List<String> rawArgValues = getAllRawValues(prefix);
        rawArgValues.add(rawArgValue);
        rawArgMultimap.put(prefix, rawArgValues);
    }

    /**
     * Returns the last value of {@code prefix}.
     */
    public Optional<String> getValue(Prefix prefix) {
        List<String> values = getAllValues(prefix);
        return values.isEmpty() ? Optional.empty() : Optional.of(values.getLast());
    }

    /** Returns the last untrimmed value of {@code prefix}. */
    public Optional<String> getRawValue(Prefix prefix) {
        List<String> values = getAllRawValues(prefix);
        return values.isEmpty() ? Optional.empty() : Optional.of(values.getLast());
    }

    /**
     * Returns all values of {@code prefix}.
     * If the prefix does not exist or has no values, this will return an empty list.
     * Modifying the returned list will not affect the underlying data structure of the ArgumentMultimap.
     */
    public List<String> getAllValues(Prefix prefix) {
        if (!argMultimap.containsKey(prefix)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(argMultimap.get(prefix));
    }

    /** Returns all untrimmed values of {@code prefix}. */
    private List<String> getAllRawValues(Prefix prefix) {
        if (!rawArgMultimap.containsKey(prefix)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(rawArgMultimap.get(prefix));
    }

    /**
     * Returns the preamble (text before the first valid prefix). Trims any leading/trailing spaces.
     */
    public String getPreamble() {
        return getValue(new Prefix("")).orElse("");
    }

    /**
     * Throws a {@code ParseException} if any of the prefixes given in {@code prefixes} appeared more than
     * once among the arguments.
     */
    public void verifyNoDuplicatePrefixesFor(Prefix... prefixes) throws ParseException {
        Prefix[] duplicatedPrefixes = Stream.of(prefixes).distinct()
                .filter(prefix -> argMultimap.containsKey(prefix) && argMultimap.get(prefix).size() > 1)
                .toArray(Prefix[]::new);

        if (duplicatedPrefixes.length > 0) {
            throw new ParseException(Messages.getErrorMessageForDuplicatePrefixes(duplicatedPrefixes));
        }
    }
}
