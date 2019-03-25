package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

public class Var extends Node {
    private String id;
    private Expression expression;

    public Var(String id) {
        this(id, null);
    }

    public Var(String id, Expression expression) {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public boolean isValid(Scope scope) {
        return scope.isInScope(id) && (expression == null || expression.isValid(scope));
    }
}
