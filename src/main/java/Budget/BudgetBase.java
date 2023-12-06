
// run in Codio 
// To see GUI, run with java and select Box Url from Codio top line menu

package Budget;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Insets;
import java.awt.event.*;
import java.awt.*;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;
import com.formdev.flatlaf.FlatIntelliJLaf;

/**
 * Main class - extends JPanel. Handles UI and calculations.
 */
public class BudgetBase extends JPanel {    // based on Swing JPanel

    // high level UI stuff
    JFrame topLevelFrame;  // top-level JFrame
    GridBagConstraints layoutConstraints = new GridBagConstraints(); // used to control layout

    // widgets which may have listeners and/or values
    private JButton calculateButton, exitButton, undoButton;      // Exit button
    private JTextField totalBudgetField, totalIncomeField, totalOutgoingField; // Total Income field
    private fieldComponents wagesComponents, loansComponents, salesComponents, otherIncomeComponents, foodComponents, rentComponents, commutingComponents, otherOutgoingsComponents; // Variables to store results per section
    private final Stack<BudgetState> states = new Stack<>();
    private JComboBox<String> chooseFrequency;
    private boolean isUserAction = true;
    private boolean errorShown = false;
    private boolean testMode = false;
    private boolean newSave = false;


    /**
     * Inner class to make reusable field components for each input field category
     */
    static class fieldComponents {
        JTextField textField;
        JComboBox<String> dropdown;

        fieldComponents(JTextField textField, JComboBox<String> dropdown) {
            this.textField = textField;
            this.dropdown = dropdown;
        }
    }

    /**
     * Constructor to initialise BudgetBase and handle frame size and padding
     */
    public BudgetBase(JFrame frame) {
        topLevelFrame = frame; // keep track of top-level frame
        setLayout(new GridBagLayout());  // use GridBag layout
        this.setBorder(new EmptyBorder(30, 30, 30, 30));
        layoutConstraints.fill = GridBagConstraints.BOTH; // Allow resizing
        layoutConstraints.weightx = 1.0;
        layoutConstraints.weighty = 1.0;

        initComponents();  // initialise components
    }

    // Getters and setters so that methods can be easily tested without making methods public

    /**
     * Used to put BudgetBase in test mode
     * In test mode, features like pop-up dialogs are disabled to enable automated testing
     * @param testMode Boolean to enable or disable test mode
     */
    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    // Getters and setters
    public Color getColour() {
        return totalBudgetField.getForeground();
    }

    public JTextField getWagesTextField() {
        return wagesComponents.textField;
    }

    public JTextField getLoansTextField() {
        return loansComponents.textField;
    }

    public JTextField getSalesTextField() {
        return salesComponents.textField;
    }

    public JTextField getOtherIncomeTextField() {
        return otherIncomeComponents.textField;
    }

    public JTextField getFoodTextField() {
        return foodComponents.textField;
    }

    public JTextField getRentTextField() {
        return rentComponents.textField;
    }

    public JTextField getCommutingTextField() {
        return commutingComponents.textField;
    }

    public JTextField getOtherOutgoingsTextField() {
        return otherOutgoingsComponents.textField;
    }

    public JComboBox<String> getWagesDropdown() {
        return wagesComponents.dropdown;
    }

    public JComboBox<String> getLoansDropdown() {
        return loansComponents.dropdown;
    }

    public JComboBox<String> getSalesDropdown() {
        return salesComponents.dropdown;
    }

    public JComboBox<String> getOtherIncomeDropdown() {
        return otherIncomeComponents.dropdown;
    }

    public JComboBox<String> getFoodDropdown() {
        return foodComponents.dropdown;
    }

    public JComboBox<String> getRentDropdown() {
        return rentComponents.dropdown;
    }

    public JComboBox<String> getCommutingDropdown() {
        return commutingComponents.dropdown;
    }

    public JComboBox<String> getOtherOutgoingsDropdown() {
        return otherOutgoingsComponents.dropdown;
    }

    public JComboBox<String> getChooseFrequency() {
        return chooseFrequency;
    }

    public JTextField getTotalBudgetField() {
        return totalBudgetField;
    }

    public JButton getUndoButton() {
        return undoButton;
    }


    /**
     * Converts value to double with 2dp
     * @param value Takes value to be converted
     * @return double value to 2dp
     */
    private double convertToDouble(double value) {
        return Double.parseDouble(String.format("%.2f", value));
    }


    /**
     * Pushes save states to the save stack
     */
    private void saveState() {
        Map<String, Double> fieldValue = new HashMap<>();
        Map<String, String> dropdownValue = new HashMap<>();

        fieldValue.put("wages", convertToDouble(getTextFieldValue(wagesComponents.textField)));
        dropdownValue.put("wage frequency", (String) wagesComponents.dropdown.getSelectedItem());
        fieldValue.put("loans", convertToDouble(getTextFieldValue(loansComponents.textField)));
        dropdownValue.put("loans frequency", (String) loansComponents.dropdown.getSelectedItem());
        fieldValue.put("sales", convertToDouble(getTextFieldValue(salesComponents.textField)));
        dropdownValue.put("sales frequency", (String) salesComponents.dropdown.getSelectedItem());
        fieldValue.put("other income", convertToDouble(getTextFieldValue(otherIncomeComponents.textField)));
        dropdownValue.put("other income frequency", (String) otherIncomeComponents.dropdown.getSelectedItem());
        fieldValue.put("food", convertToDouble(getTextFieldValue(foodComponents.textField)));
        dropdownValue.put("food frequency", (String) foodComponents.dropdown.getSelectedItem());
        fieldValue.put("rent", convertToDouble(getTextFieldValue(rentComponents.textField)));
        dropdownValue.put("rent frequency", (String) rentComponents.dropdown.getSelectedItem());
        fieldValue.put("commuting", convertToDouble(getTextFieldValue(commutingComponents.textField)));
        dropdownValue.put("commuting frequency", (String) commutingComponents.dropdown.getSelectedItem());
        fieldValue.put("other outgoings", convertToDouble(getTextFieldValue(otherOutgoingsComponents.textField)));
        dropdownValue.put("other outgoings frequency", (String) otherOutgoingsComponents.dropdown.getSelectedItem());

        for (Double value : fieldValue.values()) {
            if (Double.isNaN(value)) {
                //System.out.println("Debug: Failed to save state due to NaN value.");
                return;
            }
        }

        if (!states.isEmpty()) {
            BudgetState previousState = states.peek();
            Map<String, Double> previousFieldValue = previousState.getFieldValue();
            Map<String, String> previousDropdownValue = previousState.getDropdownValue();

            if (fieldValue.equals(previousFieldValue) && dropdownValue.equals(previousDropdownValue)) {
                //System.out.println("Debug: Current state is the same as the previous saved state, not storing current state.");
                return;
            }
        }

        //System.out.println("Debug: Saved the state.");
        states.push(new BudgetState(fieldValue, dropdownValue));
        newSave = true;
    }

    /**
     * Method to set the state to the previous saved state on the stack
     */
    private void retrieveState(BudgetState state) {
        isUserAction = false;
        Map<String, Double> fieldValues = state.getFieldValue();
        Map<String, String> dropdownValues = state.getDropdownValue();

        wagesComponents.textField.setText(String.format("%.2f", fieldValues.get("wages")));
        wagesComponents.dropdown.setSelectedItem(dropdownValues.get("wage frequency"));
        loansComponents.textField.setText(String.format("%.2f", fieldValues.get("loans")));
        loansComponents.dropdown.setSelectedItem(dropdownValues.get("loans frequency"));
        salesComponents.textField.setText(String.format("%.2f", fieldValues.get("sales")));
        salesComponents.dropdown.setSelectedItem(dropdownValues.get("sales frequency"));
        otherIncomeComponents.textField.setText(String.format("%.2f", fieldValues.get("other income")));
        otherIncomeComponents.dropdown.setSelectedItem(dropdownValues.get("other income frequency"));
        foodComponents.textField.setText(String.format("%.2f", fieldValues.get("food")));
        foodComponents.dropdown.setSelectedItem(dropdownValues.get("food frequency"));
        rentComponents.textField.setText(String.format("%.2f", fieldValues.get("rent")));
        rentComponents.dropdown.setSelectedItem(dropdownValues.get("rent frequency"));
        commutingComponents.textField.setText(String.format("%.2f", fieldValues.get("commuting")));
        commutingComponents.dropdown.setSelectedItem(dropdownValues.get("commuting frequency"));
        otherOutgoingsComponents.textField.setText(String.format("%.2f", fieldValues.get("other outgoings")));
        otherOutgoingsComponents.dropdown.setSelectedItem(dropdownValues.get("other outgoings frequency"));

        isUserAction = true;
        //System.out.println("Debug: Attempt to retrieve the state.");
    }

    /**
     * Undo function, supports multiple levels of undo
     */
    private void undo() {
        if (!states.isEmpty()) {
            BudgetState previousState;

            // If more than one state, pop most recent state
            if (states.size() > 1) {
                previousState = states.pop();
                retrieveState(previousState);

                // If new state was saved since last undo and there's still 1 or more state left, undo again
                if (newSave && states.size() > 1) {
                    previousState = states.pop();
                    retrieveState(previousState);
                }
            } else {
                // If there is one state left, peek at it but do not remove it otherwise there'll be nothing left to revert to
                previousState = states.peek();
                retrieveState(previousState);
            }

            newSave = false;
        }

        /* Debug: Print current state of the stack
        System.out.println("Debug: Current State of the Stack after Undo:");
        for (BudgetState state : states) {
            System.out.println("State: " + state.getFieldValue() + ", " + state.getDropdownValue());
        }

         */
    }


    /**
     * Initialises GUI components
     */
    private void initComponents() {

        // Top row (0) - "Incoming" label
        JLabel incomeLabel = new JLabel("Incoming");
        Font f = incomeLabel.getFont();
        incomeLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD, 16)); // Set font to bold and increase size
        incomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0)); // Add padding
        addComponent(incomeLabel, 0, 0);

        wagesComponents= addSet("Wages", 1);
        loansComponents = addSet("Loans", 2);
        salesComponents = addSet("Sales", 3);
        otherIncomeComponents = addSet("Other Income", 4);

        // Row 10 - Total Income label followed by total income field
        JLabel totalIncomeLabel = new JLabel("Total Incoming");
        totalIncomeLabel.setFont(f.deriveFont(f.getStyle() | Font.ITALIC));
        addComponent(totalIncomeLabel, 5, 0);

        // set up text box for displaying total income.  Users can view, but cannot directly edit it
        totalIncomeField = new JTextField("0", 10);   // 0 initially, with 10 columns
        totalIncomeField.setHorizontalAlignment(JTextField.RIGHT) ;    // number is at right end of field
        totalIncomeField.setEditable(false);    // user cannot directly edit this field (ie, it is read-only)
        addComponent(totalIncomeField, 5, 1);


        JLabel outgoingLabel = new JLabel("Outgoing");
        outgoingLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD, 16));
        outgoingLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0)); // Add padding
        addComponent(outgoingLabel, 6, 0);

        foodComponents = addSet("Food", 7);
        rentComponents = addSet("Rent", 8);
        commutingComponents = addSet("Commuting", 9);
        otherOutgoingsComponents = addSet("Other Outgoings", 10);

        // Row 10 - Total outgoing label followed by total outgoing field
        JLabel totalOutgoingLabel = new JLabel("Total Outgoings");
        totalOutgoingLabel.setFont(f.deriveFont(f.getStyle() | Font.ITALIC));
        addComponent(totalOutgoingLabel, 11, 0);

        // set up text box for displaying total outgoing.  Users can view, but cannot directly edit it
        totalOutgoingField = new JTextField("0", 10);   // 0 initially, with 10 columns
        totalOutgoingField.setHorizontalAlignment(JTextField.RIGHT) ;    // number is at right end of field
        totalOutgoingField.setEditable(false);    // user cannot directly edit this field (ie, it is read-only)
        addComponent(totalOutgoingField, 11, 1);

        JLabel budgetLabel = new JLabel("Budget");
        budgetLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD, 16));
        budgetLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0)); // Add padding
        addComponent(budgetLabel, 12, 0);

        // Row 10 - Total Budget label followed by total budget field
        JLabel totalBudgetLabel = new JLabel("Total Budget");
        totalBudgetLabel.setFont(f.deriveFont(f.getStyle() | Font.ITALIC));
        addComponent(totalBudgetLabel, 13, 0);

        // set up text box for displaying total budget.  Users can view, but cannot directly edit it
        totalBudgetField = new JTextField("0", 10);   // 0 initially, with 10 columns
        totalBudgetField.setHorizontalAlignment(JTextField.RIGHT) ;    // number is at right end of field
        totalBudgetField.setEditable(false);    // user cannot directly edit this field (ie, it is read-only)
        addComponent(totalBudgetField, 13, 1);

        chooseFrequency = new JComboBox<>();
        chooseFrequency.addItem("per week");
        chooseFrequency.addItem("per month");
        chooseFrequency.addItem("per year");
        addComponent(chooseFrequency, 13, 2);

        // Row 11 - Undo Button
        layoutConstraints.insets = new Insets(20, 0, 0, 0);
        undoButton = new JButton("Undo");
        addComponent(undoButton, 14, 0);

        // Row 11 - Calculate Button
        layoutConstraints.insets = new Insets(20, 0, 0, 0);
        calculateButton = new JButton("Calculate");
        addComponent(calculateButton, 14, 1);

        // Row 11 - Exit Button
        layoutConstraints.insets = new Insets(20, 0, 0, 0);
        exitButton = new JButton("Exit");
        addComponent(exitButton, 14, 2);

        // set up  listeners (in a separate method)
        initListeners();

        saveState();
    }

    /**
     * Set up listeners for some interactive components
     */
    private void initListeners() {

        // Reference: https://docs.oracle.com/javase/8/docs/api/java/awt/KeyboardFocusManager.html
        // Adds ability to lose focus on object when click anywhere in frame
        topLevelFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
            }
        });

        // exitButton - exit program when pressed
        exitButton.addActionListener(e -> System.exit(0));

        // calculateButton - call calculateTotalIncome() when pressed
        calculateButton.addActionListener(e -> {
            //System.out.println("Debug: Calculate ActionListener triggered. isUserAction = " + isUserAction);
            if (isUserAction) {
                saveState();
                calculateTotalIncome();
            }
        });

        // Choose frequency - call calculateTotalIncome() when total frequency changed
        chooseFrequency.addActionListener(e -> {
            //System.out.println("Debug: Total income frequency change triggered. isUserAction = " + isUserAction);
            if (isUserAction) {
                saveState();
                calculateTotalIncome();
            }
        });

        // undoButton - call undo() when pressed
        undoButton.addActionListener(e -> {

            undo();
            calculateTotalIncome();
        });
    }

    /**
     * Adds component to panel in the given grid position
     * @param component Component to be added
     * @param gridrow Row
     * @param gridcol Column
     */
    private void addComponent(Component component, int gridrow, int gridcol) {
        layoutConstraints.fill = GridBagConstraints.HORIZONTAL;   // always use horizontal fill
        layoutConstraints.gridx = gridcol;
        layoutConstraints.gridy = gridrow;
        add(component, layoutConstraints);

    }


    /**
     * Used to add the spreadsheet behaviour by updating when enter is pressed
     * Uses lambda
     * @param textField Field to which listener is being added to
     */
    private void addEnterListener(JTextField textField) {
        textField.addActionListener(e -> calculateTotalIncome());
    }

    /**
     * Method to easily set up each row of components, reducing repetitive set up of input fields
     * Adds event listeners for focus shift and changing dropdown
     * @param labelText Text for the label/description of the cost
     * @param gridRow Row where components will be placed
     * @return Object with text field and dropdown components
     */
    private fieldComponents addSet(String labelText, int gridRow) {

        JLabel label = new JLabel(labelText);
        addComponent(label, gridRow, 0);

        JTextField textField = new JTextField("", 10);
        textField.setHorizontalAlignment(JTextField.RIGHT);
        addComponent(textField, gridRow, 1);

        JComboBox<String> dropdown = new JComboBox<>();
        dropdown.addItem("per week");
        dropdown.addItem("per month");
        dropdown.addItem("per year");
        addComponent(dropdown, gridRow, 2);

        // Spreadsheet behaviour implementation - call the calculateTotalIncome method when focus shifts or action taken
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                //System.out.println("Debug: Focus lost triggered. isUserAction = " + isUserAction);
                if (isUserAction) {
                    saveState();
                    calculateTotalIncome();
                }
            }
        });

        // Spreadsheet behaviour implementation - call the calculateTotalIncome method when dropdown pressed
        dropdown.addActionListener(e -> {
            //System.out.println("Debug: Dropdown action triggered. isUserAction = " + isUserAction);
            if (isUserAction) {
                saveState();
                calculateTotalIncome();
            }
        });

        addEnterListener(textField);
        return new fieldComponents(textField, dropdown);
    }

    /**
     * Convert input to frequency specified in dropdown
     * @param value Value to be converted
     * @param dropdown Dropdown component with frequency
     * @return Converted value in the chosen frequency
     */
    private double conversion(double value, JComboBox<String> dropdown) {
        String frequency = (String) dropdown.getSelectedItem();

        if (frequency == null) {
            return 0;
        }

        switch (frequency) {
            case "per week":
                return value * 4.3333333;
            case "per year":
                return value / 12;
            case "per month":
            default:
                return value;
        }
    }

    /**
     * Convert value to chosen frequency from dropdown
     * @param value Value to be converted
     * @return Value converted to chosen frequency
     */
    private double freqConvert(double value) {
        String selectedFreq = (String) chooseFrequency.getSelectedItem();
        if (selectedFreq == null) {
            return value; // Default to original value if no frequency selected
        }

        switch (selectedFreq) {
            case "per week":
                return value / 4.3333333; // Monthly to weekly
            case "per year":
                return value * 12; // Monthly to yearly
            case "per month":
            default:
                return value; // If already monthly do nothing else
        }
    }

    /**
     * Calculates the total income from text values and dropdown frequencies
     * Called for every change in value
     * Can calculate fractional amounts up to 2dp
     * @return Calculated net income (surplus/deficit)
     */
    public double calculateTotalIncome() {

        // Income values from income text fields.  value is NaN if an error occurs
        double wages = conversion(getTextFieldValue(wagesComponents.textField), wagesComponents.dropdown);
        double loans = conversion(getTextFieldValue(loansComponents.textField), loansComponents.dropdown);
        double sales = conversion(getTextFieldValue(salesComponents.textField), salesComponents.dropdown);
        double otherIncome = conversion(getTextFieldValue(otherIncomeComponents.textField), otherIncomeComponents.dropdown);

        // Expense values
        double food = conversion(getTextFieldValue(foodComponents.textField), foodComponents.dropdown);
        double rent = conversion(getTextFieldValue(rentComponents.textField), rentComponents.dropdown);
        double commuting = conversion(getTextFieldValue(commutingComponents.textField), commutingComponents.dropdown);
        double otherOutgoings = conversion(getTextFieldValue(otherOutgoingsComponents.textField), otherOutgoingsComponents.dropdown);


        // clear total field and return if any value is NaN (error)
        if (Double.isNaN(wages) || Double.isNaN(loans) || Double.isNaN(sales) || Double.isNaN(otherIncome) ||
                Double.isNaN(food) || Double.isNaN(rent) || Double.isNaN(commuting) || Double.isNaN(otherOutgoings)) {
            totalBudgetField.setText("");  // clear total budget field
            totalIncomeField.setText("");  // clear total income field
            totalOutgoingField.setText("");  // clear total outgoing field
            //System.out.println("Debug: Value is NaN.");
            showError();
            return 0.0; // exit method and do nothing
        }

        // Calculate total income
        double totalIncome = wages + loans + sales + otherIncome;

        // Calculate total expenses
        double totalExpenses = food + rent + commuting + otherOutgoings;

        // Calculate net income
        double netIncome = totalIncome - totalExpenses;

        // Convert to required frequency
        double convertedNetIncome = freqConvert(netIncome);
        double convertedTotalIncome = freqConvert(totalIncome);
        double convertedTotalExpenses = freqConvert(totalExpenses);

        totalBudgetField.setText(String.format("%.2f", convertedNetIncome));
        totalIncomeField.setText(String.format("%.2f", convertedTotalIncome));
        totalOutgoingField.setText(String.format("%.2f", convertedTotalExpenses));
        if (convertedNetIncome < 0) {
            totalBudgetField.setForeground(Color.RED);
        } else {
            totalBudgetField.setForeground(Color.BLACK);
        }
        return convertedNetIncome;
    }

    /**
     * Shows error message when number is negative or NaN
     */
    private void showError() {
        if (!testMode && !errorShown) {
            errorShown = true;
            JOptionPane.showMessageDialog(topLevelFrame, "Please enter a valid positive number.", "Error", JOptionPane.ERROR_MESSAGE);
            errorShown = false;
        }
    }

    /**
     * Gets value from text field and converts to double
     * Returns 0 if field is blank and NaN if the input is not a valid number
     * @param field Text field where value needs to be retrieved
     * @return Value as a double/NaN
     */
    private double getTextFieldValue(JTextField field) {
        String fieldString = field.getText();
        if (fieldString.isBlank()) {
            return 0.0;
        } else {
            try {
                double value = Double.parseDouble(fieldString);
                return convertToDouble(Math.abs(value)); // Convert to a positive number
            } catch (NumberFormatException ex) {
                return Double.NaN;
            }
        }
    }

    // below is standard code to set up Swing, which students shouldn't need to edit much
    // standard method to show UI
    private static void createAndShowGUI() {

        //Create and set up the window.
        JFrame frame = new JFrame("Budget Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        BudgetBase newContentPane = new BudgetBase(frame);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Main method which creates and shows GUI
     */
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.

        // Reference: https://www.formdev.com/flatlaf/
        // Rounded corners and setup design theme
        UIManager.put( "Component.arc", 15 );
        UIManager.put( "Button.arc", 15 );
        UIManager.put( "TextComponent.arc", 15 );
        FlatIntelliJLaf.setup();
        javax.swing.SwingUtilities.invokeLater(BudgetBase::createAndShowGUI);
    }
}