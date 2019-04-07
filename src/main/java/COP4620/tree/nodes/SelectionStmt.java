package COP4620.tree.nodes;

import COP4620.parser.Scope;

public class SelectionStmt extends Statement {
    private Statement body, elseBody;

    public SelectionStmt(Statement body, Statement elseBody) {
        this.body = body;
        this.elseBody = elseBody;
    }

    public SelectionStmt(Statement body) {
        this(body, null);
    }

    @Override
    public boolean isValid(Scope scope) {
        body.forFunction(getFunction());
        if (elseBody != null) {
            elseBody.forFunction(getFunction());
        }
        scope.addScope();
        boolean b = body.isValid(scope) && (elseBody == null || elseBody.isValid(scope));
        scope.removeScope();
        return b;
    }
}
