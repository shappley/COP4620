package COP4620.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SymbolTableTest {
    @Test
    void add() {
        SymbolTable table = new SymbolTable();
        assertTrue(table.add(new Symbol("x", Symbol.Type.INT)));
        assertTrue(table.add(new Symbol("y", Symbol.Type.INT)));
        assertTrue(table.add(new Symbol("z", Symbol.Type.INT)));
        assertFalse(table.add(new Symbol("x", Symbol.Type.INT)));
    }

    @Test
    void contains() {
        SymbolTable table = new SymbolTable();
        table.add(new Symbol("x", Symbol.Type.INT));
        table.add(new Symbol("y", Symbol.Type.INT));
        table.add(new Symbol("z", Symbol.Type.INT));

        assertTrue(table.contains("x"));
        assertTrue(table.contains("y"));
        assertTrue(table.contains("z"));
        assertFalse(table.contains("w"));
    }
}
