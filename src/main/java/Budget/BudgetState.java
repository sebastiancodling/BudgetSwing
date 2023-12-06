package Budget;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to store a state of BudgetBase
 * Stores all inputs that need to be saved, to allow the operation of the undo method
 * Stores status of all text/numerical and dropdown fields
 */
public class BudgetState {
    private final Map<String, Double> fieldValue;
    private final Map<String, String> dropdownValue;

    /**
     * Creates new state with given field values and dropdown selections
     * @param fieldValue Field names storing numerical inputs
     * @param dropdownValue Dropdown names storing selected dropdown inputs
     */
    public BudgetState(Map<String, Double> fieldValue, Map<String, String> dropdownValue) {
        this.fieldValue = new HashMap<>(fieldValue);
        this.dropdownValue = new HashMap<>(dropdownValue);
    }

    // Getters so main class can access the values for the stack
    public Map<String, Double> getFieldValue() {
        return fieldValue;
    }

    public Map<String, String> getDropdownValue() {
        return dropdownValue;
    }
}

