package COP4620.parser.semantics.nodes;

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
        scope.addScope();
        boolean b = body.isValid(scope) && (elseBody == null || elseBody.isValid(scope));
        scope.removeScope();
        return b;
    }
}
