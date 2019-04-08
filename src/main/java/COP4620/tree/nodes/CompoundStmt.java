package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        List<Quadruple> list = new ArrayList<>();
        if (declarations != null) {
            list.addAll(declarations.getInstructions(gen));
        }
        if (statements != null) {
            list.addAll(statements.getInstructions(gen));
        }
        return list;
    }
}
