package COP4620.tree.nodes;

import COP4620.parser.FunctionSymbol;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

public abstract class Node {
    private FunctionSymbol function;

    public boolean isValid(Scope scope) {
        return true;
    }

    public FunctionSymbol getFunction() {
        return function;
    }

    public void forFunction(FunctionSymbol function) {
        this.function = function;
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