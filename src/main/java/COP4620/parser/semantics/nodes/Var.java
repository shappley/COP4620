package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;
import COP4620.parser.Symbol;

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

    public String getId() {
        return id;
    }

    @Override
    public boolean isValid(Scope scope) {
        return scope.isInScope(id)
                && (expression == null || scope.getTypeOf(id) == expression.evaluateType(scope))
                && (expression == null || expression.isValid(scope));
    }

    @Override
    public Symbol.Type evaluateType(Scope scope) {
        return scope.getTypeOf(getId());
    }
}
