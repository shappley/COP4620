package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;
import COP4620.parser.SymbolTable;

public class Args extends Node {
    private ArgList args;

    public Args(ArgList args) {
        this.args = args;
    }

    @Override
    public boolean isValid(Scope scope) {
        return args == null || args.isValid(scope);
    }
}
