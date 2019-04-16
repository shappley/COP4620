package COP4620.tree.nodes;

import COP4620.parser.Scope;

public class SelectionStmt extends Statement {
    private Expression condition;
    private Statement body, elseBody;

    public SelectionStmt(Expression condition, Statement body, Statement elseBody) {
        this.condition = condition;
        this.body = body;
        this.elseBody = elseBody;
    }

    public SelectionStmt(Expression condition, Statement body) {
        this(condition, body, null);
    }

    @Override
    public boolean isValid(Scope scope) {
        body.forFunction(getFunction());
        if (elseBody != null) {
            elseBody.forFunction(getFunction());
        }
        scope.addScope();
        boolean b = condition.isValid(scope) && body.isValid(scope) && (elseBody == null || elseBody.isValid(scope));
        scope.removeScope();
        return b;
    }
}
