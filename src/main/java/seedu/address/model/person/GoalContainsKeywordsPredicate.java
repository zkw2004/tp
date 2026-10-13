package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/** Tests that a {@code Person}'s goal matches any supplied keyword. */
public class GoalContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public GoalContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = List.copyOf(keywords);
    }

    @Override
    public boolean test(Person person) {
        return person.getGoal().map(goal -> keywords.stream()
                .anyMatch(keyword -> StringUtil.containsIgnoreCase(goal.value, keyword))).orElse(false);
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof GoalContainsKeywordsPredicate otherPredicate
                && keywords.equals(otherPredicate.keywords));
    }

    @Override
    public int hashCode() {
        return keywords.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
