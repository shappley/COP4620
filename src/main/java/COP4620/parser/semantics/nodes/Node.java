package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;
import COP4620.parser.SymbolTable;

public abstract class Node {
    public boolean isValid(Scope scope) {
        return true;
    }
}