package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;
import COP4620.parser.Symbol;

public abstract class Node {
    public boolean isValid(Scope scope) {
        return true;
    }

    public Symbol.Type evaluateType(Scope scope) {
        return null;
    }

    public Symbol.Type evaluateType(Scope scope, Node left, Node right) {
        Symbol.Type leftType = left.evaluateType(scope);
        if (right != null) {
            Symbol.Type rightType = right.evaluateType(scope);
            if (leftType == rightType) {
                return leftType;
            } else {
                return null;
            }
        }
        return leftType;
    }
}