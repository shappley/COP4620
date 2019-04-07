package COP4620.tree.nodes;

import COP4620.parser.Scope;

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
}
