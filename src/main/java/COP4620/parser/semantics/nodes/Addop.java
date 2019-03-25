package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;
import COP4620.parser.SymbolTable;

public class Addop extends Node {
    private String value;

    public Addop(String value) {
        this.value = value;
    }

    @Override
    public boolean isValid(Scope scope) {
        return true;
    }
}
