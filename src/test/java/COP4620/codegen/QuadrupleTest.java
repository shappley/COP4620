package COP4620.codegen;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Quadruple")
class QuadrupleTest {
    @Test
    void equals() {
        Quadruple a = new Quadruple(1, Operation.MUL, "x", "y", "t1");
        Quadruple b = new Quadruple(1, Operation.MUL, "x", "y", "t1");
        Quadruple c = new Quadruple(2, Operation.ADD, "t1", "3", "t2");

        assertTrue(a.equals(b));
        assertFalse(b.equals(c));
    }
}
