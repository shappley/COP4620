package COP4620.parser.semantics.nodes;

import COP4620.parser.semantics.Node;

public class ExpressionStmt extends Statement {
    private String terminal;
    public ExpressionStmt() {
        this.terminal = ";";
    }

    public ExpressionStmt(Expression expression){

    }
}
