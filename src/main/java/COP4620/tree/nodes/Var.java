package COP4620.tree.nodes;

import COP4620.parser.Scope;
import COP4620.parser.Symbol;
import COP4620.parser.Symbol.Type;

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
                && (expression == null || expression.evaluateType(scope) == Type.INT)
                && (expression == null || expression.isValid(scope));
    }

    @Override
    public Type evaluateType(Scope scope) {
        Symbol.Type type = scope.getTypeOf(getId());
        if (expression != null) {
            return type == Type.INT_ARRAY ? Type.INT : Type.FLOAT;
        }
        return type;
    }
}
