package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

public class CompoundStmt extends Statement {
    private LocalDeclarations declarations;
    private StatementList statements;

    public CompoundStmt(LocalDeclarations localDeclarations, StatementList statementList) {
        this.declarations = localDeclarations;
        this.statements = statementList;
    }

    @Override
    public boolean isValid(Scope scope) {
        return (declarations == null || declarations.isValid(scope))
                && (statements == null || statements.isValid(scope));
    }
}
