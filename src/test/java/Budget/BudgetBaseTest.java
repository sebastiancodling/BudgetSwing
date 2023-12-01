package Budget;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

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
    @Test
    public void shouldTotal()
    {
        JFrame frame = new JFrame();
        BudgetBase bb = new BudgetBase(frame);
        double value = 0.0;

        assertEquals(value, bb.calculateTotalIncome() );
    }
}
