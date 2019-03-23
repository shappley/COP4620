package COP4620.parser.semantics.nodes;

import COP4620.parser.semantics.Node;

public class SelectionStmt extends Node {
    public SelectionStmt(Statement body, Statement elseBody) {

    }

    public SelectionStmt(Statement body) {
        this(body, null);
    }
}
