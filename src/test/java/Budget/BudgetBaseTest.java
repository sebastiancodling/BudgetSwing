package Budget;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;

// Swing imports
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * Unit test for swing application.
 * We can't test the GUI, so we have to test methods that do the work.
 */
public class BudgetBaseTest 
{
    /**
     * Rigorous Test :-)
     */
    private BudgetBase bb;

    @BeforeEach
    public void setFrame() {
        JFrame frame = new JFrame();
        bb = new BudgetBase(frame);
        bb.setTestMode(true);
    }

    @Test
    public void shouldTotal() {
        // Testing that 0.0 output returned if all fields are default/blank

        JFrame frame = new JFrame();
        BudgetBase bb = new BudgetBase(frame);
        double output = 0.0;

        assertEquals(output, bb.calculateTotalIncome() );
    }

    @Test
    public void checkEmptyInput() {
        // Empty text values to check they are being marked as a 0.0

        bb.getWagesTextField().setText("");
        bb.getLoansTextField().setText("");
        bb.getSalesTextField().setText("");
        bb.getOtherIncomeTextField().setText("");
        bb.getFoodTextField().setText("");
        bb.getRentTextField().setText("");
        bb.getCommutingTextField().setText("");
        bb.getOtherOutgoingsTextField().setText("");

        double output = 0.0;
        assertEquals(output, bb.calculateTotalIncome(), 0.01); // Allow float rounding and check correct output
    }

    @Test
    public void checkInvalidInput() {
        // Checking NaN inputs are treated as 0.0

        bb.getWagesTextField().setText("NaN"); // Set an invalid input
        bb.getLoansTextField().setText("");
        bb.getSalesTextField().setText("");
        bb.getOtherIncomeTextField().setText("");
        bb.getFoodTextField().setText("");
        bb.getRentTextField().setText("");
        bb.getCommutingTextField().setText("");
        bb.getOtherOutgoingsTextField().setText("");

        double output = 0.0; // Set expected output
        assertEquals(output, bb.calculateTotalIncome(), 0.01); // Allow float rounding and check correct output
    }

    @Test
    public void testCalculateTotalIncome() {
        // Testing calculateTotalIncome method, as well as per month calculations

        // Valid inputs, assuming that it will be treated as per month
        bb.getWagesTextField().setText("1000");
        bb.getLoansTextField().setText("200");
        bb.getSalesTextField().setText("0");
        bb.getOtherIncomeTextField().setText("100");
        bb.getFoodTextField().setText("300");
        bb.getRentTextField().setText("700");
        bb.getCommutingTextField().setText("200");
        bb.getOtherOutgoingsTextField().setText("200");

        bb.getWagesDropdown().setSelectedItem("per month");
        bb.getLoansDropdown().setSelectedItem("per month");
        bb.getSalesDropdown().setSelectedItem("per month");
        bb.getOtherIncomeDropdown().setSelectedItem("per month");
        bb.getFoodDropdown().setSelectedItem("per month");
        bb.getRentDropdown().setSelectedItem("per month");
        bb.getCommutingDropdown().setSelectedItem("per month");
        bb.getOtherOutgoingsDropdown().setSelectedItem("per month");

        bb.getChooseFrequency().setSelectedItem("per month");

        // Calculates expected total and tests this is returned
        double total = (1000 + 200 + 100) - (300 + 700 + 200 + 200);
        assertEquals(total, bb.calculateTotalIncome(), 0.01);
    }

    @Test
    public void checkPositiveIncomeColour() {
        // Check the total income is black when positive

        // Inputs for a positive total
        bb.getWagesTextField().setText("1000");
        bb.getRentTextField().setText("500");

        bb.calculateTotalIncome();

        Color outputColor = Color.BLACK;
        assertEquals(outputColor, bb.getColour());
    }

    @Test
    public void checkNegativeIncomeColour() {
        // Check the total income is red when negative

        // Inputs for a negative total
        bb.getWagesTextField().setText("500");
        bb.getRentTextField().setText("1000");

        bb.calculateTotalIncome();

        Color outputColor = Color.RED;
        assertEquals(outputColor, bb.getColour());
    }

    @Test
    public void testWeekCalculations() {
        // Testing calculateTotalIncome method with per week inputs

        // Valid inputs, assuming that it will be treated as per week
        bb.getWagesTextField().setText("1000");

        bb.getWagesDropdown().setSelectedItem("per week");

        bb.getChooseFrequency().setSelectedItem("per week");

        // Calculates expected total and tests this is returned
        double total = 1000;
        assertEquals(total, bb.calculateTotalIncome(), 0.01);
    }

    @Test
    public void testYearCalculations() {
        // Testing calculateTotalIncome method with per year inputs

        // Valid inputs, assuming that it will be treated as per year
        bb.getWagesTextField().setText("1000");

        bb.getWagesDropdown().setSelectedItem("per year");

        bb.getChooseFrequency().setSelectedItem("per year");

        // Calculates expected total and tests this is returned
        double total = 1000;
        assertEquals(total, bb.calculateTotalIncome(), 0.01);
    }

    @Test
    public void testFocusShift() {
        // Testing that focus shifts update the total budget

        // Valid inputs, assuming that it will be treated as per year
        bb.getWagesTextField().setText("1000");
        bb.getFoodTextField().setText("500");

        bb.getWagesDropdown().setSelectedItem("per year");
        bb.getFoodDropdown().setSelectedItem("per year");

        bb.getChooseFrequency().setSelectedItem("per year");

        // Test simulating focus shift
        bb.getWagesTextField().transferFocus();

        // Calculate expected total income and tests this is returned
        double output = 1000 - 500;
        assertEquals(String.format("%.2f", output), bb.getTotalBudgetField().getText());
    }

    @Test
    public void testEnterPress() {
        // Testing that pressing enter update the total budget

        // Valid inputs, assuming that it will be treated as per year
        bb.getWagesTextField().setText("1000");
        bb.getFoodTextField().setText("500");

        bb.getWagesDropdown().setSelectedItem("per year");
        bb.getFoodDropdown().setSelectedItem("per year");

        bb.getChooseFrequency().setSelectedItem("per year");

        // Reference: https://stackoverflow.com/questions/21075354/how-can-i-simulate-keypress-in-junit-test
        // Test simulating enter key press
        KeyEvent enterPress = new KeyEvent(bb.getWagesTextField(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER);
        bb.getWagesTextField().dispatchEvent(enterPress);

        // Calculate expected total income and tests this is returned
        double output = 1000 - 500;
        assertEquals(String.format("%.2f", output), bb.getTotalBudgetField().getText());
    }

    @Test
    public void test1Undo() {
        // Testing a single level of undo

        // Valid inputs, assuming that it will be treated as per year
        bb.getWagesTextField().setText("1000");
        bb.getWagesDropdown().setSelectedItem("per year");
        bb.getChooseFrequency().setSelectedItem("per year");
        bb.getWagesTextField().transferFocus(); // Triggers save

        // Change value for a new state
        bb.getWagesTextField().setText("500");
        bb.getWagesTextField().transferFocus(); // Triggers save

        // Call undo
        bb.undo();

        // Check value has changed to the previous state
        String output = "1000.00";
        assertEquals(output, bb.getWagesTextField().getText());
    }

}
