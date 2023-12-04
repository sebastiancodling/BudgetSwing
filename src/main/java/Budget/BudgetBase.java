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

// class definition
public class BudgetBase extends JPanel {    // based on Swing JPanel

    // high level UI stuff
    JFrame topLevelFrame;  // top-level JFrame
    GridBagConstraints layoutConstraints = new GridBagConstraints(); // used to control layout

    // widgets which may have listeners and/or values
    private JButton calculateButton, exitButton, undoButton;      // Exit button
    private JTextField totalIncomeField; // Total Income field
    private fieldComponents wagesComponents, loansComponents, salesComponents, otherIncomeComponents, foodComponents, rentComponents, commutingComponents, otherOutgoingsComponents; // Variables to store results per section

    class fieldComponents {
        JTextField textField;
        JComboBox<String> dropdown;

        fieldComponents(JTextField textField, JComboBox<String> dropdown) {
            this.textField = textField;
            this.dropdown = dropdown;
        }
    }

    // constructor - create UI  (dont need to change this)
    public BudgetBase(JFrame frame) {
        topLevelFrame = frame; // keep track of top-level frame
        setLayout(new GridBagLayout());  // use GridBag layout
        initComponents();  // initalise components
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

        // Row 3 - Total Income label followed by total income field
        JLabel totalIncomeLabel = new JLabel("Total income");
        addComponent(totalIncomeLabel, 10, 0);

        // set up text box for displaying total income.  Users can view, but cannot directly edit it
        totalIncomeField = new JTextField("0", 10);   // 0 initially, with 10 columns
        totalIncomeField.setHorizontalAlignment(JTextField.RIGHT) ;    // number is at right end of field
        totalIncomeField.setEditable(false);    // user cannot directly edit this field (ie, it is read-only)
        addComponent(totalIncomeField, 10, 1);

        JLabel freqLabel = new JLabel("per month");
        addComponent(freqLabel, 11, 1);

        // Row 4 - Calculate Button
        calculateButton = new JButton("Calculate");
        addComponent(calculateButton, 12, 0);

        // Row 5 - Exit Button
        undoButton = new JButton("Undo");
        addComponent(undoButton, 13, 0);

        // Row 5 - Exit Button
        exitButton = new JButton("Exit");
        addComponent(exitButton, 14, 0);

        // set up  listeners (in a separate method)
        initListeners();
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
            public void focusLost(FocusEvent e) { // Call when focus changes
                calculateTotalIncome();
            }
        });

        dropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // Call when dropdown option selected
                calculateTotalIncome();
            }
        });

        return new fieldComponents(textField, dropdown);
    }

    // Method to convert user inputs to per month
    private double conversion(double value, JComboBox<String> dropdown) {
        String frequency = (String) dropdown.getSelectedItem();
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
            System.out.println("Error: value is NaN");
            return 0.0;  // exit method and do nothing
        }

        // Calculate total income
        double totalIncome = wages + loans + sales + otherIncome;

        // Calculate total expenses
        double totalExpenses = food + rent + commuting + otherOutgoings;

        // Calculate net income
        double netIncome = totalIncome - totalExpenses;

        totalIncomeField.setText(String.format("%.2f",netIncome));  // format with 2 digits after the .
        if (netIncome < 0) {
            totalIncomeField.setForeground(Color.RED);  // Set  colour to red for negative net income
        } else {
            totalIncomeField.setForeground(Color.BLACK);  // Set text colour to black for positive net income
        }
        return netIncome;
    }

    // return the value of a text field as a double
    // --return 0 if field is blank
    // --return NaN if field is not a number
    private double getTextFieldValue(JTextField field) {

        // get value as String from field
        String fieldString = field.getText();  // get text from text field

        if (fieldString.isBlank()) {   // if text field is blank, return 0
            return 0;
        }

        else {  // if text field is not blank, parse it into a double
            try {
                return Double.parseDouble(fieldString);  // parse field number into a double
             } catch (java.lang.NumberFormatException ex) {  // catch invalid number exception
                JOptionPane.showMessageDialog(topLevelFrame, "Please enter a valid number");  // show error message
                return Double.NaN;  // return NaN to show that field is not a number
            }
        }
    }


// below is standard code to set up Swing, which students shouldnt need to edit much
    // standard mathod to show UI
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