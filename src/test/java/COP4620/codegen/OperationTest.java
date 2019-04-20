package COP4620.codegen;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Operation")
class OperationTest {
    @Test
    void getRelop() {
        assertEquals(Operation.BRGT, Operation.getRelop(">"));
        assertEquals(Operation.BRGE, Operation.getRelop(">="));
        assertEquals(Operation.BRLT, Operation.getRelop("<"));
        assertEquals(Operation.BRLE, Operation.getRelop("<="));
        assertEquals(Operation.BREQ, Operation.getRelop("=="));
        assertEquals(Operation.BRNEQ, Operation.getRelop("!="));
        assertEquals(null, Operation.getRelop("djfghdfg")); //nonsense
    }

    @Test
    void getNegatedRelop() {
        assertEquals(Operation.BRLE, Operation.getNegatedRelop(">"));
        assertEquals(Operation.BRLT, Operation.getNegatedRelop(">="));
        assertEquals(Operation.BRGE, Operation.getNegatedRelop("<"));
        assertEquals(Operation.BRGT, Operation.getNegatedRelop("<="));
        assertEquals(Operation.BRNEQ, Operation.getNegatedRelop("=="));
        assertEquals(Operation.BREQ, Operation.getNegatedRelop("!="));
        assertEquals(null, Operation.getNegatedRelop("djfghdfg")); //nonsense
    }

    @Test
    void getAddOp() {
        assertEquals(Operation.ADD, Operation.getAddOp("+"));
        assertEquals(Operation.SUB, Operation.getAddOp("-"));
        assertEquals(Operation.MUL, Operation.getAddOp("*"));
        assertEquals(Operation.DIV, Operation.getAddOp("/"));
        assertEquals(null, Operation.getAddOp("dfgdfg")); //nonsense
    }
}
