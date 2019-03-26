package COP4620.parser.semantics.nodes;

import COP4620.parser.FunctionSymbol;
import COP4620.parser.Scope;

public class Args extends Node {
    private ArgList args;
    private FunctionSymbol function;

    public Args(ArgList args) {
        this.args = args;
    }

    public void forFunction(FunctionSymbol function) {
        this.function = function;
    }

    @Override
    public boolean isValid(Scope scope) {
        if (args != null) {
            args.forFunction(function);
        }
        return args == null || args.isValid(scope);
    }
}
