package COP4620.parser.semantics.nodes;

import COP4620.lexer.Keyword;
import COP4620.parser.semantics.Node;

public class VariableDeclaration extends Node {
    private String id;

    public VariableDeclaration(Node typeSpecifier, String id) {
        super(typeSpecifier.getValue());
        this.id = id;
    }

    public String getTypeSpecifier() {
        return getValue()[0].getValue();
    }

    @Override
    public boolean isValid() {
        return !getTypeSpecifier().equals(Keyword.VOID.getValue());
    }
}
