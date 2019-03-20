package COP4620.parser.semantics.nodes;

import COP4620.parser.semantics.Node;

public class Program extends Node {
    private DeclarationList declarationList;

    public Program(DeclarationList declarationList) {
        this.declarationList = declarationList;
    }
}
