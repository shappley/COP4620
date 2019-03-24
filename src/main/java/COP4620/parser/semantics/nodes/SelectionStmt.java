package COP4620.parser.semantics.nodes;

public class SelectionStmt extends Statement {
    public SelectionStmt(Statement body, Statement elseBody) {

    }

    public SelectionStmt(Statement body) {
        this(body, null);
    }
}
