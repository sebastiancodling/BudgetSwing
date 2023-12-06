// base code for student budget assessment
// Students do not need to use this code in their assessment, fine to junk it and do something different!
//
// Your submission must be a maven project, and must be submitted via Codio, and run in Codio
//
// user can enter in wages and loans and calculate total income
//
// run in Codio 
// To see GUI, run with java and select Box Url from Codio top line menu
//
// Layout - Uses GridBag layout in a straightforward way, every component has a (column, row) position in the UI grid
// Not the prettiest layout, but relatively straightforward
// Students who use IntelliJ or Eclipse may want to use the UI designers in these IDEs , instead of GridBagLayout
package Budget;
//Test
// Swing imports
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

// class definition
public class BudgetBase extends JPanel {    // based on Swing JPanel

    // high level UI stuff
    JFrame topLevelFrame;  // top-level JFrame
    GridBagConstraints layoutConstraints = new GridBagConstraints(); // used to control layout

    // widgets which may have listeners and/or values
    private JButton calculateButton, exitButton, undoButton;      // Exit button
    private JTextField totalIncomeField; // Total Income field
    private fieldComponents wagesComponents, loansComponents, salesComponents, otherIncomeComponents, foodComponents, rentComponents, commutingComponents, otherOutgoingsComponents; // Variables to store results per section
    private Stack<BudgetState> states = new Stack<>();
    private JComboBox<String> chooseFrequency;
    private boolean isUserAction = true;
    private boolean errorShown = false;
    private boolean testMode = false;
    private boolean newSave = false;

    class fieldComponents {
        JTextField textField;
        JComboBox<String> dropdown;

        fieldComponents(JTextField textField, JComboBox<String> dropdown) {
            this.textField = textField;
            this.dropdown = dropdown;
        }
    }

    // constructor - create UI  (don't need to change this)
    public BudgetBase(JFrame frame) {
        topLevelFrame = frame; // keep track of top-level frame
        setLayout(new GridBagLayout());  // use GridBag layout
        initComponents();  // initalise components
    }

    // Getters and setters
    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public Color getColour() {
        return totalIncomeField.getForeground();
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

    private double convertToDouble(double value) {
        return Double.parseDouble(String.format("%.2f", value));
    }

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
                // If there is one state left, peek at it but do not remove it otherwise there'll be nothing more to revert to
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


    // initialise components
    // Note that this method is quite long.  Can be shortened by putting Action Listener stuff in a separate method
    // will be generated automatically by IntelliJ, Eclipse, etc
    private void initComponents() {

        // Top row (0) - "Incoming" label
        JLabel incomeLabel = new JLabel("Incoming");
        addComponent(incomeLabel, 0, 0);

        wagesComponents= addSet("Wages", 1);
        loansComponents = addSet("Loans", 2);
        salesComponents = addSet("Sales", 3);
        otherIncomeComponents = addSet("Other Income", 4);

        JLabel outgoingLabel = new JLabel("Outgoing");
        addComponent(outgoingLabel, 5, 0);

        foodComponents = addSet("Food", 6);
        rentComponents = addSet("Rent", 7);
        commutingComponents = addSet("Commuting", 8);
        otherOutgoingsComponents = addSet("Other Outgoings", 9);

        // Row 10 - Total Income label followed by total income field
        JLabel totalIncomeLabel = new JLabel("Total income");
        addComponent(totalIncomeLabel, 10, 0);

        // set up text box for displaying total income.  Users can view, but cannot directly edit it
        totalIncomeField = new JTextField("0", 10);   // 0 initially, with 10 columns
        totalIncomeField.setHorizontalAlignment(JTextField.RIGHT) ;    // number is at right end of field
        totalIncomeField.setEditable(false);    // user cannot directly edit this field (ie, it is read-only)
        addComponent(totalIncomeField, 10, 1);

        chooseFrequency = new JComboBox<>();
        chooseFrequency.addItem("per week");
        chooseFrequency.addItem("per month");
        chooseFrequency.addItem("per year");
        addComponent(chooseFrequency, 10, 2);

        // Row 11 - Calculate Button
        calculateButton = new JButton("Calculate");
        addComponent(calculateButton, 11, 0);

        // Row 11 - Undo Button
        undoButton = new JButton("Undo");
        addComponent(undoButton, 11, 1);

        // Row 11 - Exit Button
        exitButton = new JButton("Exit");
        addComponent(exitButton, 11, 2);

        // set up  listeners (in a separate method)
        initListeners();

        saveState();
    }

    // set up listeners
    // initially just for buttons, can add listeners for text fields
    private void initListeners() {

        // exitButton - exit program when pressed
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // calculateButton - call calculateTotalIncome() when pressed
        calculateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Debug: Calculate ActionListener triggered. isUserAction = " + isUserAction);
                if (isUserAction) {
                    saveState();
                    calculateTotalIncome();
                }
            }
        });

        // Choose frequency - call calculateTotalIncome() when total frequency changed
        chooseFrequency.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Debug: Total income frequency change triggered. isUserAction = " + isUserAction);
                if (isUserAction) {
                    saveState();
                    calculateTotalIncome();
                }
            }
        });

        // undoButton - call undo() when pressed
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {

                undo();
                calculateTotalIncome();
            }
        });
    }

    // add a component at specified row and column in UI.  (0,0) is top-left corner
    private void addComponent(Component component, int gridrow, int gridcol) {
        layoutConstraints.fill = GridBagConstraints.HORIZONTAL;   // always use horizontal fill
        layoutConstraints.gridx = gridcol;
        layoutConstraints.gridy = gridrow;
        add(component, layoutConstraints);

    }

    private void addEnterListener(JTextField textField) {
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTotalIncome();
            }
        });
    }

    // Method to add replicated sets
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

        dropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Debug: Dropdown action triggered. isUserAction = " + isUserAction);
                if (isUserAction) {
                    saveState();
                    calculateTotalIncome();
                }
            }
        });

        addEnterListener(textField);
        return new fieldComponents(textField, dropdown);
    }

    // Method to convert user inputs to per month
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

    // Display net income in the desired frequency of the dropdown selection
    private double freqConvert(double value) {
        String selectedFreq = (String) chooseFrequency.getSelectedItem();
        if (selectedFreq == null) {
            return value; // Default to original value if no frequency is selected
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

    // update totalIncomeField (eg, when Calculate is pressed)
    // use double to hold numbers, so user can type fractional amounts such as 134.50
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
            totalIncomeField.setText("");  // clear total income field
            //System.out.println("Debug: Value is NaN.");
            showError("Please enter a valid number.");
            return 0.0; // exit method and do nothing
        }

        // Calculate total income
        double totalIncome = wages + loans + sales + otherIncome;

        // Calculate total expenses
        double totalExpenses = food + rent + commuting + otherOutgoings;

        // Calculate net income
        double netIncome = totalIncome - totalExpenses;

        double convertedNetIncome = freqConvert(netIncome);

        totalIncomeField.setText(String.format("%.2f", convertedNetIncome));
        if (convertedNetIncome < 0) {
            totalIncomeField.setForeground(Color.RED);
        } else {
            totalIncomeField.setForeground(Color.BLACK);
        }
        return convertedNetIncome;
    }

    private void showError(String message) {
        if (!testMode && !errorShown) {
            errorShown = true;
            JOptionPane.showMessageDialog(topLevelFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
            errorShown = false;
        } else {
            System.out.println("Error: " + message);
        }
    }

    // return the value of a text field as a double
    // --return 0 if field is blank
    // --return NaN if field is not a number
    private double getTextFieldValue(JTextField field) {
        String fieldString = field.getText();
        if (fieldString.isBlank()) {
            return 0.0;
        } else {
            try {
                double value = Double.parseDouble(fieldString);
                return convertToDouble(value);
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

    // standard main class to set up Swing UI
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}