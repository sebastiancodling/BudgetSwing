package Budget;

import java.util.HashMap;
import java.util.Map;

public class BudgetState {
    private final Map<String, Double> fieldValue;
    private final Map<String, String> dropdownValue;

    public BudgetState(Map<String, Double> fieldValue, Map<String, String> dropdownValue) {
        this.fieldValue = new HashMap<>(fieldValue);
        this.dropdownValue = new HashMap<>(dropdownValue);
    }

    public Map<String, Double> getFieldValue() {
        return fieldValue;
    }

    public Map<String, String> getDropdownValue() {
        return dropdownValue;
    }
}

