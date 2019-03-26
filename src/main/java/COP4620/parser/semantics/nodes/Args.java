package COP4620.parser.semantics.nodes;

import COP4620.parser.FunctionSymbol;
import COP4620.parser.Scope;

public class Args extends Node {
    private ArgList args;

    public Args(ArgList args) {
        this.args = args;
    }

    @Override
    public boolean isValid(Scope scope) {
        if (args != null) {
            args.forFunction(getFunction());
        }
        return args == null || args.isValid(scope);
    }
}
