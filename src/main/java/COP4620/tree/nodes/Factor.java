package COP4620.tree.nodes;

import COP4620.lexer.Token;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

import static COP4620.util.StringUtil.isInteger;

public class Factor extends Node {
    private Node child;

    public Factor(Node child) {
        this.child = child;
    }

    @Override
    public boolean isValid(Scope scope) {
        return child.isValid(scope);
    }

    @Override
    public Symbol.Type evaluateType(Scope scope) {
        if (child instanceof TerminalNode) {
            Token token = ((TerminalNode) child).getToken();
            return isInteger(token.getValue()) ? Symbol.Type.INT : Symbol.Type.FLOAT;
        }
        return child.evaluateType(scope);
    }
}
