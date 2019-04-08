package COP4620.tree.nodes;

import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;

import java.util.List;

public class StatementList extends Node {
    private Statement statement;
    private StatementList statementList;

    public StatementList(Statement statement, StatementList statementList) {
        this.statement = statement;
        this.statementList = statementList;
    }

    @Override
    public boolean isValid(Scope scope) {
        statement.forFunction(getFunction());
        if (statementList != null) {
            statementList.forFunction(getFunction());
        }
        return statement.isValid(scope) && (statementList == null || statementList.isValid(scope));
    }

    @Override
    public List<Quadruple> getInstructions() {
        List<Quadruple> list = statement.getInstructions();
        if (statementList != null) {
            list.addAll(statementList.getInstructions());
        }
        return list;
    }
}
