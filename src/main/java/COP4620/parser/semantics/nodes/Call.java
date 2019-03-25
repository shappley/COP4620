package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

public class Call extends Node {
    private String id;
    private Args args;

    public Call(String id, Args args) {
        this.id = id;
        this.args = args;
    }

    @Override
    public boolean isValid(Scope scope) {
        //TODO check function args = params
        return scope.hasFunction(id) && (args == null || args.isValid(scope));
    }
}
