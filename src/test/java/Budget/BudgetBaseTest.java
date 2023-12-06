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
 * We can't test the GUI so we have to test methods that do the work.
 * Add more tests for any manipulations of values in your application.
 * Rewrite methods so that they are not void, but return values, and can be tested.
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
        // Empty text values to check they are being marked as a 0

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
        // Testing calculateTotalIncome method

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

}
