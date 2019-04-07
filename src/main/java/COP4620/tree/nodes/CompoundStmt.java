package COP4620.tree.nodes;

import COP4620.parser.Scope;

public class CompoundStmt extends Statement {
    private LocalDeclarations declarations;
    private StatementList statements;

    public CompoundStmt(LocalDeclarations localDeclarations, StatementList statementList) {
        this.declarations = localDeclarations;
        this.statements = statementList;
    }

    public boolean isValid(Scope scope, boolean newScope) {
        if (statements != null) {
            statements.forFunction(getFunction());
        }
        if (newScope) {
            scope.addScope();
        }
        boolean b = (declarations == null || declarations.isValid(scope))
                && (statements == null || statements.isValid(scope));
        scope.removeScope();
        return b;
    }

    @Override
    public boolean isValid(Scope scope) {
        return isValid(scope, true);
    }
}
