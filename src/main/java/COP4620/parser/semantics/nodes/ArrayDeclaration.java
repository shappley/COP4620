package COP4620.parser.semantics.nodes;

import COP4620.parser.semantics.Node;

import static COP4620.util.StringUtil.isInteger;

public class ArrayDeclaration extends VariableDeclaration {
    private String size;

    public ArrayDeclaration(Node typeSpecifier, String id, String size) {
        super(typeSpecifier, id);
        this.size = size;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && isInteger(size);
    }
}
