package COP4620.tree.nodes;

import COP4620.parser.FunctionSymbol;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

public class Call extends Node {
    private String id;
    private Args args;

    public Call(String id, Args args) {
        this.id = id;
        this.args = args;
    }

    @Override
    public boolean isValid(Scope scope) {
        FunctionSymbol function = scope.getFunction(id);
        if (args != null) {
            args.forFunction(function);
        } else if (function.parameters() != 0) {
            return false;
        }
        return function != null && (args == null || args.isValid(scope));
    }

    @Override
    public Symbol.Type evaluateType(Scope scope) {
        return scope.getTypeOf(id);
    }
}
