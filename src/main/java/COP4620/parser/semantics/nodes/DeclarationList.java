package COP4620.parser.semantics.nodes;

import COP4620.parser.semantics.Node;

public class DeclarationList extends Node {
    private Declaration[] declarations;

    public DeclarationList(Declaration... declarations) {
        this.declarations = declarations;
    }

    public Declaration[] getDeclarations() {
        return declarations;
    }
}
